package ie.dpd.model;

// No JPA annotations are needed. This is now a simple data holder.
public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean completed;

    // Getters and Setters (remain the same)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}