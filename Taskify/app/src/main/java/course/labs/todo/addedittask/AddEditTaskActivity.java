package course.labs.todo.addedittask;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import course.labs.todo.common.RetainedFragmentManager;
import course.labs.todo.model.Task;
import course.labs.todo.utils.ActivityUtils;
import course.labs.todo.tasks.R;
/**
 * Created by Lokesh on 2/18/2017.
 * This activity supports adding a new task and editing an existing task
 */

public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    /**
     * Used to retain the AddEditTaskPresenter state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);

    private AddEditTaskFragment mAddEditTaskFragment;

    private AddEditOperations.Presenter mAddEditTaskPresenter;
    private int mTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.addtasks_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTaskId = getIntent().getIntExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID,-1);

        mAddEditTaskFragment =
                (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mAddEditTaskFragment ==null) {
            mAddEditTaskFragment = AddEditTaskFragment.newInstance();

            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                actionBar.setTitle(getString(R.string.edit_activity_title));
            } else {
                actionBar.setTitle(getString(R.string.add_activity_title));
            }
        }

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), mAddEditTaskFragment, R.id.contentFrame);

        // Create  OR get the presenter
        handleConfigurationChanges(mTaskId);

        mAddEditTaskFragment.setPresenter(mAddEditTaskPresenter);
    }

    private void handleConfigurationChanges(int taskId) {

        if (mRetainedFragmentManager.firstTimeIn()) {

            Log.d(TAG, "First time onCreate() call");

            // Create the AddEditTaskPresenter object one time.
            mAddEditTaskPresenter = new AddEditOperationsImpl(
                    getApplicationContext(),taskId, mAddEditTaskFragment);

            // Store the AddEditTaskPresenter into the RetainedFragmentManager.
            mRetainedFragmentManager.put("ADDEDIT_TASK_PRESENTER",
                    mAddEditTaskPresenter);

        } else  {
            //Get the AddEditTaskPresenter
            mAddEditTaskPresenter = mRetainedFragmentManager.get("ADDEDIT_TASK_PRESENTER");

            if (mAddEditTaskPresenter == null) {

                mAddEditTaskPresenter = new AddEditOperationsImpl(
                        getApplicationContext(), taskId,mAddEditTaskFragment);

                // Store the AddEditTaskPresenter into the RetainedFragmentManager.
                mRetainedFragmentManager.put("ADDEDIT_TASK_PRESENTER",
                        mAddEditTaskPresenter);
                }
            else {
                mAddEditTaskPresenter.onConfigurationChange(mAddEditTaskFragment);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save :
                Task task;
                // Check if the save is for a new task or updating a existing task
                if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID))
                    task = mAddEditTaskFragment.getUpdateTaskDetails();
                else
                     task = mAddEditTaskFragment.getNewTaskDetails();

                if (task.isEmpty())
                    mAddEditTaskFragment.showEmptyTaskError();
                else {
                    mAddEditTaskPresenter.saveTask(task);
                    mAddEditTaskFragment.showTasksList();
                }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
