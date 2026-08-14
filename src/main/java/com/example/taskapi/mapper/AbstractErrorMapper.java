package com.example.taskapi.mapper;

import com.example.taskapi.dto.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Clock;
import java.time.LocalDateTime;

abstract class AbstractErrorMapper {
    protected Response response(Response.Status status, String code, String message,
                                UriInfo uriInfo, Clock clock) {
        String path = uriInfo == null ? "" : uriInfo.getRequestUri().getPath();
        ErrorResponse error = new ErrorResponse(code, message, LocalDateTime.now(clock), path);
        return Response.status(status).type(MediaType.APPLICATION_JSON_TYPE).entity(error).build();
    }
}
