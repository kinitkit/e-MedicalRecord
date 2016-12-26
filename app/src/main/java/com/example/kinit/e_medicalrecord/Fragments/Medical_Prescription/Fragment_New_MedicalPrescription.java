package com.example.kinit.e_medicalrecord.Fragments.Medical_Prescription;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Adapters.RecyclerViewAdapter_Drug_List;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Drug;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Open_Add_Drug;
import com.example.kinit.e_medicalrecord.BusStation.Bus_ToolbarTitle;
import com.example.kinit.e_medicalrecord.BusStation.Pop_BackStack;
import com.example.kinit.e_medicalrecord.Classes.DatePickerFragment;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.Classes.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fragment_New_MedicalPrescription extends Fragment implements View.OnClickListener {
    //View
    View rootView;

    //Primitive Data Types
    int patient_id, user_id;

    //Widgets
    EditText et_clinic, et_physicianName, et_prescriptionDate;
    Button btn_add, btn_save;
    RecyclerView recyclerView_Content;
    RecyclerView.Adapter recyclerViewAdapter_Content;
    RecyclerView.LayoutManager recyclerViewLayoutM_Content;

    //Classes
    ArrayList<Bus_Drug> busDrugs;
    Viewer viewer;
    DatePickerFragment datePickerFragment;

    //App
    Custom_AlertDialog alertDialog;

    //Utils
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id");
            user_id = bundle.getInt("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_medical_prescription, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        alertDialog = new Custom_AlertDialog(getActivity());
        busDrugs = new ArrayList<>();
        et_clinic = (EditText) rootView.findViewById(R.id.et_clinic);
        et_physicianName = (EditText) rootView.findViewById(R.id.et_physicianName);
        et_prescriptionDate = (EditText) rootView.findViewById(R.id.et_prescriptionDate);
        et_prescriptionDate.setOnClickListener(this);

        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        recyclerView_Content = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        loadToRecyclerView();

        calendar = Calendar.getInstance();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown()) {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    setCalendar(calendar);
                }
            }
        });

        setCalendar(calendar);
        if (viewer != null) {
            et_physicianName.setFocusableInTouchMode(false);
            et_physicianName.setKeyListener(null);
            et_physicianName.setText(viewer.name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                BusStation.getBus().post(new Bus_Open_Add_Drug());
                break;

            case R.id.btn_save:
                Custom_AlertDialog alertDialog = new Custom_AlertDialog(getActivity());
                String clinic = et_clinic.getText().toString().trim(),
                        physicianName = et_physicianName.getText().toString().trim();
                if (busDrugs.size() > 0) {
                    if (!physicianName.isEmpty()) {
                        sendData(physicianName, clinic);
                    } else {
                        et_physicianName.setError("Required Field");
                    }
                } else {
                    alertDialog.show("Ooops!", "Please add a drug/medication");
                }
                break;

            case R.id.et_prescriptionDate:
                datePickerFragment.show(getFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
                break;
        }
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void insertDrug(Bus_Drug busDrug) {
        this.busDrugs.add(busDrug);
        recyclerViewAdapter_Content.notifyDataSetChanged();
    }

    public void updateDrug(Bus_Drug busDrug) {
        this.busDrugs.set(busDrug.position, busDrug);
        recyclerViewAdapter_Content.notifyDataSetChanged();
    }

    void loadToRecyclerView() {
        recyclerViewAdapter_Content = new RecyclerViewAdapter_Drug_List(busDrugs);
        recyclerViewLayoutM_Content = new LinearLayoutManager(getActivity());
        recyclerView_Content.setLayoutManager(recyclerViewLayoutM_Content);
        recyclerView_Content.setAdapter(recyclerViewAdapter_Content);
        setupItemTouchHelper();
    }

    void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RecyclerViewAdapter_Drug_List adapter_surgery = (RecyclerViewAdapter_Drug_List) recyclerView_Content.getAdapter();
                deleteItemConfirmation(swipedPosition, adapter_surgery);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_Content);
    }

    void deleteItemConfirmation(final int position, final RecyclerViewAdapter_Drug_List adapter_drug) {
        alertDialog.builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter_drug.remove(position, true);
                busDrugs.remove(position);
                dialog.dismiss();
            }
        });
        alertDialog.builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter_drug.remove(position, false);
                dialog.dismiss();
            }
        });
        alertDialog.show("Confirm delete", "Are you sure you want to permanently delete the item?");
    }

    void sendData(final String physicianName, final String clinicName) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    if (jsonObject.getString("code").equals("successful")) {
                                        BusStation.getBus().post(new Pop_BackStack());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "insertNewMedPrescription");
                    params.put("device", "mobile");
                    params.put("patient_id", String.valueOf(patient_id));
                    params.put("clinic_name", clinicName);
                    params.put("physician_name", physicianName);
                    params.put("date_prescribed", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    if (viewer != null) {
                        params.put("user_data_id", String.valueOf(viewer.user_id));
                    } else {
                        params.put("user_data_id", String.valueOf(user_id));
                    }
                    for (int x = 0; x < busDrugs.size(); x++) {
                        params.put("drug[" + (x) + "]", busDrugs.get(x).drug);
                        params.put("strength[" + (x) + "]", busDrugs.get(x).strength);
                        params.put("dosage[" + (x) + "]", busDrugs.get(x).amount);
                        params.put("route[" + (x) + "]", busDrugs.get(x).route);
                        params.put("frequency[" + (x) + "]", busDrugs.get(x).frequency);
                        params.put("indication[" + (x) + "]", busDrugs.get(x).why);
                        params.put("many[" + (x) + "]", busDrugs.get(x).many);
                        params.put("refill[" + (x) + "]", busDrugs.get(x).refill);
                    }
                    return params;
                }
            };
            Custom_Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_prescriptionDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().post(new Bus_ToolbarTitle("New Medical Prescription", null));
    }
}
