package com.example.taskapi.mapper;

import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class JsonParseExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<JsonParseException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public JsonParseExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(JsonParseException exception) {
        return response(Response.Status.BAD_REQUEST, "VALIDATION_ERROR",
                "Request body is invalid", uriInfo, clock);
    }
}
