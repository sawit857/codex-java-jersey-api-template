package com.example.taskapi.resource;

import com.example.taskapi.dto.CreateTaskRequest;
import com.example.taskapi.dto.TaskResponse;
import com.example.taskapi.dto.UpdateTaskRequest;
import com.example.taskapi.dto.UpdateTaskStatusRequest;
import com.example.taskapi.service.TaskService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
    private final TaskService service;

    @Context
    private UriInfo uriInfo;

    public TaskResource(TaskService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateTaskRequest request) {
        TaskResponse response = service.create(request);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(response.getId())).build();
        return Response.created(location).entity(response).build();
    }

    @GET
    public List<TaskResponse> list(@QueryParam("status") String status) {
        return service.list(status);
    }

    @GET
    @Path("/{taskId}")
    public TaskResponse get(@PathParam("taskId") Long taskId) {
        return service.get(taskId);
    }

    @PUT
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public TaskResponse update(@PathParam("taskId") Long taskId, UpdateTaskRequest request) {
        return service.update(taskId, request);
    }

    @PATCH
    @Path("/{taskId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public TaskResponse updateStatus(@PathParam("taskId") Long taskId,
                                     UpdateTaskStatusRequest request) {
        return service.updateStatus(taskId, request);
    }

    @DELETE
    @Path("/{taskId}")
    public Response delete(@PathParam("taskId") Long taskId) {
        service.delete(taskId);
        return Response.noContent().build();
    }
}
