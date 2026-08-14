package com.example.taskapi.service;

import com.example.taskapi.dto.CreateTaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.UpdateTaskRequest;
import com.example.taskapi.dto.UpdateTaskStatusRequest;
import com.example.taskapi.exception.InvalidTaskStatusException;
import com.example.taskapi.exception.TaskNotFoundException;
import com.example.taskapi.exception.TaskValidationException;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-07-19T15:30:00Z"), ZoneOffset.UTC);
        service = new TaskService(new InMemoryTaskRepository(), clock);
    }

    @Test
    void create_validRequest_createsOpenTaskWithServerFields() {
        CreateTaskRequest request = createRequest("  Prepare API  ", "  Document errors  ");

        TaskResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Prepare API");
        assertThat(response.getDescription()).isEqualTo("Document errors");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 7, 19, 15, 30));
        assertThat(response.getUpdatedAt()).isEqualTo(response.getCreatedAt());
    }

    @Test
    void create_blankTitle_throwsValidationException() {
        assertThatThrownBy(() -> service.create(createRequest("   ", null)))
                .isInstanceOf(TaskValidationException.class)
                .hasMessageContaining("title");
    }

    @Test
    void get_unknownId_throwsTaskNotFoundException() {
        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void update_existingTask_changesEditableFieldsOnly() {
        TaskResponse created = service.create(createRequest("Old", "Old description"));
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("New");
        request.setDescription("New description");

        TaskResponse updated = service.update(created.getId(), request);

        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("New description");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(updated.getCreatedAt()).isEqualTo(created.getCreatedAt());
    }

    @Test
    void updateStatus_invalidValue_throwsInvalidStatusException() {
        TaskResponse created = service.create(createRequest("Task", null));
        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus("DONE");

        assertThatThrownBy(() -> service.updateStatus(created.getId(), request))
                .isInstanceOf(InvalidTaskStatusException.class);
    }

    @Test
    void delete_unknownId_throwsTaskNotFoundException() {
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    private CreateTaskRequest createRequest(String title, String description) {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setDescription(description);
        return request;
    }
}
