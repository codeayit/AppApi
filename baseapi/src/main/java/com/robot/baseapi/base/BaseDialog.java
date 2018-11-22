package com.robot.baseapi.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.robot.baseapi.R;


/**
 * Created by lny on 2017/11/29.
 */

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialgTtyle);
        initDialg();
    }

    public abstract void initDialg();

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        setCancelable(false);
    }

    @Override
    public void show() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        super.show();
    }
}
