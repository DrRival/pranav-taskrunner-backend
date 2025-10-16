package com.pranav.taskrunner.web;

import com.pranav.taskrunner.model.Task;
import com.pranav.taskrunner.model.TaskExecution;
import com.pranav.taskrunner.repo.TaskRepository;
import com.pranav.taskrunner.service.CommandValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;
    private final CommandValidator validator = new CommandValidator();

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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create/Update (id optional)
    @PutMapping
    public ResponseEntity<?> upsert(@RequestBody Task t) {
        if (t.getCommand() != null && !validator.isCommandSafe(" " + t.getCommand() + " ")) {
            return ResponseEntity.badRequest().body("Unsafe command");
        }
        // if no id provided, generate one to keep things simple
        if (t.getId() == null || t.getId().isBlank()) {
            t.setId(UUID.randomUUID().toString());
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
    public List<Task> findByName(@RequestParam(name = "q") String q) {
        return repo.findByNameContainingIgnoreCase(q);
    }

    // For Task 1 we simulate execution (no k8s yet). We'll append a TaskExecution with output = "SIMULATED".
    // In Task 2 we'll replace this with a real Kubernetes pod run.
    @PutMapping("/{id}/executions")
    public ResponseEntity<?> run(@PathVariable String id, @RequestBody(required = false) Map<String, String> body) {
        Optional<Task> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Task task = opt.get();
        String cmd = task.getCommand();
        if (cmd != null && !validator.isCommandSafe(" " + cmd + " ")) {
            return ResponseEntity.badRequest().body("Unsafe command");
        }

        TaskExecution exec = new TaskExecution();
        exec.setStartTime(Instant.now());
        // Simulate doing work
        String override = body != null ? body.get("command") : null;
        String effective = (override != null && !override.isBlank()) ? override : cmd;
        exec.setOutput("SIMULATED_RUN: " + (effective == null ? "(no command)" : effective));
        exec.setEndTime(Instant.now());

        task.getTaskExecutions().add(exec);
        repo.save(task);
        return ResponseEntity.ok(exec);
    }
}
