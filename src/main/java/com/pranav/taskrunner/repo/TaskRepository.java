package com.pranav.taskrunner.repo;

import com.pranav.taskrunner.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByNameContainingIgnoreCase(String namePart);
}
