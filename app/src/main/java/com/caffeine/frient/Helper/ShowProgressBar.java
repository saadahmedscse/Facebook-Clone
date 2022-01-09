package com.caffeine.frient.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.caffeine.frient.R;

public class ShowProgressBar {

    public Dialog progressBar;
    Context context;

    public ShowProgressBar() {
    }

    public ShowProgressBar(Context context) {
        this.context = context;
    }

    public void setContext(){
        progressBar = new Dialog(context);
    }

    public void show(){
        progressBar.setContentView(R.layout.progress_bar);
        progressBar.setCancelable(false);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.show();
    }

    public void dismiss(){
        progressBar.dismiss();
    }
}
