package course.labs.todo.tasks;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import course.labs.todo.addedittask.AddEditTaskActivity;
import course.labs.todo.model.Task;
import course.labs.todo.taskdetail.TaskDetailActivity;
import course.labs.todo.utils.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements TasksOperations.View {

    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    private LinearLayout mTasksView;
    private View mNoTasksView;
    private ImageView mNoTaskIcon;
    private TextView mNoTaskMainView;

    private TasksOperations.TasksPresenter mTaskPresenter;
    private TaskListAdapter mTaskListAdapter;

    private ListView mListView;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskListAdapter = new TaskListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskPresenter.start();
    }

    @Override
    public void setPresenter(TasksOperations.TasksPresenter presenter) {
        mTaskPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If a task was successfully added, show toast
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            ActivityUtils.showMessage(getView(), getString(R.string.msg_save));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.tasks_frag, container, false);

        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.tasks_list);
        listView.setAdapter(mTaskListAdapter);

        mTasksView = (LinearLayout) root.findViewById(R.id.tasksLL);
        // Set up  no tasks view
        mNoTasksView = root.findViewById(R.id.noTasks);
        mNoTaskIcon = (ImageView) root.findViewById(R.id.noTasksIcon);
        mNoTaskMainView = (TextView) root.findViewById(R.id.noTasksMain);

        // Set up floating action button, for adding a task
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskPresenter.addTask();
            }
        });

        // Show task details
       listView.setOnItemClickListener( new OnItemClickListener() {

           @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Task todo =
                        (Task) adapter.getItemAtPosition(position);
                Log.d(TAG, todo.toString());
                if (todo != null)
                    showTaskDetails(todo);
            }
        });

        return root;
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTaskListAdapter.setTaskList(tasks);

        mTasksView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
    }

    @Override
    public void showTaskDetails(Task task) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.getId());
        startActivity(intent);
    }

    @Override
    public void createNewTask() {

         Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
         startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showLoadingTasksError() {
        ActivityUtils.showToast(getContext(),getString(R.string.msg_error_load_tasks));
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {

        mTasksView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);

        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }

}
