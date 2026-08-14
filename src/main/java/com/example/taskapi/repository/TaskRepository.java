package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task create(Task task);
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByStatus(TaskStatus status);
    boolean deleteById(Long id);
}
