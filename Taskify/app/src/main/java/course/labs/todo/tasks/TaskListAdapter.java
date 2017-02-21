package course.labs.todo.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import course.labs.todo.model.Task;

/**
 * Created by Lokesh on 2/19/2017.
 */

public class TaskListAdapter extends BaseAdapter {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG =
            getClass().getSimpleName();

    private static LayoutInflater inflater = null;
    private List<Task> taskList = new ArrayList<Task>();
    private Context mContext;

    static class ViewHolder {
        TextView title;
        TextView priority;
        TextView dueDate;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder  holder = new ViewHolder();

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.todo_item, parent, false);

            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.priority = (TextView) rowView.findViewById(R.id.priority);
            holder.dueDate = (TextView) rowView.findViewById(R.id.date);

            rowView.setTag(holder);
        }
        else
            holder = (ViewHolder) rowView.getTag();

        final Task task = (Task) getItem(position);

        holder.title.setText(task.getTitle());
        holder.priority.setText(task.getPriority());
        holder.dueDate.setText(task.getDueDate());

        return rowView;
    }

    public List<Task> getTaskList() { return taskList; }

    public void addItem(Task listItem) {
        taskList.add(listItem);
        notifyDataSetChanged();
    }

    public void setTaskList(List<Task> list) {
        this.taskList = list;
        notifyDataSetChanged();
    }

    public void clearTaskList() {
        taskList.clear();
        notifyDataSetChanged();
    }

}
