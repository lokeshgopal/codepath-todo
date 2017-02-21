package course.labs.todo.model;

import android.support.annotation.NonNull;

/**
 * Created by Lokesh on 2/15/2017.
 * define all the db operations on the tasks
 */

import java.util.List;

public interface TaskDBOperations {

    List<Task> getTasks();

    Task getTask(Integer taskId);

    long saveTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void updateTask(@NonNull Task task);

    void clearCompletedTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);

    int getRowSequence();
}
