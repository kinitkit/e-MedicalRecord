package com.e_medicalRecord.kinit.e_medicalrecord.Admission.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Search_Tagged_MedicalPrescription;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Patient;
import com.e_medicalRecord.kinit.e_medicalrecord.Admission.Fragment.Fragment_Search_Physician;
import com.e_medicalRecord.kinit.e_medicalrecord.Admission.Fragment.Fragment_Tagged_Admission;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

public class Tagged_Admission extends AppCompatActivity {

    //Primitive Data Types
    int admission_id;

    //Classes
    Patient patient;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_tagged_admission);
        init();
    }

    void init() {
        admission_id = getIntent().getIntExtra("admission_id", 0);
        patient = getIntent().getExtras().getParcelable("patient");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tagged Physicians");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment_Tagged_Admission fragmentTaggedAdmission = new Fragment_Tagged_Admission();
        fragmentTaggedAdmission.setAdmission_id(admission_id);
        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragmentTaggedAdmission).commit();
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
                BusStation.getBus().post(new Bus_Search_Tagged_MedicalPrescription(query.trim(), patient.user_data_id, admission_id));
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
