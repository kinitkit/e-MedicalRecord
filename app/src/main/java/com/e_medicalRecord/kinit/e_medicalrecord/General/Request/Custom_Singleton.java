package com.e_medicalRecord.kinit.e_medicalrecord.General.Request;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Custom_Singleton extends Application {
    private static Custom_Singleton ourInstance;
    private RequestQueue requestQueue;
    private Context context;

    public Custom_Singleton(){

    }

    private Custom_Singleton(Context context) {
        this.context = context.getApplicationContext();
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized Custom_Singleton getInstance(Context context){
        if(ourInstance == null){
            ourInstance = new Custom_Singleton(context);
        }
        return ourInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T>void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
