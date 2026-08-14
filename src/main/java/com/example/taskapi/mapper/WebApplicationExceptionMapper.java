package com.example.taskapi.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.time.Clock;

public class WebApplicationExceptionMapper extends AbstractErrorMapper
        implements ExceptionMapper<WebApplicationException> {
    private final Clock clock;

    @Context
    private UriInfo uriInfo;

    public WebApplicationExceptionMapper(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Response toResponse(WebApplicationException exception) {
        int statusCode = exception.getResponse().getStatus();
        if (statusCode == Response.Status.METHOD_NOT_ALLOWED.getStatusCode()) {
            return response(Response.Status.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
                    "HTTP method is not allowed for this resource", uriInfo, clock);
        }
        if (statusCode == Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode()) {
            return response(Response.Status.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE",
                    "Content-Type must be application/json", uriInfo, clock);
        }
        if (statusCode == Response.Status.BAD_REQUEST.getStatusCode()) {
            return response(Response.Status.BAD_REQUEST, "VALIDATION_ERROR",
                    "Request body is invalid", uriInfo, clock);
        }
        // Preserve framework responses not covered by the documented common-error contract.
        // This avoids converting an unrelated 4xx response into INTERNAL_ERROR.
        return exception.getResponse();
    }
}
