package ryan.com.librarybase.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewInside extends ListView {

	public ListViewInside(Context context) {
		super(context);
		init();
	}

	public ListViewInside(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ListViewInside(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	void init() {
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
