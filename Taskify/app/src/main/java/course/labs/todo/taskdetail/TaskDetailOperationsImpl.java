package course.labs.todo.taskdetail;

import android.content.Context;

import com.google.common.base.Strings;

import java.lang.ref.WeakReference;

import course.labs.todo.model.Task;
import course.labs.todo.model.mediator.TasksMediator;
import course.labs.todo.model.mediator.TasksMediatorImpl;
import course.labs.todo.tasks.TasksOperations;

/**
 * Created by Lokesh on 2/18/2017.
 * This presenter object fetches the task details and forwards task editing and performs delete operations
 */

public class TaskDetailOperationsImpl implements TaskDetailOperations.Presenter {

    private TaskDetailOperations.View mTaskDetailsOperationView;
    private TasksMediator mTaskMediator;
    private Integer mTaskId;

    public TaskDetailOperationsImpl(Context context, Integer taskId, TaskDetailOperations.View view) {
        mTaskId = taskId;
        mTaskDetailsOperationView = view;
        mTaskMediator = TasksMediatorImpl.getInstance(context);
    }

    @Override
    public void start() {
        fetchTask();
    }

    private void fetchTask() {
        if (mTaskId ==-1) {
            mTaskDetailsOperationView.showTaskIsMissing();
            return;
        } else {
            mTaskMediator.getTask(mTaskId, new TasksMediator.GetTaskCallBack() {
                @Override
                public void onTaskLoaded(Task task) {
                    mTaskDetailsOperationView.showTaskDetails(task);
                }

                @Override
                public void onDataNotAvailable() {
                    mTaskDetailsOperationView.showTaskIsMissing();
                }
            });
        }
    }

    @Override
    public void editTask() {
        if (mTaskId ==-1) {
            mTaskDetailsOperationView.showTaskIsMissing();
            return;
        }
        mTaskDetailsOperationView.showEditTask(mTaskId);
    }

    @Override
    public void deleteTask() {

        if (mTaskId ==-1) {
            mTaskDetailsOperationView.showTaskIsMissing();
            return;
        } else {
            mTaskMediator.deleteTask(mTaskId.toString());
            mTaskDetailsOperationView.showTaskDeleted();
        }
    }

    @Override
    public void onConfigurationChange(TaskDetailOperations.View view) {
        mTaskDetailsOperationView = view;
    }


}
