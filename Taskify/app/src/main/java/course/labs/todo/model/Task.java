package course.labs.todo.model;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

/**
 * Created by Lokesh on 2/14/2017.
 *  Pojo holding the task information
 */

public class Task {

    private Integer Id;
    @NonNull
    private String title;
    private String description;
    private String priority;
    private String dueDate;
    private boolean status;

    public Task(Integer Id, @NonNull String title, String description, String dueDate, String priority, boolean status) {
        this.Id = Id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer mId) {
        this.Id = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mtitle) {
        this.title = mtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) && Strings.isNullOrEmpty(description);
    }
}
