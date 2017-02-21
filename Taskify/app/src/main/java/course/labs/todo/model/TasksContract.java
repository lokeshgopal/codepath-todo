package course.labs.todo.model;

import android.provider.BaseColumns;

/**
 * Created by Lokesh on 2/15/2017.
 *  setup the table columns for storing tasks
 */

public final class TasksContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TasksContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TASK_ID = "taskid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DUEDATE = "date";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_STATUS = "status";
    }

}
