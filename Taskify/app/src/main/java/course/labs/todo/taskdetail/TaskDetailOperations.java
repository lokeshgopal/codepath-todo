package course.labs.todo.taskdetail;

import course.labs.todo.common.BasePresenter;
import course.labs.todo.common.BaseView;
import course.labs.todo.model.Task;

/**
 * Created by Lokesh on 2/18/2017.
 */

public interface TaskDetailOperations {

    interface View extends BaseView<Presenter> {

        void showEditTask(Integer taskId);
        void showTaskDeleted();
        // show message on missing task
        void showTaskIsMissing();
        // Show task details in UI
        void showTaskDetails(Task task);
        // show task list UI
        void showTaskList();
    }

    interface Presenter extends BasePresenter{

        void editTask();
        void deleteTask();
        void onConfigurationChange(View view);
    }
}
