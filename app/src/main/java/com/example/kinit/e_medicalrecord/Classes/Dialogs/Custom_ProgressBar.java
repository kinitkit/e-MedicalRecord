package com.example.kinit.e_medicalrecord.Classes.Dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kinit.e_medicalrecord.R;

public class Custom_ProgressBar {
    LinearLayout linearProgress;
    View container;
    Activity activity;

    public Custom_ProgressBar(Activity activity) {
        this.activity = activity;
        linearProgress = (LinearLayout) this.activity.findViewById(R.id.linearProgress);
        container = this.activity.findViewById(R.id.container);
    }

    public void show(){
        activity.setProgressBarIndeterminateVisibility(true);
        linearProgress.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    public  void hide(){
        activity.setProgressBarIndeterminateVisibility(false);
        linearProgress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }
}
