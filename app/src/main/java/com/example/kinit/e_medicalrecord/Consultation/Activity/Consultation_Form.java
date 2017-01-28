package com.example.kinit.e_medicalrecord.Consultation.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Consultation.Bus.Bus_ConsultationROS;
import com.example.kinit.e_medicalrecord.Consultation.Bus.Bus_ConsultationRos_OnLongClick;
import com.example.kinit.e_medicalrecord.Consultation.Class.ArrayAdapter_Strings;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;
import com.example.kinit.e_medicalrecord.Consultation.Class.Review_Of_Systems;
import com.example.kinit.e_medicalrecord.Consultation.Fragment.Review_Of_Systems_Dialog;
import com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView.RecyclerViewAdapter_ReviewOfSystems;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_AlertDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressBar;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.General.Classes.Dialogs.DatePickerFragment;
import com.example.kinit.e_medicalrecord.General.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.General.Request.UrlString;
import com.example.kinit.e_medicalrecord.Profile.Class.Patient;
import com.example.kinit.e_medicalrecord.Profile.Class.Viewer;
import com.example.kinit.e_medicalrecord.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Consultation_Form extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    Patient patient;
    Viewer viewer;
    SimpleDateFormat simpleDateFormat;
    Consultation consultation;
    Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick;
    ArrayList<Consultation_ROS> consultationRos_general, consultationRos_skinBreast, consultationRos_eyesEars,
            consultationRos_cardio, consultationRos_respi, consultationRos_gastro, consultationRos_genito,
            consultationRos_musculo, consultationRos_neuro, consultationRos_allergic;
    ArrayList<Review_Of_Systems> reviewOfSystems;
    ArrayAdapter_Strings arrayAdapterStrings;

    Custom_AlertDialog alertDialog;
    Custom_ProgressDialog progressDialog;
    Custom_ProgressBar progressBar;
    Review_Of_Systems_Dialog reviewOfSystemsDialog;

    //Widgets
    EditText et_physician, et_date, et_chiefComplaint, et_presentIllness, et_diagnosis, et_height, et_weight, et_temperature,
            et_bloodPressure, et_respirationRate, et_pulseRate;
    Button btn_save;
    ImageButton btn_add;
    LinearLayout linear_general, linear_skinBreast, linear_eyesEars, linear_cardio, linear_respi, linear_gastro, linear_genito,
            linear_musculo, linear_neuro, linear_allergic;
    RecyclerView recyclerView_general, recyclerView_skinBreast, recyclerView_eyesEars, recyclerView_cardio, recyclerView_respi,
            recyclerView_gastro, recyclerView_genito, recyclerView_musculo, recyclerView_neuro, recyclerView_allergic;
    RecyclerViewAdapter_ReviewOfSystems adapter_general, adapter_skinBreast, adapter_eyesEars, adapter_cardio, adapter_respi, adapter_gastro, adapter_genito,
            adapter_musculo, adapter_neuro, adapter_allergic;
    Calendar calendar;
    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_consultation_form);
        init();
    }

    void init() {
        intent = getIntent();
        patient = intent.getExtras().getParcelable("patient");
        viewer = intent.getExtras().getParcelable("viewer");
        if (intent.hasExtra("consultation")) {
            consultation = intent.getExtras().getParcelable("consultation");
        }

        progressDialog = new Custom_ProgressDialog(this);
        alertDialog = new Custom_AlertDialog(this);
        progressBar = new Custom_ProgressBar(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consultation Form");
        getSupportActionBar().setSubtitle(patient.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_physician = (EditText) findViewById(R.id.et_physician);
        et_date = (EditText) findViewById(R.id.et_date);
        et_chiefComplaint = (EditText) findViewById(R.id.et_chiefComplaint);
        et_presentIllness = (EditText) findViewById(R.id.et_presentIllness);
        et_diagnosis = (EditText) findViewById(R.id.et_diagnosis);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_temperature = (EditText) findViewById(R.id.et_temperature);
        et_bloodPressure = (EditText) findViewById(R.id.et_bloodPressure);
        et_respirationRate = (EditText) findViewById(R.id.et_respirationRate);
        et_pulseRate = (EditText) findViewById(R.id.et_pulseRate);
        et_date.setOnClickListener(this);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_save.setOnClickListener(this);
        btn_add.setOnClickListener(this);

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
        reviewOfSystemsDialog = new Review_Of_Systems_Dialog();
        initLinearLayoutsAndRecyclerView();

        getVaccines();
        if (consultation != null) {
            //fetchData();
        }
    }

    void initLinearLayoutsAndRecyclerView() {
        initArrayList();

        //LinearLayout Section
        linear_general = (LinearLayout) findViewById(R.id.linear_general);
        linear_skinBreast = (LinearLayout) findViewById(R.id.linear_skinBreast);
        linear_eyesEars = (LinearLayout) findViewById(R.id.linear_eyesEars);
        linear_cardio = (LinearLayout) findViewById(R.id.linear_cardio);
        linear_respi = (LinearLayout) findViewById(R.id.linear_respi);
        linear_gastro = (LinearLayout) findViewById(R.id.linear_gastro);
        linear_genito = (LinearLayout) findViewById(R.id.linear_genito);
        linear_musculo = (LinearLayout) findViewById(R.id.linear_musculo);
        linear_neuro = (LinearLayout) findViewById(R.id.linear_neuro);
        linear_allergic = (LinearLayout) findViewById(R.id.linear_allergic);

        //RecyclerView Section
        recyclerView_general = (RecyclerView) findViewById(R.id.recyclerView_general);
        recyclerView_skinBreast = (RecyclerView) findViewById(R.id.recyclerView_skinBreast);
        recyclerView_eyesEars = (RecyclerView) findViewById(R.id.recyclerView_eyesEars);
        recyclerView_cardio = (RecyclerView) findViewById(R.id.recyclerView_cardio);
        recyclerView_respi = (RecyclerView) findViewById(R.id.recyclerView_respi);
        recyclerView_gastro = (RecyclerView) findViewById(R.id.recyclerView_gastro);
        recyclerView_genito = (RecyclerView) findViewById(R.id.recyclerView_genito);
        recyclerView_musculo = (RecyclerView) findViewById(R.id.recyclerView_musculo);
        recyclerView_neuro = (RecyclerView) findViewById(R.id.recyclerView_neuro);
        recyclerView_allergic = (RecyclerView) findViewById(R.id.recyclerView_allergic);

        //RecyclerView Adapter Section
        adapter_general = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_general));
        adapter_skinBreast = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_skinBreast));
        adapter_eyesEars = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_eyesEars));
        adapter_cardio = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_cardio));
        adapter_respi = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_respi));
        adapter_gastro = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_gastro));
        adapter_genito = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_genito));
        adapter_musculo = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_musculo));
        adapter_neuro = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_neuro));
        adapter_allergic = new RecyclerViewAdapter_ReviewOfSystems(getClone(consultationRos_allergic));

        //Set RecyclerView Adapter Section
        recyclerView_general.setAdapter(adapter_general);
        recyclerView_skinBreast.setAdapter(adapter_skinBreast);
        recyclerView_eyesEars.setAdapter(adapter_eyesEars);
        recyclerView_cardio.setAdapter(adapter_cardio);
        recyclerView_respi.setAdapter(adapter_respi);
        recyclerView_gastro.setAdapter(adapter_gastro);
        recyclerView_genito.setAdapter(adapter_genito);
        recyclerView_musculo.setAdapter(adapter_musculo);
        recyclerView_neuro.setAdapter(adapter_neuro);
        recyclerView_allergic.setAdapter(adapter_allergic);
        //
        //RecyclerView LayoutManager Section
        RecyclerView.LayoutManager layoutManager_general, layoutManager_SkinBreast, layoutManager_eyesEars,
                layoutManager_cardio, layoutManager_respi, layoutManager_gastro, layoutManager_genito,
                layoutManager_musculo, layoutManager_neuro, layoutManager_allergic;

        layoutManager_general = new LinearLayoutManager(this);
        layoutManager_SkinBreast = new LinearLayoutManager(this);
        layoutManager_eyesEars = new LinearLayoutManager(this);
        layoutManager_cardio = new LinearLayoutManager(this);
        layoutManager_respi = new LinearLayoutManager(this);
        layoutManager_gastro = new LinearLayoutManager(this);
        layoutManager_genito = new LinearLayoutManager(this);
        layoutManager_musculo = new LinearLayoutManager(this);
        layoutManager_neuro = new LinearLayoutManager(this);
        layoutManager_allergic = new LinearLayoutManager(this);

        recyclerView_general.setLayoutManager(layoutManager_general);
        recyclerView_skinBreast.setLayoutManager(layoutManager_SkinBreast);
        recyclerView_eyesEars.setLayoutManager(layoutManager_eyesEars);
        recyclerView_cardio.setLayoutManager(layoutManager_cardio);
        recyclerView_respi.setLayoutManager(layoutManager_respi);
        recyclerView_gastro.setLayoutManager(layoutManager_gastro);
        recyclerView_genito.setLayoutManager(layoutManager_genito);
        recyclerView_musculo.setLayoutManager(layoutManager_musculo);
        recyclerView_neuro.setLayoutManager(layoutManager_neuro);
        recyclerView_allergic.setLayoutManager(layoutManager_allergic);

        initArrayList();
    }

    void initArrayList() {
        consultationRos_general = new ArrayList<>();
        consultationRos_skinBreast = new ArrayList<>();
        consultationRos_eyesEars = new ArrayList<>();
        consultationRos_cardio = new ArrayList<>();
        consultationRos_respi = new ArrayList<>();
        consultationRos_gastro = new ArrayList<>();
        consultationRos_genito = new ArrayList<>();
        consultationRos_musculo = new ArrayList<>();
        consultationRos_neuro = new ArrayList<>();
        consultationRos_allergic = new ArrayList<>();
    }

    ArrayList<Consultation_ROS> getClone(ArrayList<Consultation_ROS> consultationRoses) {
        return (ArrayList<Consultation_ROS>) consultationRoses.clone();
    }

    void insertCheckWhichCategory(Consultation_ROS consultationRos) {
        switch (consultationRos.rosCategoryId) {
            case 1:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_general, adapter_general, linear_general);
                break;
            case 2:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_skinBreast, adapter_skinBreast, linear_skinBreast);
                break;
            case 3:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_eyesEars, adapter_eyesEars, linear_eyesEars);
                break;
            case 4:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_cardio, adapter_cardio, linear_cardio);
                break;
            case 5:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_respi, adapter_respi, linear_respi);
                break;
            case 6:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_gastro, adapter_gastro, linear_gastro);
                break;
            case 7:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_genito, adapter_genito, linear_genito);
                break;
            case 8:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_musculo, adapter_musculo, linear_musculo);
                break;
            case 9:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_neuro, adapter_neuro, linear_neuro);
                break;
            case 10:
                insertToArrayAndUpdateAdapter(consultationRos, consultationRos_allergic, adapter_allergic, linear_allergic);
                break;
        }
    }

    void updateCheckWhichCategory(Bus_ConsultationROS consultationROS) {
        switch (busConsultationRosOnLongClick.consultationRos.rosCategoryId) {
            case 1:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_general, adapter_general);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_general, adapter_general, linear_general);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 2:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_skinBreast, adapter_skinBreast);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_skinBreast, adapter_skinBreast, linear_skinBreast);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 3:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_eyesEars, adapter_eyesEars);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_eyesEars, adapter_eyesEars, linear_eyesEars);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 4:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_cardio, adapter_cardio);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_cardio, adapter_cardio, linear_cardio);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 5:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_respi, adapter_respi);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_respi, adapter_respi, linear_respi);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 6:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_gastro, adapter_gastro);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_gastro, adapter_gastro, linear_gastro);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 7:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_genito, adapter_genito);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_genito, adapter_genito, linear_genito);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 8:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_musculo, adapter_musculo);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_musculo, adapter_musculo, linear_musculo);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 9:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_neuro, adapter_neuro);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_neuro, adapter_neuro, linear_neuro);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
            case 10:
                if (busConsultationRosOnLongClick.consultationRos.rosCategoryId == consultationROS.consultationRos.rosCategoryId) {
                    updateArrayAndAdapter(busConsultationRosOnLongClick, consultationROS.consultationRos, consultationRos_allergic, adapter_allergic);
                } else {
                    deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_allergic, adapter_allergic, linear_allergic);
                    insertCheckWhichCategory(consultationROS.consultationRos);
                }
                break;
        }
    }

    void deleteCheckWhichCategory(Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick) {
        switch (busConsultationRosOnLongClick.consultationRos.rosCategoryId) {
            case 1:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_general, adapter_general, linear_general);
                break;
            case 2:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_skinBreast, adapter_skinBreast, linear_skinBreast);
                break;
            case 3:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_eyesEars, adapter_eyesEars, linear_eyesEars);
                break;
            case 4:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_cardio, adapter_cardio, linear_cardio);
                break;
            case 5:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_respi, adapter_respi, linear_respi);
                break;
            case 6:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_gastro, adapter_gastro, linear_gastro);
                break;
            case 7:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_genito, adapter_genito, linear_genito);
                break;
            case 8:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_musculo, adapter_musculo, linear_musculo);
                break;
            case 9:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_neuro, adapter_neuro, linear_neuro);
                break;
            case 10:
                deleteFromArrayAndAdapter(busConsultationRosOnLongClick, consultationRos_allergic, adapter_allergic, linear_allergic);
                break;
        }
    }

    void updateArrayAndAdapter(Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick, Consultation_ROS consultationRos,
                               ArrayList<Consultation_ROS> consultationRos_array, RecyclerViewAdapter_ReviewOfSystems consultationRos_adapter) {
        consultationRos_array.set(busConsultationRosOnLongClick.position, consultationRos);
        consultationRos_adapter.updateItem(busConsultationRosOnLongClick.position, consultationRos);
        this.busConsultationRosOnLongClick = null;
    }

    void insertToArrayAndUpdateAdapter(Consultation_ROS consultationRos, ArrayList<Consultation_ROS> consultationRos_array,
                                       RecyclerViewAdapter_ReviewOfSystems consultationRos_adapter, LinearLayout consultationRos_linearLayout) {
        consultationRos_array.add(consultationRos);
        checkIfArrayIsEmpty(consultationRos_array, consultationRos_linearLayout);
        consultationRos_adapter.addItem(consultationRos);
    }

    void deleteFromArrayAndAdapter(Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick, ArrayList<Consultation_ROS> consultationRos_array,
                                   RecyclerViewAdapter_ReviewOfSystems consultationRos_adapter, LinearLayout consultationRos_linearLayout) {
        consultationRos_array.remove(busConsultationRosOnLongClick.position);
        consultationRos_adapter.removeItem(busConsultationRosOnLongClick.position);
        checkIfArrayIsEmpty(consultationRos_array, consultationRos_linearLayout);
        this.busConsultationRosOnLongClick = null;
    }

    void checkIfArrayIsEmpty(ArrayList<Consultation_ROS> consultationRos_array, LinearLayout consultationRos_linearLayout) {
        if (consultationRos_array.size() > 0) {
            consultationRos_linearLayout.setVisibility(View.VISIBLE);
        } else {
            consultationRos_linearLayout.setVisibility(View.GONE);
        }
    }

    void setCalendar(Calendar calendar) {
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        et_date.setText(simpleDateFormat.format(calendar.getTime()));
    }

    boolean validateEditText(EditText editText, String text) {
        boolean isThereNoError = true;

        if (text.isEmpty()) {
            editText.setError(getString(R.string.required_field));
            isThereNoError = false;
        }

        return isThereNoError;
    }

    void showDialog(Consultation_ROS consultationRos) {
        reviewOfSystemsDialog.show(getSupportFragmentManager(), "Review Of Systems Dialog");
        if (consultationRos != null) {
            reviewOfSystemsDialog.setFields(new Consultation_ROS(consultationRos.remarks, consultationRos.consultationRosId,
                    consultationRos.item, consultationRos.rosCategoryId, consultationRos.category));
        }
    }

    void action_AlertDialog(final Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick) {
        final CharSequence actions[] = {"Edit", "Delete"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Action");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDialog(busConsultationRosOnLongClick.consultationRos);
                        break;
                    case 1:
                        deleteCheckWhichCategory(busConsultationRosOnLongClick);
                        break;
                }
            }
        });
        builder.show();
    }

    void getVaccines() {
        try {
            progressBar.show();
            reviewOfSystems = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(UrlString.POST, UrlString.URL_CONSULTATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("error", response);
                            try {
                                JSONArray rootJsonArray = new JSONArray(response), jsonArray;
                                JSONObject jsonObject = rootJsonArray.getJSONObject(0);
                                if (jsonObject.has("code")) {
                                    String code = jsonObject.getString("code");
                                    switch (code) {
                                        case "success":
                                            jsonArray = rootJsonArray.getJSONArray(1);
                                            arrayAdapterStrings = new ArrayAdapter_Strings(jsonArray);
                                            reviewOfSystems = arrayAdapterStrings.reviewOfSystems;
                                            reviewOfSystemsDialog.setArrayList(arrayAdapterStrings);
                                            break;
                                        case "empty":
                                        case "error":
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressBar.hide();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.show();
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getVaccines");
                    params.put("device", "mobile");

                    return params;
                }
            };
            Custom_Singleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressBar.hide();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onClickDialogOk(Bus_ConsultationROS consultationROS) {
        switch (consultationROS.queryType) {
            case INSERT:
                insertCheckWhichCategory(consultationROS.consultationRos);
                break;
            case UPDATE:
                updateCheckWhichCategory(consultationROS);
                break;
        }
    }

    @Subscribe
    public void onLongClickRos(Bus_ConsultationRos_OnLongClick busConsultationRosOnLongClick) {
        this.busConsultationRosOnLongClick = busConsultationRosOnLongClick;
        action_AlertDialog(this.busConsultationRosOnLongClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                //onClickSave();
                break;
            case R.id.btn_add:
                showDialog(null);
                break;
            case R.id.et_date:
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                datePickerFragment.setCurrentDate(calendar);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
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
}
