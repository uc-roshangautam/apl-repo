import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataProcessingSystem {
    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Task to represent work items
    static class Task {
        private final String id;
        private final String data;
        private final int processingTime;

        public Task(String data, int processingTime) {
            this.id = UUID.randomUUID().toString().substring(0, 8);
            this.data = data;
            this.processingTime = processingTime;
        }

        public String getId() { return id; }
        public String getData() { return data; }
        public int getProcessingTime() { return processingTime; }
    }

    // Result of processed task
    static class TaskResult {
        private final String taskId;
        private final String workerId;
        private final String processedData;
        private final LocalDateTime processingTime;

        public TaskResult(String taskId, String workerId, String processedData) {
            this.taskId = taskId;
            this.workerId = workerId;
            this.processedData = processedData;
            this.processingTime = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return String.format(
                "TaskResult | Task ID: %s | Worker: %s | Time: %s | Output: %s",
                taskId, workerId, processingTime.format(formatter), processedData
            );
        }
    }

    // Thread-safe queue
    static class TaskQueue {
        private final Queue<Task> tasks = new LinkedList<>();
        private boolean isShutdown = false;

        public synchronized void addTask(Task task) {
            if (isShutdown) {
                System.out.println("[QUEUE] Queue is shutdown. Cannot add task.");
                return;
            }
            tasks.add(task);
            System.out.printf("[QUEUE] Added task: %s\n", task.getId());
            this.notifyAll();
        }

        public synchronized Task getTask() throws InterruptedException {
            while (tasks.isEmpty() && !isShutdown) {
                wait();
            }
            if (tasks.isEmpty() && isShutdown) {
                return null;
            }
            Task task = tasks.poll();
            if (task != null) {
                System.out.printf("[QUEUE] Retrieved task: %s\n", task.getId());
            }
            return task;
        }

        public synchronized void shutdown() {
            isShutdown = true;
            notifyAll();
            System.out.println("[QUEUE] Task queue shutdown initiated.");
        }
    }

    // Thread-safe collector
    static class ResultCollector {
        private final List<TaskResult> results = Collections.synchronizedList(new ArrayList<>());
        private final Map<String, Integer> workerCounts = new ConcurrentHashMap<>();

        public void addResult(TaskResult result) {
            results.add(result);      
            workerCounts.merge(result.workerId, 1, Integer::sum);
            System.out.printf("[RESULT] Task %s processed by %s\n", result.taskId, result.workerId);
        }

        public void printSummary() {
            System.out.println("\n....... Results Summary .......");
            System.out.printf("Total tasks processed: %d\n", results.size());
            workerCounts.forEach((worker, count) ->
                System.out.printf("Worker %-10s processed %d task(s)\n", worker, count)
            );
            System.out.println("Data processing completed.");
        }
    }

    // Worker class to process tasks
    static class Worker extends Thread {
        private final String workerId;
        private final TaskQueue taskQueue;
        private final ResultCollector resultCollector;
        private final AtomicBoolean isRunning = new AtomicBoolean(true);

        public Worker(String workerId, TaskQueue taskQueue, ResultCollector resultCollector) {
            this.workerId = workerId;
            this.taskQueue = taskQueue;
            this.resultCollector = resultCollector;
        }

        @Override
        public void run() {
            System.out.printf("[WORKER-%s] Started\n", workerId);
            while (isRunning.get()) {
                try {
                    Task task = taskQueue.getTask();
                    if (task == null) {
                        isRunning.set(false);
                        System.out.printf("[WORKER-%s] No more tasks. Shutting down...\n", workerId);
                        break;
                    }

                    System.out.printf("[WORKER-%s] Processing task: %s\n", workerId, task.getId());
                    Thread.sleep(task.getProcessingTime());
                    String processedData = task.getData().toUpperCase();
                    TaskResult result = new TaskResult(task.getId(), workerId, processedData);
                    resultCollector.addResult(result);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.printf("[WORKER-%s] Interrupted. Exiting...\n", workerId);
                    break;
                }
            }
            System.out.printf("[WORKER-%s] Shutdown complete.\n", workerId);
        }
    }

    // Main execution
    public static void main(String[] args) throws InterruptedException {
        int numWorkers = 4;
        int numTasks = 20;

        TaskQueue taskQueue = new TaskQueue();
        ResultCollector resultCollector = new ResultCollector();
        List<Worker> workers = new ArrayList<>();

        System.out.printf("[MAIN] Starting %d workers...\n", numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            Worker worker = new Worker("worker-" + i, taskQueue, resultCollector);
            worker.start();
            workers.add(worker);
        }

        System.out.printf("[MAIN] Adding %d tasks to the queue...\n", numTasks);
        for (int i = 0; i < numTasks; i++) {
            String data = "data-" + (i + 1);
            int delay = 200 + random.nextInt(300);
            taskQueue.addTask(new Task(data, delay));
        }

        System.out.println("[MAIN] All tasks added. Initiating shutdown...");
        taskQueue.shutdown();

        for (Worker worker : workers) {
            worker.join();
        }

        resultCollector.printSummary();
    }
}