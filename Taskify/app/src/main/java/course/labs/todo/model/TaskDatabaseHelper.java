package course.labs.todo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Lokesh on 2/15/2017.
 *  Helper object to setup the todo_tasks db
 */

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Database name.
     */
    private static String DATABASE_NAME =
            "todo_tasks.db";

    /**
     * Database version number, which is updated with each schema
     * change.
     */
    private static int DATABASE_VERSION = 2;

    /**
     * SQL statement used to create the Todo tasks table.
     */
    final String SQL_CREATE_TODO_TABLE =
            "CREATE TABLE "
                    + TasksContract.TaskEntry.TABLE_NAME + " ("
                    + TasksContract.TaskEntry._ID + " INTEGER PRIMARY KEY, "
                    + TasksContract.TaskEntry.COLUMN_NAME_TASK_ID+ " INTEGER,"
                    + TasksContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
                    + TasksContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT , "
                    + TasksContract.TaskEntry.COLUMN_NAME_DUEDATE + " TEXT , "
                    + TasksContract.TaskEntry.COLUMN_NAME_PRIORITY+ " TEXT , "
                    + TasksContract.TaskEntry.COLUMN_NAME_STATUS+ " INTEGER"
                    + " );";
    
    public TaskDatabaseHelper(Context context) {
        super(context,     
                   context.getCacheDir() 
                           + File.separator 
                           + DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    // only required for new version of Schema/DB change
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     // Delete the existing tables.
        db.execSQL("DROP TABLE IF EXISTS "
                + TasksContract.TaskEntry.TABLE_NAME);
        // Create the new tables.
        onCreate(db);
    }

}
