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
	TaskID         string
	WorkerID       string
	ProcessedData  string
	ProcessingTime time.Time
}

func (tr TaskResult) String() string {
	return fmt.Sprintf("TaskResult | TaskID: %s | Worker: %s | Time: %s | Output: %s",
		tr.TaskID, tr.WorkerID, tr.ProcessingTime.Format("2006-01-02 15:04:05.000"), tr.ProcessedData)
}

type ResultCollector struct {
	mu           sync.Mutex
	results      []TaskResult
	workerCounts map[string]int
	file         *os.File
}

func NewResultCollector(filePath string) (*ResultCollector, error) {
	file, err := os.Create(filePath)
	if err != nil {
		return nil, err
	}
	return &ResultCollector{
		results:      make([]TaskResult, 0),
		workerCounts: make(map[string]int),
		file:         file,
	}, nil
}

func (rc *ResultCollector) AddResult(result TaskResult) {
	rc.mu.Lock()
	defer rc.mu.Unlock()

	rc.results = append(rc.results, result)
	rc.workerCounts[result.WorkerID]++

	_, err := rc.file.WriteString(result.String() + "\n")
	if err != nil {
		log.Printf("[ERROR] Writing to file failed: %v", err)
	}
}

func (rc *ResultCollector) PrintSummary() {
	rc.mu.Lock()
	defer rc.mu.Unlock()

	fmt.Println("\n....... Results Summary .......")
	fmt.Printf("Total tasks processed: %d\n", len(rc.results))
	for worker, count := range rc.workerCounts {
		fmt.Printf("Worker %-10s processed %d task(s)\n", worker, count)
	}
	fmt.Println("Data processing completed.")
}

func worker(id string, tasks <-chan Task, wg *sync.WaitGroup, rc *ResultCollector) {
	defer wg.Done()
	log.Printf("[WORKER-%s] Started\n", id)

	for task := range tasks {
		log.Printf("[WORKER-%s] Processing task: %s\n", id, task.ID)
		time.Sleep(task.ProcessingTime)

		processed := TaskResult{
			TaskID:         task.ID,
			WorkerID:       id,
			ProcessedData:  toUpper(task.Data),
			ProcessingTime: time.Now(),
		}
		rc.AddResult(processed)
		log.Printf("[WORKER-%s] Completed task: %s\n", id, task.ID)
	}

	log.Printf("[WORKER-%s] Exiting\n", id)
}

func toUpper(s string) string {
	return fmt.Sprintf("%s", string([]rune(s)))
}

func main() {
	rand.Seed(time.Now().UnixNano())
	numWorkers := 4
	numTasks := 20

	taskChan := make(chan Task, numTasks)
	var wg sync.WaitGroup

	rc, err := NewResultCollector("results.log")
	if err != nil {
		log.Fatalf("Error creating result collector: %v", err)
	}
	defer rc.file.Close()

	log.Printf("[MAIN] Starting %d workers...\n", numWorkers)
	for i := 0; i < numWorkers; i++ {
		wg.Add(1)
		go worker(fmt.Sprintf("worker-%d", i), taskChan, &wg, rc)
	}

	log.Printf("[MAIN] Adding %d tasks...\n", numTasks)
	for i := 0; i < numTasks; i++ {
		task := Task{
			ID:             fmt.Sprintf("task-%02d", i+1),
			Data:           fmt.Sprintf("data-%02d", i+1),
			ProcessingTime: time.Duration(200+rand.Intn(300)) * time.Millisecond,
		}
		taskChan <- task
	}
	close(taskChan)

	wg.Wait()
	rc.PrintSummary()
}