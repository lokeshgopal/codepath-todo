package course.labs.todo.model.mediator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import course.labs.todo.model.Task;
import course.labs.todo.model.TaskDBOperations;
import course.labs.todo.model.TaskDBOperationsImpl;

/**
 * Created by Lokesh on 2/17/2017.
 *  This interacts with db to perform db operations on task database
 *  most of the calls are asynchronous in nature, to make sure the db operations are performed on a separate thread
 */

public class TasksMediatorImpl implements TasksMediator {

    private static TasksMediator INSTANCE =null;

    private TaskDBOperations mTaskDBOperations;

    private Exception mError;

    // prevent direct instantiating this class
    private TasksMediatorImpl(Context context) {

            mTaskDBOperations = TaskDBOperationsImpl.getInstance(context);
    }

    public static TasksMediator getInstance(Context context) {

        if (INSTANCE ==null)
            INSTANCE = new TasksMediatorImpl(context);

        return INSTANCE;
    }

    @Override
    public void saveTask(@NonNull Task task, SaveTaskCallBack callBack) {
           new storeTask(task,callBack).execute();
    }

    private class storeTask extends AsyncTask<Void, Void, Long> {

        long rowid;
        Task task;
        SaveTaskCallBack callBack;

        private storeTask(Task task, SaveTaskCallBack callBack){
            this.task = task;
            this.callBack = callBack;
        }

        @Override
        protected Long doInBackground(Void... params) {
            try {

                rowid = mTaskDBOperations.saveTask(task);
            } catch (Exception e) {
                mError = e;
            }
            return rowid;
        }

        @Override
        protected void onPostExecute(Long result) {

            if (mError != null || result == -1)
                callBack.onError();
            else
                callBack.onSuccess();
        }
    }

    @Override
    public void getTasks(LoadTasksCallBack callBack) {
              new fetchTasks().execute(callBack);
    }

    private class fetchTasks extends AsyncTask<LoadTasksCallBack, Void, List<Task>> {

        List<Task> tasks;
        LoadTasksCallBack callBack;

        @Override
        protected List<Task> doInBackground(LoadTasksCallBack... params) {
            try {
                callBack = params[0];
                tasks= mTaskDBOperations.getTasks();

            } catch (Exception e) {
                mError = e;
            }
            return tasks;
        }

        @Override
        protected void onPostExecute(List<Task>  result) {

            if (mError != null || result.isEmpty())
                callBack.onDataNotAvailable();
            else
                callBack.onTasksLoaded(result);
        }
    }

    @Override
    public void getTask(Integer taskId, GetTaskCallBack callBack) {

        new fetchTask(taskId, callBack).execute();
    }

    private class fetchTask extends AsyncTask<Void, Void, Task> {

        Integer taskId;
        GetTaskCallBack callBack;

        private fetchTask(Integer taskId, GetTaskCallBack callBack) {
            this.taskId = taskId;
            this.callBack = callBack;
        }

        Task task;
        @Override
        protected Task doInBackground(Void... params) {
            try {

                task= mTaskDBOperations.getTask(taskId);

            } catch (Exception e) {
                mError = e;
            }
            return task;
        }

        @Override
        protected void onPostExecute(Task result) {

            if (mError != null || result ==null)
                callBack.onDataNotAvailable();
            else
               callBack.onTaskLoaded(result);
        }
    }

    @Override
    public void updateTask(@NonNull Task task, SaveTaskCallBack callBack) {
        new modifyTask(task, callBack).execute();

    }

    private class modifyTask extends AsyncTask<Void, Void, Void> {

        Task task;
        SaveTaskCallBack callBack;

        private modifyTask(Task task, SaveTaskCallBack callBack) {
            this.task = task;
            this.callBack = callBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                 mTaskDBOperations.updateTask(task);

            } catch (Exception e) {
                mError = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            if (mError != null )
                callBack.onError();
            else
                callBack.onSuccess();
        }
    }

    // synchronous call
    @Override
    public int getMaxTaskId() {

        return mTaskDBOperations.getRowSequence();
         //new fetchMaxTaskId().execute(callBack);
    }

    private class fetchMaxTaskId extends AsyncTask<MaxTaskIdCallBack, Void, Integer> {

        MaxTaskIdCallBack callBack;
        @Override
        protected Integer doInBackground(MaxTaskIdCallBack... params) {
            int maxValue=0;
            try {
                callBack = params[0];
                maxValue= mTaskDBOperations.getRowSequence();

            } catch (Exception e) {
                mError = e;
            }
            return maxValue;
        }

        @Override
        protected void onPostExecute(Integer result) {

            callBack.onFinish(result);
        }
    }

    @Override
    public void deleteTask(@NonNull String taskId) {

        new removeTask().execute(taskId);
    }

    private class removeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {

                mTaskDBOperations.deleteTask(params[0]);

            } catch (Exception e) {
                mError = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
//
//            if (mError != null )
//                mListener.onError();
//            else
//                mListener.onSuccess();
        }

    }

    @Override
    public void deleteAllTasks() {

        new removeTasks().execute();
    }

    private class removeTasks extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                mTaskDBOperations.deleteAllTasks();

            } catch (Exception e) {
                mError = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
//            if (mError != null )
//                mListener.onError();
//            else
//                mListener.onSuccess();
        }
    }

  /*  @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }*/

}
