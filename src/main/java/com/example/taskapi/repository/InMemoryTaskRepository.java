package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<Long, Task>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Task create(Task task) {
        Task stored = copy(task);
        stored.setId(sequence.incrementAndGet());
        tasks.put(stored.getId(), stored);
        return copy(stored);
    }

    @Override
    public Task save(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task and task ID are required");
        }
        Task stored = copy(task);
        tasks.put(stored.getId(), stored);
        return copy(stored);
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task task = tasks.get(id);
        return task == null ? Optional.<Task>empty() : Optional.of(copy(task));
    }

    @Override
    public List<Task> findAll() {
        return sortedCopies(tasks.values());
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        List<Task> result = new ArrayList<Task>();
        for (Task task : tasks.values()) {
            if (task.getStatus() == status) {
                result.add(copy(task));
            }
        }
        result.sort(Comparator.comparing(Task::getId));
        return result;
    }

    @Override
    public boolean deleteById(Long id) {
        return tasks.remove(id) != null;
    }

    private List<Task> sortedCopies(Iterable<Task> source) {
        List<Task> result = new ArrayList<Task>();
        for (Task task : source) {
            result.add(copy(task));
        }
        result.sort(Comparator.comparing(Task::getId));
        return result;
    }

    private Task copy(Task task) {
        return new Task(task);
    }
}
