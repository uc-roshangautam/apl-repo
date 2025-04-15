package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

type Task struct {
	ID             string
	Data           string
	ProcessingTime time.Duration
}

type TaskResult struct {
	TaskID        string
	WorkerID      string
	ProcessedData string
	ProcessingTime time.Time
}

func (tr TaskResult) String() string {
	return fmt.Sprintf("TaskResult{taskId='%s', workerId='%s', processedData='%s', processingTime=%s}",
		tr.TaskID, tr.WorkerID, tr.ProcessedData, tr.ProcessingTime.Format("2006-01-02 15:04:05.000"))
}

type ResultCollector struct {
	results      []TaskResult
	mu           sync.Mutex
	outputFile   *os.File
	workerCounts map[string]int
}

func NewResultCollector(outputPath string) (*ResultCollector, error) {
	file, err := os.Create(outputPath)
	if err != nil {
		return nil, fmt.Errorf("failed to create output file: %w", err)
	}

	return &ResultCollector{
		results:      make([]TaskResult, 0),
		outputFile:   file,
		workerCounts: make(map[string]int),
	}, nil
}

func (rc *ResultCollector) AddResult(result TaskResult) error {
	rc.mu.Lock()
	defer rc.mu.Unlock()

	rc.results = append(rc.results, result)
	rc.workerCounts[result.WorkerID]++

	_, err := fmt.Fprintln(rc.outputFile, result.String())
	if err != nil {
		return fmt.Errorf("failed to write result to file: %w", err)
	}

	fmt.Printf("Added result for task: %s processed by worker: %s\n", result.TaskID, result.WorkerID)
	return nil
}

func (rc *ResultCollector) Close() error {
	return rc.outputFile.Close()
}

func (rc *ResultCollector) PrintSummary() {
	rc.mu.Lock()
	defer rc.mu.Unlock()

	fmt.Println(".... Results Summary ....")
	fmt.Printf("Total tasks processed: %d\n", len(rc.results))

	for workerID, count := range rc.workerCounts {
		fmt.Printf("Worker %s processed %d tasks\n", workerID, count)
	}
}

func worker(id string, taskChan <-chan Task, resultCollector *ResultCollector, wg *sync.WaitGroup) {
	defer wg.Done()
	fmt.Printf("Worker %s started\n", id)

	for task := range taskChan {
		fmt.Printf("Worker %s processing task: %s\n", id, task.ID)

		time.Sleep(task.ProcessingTime)

		processedData := fmt.Sprintf("Processed by worker %s: %s", id, task.Data)

		result := TaskResult{
			TaskID:        task.ID,
			WorkerID:      id,
			ProcessedData: processedData,
			ProcessingTime: time.Now(),
		}

		err := resultCollector.AddResult(result)
		if err != nil {
			fmt.Printf("Error adding result: %v\n", err)
		}

		fmt.Printf("Worker %s completed task: %s\n", id, task.ID)
	}

	fmt.Printf("Worker %s shutting down\n", id)
}

func generateTask(id int) Task {
	return Task{
		ID:             fmt.Sprintf("task-%d", id),
		Data:           fmt.Sprintf("Task data %d", id),
		ProcessingTime: time.Duration(100+rand.Intn(900)) * time.Millisecond,
	}
}

func main() {
	fmt.Println("Starting Data Processing System")

	rand.Seed(time.Now().UnixNano())

	numWorkers := 5
	numTasks := 20
	outputFilePath := "results_go.txt"

	resultCollector, err := NewResultCollector(outputFilePath)
	if err != nil {
		fmt.Printf("Failed to create result collector: %v\n", err)
		return
	}
	defer resultCollector.Close()

	taskChan := make(chan Task, numTasks)

	var wg sync.WaitGroup

	fmt.Printf("Starting %d workers\n", numWorkers)
	for i := 0; i < numWorkers; i++ {
		workerID := fmt.Sprintf("worker-%d", i)
		wg.Add(1)
		go worker(workerID, taskChan, resultCollector, &wg)
	}

	fmt.Printf("Adding %d tasks to the queue\n", numTasks)
	for i := 0; i < numTasks; i++ {
		task := generateTask(i)
		taskChan <- task

		time.Sleep(50 * time.Millisecond)
	}

	fmt.Println("All tasks added, closing task channel")
	close(taskChan)

	fmt.Println("Waiting for workers to finish")
	wg.Wait()

	resultCollector.PrintSummary()
	fmt.Println("Data processing system completed")
}