package ryan.com.librarybase.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by ryan on 16/3/16.
 */
public class MyListView extends PullToRefreshListView {
    public ListView listView;
    public MyListView(Context context) {
        super(context);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListView(Context context, Mode mode) {
        super(context, mode);
        initView();
    }

    public MyListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        initView();
    }

    private void initView()
    {
        setEmptyView();
        setScrollingWhileRefreshingEnabled(true);
        listView = getRefreshableView();
    }
}
