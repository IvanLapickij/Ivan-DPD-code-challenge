package ie.dpd.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/summaries") // decalres as rest resource
@RequestScoped //a new object is created for each request
public class SummaryResoure {

    @Resource(name = "TaskDB")
    private DataSource dataSource;

    
    @POST
    @Produces(MediaType.APPLICATION_JSON)   //defines post endpoint that returns JSON
    public Response create() {  //calling POST /summaries will trigger this method

        String countSql =
            "SELECT " +
            "SUM(CASE WHEN completed THEN 1 ELSE 0 END) AS c, " +
            "SUM(CASE WHEN completed THEN 0 ELSE 1 END) AS p, " +
            "COUNT(*) AS t FROM tasks";
            // c → number of completed tasks (completed = true).
            // p → number of pending tasks (completed = false).
            // t → total tasks.

        //insertSql - Prepares an insert into the task_summaries table.
        String insertSql =
            "INSERT INTO task_summaries (completed_count, pending_count, total_tasks) VALUES (?, ?, ?)";

        //Opens a DB connection and runs the count query.
        //rs will have one row with the 3 counts.
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps1 = cn.prepareStatement(countSql);
             ResultSet rs = ps1.executeQuery()) {
            
            //Extracts the numbers into variables.
            rs.next();
            long c = rs.getLong("c"), p = rs.getLong("p"), t = rs.getLong("t");

            //Inserts a new row into task_summaries
            try (PreparedStatement ps2 = cn.prepareStatement(insertSql)) {
                ps2.setLong(1, c);
                ps2.setLong(2, p);
                ps2.setLong(3, t);
                ps2.executeUpdate();
            }
            //returns HTTP 201 Created. summary as JSON
            return Response.status(Response.Status.CREATED)
                           .entity(Map.of("completed", c, "pending", p, "total", t))
                           .build();
        //error handling if fails returns 500 Internal Server Error
        } catch (SQLException e) {
            return Response.serverError().entity(Map.of("error", e.getMessage())).build();
        }
    }
}
