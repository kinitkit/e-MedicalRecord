package com.example.kinit.e_medicalrecord.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.My_Physician.Bus_Search_Physician;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_Item;
import com.example.kinit.e_medicalrecord.Fragments.My_Physician.Fragment_My_Physician_List;
import com.example.kinit.e_medicalrecord.Fragments.My_Physician.Fragment_Search_Physician;
import com.example.kinit.e_medicalrecord.R;

public class My_Physician extends AppCompatActivity {
    int patient_id, user_id;
    String patient_name;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_physician);
        init();
    }

    void init() {
        patient_id = getIntent().getIntExtra("patient_id", 0);
        user_id = getIntent().getIntExtra("user_id", 0);
        patient_name = getIntent().getStringExtra("patient_name");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Physicians");
        getSupportActionBar().setSubtitle(patient_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment_My_Physician_List fragmentMyPhysicianList = new Fragment_My_Physician_List();
        fragmentMyPhysicianList.setPatient_id(patient_id);
        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentMyPhysicianList).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                fragmentManager.popBackStack();
                return true;
            }
        });

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BusStation.getBus().post(new Bus_Search_Physician(query, user_id));
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Fragment_Search_Physician fragmentSearchPhysician = new Fragment_Search_Physician();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frameLayout, fragmentSearchPhysician);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
