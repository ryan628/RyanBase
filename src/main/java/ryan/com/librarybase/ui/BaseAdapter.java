package ryan.com.librarybase.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ryan.com.librarybase.modle.JumpIntentParam;

import java.util.List;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/12/10 10:07.
 */

public abstract class BaseAdapter<T,T1> extends android.widget.BaseAdapter {
    public Context mContext;
    private BaseActivity baseActivity;
    private List<T> datas;
    private LayoutInflater layoutInflater;
    private int layout;
    public Class<T1> T2;
    public BaseAdapter(BaseActivity baseActivity1,int res,Class<T1> T3) {
        super();
        this.mContext = baseActivity1.mContext;
        this.baseActivity=baseActivity1;
        this.layoutInflater = (LayoutInflater.from(baseActivity1.mContext));
        this.layout = res;
        this.T2 = T3;
    }

    public void setDatas(List<T> data){
        this.datas = data;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return this.datas == null?null:(position >= this.datas.size()?null:this.datas.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T1 holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(layout,parent,false);
            try {
                holder=T2.newInstance();
            } catch (Exception e) {
                holder=null;
            }
            initViews(holder,convertView);
            convertView.setTag(holder);

        }else{
            holder = (T1)convertView.getTag();
        }
        if(datas != null && datas.size()>0){
            initDatas(holder,getItem(position));
            initEvent(convertView,getItem(position));
        }
        return convertView;
    }

    public void jumpForResult(Class c, int requestCode) {
        baseActivity.jumpForResult(c,requestCode);
    }

    public void jumpForResult(Class c, int requestCode, JumpIntentParam... jp) {
        baseActivity.jumpForResult(c,requestCode,jp);
    }

    public void jump(Class c) {
        jump(mContext, c);
    }

    public void jump(Class c, JumpIntentParam... jp) {
        jump(mContext, c, jp);
    }

    public void jump(Context c, Class cl) {
        baseActivity.jump(c,cl);
    }

    public void jump(Context c, Class cl, JumpIntentParam... jp) {
        baseActivity.jump(c,cl,jp);
    }

    public abstract void initViews(T1 o,View v);
    public abstract void initDatas(T1 o,T t);
    public void initEvent(View o,T t){}
}
