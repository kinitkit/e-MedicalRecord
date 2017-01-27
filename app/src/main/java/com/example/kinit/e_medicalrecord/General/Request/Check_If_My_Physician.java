package com.example.kinit.e_medicalrecord.General.Request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Check_If_My_Physician {
    Context context;
    boolean isMyPhysician = false;

    public Check_If_My_Physician(Context context) {
        this.context = context;
    }

    public boolean isMyPhysician(final int medicalStaffId){
        try{
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isMyPhysician = true;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "isMyPhysician");
                    params.put("device", "mobile");
                    params.put("medical_staff_id", String.valueOf(medicalStaffId));
                    return params;
                }
            };
            Custom_Singleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
        return isMyPhysician;
    }
}
