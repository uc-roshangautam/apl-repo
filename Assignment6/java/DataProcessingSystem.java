import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataProcessingSystem {
    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
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
            return String.format("TaskResult{taskId='%s', workerId='%s', processedData='%s', processingTime=%s}",
                    taskId, workerId, processedData, processingTime.format(formatter));
        }
    }
    
    
    static class TaskQueue {
        private final Queue<Task> tasks = new LinkedList<>();
        private boolean isShutdown = false;
        
        
        public synchronized void addTask(Task task) {
            if (isShutdown) {
                System.out.println("Queue is shutdown, no new tasks");
                return;
            }
            tasks.add(task);
            System.out.println("Added task: " + task.getId());
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
                System.out.println("Retrieved task: " + task.getId());
            }
            return task;
        }
        
        
        public synchronized void shutdown() {
            isShutdown = true;
            notifyAll(); 
            System.out.println("Task queue shutdown initiated");
        }
    }
    
    
    static class ResultCollector {
        private final List<TaskResult> results = Collections.synchronizedList(new ArrayList<>());
        private final String outputFilePath;
        private final Map<String, Integer> workerCounts = new ConcurrentHashMap<>();
        
        public ResultCollector(String outputFilePath) {
            this.outputFilePath = outputFilePath;
            
            try (FileWriter fw = new FileWriter(outputFilePath, false)) {
                
            } catch (IOException e) {
                System.err.println("Error creating output file: " + e.getMessage());
            }
        }
        
        
        public void addResult(TaskResult result) {
            results.add(result);
            workerCounts.merge(result.workerId, 1, Integer::sum);
            
            System.out.println("Added result for task: " + result.taskId + 
                      " processed by worker: " + result.workerId);
            
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
                writer.write(result.toString());
                writer.newLine();
            } catch (IOException e) {
                System.err.println("[ERROR] Error writing result to file: " + e.getMessage());
            }
        }
        
        
        public void printSummary() {
            System.out.println("....... Results Summary .......");
            System.out.println("Total tasks processed: " + results.size());
            
            for (Map.Entry<String, Integer> entry : workerCounts.entrySet()) {
                System.out.println("Worker " + entry.getKey() + " processed " + entry.getValue() + " tasks");
            }
        }
    }
    
    
    static class Worker implements Runnable {
        private final String id;
        private final TaskQueue taskQueue;
        private final ResultCollector resultCollector;
        private final AtomicBoolean running = new AtomicBoolean(true);
        
        public Worker(String id, TaskQueue taskQueue, ResultCollector resultCollector) {
            this.id = id;
            this.taskQueue = taskQueue;
            this.resultCollector = resultCollector;
        }
        
        @Override
        public void run() {
            System.out.println("Worker " + id + " started");
            
            try {
                while (running.get()) {
                    Task task = null;
                    try {
                        task = taskQueue.getTask();
                        
                        if (task == null) {
                            System.out.println("Worker " + id + " received null task, shutting down");
                            break;
                        }
                        
                        
                        String processedData = processTask(task);
                        
                        
                        TaskResult result = new TaskResult(task.getId(), id, processedData);
                        resultCollector.addResult(result);
                        
                    } catch (InterruptedException e) {
                        System.out.println("Worker " + id + " was interrupted: " + e.getMessage());
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        System.err.println("Worker " + id + " encountered an error processing task: " + e.getMessage());
                    }
                }
            } finally {
                System.out.println("Worker " + id + " is shutting down");
            }
        }
        
        private String processTask(Task task) throws InterruptedException {
            System.out.println("Worker " + id + " processing task: " + task.getId());
            
            try {
                
                Thread.sleep(task.getProcessingTime());
                
                
                String processedData = "Processed by worker " + id + ": " + task.getData().toUpperCase();
                System.out.println("Worker " + id + " completed task: " + task.getId());
                
                return processedData;
            } catch (InterruptedException e) {
                System.out.println("Worker " + id + " was interrupted while processing task: " + task.getId());
                throw e;
            }
        }
    }
    
    public static void main(String[] args) {
        
        int numWorkers = 4;
        int numTasks = 20;
        String outputFilePath = "results.txt";
        
        
        TaskQueue taskQueue = new TaskQueue();
        ResultCollector resultCollector = new ResultCollector(outputFilePath);
        
        
        ExecutorService executorService = Executors.newFixedThreadPool(numWorkers);
        List<Worker> workers = new ArrayList<>();
        
        System.out.println("Starting " + numWorkers + " workers");
        for (int i = 0; i < numWorkers; i++) {
            Worker worker = new Worker("worker-" + i, taskQueue, resultCollector);
            workers.add(worker);
            executorService.submit(worker);
        }
        
        
        System.out.println("Adding " + numTasks + " tasks to the queue");
        try {
            for (int i = 0; i < numTasks; i++) {
                
                int processingTime = 100 + random.nextInt(900);
                Task task = new Task("Task data " + i, processingTime);
                taskQueue.addTask(task);
                
                
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while adding tasks: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        
        
        System.out.println("All tasks added, initiating shutdown");
        taskQueue.shutdown();
        
        
        try {
            
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("Not all tasks completed in time, forcing shutdown");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted while waiting for workers to finish: " + e.getMessage());
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        
        resultCollector.printSummary();
        System.out.println("Data processing completed");
    }
} 