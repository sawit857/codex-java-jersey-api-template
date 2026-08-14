package com.example.taskapi.mapper;

import com.example.taskapi.exception.TaskValidationException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class TaskValidationExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<TaskValidationException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public TaskValidationExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(TaskValidationException exception) {
        return response(Response.Status.BAD_REQUEST, "VALIDATION_ERROR",
                exception.getMessage(), uriInfo, clock);
    }
}
