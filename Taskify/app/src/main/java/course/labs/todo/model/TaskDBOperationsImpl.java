package course.labs.todo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import course.labs.todo.model.TasksContract.TaskEntry;


/**
 * Created by Lokesh on 2/15/2017.
 *  Implements all the db operations on the tasks
 */

public class TaskDBOperationsImpl implements TaskDBOperations {

    private static TaskDBOperationsImpl INSTANCE=null;

    private TaskDatabaseHelper mTaskDbHelper;

    // Prevent direct instantiation
    private TaskDBOperationsImpl(@NonNull Context context) {
        checkNotNull(context);
        mTaskDbHelper = new TaskDatabaseHelper(context);
    }

    public static TaskDBOperationsImpl getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TaskDBOperationsImpl(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_TASK_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_DUEDATE,
                TaskEntry.COLUMN_NAME_PRIORITY,
                TaskEntry.COLUMN_NAME_STATUS
        };

        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Integer itemId = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TASK_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                String dueDate =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DUEDATE));
                String priority =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PRIORITY));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_STATUS)) == 1;
                Task task = new Task(itemId, title, description, dueDate, priority,completed);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return tasks;
    }

    @Override
    public Task getTask(Integer taskId) {

        SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_TASK_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_DUEDATE,
                TaskEntry.COLUMN_NAME_PRIORITY,
                TaskEntry.COLUMN_NAME_STATUS
        };

        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " = ?";
        String[] selectionArgs = { taskId.toString() };

        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Task task = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Integer itemId = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TASK_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                String dueDate =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DUEDATE));
                String priority =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PRIORITY));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_STATUS)) == 1;
                 task = new Task(itemId, title, description, dueDate, priority,completed);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return task;
    }

    @Override
    public long saveTask(@NonNull Task task) {

        long rowid= -1;
        checkNotNull(task);
        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_ID,task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_DUEDATE, task.getDueDate());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, task.getPriority());
        values.put(TaskEntry.COLUMN_NAME_STATUS, task.isStatus());
        rowid= db.insert(TaskEntry.TABLE_NAME, null, values);

        db.close();

        return rowid;
    }

    @Override
    public void completeTask(@NonNull String taskId) {

        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_STATUS, true);

        String selection = TaskEntry.COLUMN_NAME_TASK_ID+ " = ?";
        String[] selectionArgs = {taskId };

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void updateTask(@NonNull Task task) {

        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_DUEDATE, task.getDueDate());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, task.getPriority());
        values.put(TaskEntry.COLUMN_NAME_STATUS, task.isStatus());

        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " = ?";
        String[] selectionArgs = { task.getId().toString() };

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void clearCompletedTasks() {

        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_STATUS+ " = ?";
        String[] selectionArgs = { "1" };

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteAllTasks() {

        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_TASK_ID+ " = ?";
        String[] selectionArgs = { taskId };

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public int getRowSequence() {
        SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        String sql = "select max(taskid) from task";
        Cursor c = db.rawQuery(sql,null);

        int maxvalue =0;
        if (c!=null && c.moveToFirst()) {
            maxvalue = c.getInt(0);
            c.close();
        }
        db.close();
        return maxvalue;
    }
}
