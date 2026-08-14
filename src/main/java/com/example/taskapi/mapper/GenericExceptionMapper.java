package com.example.taskapi.mapper;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class GenericExceptionMapper extends AbstractErrorMapper implements ExceptionMapper<Throwable> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public GenericExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(Throwable exception) {
        return response(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "An unexpected error occurred", uriInfo, clock);
    }
}
