package main

import (
	"fmt"
	"log"
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
	
	log.Printf("Added result for task: %s processed by worker: %s", result.TaskID, result.WorkerID)
	return nil
}

func (rc *ResultCollector) Close() error {
	return rc.outputFile.Close()
}

func (rc *ResultCollector) PrintSummary() {
	rc.mu.Lock()
	defer rc.mu.Unlock()

	log.Println(".... Results Summary ....")
	log.Printf("Total tasks processed: %d", len(rc.results))
	
	for workerID, count := range rc.workerCounts {
		log.Printf("Worker %s processed %d tasks", workerID, count)
	}
}

func worker(id string, taskChan <-chan Task, resultCollector *ResultCollector, wg *sync.WaitGroup) {
	defer wg.Done()
	log.Printf("Worker %s started", id)
	
	for task := range taskChan {
		log.Printf("Worker %s processing task: %s", id, task.ID)
		
		
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
			log.Printf("Error adding result: %v", err)
		}
		
		log.Printf("Worker %s completed task: %s", id, task.ID)
	}
	
	log.Printf("Worker %s shutting down", id)
}

func generateTask(id int) Task {
	return Task{
		ID:             fmt.Sprintf("task-%d", id),
		Data:           fmt.Sprintf("Task data %d", id),
		ProcessingTime: time.Duration(100+rand.Intn(900)) * time.Millisecond,
	}
}

func main() {
	log.Println("Starting Data Processing System")
	
	
	rand.Seed(time.Now().UnixNano())
	
	
	numWorkers := 5
	numTasks := 20
	outputFilePath := "results_go.txt"
	
	
	resultCollector, err := NewResultCollector(outputFilePath)
	if err != nil {
		log.Fatalf("Failed to create result collector: %v", err)
	}
	defer resultCollector.Close()
	
	
	taskChan := make(chan Task, numTasks)
	
	
	var wg sync.WaitGroup
	
	
	log.Printf("Starting %d workers", numWorkers)
	for i := 0; i < numWorkers; i++ {
		workerID := fmt.Sprintf("worker-%d", i)
		wg.Add(1)
		go worker(workerID, taskChan, resultCollector, &wg)
	}
	
	
	log.Printf("Adding %d tasks to the queue", numTasks)
	for i := 0; i < numTasks; i++ {
		task := generateTask(i)
		taskChan <- task
		
		
		time.Sleep(50 * time.Millisecond)
	}
	
	
	log.Println("All tasks added, closing task channel")
	close(taskChan)
	
	
	log.Println("Waiting for workers to finish")
	wg.Wait()
	
	
	resultCollector.PrintSummary()
	log.Println("Data processing system completed")
} 