package course.labs.todo.model.mediator;

import android.support.annotation.NonNull;

import java.util.List;

import course.labs.todo.model.Task;

/**
 * Created by Lokesh on 2/16/2017.
 *  This defines the operations that will be used by presenter to perform db operations on task database
 */

public interface TasksMediator {

    interface LoadTasksCallBack {
        void onTasksLoaded(List<Task> tasks);
        void onDataNotAvailable();
    }

    interface GetTaskCallBack {
        void onTaskLoaded(Task task);
        void  onDataNotAvailable();
    }

    interface SaveTaskCallBack {
        void onSuccess();
        void onError();
    }

    interface MaxTaskIdCallBack{
        int onFinish(int value);
    }

    public void getTasks(LoadTasksCallBack callBack);
    public void getTask(Integer taskId, GetTaskCallBack callBack);

    public void saveTask(@NonNull Task task, SaveTaskCallBack callBack);
    public void updateTask(@NonNull Task task, SaveTaskCallBack callBack);

    public void deleteTask(@NonNull String taskId);

    public int getMaxTaskId();

    void deleteAllTasks();
   // void completeTask(@NonNull String taskId);
  //  void clearCompletedTasks();

}
