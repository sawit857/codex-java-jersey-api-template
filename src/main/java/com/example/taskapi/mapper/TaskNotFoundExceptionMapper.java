package com.example.taskapi.mapper;

import com.example.taskapi.exception.TaskNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class TaskNotFoundExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<TaskNotFoundException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public TaskNotFoundExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(TaskNotFoundException exception) {
        String id = exception.getTaskId() == null ? "unknown" : String.valueOf(exception.getTaskId());
        return response(Response.Status.NOT_FOUND, "TASK_NOT_FOUND",
                "Task " + id + " was not found", uriInfo, clock);
    }
}
