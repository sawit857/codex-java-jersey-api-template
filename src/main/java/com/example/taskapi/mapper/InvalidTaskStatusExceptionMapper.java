package com.example.taskapi.mapper;

import com.example.taskapi.exception.InvalidTaskStatusException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class InvalidTaskStatusExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<InvalidTaskStatusException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public InvalidTaskStatusExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(InvalidTaskStatusException exception) {
        return response(Response.Status.BAD_REQUEST, "INVALID_TASK_STATUS",
                "Task status is invalid", uriInfo, clock);
    }
}
