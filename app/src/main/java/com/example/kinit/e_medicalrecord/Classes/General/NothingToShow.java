package com.example.kinit.e_medicalrecord.Classes.General;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class NothingToShow {

    public static void showNothingToShow(ArrayList arrayList, RecyclerView recyclerView_Content, LinearLayout nothingToShow) {
        if (arrayList.size() > 0) {
            recyclerView_Content.setVisibility(View.VISIBLE);
            nothingToShow.setVisibility(View.GONE);
        } else {
            recyclerView_Content.setVisibility(View.GONE);
            nothingToShow.setVisibility(View.VISIBLE);
        }
    }

    public static void showNothingToShow(ArrayList arrayList, ExpandableListView expandableListView, LinearLayout nothingToShow) {
        if (arrayList.size() > 0) {
            expandableListView.setVisibility(View.VISIBLE);
            nothingToShow.setVisibility(View.GONE);
        } else {
            expandableListView.setVisibility(View.GONE);
            nothingToShow.setVisibility(View.VISIBLE);
        }
    }
}
