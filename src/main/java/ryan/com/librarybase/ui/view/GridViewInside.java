package ryan.com.librarybase.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 嵌套在ListView中的GridView ,
 * ListView直接嵌套android.widget.GridView会导致GridView只显示的第一行数据
 * 
 * @author runningorion
 * 
 */
public class GridViewInside extends GridView 
{   
	public GridViewInside(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GridViewInside(Context context) {
		super(context);
	}
	
	public GridViewInside(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 设置不滚动 
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	@Override
	protected void attachLayoutAnimationParameters(View child, android.view.ViewGroup.LayoutParams params, int index, int count) {
		super.attachLayoutAnimationParameters(child, params, index, count);
	}


	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
	}
	
	
	
	
	
	
	
}
