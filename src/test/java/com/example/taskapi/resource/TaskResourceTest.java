package com.example.taskapi.resource;

import com.example.taskapi.config.TaskApiApplication;
import com.example.taskapi.dto.CreateTaskRequest;
import com.example.taskapi.dto.ErrorResponse;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.UpdateTaskRequest;
import com.example.taskapi.dto.UpdateTaskStatusRequest;
import com.example.taskapi.model.TaskStatus;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskResourceTest extends JerseyTest {

    @BeforeEach
    void startJersey() throws Exception {
        super.setUp();
    }

    @AfterEach
    void stopJersey() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        Clock clock = Clock.fixed(Instant.parse("2026-07-19T15:30:00Z"), ZoneOffset.UTC);
        return new TaskApiApplication(clock);
    }

    @Test
    void create_validRequest_returnsCreatedLocationAndOpenTask() {
        Response response = target("tasks").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createRequest("Prepare API", "Document errors")));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getLocation()).isNotNull();
        assertThat(response.getLocation().getPath()).endsWith("/tasks/1");
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);

        TaskResponse body = response.readEntity(TaskResponse.class);
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(body.getCreatedAt()).isEqualTo(body.getUpdatedAt());
    }

    @Test
    void list_statusFilter_returnsMatchingTasks() {
        target("tasks").request().post(Entity.json(createRequest("Open task", null)));
        TaskResponse second = target("tasks").request()
                .post(Entity.json(createRequest("Completed task", null)))
                .readEntity(TaskResponse.class);
        UpdateTaskStatusRequest status = new UpdateTaskStatusRequest();
        status.setStatus("COMPLETED");
        target("tasks/" + second.getId() + "/status").request()
                .method("PATCH", Entity.json(status));

        List<TaskResponse> tasks = target("tasks").queryParam("status", "COMPLETED")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<TaskResponse>>() { });

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Completed task");
    }

    @Test
    void get_unknownTask_returnsDocumentedNotFoundError() {
        Response response = target("tasks/999").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertThat(response.getStatus()).isEqualTo(404);
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertThat(error.getCode()).isEqualTo("TASK_NOT_FOUND");
        assertThat(error.getPath()).endsWith("/tasks/999");
        assertThat(error.getMessage()).doesNotContain("Exception");
    }

    @Test
    void update_existingTask_returnsUpdatedTask() {
        TaskResponse created = createTask("Before");
        UpdateTaskRequest update = new UpdateTaskRequest();
        update.setTitle("After");
        update.setDescription("Updated");

        Response response = target("tasks/" + created.getId()).request()
                .put(Entity.json(update));

        assertThat(response.getStatus()).isEqualTo(200);
        TaskResponse body = response.readEntity(TaskResponse.class);
        assertThat(body.getTitle()).isEqualTo("After");
        assertThat(body.getDescription()).isEqualTo("Updated");
        assertThat(body.getStatus()).isEqualTo(TaskStatus.OPEN);
    }

    @Test
    void updateStatus_invalidStatus_returnsDocumentedError() {
        TaskResponse created = createTask("Task");
        UpdateTaskStatusRequest status = new UpdateTaskStatusRequest();
        status.setStatus("DONE");

        Response response = target("tasks/" + created.getId() + "/status").request()
                .method("PATCH", Entity.json(status));

        assertThat(response.getStatus()).isEqualTo(400);
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertThat(error.getCode()).isEqualTo("INVALID_TASK_STATUS");
    }

    @Test
    void delete_existingTask_returnsNoContentWithEmptyEntity() {
        TaskResponse created = createTask("Delete me");

        Response response = target("tasks/" + created.getId()).request().delete();

        assertThat(response.getStatus()).isEqualTo(204);
        assertThat(response.hasEntity()).isFalse();
    }

    @Test
    void create_blankTitle_returnsValidationError() {
        Response response = target("tasks").request()
                .post(Entity.json(createRequest("   ", null)));

        assertThat(response.getStatus()).isEqualTo(400);
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertThat(error.getCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    void create_unsupportedContentType_returnsUnsupportedMediaTypeError() {
        Response response = target("tasks").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity("title=Task", MediaType.TEXT_PLAIN_TYPE));

        assertThat(response.getStatus()).isEqualTo(415);
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertThat(error.getCode()).isEqualTo("UNSUPPORTED_MEDIA_TYPE");
    }

    @Test
    void create_malformedJson_returnsValidationError() {
        Response response = target("tasks").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity("{invalid-json", MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(400);
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertThat(error.getCode()).isEqualTo("VALIDATION_ERROR");
    }

    private TaskResponse createTask(String title) {
        return target("tasks").request()
                .post(Entity.json(createRequest(title, null)))
                .readEntity(TaskResponse.class);
    }

    private CreateTaskRequest createRequest(String title, String description) {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setDescription(description);
        return request;
    }
}
