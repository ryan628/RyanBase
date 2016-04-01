package ryan.com.librarybase.ui.view;/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/5/13 21:19.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import ryan.com.librarybase.utils.Constants;
import ryan.com.librarybase.utils.Log;


public class Alert1DrawView extends View {
    private int x;
    private int y;
    private int w;
    private int h;

    public Alert1DrawView(Context context, int[] location, int w, int h) {
        super(context);
        this.x = location[0];
        this.y = location[1];
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
         * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
         * drawLine 绘制直线 drawPoin 绘制点
         */
        int rgb = Color.LTGRAY;
        try{
        int[] cl = new int[3];
        cl[0]= Constants.alertRB;
        cl[1]=Constants.alertGB;
        cl[2]=Constants.alertBB;
                String R = Integer.toHexString(cl[0]);
                String G = Integer.toHexString(cl[1]);
                String B = Integer.toHexString(cl[2]);
                String RGB = "#" + ("FF" + (R.length() == 1 ? "0" + R : R) + (G.length() == 1 ? "0" + G : G) + (B.length() == 1 ? "0" + B : B)).toUpperCase();
                rgb = Color.parseColor(RGB);
            } catch (Exception e) {
                Log.e("E:" + e.toString());
        }
        // 创建画笔
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);//充满
        p.setColor(rgb);
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        p.setStrokeWidth((float) 7.0);

        canvas.drawLine(1, 1, w, 1, p);
        canvas.drawLine(1, 1, 1, h, p);
        canvas.drawLine(w, 1, w, h, p);
        canvas.drawLine(1, h, w / 3, h, p);
        canvas.drawLine((int) Math.round(w / 1.5), h, w, h, p);

    }
}
