package com.example.taskapi.service;

import com.example.taskapi.dto.CreateTaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.UpdateTaskRequest;
import com.example.taskapi.dto.UpdateTaskStatusRequest;
import com.example.taskapi.exception.InvalidTaskStatusException;
import com.example.taskapi.exception.TaskNotFoundException;
import com.example.taskapi.exception.TaskValidationException;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.TaskRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final TaskRepository repository;
    private final Clock clock;

    public TaskService(TaskRepository repository, Clock clock) {
        if (repository == null || clock == null) {
            throw new IllegalArgumentException("Repository and clock are required");
        }
        this.repository = repository;
        this.clock = clock;
    }

    public TaskResponse create(CreateTaskRequest request) {
        validateRequest(request);
        LocalDateTime now = LocalDateTime.now(clock);
        Task task = new Task(null, normalizeRequiredTitle(request.getTitle()),
                normalizeOptional(request.getDescription()), TaskStatus.OPEN, now, now);
        return toResponse(repository.create(task));
    }

    public List<TaskResponse> list(String statusValue) {
        List<Task> tasks;
        if (statusValue == null || statusValue.trim().isEmpty()) {
            tasks = repository.findAll();
        } else {
            tasks = repository.findByStatus(parseStatus(statusValue));
        }
        List<TaskResponse> responses = new ArrayList<TaskResponse>();
        for (Task task : tasks) {
            responses.add(toResponse(task));
        }
        return responses;
    }

    public TaskResponse get(Long id) {
        return toResponse(requireTask(id));
    }

    public TaskResponse update(Long id, UpdateTaskRequest request) {
        if (request == null) {
            throw new TaskValidationException("Request body is required");
        }
        Task task = requireTask(id);
        task.setTitle(normalizeRequiredTitle(request.getTitle()));
        task.setDescription(normalizeOptional(request.getDescription()));
        task.setUpdatedAt(LocalDateTime.now(clock));
        return toResponse(repository.save(task));
    }

    public TaskResponse updateStatus(Long id, UpdateTaskStatusRequest request) {
        if (request == null) {
            throw new InvalidTaskStatusException(null);
        }
        Task task = requireTask(id);
        task.setStatus(parseStatus(request.getStatus()));
        task.setUpdatedAt(LocalDateTime.now(clock));
        return toResponse(repository.save(task));
    }

    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
    }

    private void validateRequest(CreateTaskRequest request) {
        if (request == null) {
            throw new TaskValidationException("Request body is required");
        }
        normalizeRequiredTitle(request.getTitle());
    }

    private String normalizeRequiredTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new TaskValidationException("Task title is required");
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private TaskStatus parseStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidTaskStatusException(value);
        }
        try {
            return TaskStatus.valueOf(value.trim());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTaskStatusException(value);
        }
    }

    private Task requireTask(Long id) {
        if (id == null) {
            throw new TaskNotFoundException(null);
        }
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
