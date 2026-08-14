package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTaskRepositoryTest {

    @Test
    void create_multipleTasks_assignsUniqueIncreasingIds() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();

        Task first = repository.create(task("First", TaskStatus.OPEN));
        Task second = repository.create(task("Second", TaskStatus.COMPLETED));

        assertThat(first.getId()).isEqualTo(1L);
        assertThat(second.getId()).isEqualTo(2L);
    }

    @Test
    void findAll_returnedTaskDoesNotMutateStoredState() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        Task created = repository.create(task("Original", TaskStatus.OPEN));

        List<Task> returned = repository.findAll();
        returned.get(0).setTitle("Changed outside repository");

        assertThat(repository.findById(created.getId()).get().getTitle()).isEqualTo("Original");
    }

    @Test
    void findByStatus_returnsOnlyMatchingTasks() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        repository.create(task("Open", TaskStatus.OPEN));
        repository.create(task("Done", TaskStatus.COMPLETED));

        assertThat(repository.findByStatus(TaskStatus.OPEN))
                .extracting(Task::getTitle)
                .containsExactly("Open");
    }

    @Test
    void create_concurrentCalls_assignsUniqueIds() throws Exception {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            Callable<Long> action = () -> repository.create(task("Concurrent", TaskStatus.OPEN)).getId();
            Future<Long> first = executor.submit(action);
            Future<Long> second = executor.submit(action);
            Future<Long> third = executor.submit(action);
            Future<Long> fourth = executor.submit(action);

            assertThat(Arrays.asList(first.get(), second.get(), third.get(), fourth.get()))
                    .doesNotHaveDuplicates();
        } finally {
            executor.shutdownNow();
        }
    }

    private Task task(String title, TaskStatus status) {
        LocalDateTime time = LocalDateTime.of(2026, 7, 19, 15, 30);
        return new Task(null, title, null, status, time, time);
    }
}
