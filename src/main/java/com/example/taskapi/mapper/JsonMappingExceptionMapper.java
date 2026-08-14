package com.example.taskapi.mapper;

import com.fasterxml.jackson.databind.JsonMappingException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class JsonMappingExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<JsonMappingException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public JsonMappingExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(JsonMappingException exception) {
        return response(Response.Status.BAD_REQUEST, "VALIDATION_ERROR",
                "Request body is invalid", uriInfo, clock);
    }
}
