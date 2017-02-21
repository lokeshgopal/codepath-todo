package course.labs.todo.tasks;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import course.labs.todo.common.RetainedFragmentManager;
import course.labs.todo.utils.ActivityUtils;

public class TasksActivity extends AppCompatActivity {

    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    /**
     * Used to retain the TaskPresenter state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);
    //
    private TasksOperations.TasksPresenter mTaskPresenter;
    private TasksFragment mTasksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTasksFragment=
                (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mTasksFragment ==null) {
            //Create task fragment
            mTasksFragment = TasksFragment.newInstance();

            //Attach the fragment to Activity
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mTasksFragment, R.id.contentFrame);
        }

        // // Create  OR get the presenter
        handleConfigurationChanges();

        mTasksFragment.setPresenter(mTaskPresenter);
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {

            Log.d(TAG, "First time onCreate() call");

            // Create the TaskPresenter object one time.
            mTaskPresenter = new TasksOperationsImpl(getApplicationContext(), mTasksFragment);

            // Store the TaskPresenter into the RetainedFragmentManager.
            mRetainedFragmentManager.put("TASK_PRESENTER_STATE",
                    mTaskPresenter);

        } else {
            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured.
            Log.d(TAG, "Second or subsequent onCreate() call");

            // Obtain the TaskPresenter object from the
            // RetainedFragmentManager.
            mTaskPresenter =
                    mRetainedFragmentManager.get("TASK_PRESENTER_STATE");

            // This check shouldn't be necessary under normal
            // circumstances, but it's better to lose state than to
            // crash!

            if (mTaskPresenter == null) {
                // Create the TaskPresenter object one time.
                mTaskPresenter = new TasksOperationsImpl(getApplicationContext(), mTasksFragment);

                // Store the TaskPresenter into the RetainedFragmentManager.
                mRetainedFragmentManager.put("TASK_PRESENTER_STATE",
                        mTaskPresenter);

            } else {
                // Inform it that the runtime configuration change has
                // completed.
                mTaskPresenter.onConfigurationChange(mTasksFragment);
            }
        }
    }
}
