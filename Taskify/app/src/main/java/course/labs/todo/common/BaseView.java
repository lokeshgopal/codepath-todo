package course.labs.todo.common;

/**
 * Created by Lokesh on 2/14/2017.
 * This method will set the presenter for a given view (fragment)
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

}
