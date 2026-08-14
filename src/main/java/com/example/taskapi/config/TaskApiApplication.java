package com.example.taskapi.config;

import com.example.taskapi.mapper.GenericExceptionMapper;
import com.example.taskapi.mapper.InvalidTaskStatusExceptionMapper;
import com.example.taskapi.mapper.JsonMappingExceptionMapper;
import com.example.taskapi.mapper.JsonParseExceptionMapper;
import com.example.taskapi.mapper.TaskNotFoundExceptionMapper;
import com.example.taskapi.mapper.TaskValidationExceptionMapper;
import com.example.taskapi.mapper.WebApplicationExceptionMapper;
import com.example.taskapi.repository.InMemoryTaskRepository;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.resource.TaskResource;
import com.example.taskapi.service.TaskService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.time.Clock;

public class TaskApiApplication extends ResourceConfig {
    public TaskApiApplication() {
        this(Clock.systemUTC());
    }

    public TaskApiApplication(Clock clock) {
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository, clock);

        register(new TaskResource(service));
        register(new TaskNotFoundExceptionMapper(clock));
        register(new TaskValidationExceptionMapper(clock));
        register(new InvalidTaskStatusExceptionMapper(clock));
        register(new JsonParseExceptionMapper(clock));
        register(new JsonMappingExceptionMapper(clock));
        register(new WebApplicationExceptionMapper(clock));
        register(new GenericExceptionMapper(clock));
        register(ObjectMapperProvider.class);
        register(JacksonFeature.withoutExceptionMappers());
    }
}
