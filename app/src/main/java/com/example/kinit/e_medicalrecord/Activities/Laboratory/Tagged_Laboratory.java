package com.example.kinit.e_medicalrecord.Activities.Laboratory;

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
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Search_Tagged_MedicalPrescription;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Tagged_Physician_List;
import com.example.kinit.e_medicalrecord.Classes.User.Patient;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Search_Physician;
import com.example.kinit.e_medicalrecord.Fragments.Laboratory.Fragment_Tagged_Laboratory;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class Tagged_Laboratory extends AppCompatActivity {

    //Primitive Data Types
    int lab_id;

    //Classes
    ArrayList<Tagged_Physician_List> taggedPhysicianList;
    Patient patient;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_laboratory);
        init();
    }

    void init() {
        lab_id = getIntent().getIntExtra("lab_id", 0);
        patient = getIntent().getExtras().getParcelable("patient");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tagged Physicians");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment_Tagged_Laboratory fragmentTaggedLaboratory = new Fragment_Tagged_Laboratory();
        fragmentTaggedLaboratory.setLab_id(lab_id);
        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentTaggedLaboratory).commit();
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
                BusStation.getBus().post(new Bus_Search_Tagged_MedicalPrescription(query.trim(), patient.user_data_id, lab_id));
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
    public void onDestroy() {
        super.onDestroy();
    }
}
