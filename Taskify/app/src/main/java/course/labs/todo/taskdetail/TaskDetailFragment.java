package course.labs.todo.taskdetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.common.base.Strings;

import course.labs.todo.addedittask.AddEditTaskActivity;
import course.labs.todo.addedittask.AddEditTaskFragment;
import course.labs.todo.model.Task;
import course.labs.todo.tasks.R;
import course.labs.todo.utils.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Has all the task details view objects hosted in this fragment
 */
public class TaskDetailFragment extends Fragment implements TaskDetailOperations.View {

    private static final  int REQUEST_EDIT_TASK = 1;

    private EditText mTaskTitle;
    private EditText mTaskDescription;
    private EditText mDueDate;
    private EditText mPriority;
    private EditText mStatus;

    private TaskDetailOperations.Presenter mTaskDetailOperations;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    public static TaskDetailFragment getInstance() {
        return new TaskDetailFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskDetailOperations.start();
    }

    @Override
    public void setPresenter(TaskDetailOperations.Presenter presenter) {
        mTaskDetailOperations = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.taskdetail_frag, container, false);

        // Get references to view objects
        mTaskTitle = (EditText) root.findViewById(R.id.task_name);
        mTaskDescription = (EditText) root.findViewById(R.id.task_desc);
        mDueDate = (EditText) root.findViewById(R.id.due_date);
        mPriority = (EditText) root.findViewById(R.id.priority);
        mStatus = (EditText) root.findViewById(R.id.status);

        return root;
    }

    @Override
    public void showEditTask(Integer taskId) {

        Intent intent =  new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent,REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        ActivityUtils.showToast(getContext(), getString(R.string.msg_deleted));
    }

    @Override
    public void showTaskIsMissing() {
        ActivityUtils.showToast(getContext(),getString(R.string.msg_missing_task));
    }

    @Override
    public void showTaskDetails(Task task) {

        String title = task.getTitle();
        String description = task.getDescription();
        String dueDate = task.getDueDate();
        String priority = task.getPriority();
        String status = (task.isStatus() ==false?getString(R.string.txt_not_done):getString(R.string.txt_done));

        mTaskTitle.setText(title);
        mTaskDescription.setText(description);
        mDueDate.setText(dueDate);
        mPriority.setText(priority);
        mStatus.setText(status);
    }

    @Override
    public void showTaskList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

}
