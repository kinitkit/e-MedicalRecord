package com.example.kinit.e_medicalrecord.Request;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Custom_Singleton extends Application {
    private static Custom_Singleton ourInstance;
    private RequestQueue requestQueue;
    private static Context context;

    public Custom_Singleton(){

    }

    private Custom_Singleton(Context context) {
        this.context = context;
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Custom_Singleton getInstance(Context context){
        if(ourInstance == null){
            ourInstance = new Custom_Singleton(context);
        }
        return ourInstance;
    }

    public <T>void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
