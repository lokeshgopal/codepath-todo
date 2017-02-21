package course.labs.todo.addedittask;

import android.content.Context;

import course.labs.todo.model.Task;
import course.labs.todo.model.mediator.TasksMediator;
import course.labs.todo.model.mediator.TasksMediatorImpl;

/**
 * Created by Lokesh on 2/18/2017.
 *  Presenter for Add/Update task, it fetches an existing task for editing
 */

public class AddEditOperationsImpl implements AddEditOperations.Presenter {

    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    private AddEditOperations.View mAddEditOperationsView;
    private TasksMediator mTasksMediator;
    private int mTaskId;

    public AddEditOperationsImpl(Context context, int taskId, AddEditOperations.View view) {
        mTaskId = taskId;
        mAddEditOperationsView =view;
        mTasksMediator = TasksMediatorImpl.getInstance(context);
    }

    @Override
    public void start() {
        // if its not a new task, populate the task details
        if (!isNewTask())
            populateTask();

    }

    @Override
    public int getMaxTaskId() {
        return mTasksMediator.getMaxTaskId();
    }

    @Override
    public void saveTask(Task task) {
        // save new task and update existing task
        if (isNewTask()) {
            createTask(task);
        } else {
            updateTask(task);
        }
    }

    private void createTask(Task task) {

        mTasksMediator.saveTask(task, new TasksMediator.SaveTaskCallBack() {
            @Override
            public void onSuccess() {
                mAddEditOperationsView.onSuccess();
            }

            @Override
            public void onError() {
                mAddEditOperationsView.onError();
            }
        });
    }

    private  void updateTask(Task task) {

        mTasksMediator.updateTask(task, new TasksMediator.SaveTaskCallBack() {
            @Override
            public void onSuccess() {
                mAddEditOperationsView.onSuccess();
            }

            @Override
            public void onError() {
                mAddEditOperationsView.onError();
            }
        });
    }

    @Override
    public void populateTask() {

        if (isNewTask()) {
            mAddEditOperationsView.showTaskIsMissing();
            return;
        } else {
            mTasksMediator.getTask(mTaskId, new TasksMediator.GetTaskCallBack() {
                @Override
                public void onTaskLoaded(Task task) {
                    mAddEditOperationsView.showTaskDetails(task);
                }

                @Override
                public void onDataNotAvailable() {
                    mAddEditOperationsView.showTaskIsMissing();
                }
            });
        }
    }

    private boolean isNewTask() {
        return mTaskId == -1;
    }

    @Override
    public void onConfigurationChange(AddEditOperations.View view) {
        mAddEditOperationsView = view;
    }

}
