package ie.dpd.resource;

import java.sql.SQLException;
import java.util.List;

import ie.dpd.model.Task;
import ie.dpd.repository.TaskRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tasks")
@RequestScoped
public class TaskResource {

    @Inject
    private TaskRepository taskRepository;

    @GET
    @Path("/{id}") // e.g., /api/tasks/1
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") Long id) {
        return taskRepository.findById(id)
                .map(task -> Response.ok(task).build()) // Returns 200 OK with the task
                .orElse(Response.status(Response.Status.NOT_FOUND).build()); // Returns 404 if not found
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            return Response.ok(tasks).build();
        } catch (SQLException e) {
            // Log the exception and return an internal server error
            e.printStackTrace();
            return Response.serverError().entity("Error fetching tasks from the database.").build();
        }
    }
}