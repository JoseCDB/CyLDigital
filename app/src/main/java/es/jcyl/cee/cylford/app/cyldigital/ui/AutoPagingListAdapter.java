package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Adaptador genérico que notifica cuando la lista alcanza su final. Util para auto-paginación.
 * @param <T>
 */
public abstract class AutoPagingListAdapter<T> extends ArrayAdapter<T> implements
        AbsListView.OnScrollListener {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_LOADING = 1;

    public AutoPagingListAdapter(Context context, int textViewResourceId,
                                 List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    /** A lock to prevent another scrolling event to be triggered if
     *  one is already in session */
    protected boolean canScroll = false;

    protected abstract void onScrollNext();

    public void lock() {
        canScroll = false;
    }

    public void unlock() {
        canScroll = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount - 1 == getCount() && canScroll) {
            onScrollNext();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        if( getItem(position) == null){
            return TYPE_LOADING;
        }else{
            return TYPE_ITEM;
        }
    }


}