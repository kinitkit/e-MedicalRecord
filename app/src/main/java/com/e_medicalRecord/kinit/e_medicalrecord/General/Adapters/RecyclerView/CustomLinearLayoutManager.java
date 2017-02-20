package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;


public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    /*public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }*/

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return false;
    }
}
