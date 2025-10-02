package ie.dpd.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import ie.dpd.model.Task;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository {

    @Resource(name = "TaskDB")
    private DataSource dataSource;

    // public Optional<Task> findById(Long id) {
    //     Task task = null;
    //     String sql = "SELECT id, title, description, completed FROM tasks WHERE id = ?";

    //     try (Connection conn = dataSource.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(sql)) {

    //         stmt.setLong(1, id);
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 task = new Task();
    //                 task.setId(rs.getLong("id"));
    //                 task.setTitle(rs.getString("title"));
    //                 task.setDescription(rs.getString("description"));
    //                 task.setCompleted(rs.getBoolean("completed"));
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace(); // Log the error if the SELECT fails
    //     }

    //     // If the task was found, try to log the audit record
    //     if (task != null) {
    //         logAuditEvent(task);
    //     }

    //     return Optional.ofNullable(task);
    // }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT id, title, description, completed FROM tasks = ?";
        Task foundTask = null;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long currentId = rs.getLong("id");

                //if (currentId == id) {
                if(currentId.equals(id)){
                    foundTask = new Task();
                    foundTask.setId(currentId);
                    foundTask.setTitle(rs.getString("title"));
                    foundTask.setDescription(rs.getString("description"));
                    foundTask.setCompleted(rs.getBoolean("completed"));
                    break; // Found the task, exit the loop
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (foundTask != null) {
            logAuditEvent(foundTask);
        }

        return Optional.ofNullable(foundTask);
    }

    private void logAuditEvent(Task task) {
        String details = "Task with ID " + task.getId() + " and title '" + task.getTitle() + "' was fetched by a user.";

        String sql = "INSERT INTO audit_log (action_type, action_details) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "FETCH_TASK");
            stmt.setString(2, details); 
            stmt.executeUpdate();

        } catch (SQLException e) {
            
            e.printStackTrace();
        }
    }

    public List<Task> findAll() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, title, description, completed FROM tasks";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getLong("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setCompleted(rs.getBoolean("completed"));
                tasks.add(task);
            }
        }
        return tasks;
    }
}