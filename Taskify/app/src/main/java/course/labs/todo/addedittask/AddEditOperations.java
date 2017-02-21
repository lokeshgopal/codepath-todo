package course.labs.todo.addedittask;

import course.labs.todo.common.BasePresenter;
import course.labs.todo.common.BaseView;
import course.labs.todo.model.Task;

/**
 * Created by Lokesh on 2/18/2017.
 * supported method calls for the Add/Edit view (fragment) and presenter
 */

public interface AddEditOperations {

    interface View extends BaseView<Presenter> {

        void showTasksList();
        void showEmptyTaskError();
        void showTaskIsMissing();
        void showTaskDetails(Task task);
        void onSuccess();
        void onError();
    }

    interface Presenter extends BasePresenter {

        int getMaxTaskId();
        void saveTask(Task task);
        void populateTask();
        void onConfigurationChange(View view);

    }
}
