package course.labs.todo.taskdetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import course.labs.todo.common.RetainedFragmentManager;
import course.labs.todo.tasks.R;
import course.labs.todo.utils.ActivityUtils;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static final int REQUEST_EDIT_TASK =1;
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    /**
     * Used to retain the TaskDetailPresenter state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManger
            = new RetainedFragmentManager(this.getFragmentManager(),TAG);

    private TaskDetailFragment mTaskDetailFragment;

    private TaskDetailOperations.Presenter mTaskDetailOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setDisplayShowHomeEnabled(true);

        // Get the requested task id
        Integer taskId= getIntent().getIntExtra(EXTRA_TASK_ID,-1);

        mTaskDetailFragment =
                (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mTaskDetailFragment ==null) {
            mTaskDetailFragment = TaskDetailFragment.getInstance();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),mTaskDetailFragment, R.id.contentFrame);

        }

        onHandleConfigurationChange(taskId);

        mTaskDetailFragment.setPresenter(mTaskDetailOperations);
    }

    private void onHandleConfigurationChange(Integer taskId) {

        if (mRetainedFragmentManger.firstTimeIn()) {

            mTaskDetailOperations = new TaskDetailOperationsImpl(
                    getApplicationContext(), taskId, mTaskDetailFragment);

            mRetainedFragmentManger.put("TASK_DETAIL_PRESENTER",mTaskDetailOperations);

        } else {

            mTaskDetailOperations = mRetainedFragmentManger.get("TASK_DETAIL_PRESENTER");

            if (mTaskDetailOperations ==null) {

                mTaskDetailOperations = new TaskDetailOperationsImpl(
                        getApplicationContext(), taskId, mTaskDetailFragment);

                mRetainedFragmentManger.put("TASK_DETAIL_PRESENTER",mTaskDetailOperations);

            } else {

                mTaskDetailOperations.onConfigurationChange(mTaskDetailFragment);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit :
                mTaskDetailOperations.editTask();
                return true;
            case R.id.menu_delete:
                mTaskDetailOperations.deleteTask();
                mTaskDetailFragment.showTaskList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
