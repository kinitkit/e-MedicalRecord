package com.example.kinit.e_medicalrecord.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kinit.e_medicalrecord.R;

public class Base_Activity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.kinit.e_medicalrecord", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("LOGIN_AUTHENTICATION", false);

        if(isLoggedIn){
            int user_id = sharedPreferences.getInt("user_id", 0), patient_id = sharedPreferences.getInt("patient_id", 0),
                    medicalStaff_id = sharedPreferences.getInt("medicalStaff_id", 0);

            intent = new Intent(getBaseContext(), Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user_id", user_id);
            intent.putExtra("patient_id", patient_id);
            intent.putExtra("medicalStaff_id", medicalStaff_id);
            startActivity(intent);
        }
        else{
            intent = new Intent(getBaseContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
