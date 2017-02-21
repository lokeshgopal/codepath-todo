package course.labs.todo.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import course.labs.todo.model.Task;
import course.labs.todo.tasks.R;
import course.labs.todo.utils.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lokesh on 2/18/2017.
 * This fragment host view objects that supports adding a new task and editing an existing task
 */

public class AddEditTaskFragment extends Fragment implements AddEditOperations.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private EditText mTaskName;
    private EditText mTaskDescription;
    private DatePicker mDueDate;
    private Spinner mPriority;
    private Spinner mStatus;
    private int mTaskId =-1;

    private String mSetDueDate;
    private ArrayAdapter<CharSequence> mPriorityAdapter;
    private ArrayAdapter<CharSequence> mStatusAdapter;

    private AddEditOperations.Presenter mAddEditOperationsPresenter;


    public AddEditTaskFragment() {
    }

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddEditOperationsPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.addtasks_frag,container,false);

        mTaskName = (EditText) root.findViewById(R.id.task_name);
        mTaskDescription = (EditText)  root.findViewById(R.id.task_desc);
        mDueDate = (DatePicker) root.findViewById(R.id.due_date);
        mPriority = (Spinner) root.findViewById(R.id.priority);
        mStatus = (Spinner) root.findViewById(R.id.status);

        // set the default date
        mSetDueDate = (mDueDate.getMonth() +1) +"/" +mDueDate.getDayOfMonth() +"/"+mDueDate.getYear();

        // Get updated date after user changes the date
        mDueDate.init(mDueDate.getYear(),
                mDueDate.getMonth(),
                mDueDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         int month = monthOfYear +1;
                         mSetDueDate = month +"/"+dayOfMonth + "/" + year;
                      //  ActivityUtils.showToast(getContext(), year+"-"+month +"-"+dayOfMonth);
                    }
                }
        );

        // Create an Adapter that holds list of priorities
        mPriorityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_status, R.layout.dropdown_item );

        mPriorityAdapter.setDropDownViewResource(R.layout.dropdown_item);
        // Set the Adapter for the spinner
        mPriority.setAdapter(mPriorityAdapter);

        //Create an Adapter that holds task status
        mStatusAdapter= ArrayAdapter.createFromResource(
                getContext(),R.array.todo_status, R.layout.dropdown_item);

        mStatusAdapter.setDropDownViewResource(R.layout.dropdown_item);
        // Set the Adapter for the spinner
        mStatus.setAdapter(mStatusAdapter);
        // Set the default value for status
        mStatus.setSelection(mStatusAdapter.getPosition(getString(R.string.txt_not_done)));

        return root;
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showEmptyTaskError() {
        ActivityUtils.showToast(getContext(),getString(R.string.msg_missing_taskinfo));
    }

    @Override
    public void showTaskIsMissing() {
        ActivityUtils.showToast(getContext(),getString(R.string.msg_missing_task));
    }

    // Show the task details in the UI
    @Override
    public void showTaskDetails(Task task) {

        mTaskId = task.getId();
        mTaskName.setText(task.getTitle());
        mTaskDescription.setText(task.getDescription());

        // set the due date.. which is in mm/dd/yyyy format
        String dueDate = task.getDueDate();
        String delims = "[/]";
        String[] fields =dueDate.split(delims);

        mDueDate.updateDate(Integer.parseInt(fields[2]),Integer.parseInt(fields[0])-1,Integer.parseInt(fields[1]));

        mPriority.setSelection(mPriorityAdapter.getPosition(task.getPriority()));

        String status = task.isStatus() ? getString(R.string.txt_done):getString(R.string.txt_not_done);
        mStatus.setSelection(mStatusAdapter.getPosition(status));
    }

    @Override
    public void onSuccess() {
        ActivityUtils.showToast(getContext(), getString(R.string.msg_save));
    }

    @Override
    public void onError() {
        ActivityUtils.showToast(getContext(),getString(R.string.msg_save_error));
    }

    public Task getNewTaskDetails() {

        return new Task(mAddEditOperationsPresenter.getMaxTaskId()+1,
                mTaskName.getText().toString(),
                mTaskDescription.getText().toString(),
                mSetDueDate,
                mPriority.getSelectedItem().toString(),
                mStatus.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.txt_done))?true:false);
    }

    public Task getUpdateTaskDetails() {

        return new Task(mTaskId,
                mTaskName.getText().toString(),
                mTaskDescription.getText().toString(),
                mSetDueDate,
                mPriority.getSelectedItem().toString(),
                mStatus.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.txt_done))?true:false);
    }

    @Override
    public void setPresenter(AddEditOperations.Presenter presenter) {
        mAddEditOperationsPresenter = checkNotNull(presenter);
    }
}
