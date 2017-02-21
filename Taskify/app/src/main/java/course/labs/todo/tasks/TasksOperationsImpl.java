package course.labs.todo.tasks;

import android.content.Context;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import course.labs.todo.model.Task;
import course.labs.todo.model.mediator.TasksMediator;
import course.labs.todo.model.mediator.TasksMediatorImpl;

/**
 * Created by Lokesh on 2/15/2017.
 *  This presenter supports initiation of Adding a task, fetch the data from SQLite db and populate the list view
 *  it handles the configuration change of the device.
 */

public class TasksOperationsImpl implements TasksOperations.TasksPresenter {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    private TasksOperations.View mTaskOperationView;

    private TasksMediator mTasksMediator;

    public TasksOperationsImpl(Context context, TasksOperations.View view) {

        mTasksMediator = TasksMediatorImpl.getInstance(context);
        mTaskOperationView = view;
        mTaskOperationView.setPresenter(this);
    }

    @Override
    public void onConfigurationChange(TasksOperations.View view) {
        mTaskOperationView = view;
    }

    @Override
    public void loadTasks() {

        // set the progress bar
        mTasksMediator.getTasks(new TasksMediator.LoadTasksCallBack() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {

                List<Task> sortedTasks = getTasksSorted(tasks);
                mTaskOperationView.showTasks(sortedTasks);
            }

            @Override
            public void onDataNotAvailable() {
                mTaskOperationView.showLoadingTasksError();
            }
        });
        // unset the progress bar
    }

   // very simple sorting based on priority
    private List<Task> getTasksSorted(List<Task> tasks) {

       Task[] unSortedTasks = tasks.toArray(new Task[tasks.size()]);

        Arrays.sort(unSortedTasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getPriority().compareTo(task2.getPriority());
            }
        });

        return Arrays.asList(unSortedTasks);
    }

    @Override
    public void addTask() {
        mTaskOperationView.createNewTask();
    }

    @Override
    public void start() {
        loadTasks();
    }
}
