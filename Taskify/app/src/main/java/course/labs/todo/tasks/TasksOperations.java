package course.labs.todo.tasks;

import java.util.List;

import course.labs.todo.common.BasePresenter;
import course.labs.todo.model.Task;
import course.labs.todo.common.BaseView;

/**
 * Created by Lokesh on 2/13/2017.
 *  Operations supported by the fragment (view) and the presenter
 */

public interface TasksOperations {

    interface View extends BaseView<TasksPresenter> {

        void showTasks(List<Task> tasks);
        void showTaskDetails(Task task);
        void createNewTask();
        void showLoadingTasksError();
    }

     interface TasksPresenter extends BasePresenter {

        void loadTasks();
        void addTask();
        void onConfigurationChange(View view);
    }

}

