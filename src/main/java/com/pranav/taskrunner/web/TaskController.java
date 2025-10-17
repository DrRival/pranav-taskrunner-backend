package com.pranav.taskrunner.web;

import com.pranav.taskrunner.model.Task;
import com.pranav.taskrunner.model.TaskExecution;
import com.pranav.taskrunner.repo.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getOne(@PathVariable String id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Task> upsert(@RequestBody Task t) {
        if (t.getId() == null || t.getId().isBlank()) {
            t.setId(UUID.randomUUID().toString());
        }
        if (t.getTaskExecutions() == null) {
            t.setTaskExecutions(new ArrayList<>());
        }
        Task saved = repo.save(t);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find")
    public List<Task> findByName(@RequestParam String q) {
        return repo.findByNameContainingIgnoreCase(q);
    }

    @PutMapping("/{id}/executions")
    public ResponseEntity<?> run(@PathVariable String id) {
        Optional<Task> optTask = repo.findById(id);
        if (optTask.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }

        Task task = optTask.get();
        if (task.getCommand() == null || task.getCommand().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No command specified"));
        }

        // Simulate execution
        TaskExecution exec = new TaskExecution();
        exec.setStartTime(Instant.now());
        exec.setOutput("SIMULATED_RUN: " + task.getCommand());
        exec.setEndTime(Instant.now());

        // Ensure the executions list exists
        if (task.getTaskExecutions() == null) {
            task.setTaskExecutions(new ArrayList<>());
        }

        task.getTaskExecutions().add(exec);
        repo.save(task);

        return ResponseEntity.ok(exec);
    }
}
