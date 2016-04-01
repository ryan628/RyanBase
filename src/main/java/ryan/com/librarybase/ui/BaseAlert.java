package ryan.com.librarybase.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ryan.com.librarybase.AppManager;
import ryan.com.librarybase.R;
import ryan.com.librarybase.ui.view.Alert1DrawView;
import ryan.com.librarybase.ui.view.Alert2DrawView;
import ryan.com.librarybase.utils.Constants;


public class BaseAlert extends BaseActivity {
    private LinearLayout layout;
    private String type;
    private String okName;
    private String cancelName;
    private String customName;
    private String customTitle;
    private String customContent;
    private int isUptate;
    private int finishType=0;
    private Button ok;
    public  Button alert_cancel;
    private TextView Title;
    private TextView Content;
    public  Button alert_custom;
    private boolean isOne;

    @Override
    public void initContentView() {
        type = getIntent().getExtras()==null?"":getIntent().getExtras().getString("type", "1");
        okName = getIntent().getExtras()==null?"":getIntent().getExtras().getString("okName", getResources().getString(R.string.OK));
        cancelName = getIntent().getExtras()==null?"":getIntent().getExtras().getString("cancelName", getResources().getString(R.string.cancel));
        customName = getIntent().getExtras()==null?"":getIntent().getExtras().getString("customName", getResources().getString(R.string.OK));
        customTitle = getIntent().getExtras()==null?"":getIntent().getExtras().getString("title", getResources().getString(R.string.prompt));
        customContent = getIntent().getExtras()==null?"":getIntent().getExtras().getString("content", "content");
        isUptate = getIntent().getExtras()==null?0:getIntent().getExtras().getInt("update", 0);
        finishType = getIntent().getExtras()==null?0:getIntent().getExtras().getInt("finish_type", 0);

        if (type.equals("1")) {
            isOne = true;
            setContentView(R.layout.base_alert1);
        } else {
            isOne = false;
            setContentView(R.layout.base_alert2);
        }
    }

    @Override
    public void initView() {
        if (isOne) {
            layout = (LinearLayout) findViewById(R.id.my_alert1);

            final RelativeLayout layout = (RelativeLayout) findViewById(R.id.alertBg1);
            ViewTreeObserver vto2 = layout.getViewTreeObserver();
            vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int[] location = new int[2];
                    layout.getLocationOnScreen(location);
                    final Alert1DrawView view = new Alert1DrawView(mContext, location, layout.getWidth(), layout.getHeight());
                    view.invalidate();
                    layout.addView(view);
                }
            });
            Title = (TextView) findViewById(R.id.my_alert1_title);
            Content = (TextView) findViewById(R.id.my_alert1_content);
            ok = (Button) findViewById(R.id.alert_ok);
            try {
                int[] cl = new int[3];
                cl[0] = Constants.alertR;
                cl[1] = Constants.alertG;
                cl[2] = Constants.alertB;
                Title.setTextColor(Color.rgb(cl[0], cl[1], cl[2]));
                Content.setTextColor(Color.rgb(Constants.alertRC, Constants.alertGC, Constants.alertBC));
                ok.setTextColor(Color.rgb(cl[0], cl[1], cl[2]));
            } catch (Exception e) {
            }
            Title.setText(customTitle);
            Content.setText(Html.fromHtml(customContent));
            Content.setMovementMethod(LinkMovementMethod.getInstance());
            ok.setText(okName);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = getIntent();
                    setResult(isUptate == 1 ? 3 : 4, it);
                    it.putExtra("type",finishType);
                    close();
                }
            });
        } else {
            layout = (LinearLayout) findViewById(R.id.my_alert2);
            Title = (TextView) findViewById(R.id.my_alert2_title);
            Content = (TextView) findViewById(R.id.my_alert2_content);
            alert_cancel = (Button) findViewById(R.id.alert_cancel);
            alert_custom = (Button) findViewById(R.id.alert_custom);
            final RelativeLayout layout = (RelativeLayout) findViewById(R.id.alertBg2);
            ViewTreeObserver vto2 = layout.getViewTreeObserver();
            vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int[] location = new int[2];
                    layout.getLocationOnScreen(location);
                    final Alert2DrawView view = new Alert2DrawView(mContext, location, layout.getWidth(), layout.getHeight());
                    view.invalidate();
                    layout.addView(view);
                }
            });
            int[] cl = new int[]{Constants.alertR, Constants.alertG, Constants.alertB};
            Title.setTextColor(Color.rgb(cl[0], cl[1], cl[2]));
            Content.setTextColor(Color.rgb(Constants.alertRC, Constants.alertGC, Constants.alertBC));
            alert_cancel.setTextColor(Color.rgb(cl[0], cl[1], cl[2]));
            alert_custom.setTextColor(Color.rgb(cl[0], cl[1], cl[2]));

            Title.setText(customTitle);
            Content.setText(Html.fromHtml(customContent));
            Content.setMovementMethod(LinkMovementMethod.getInstance());
            alert_cancel.setText(cancelName);
            alert_custom.setText(customName);
            alert_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = getIntent();
                    setResult(isUptate == 1 ? 3 : 2, it);
                    it.putExtra("type",finishType);
                    close();
                }
            });
            alert_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = getIntent();
                    setResult(1, it);
                    it.putExtra("type",finishType);
                    close();
                }
            });
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void afterOnCreate() {

    }

    @Override
    public void close() {
        AppManager.getAppManager().finishActivity(this);
    }
}
