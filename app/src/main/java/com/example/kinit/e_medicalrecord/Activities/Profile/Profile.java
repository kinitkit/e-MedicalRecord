package com.example.kinit.e_medicalrecord.Activities.Profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kinit.e_medicalrecord.Activities.Login;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Mode;
import com.example.kinit.e_medicalrecord.BusStation.Profile.Bus_Image_Click;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_Item;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_Item_OnClick;
import com.example.kinit.e_medicalrecord.Classes.Dialogs.Custom_ProgressDialog;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Mode;
import com.example.kinit.e_medicalrecord.Fragments.Profile.Fragment_Search;
import com.example.kinit.e_medicalrecord.Fragments.Profile.Fragment_Profile;
import com.example.kinit.e_medicalrecord.Interfaces.Profile_Communicator;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Custom_Singleton;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    //Data Holder
    Intent intent;
    SharedPreferences sharedPreferences;

    //Primitive Data Types
    boolean isPatient = false, isMedicalStaff = false;
    String recentCode = "";

    //Fragment
    Fragment_Profile fragment_profile;

    //App
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Custom_ProgressDialog progressDialog;

    //Classes
    Viewer viewer;
    User user;
    Mode mode;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new Custom_ProgressDialog(this);
        initIntent();
        getUserData();
    }

    public void initIntent() {
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        user = new User();

        //Intent
        Intent intent = getIntent();
        user.setUser_data_id(intent.getIntExtra("user_id", 0));
        user.setPatient_id(intent.getIntExtra("patient_id", 0));
        user.setMedical_staff_id(intent.getIntExtra("medicalStaff_id", 0));
        if (user.getPatient_id() != 0) {
            mode = Mode.PATIENT;
            isPatient = true;
        }
        if (user.getMedical_staff_id() != 0) {
            mode = Mode.MEDICAL_STAFF;
            isMedicalStaff = true;
        }
        if (intent.hasExtra("viewer_user_id")) {
            viewer = new Viewer();
            viewer.name = intent.getStringExtra("viewer_name");
            viewer.user_id = intent.getIntExtra("viewer_user_id", 0);
            viewer.patient_id = intent.getIntExtra("viewer_patient_id", 0);
            viewer.medicalStaff_id = intent.getIntExtra("viewer_medicalStaff_id", 0);
            viewer.mode = Mode.values()[intent.getIntExtra("viewer_ordinal", 0)];
        }
    }

    void init() {
        //SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.kinit.e_medicalrecord", Context.MODE_PRIVATE);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(user.getFullName());
        if (viewer != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Fragment
        fragment_profile = new Fragment_Profile();
        fragment_profile.setUser(user, mode, viewer);

        //FragmentManager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragment_profile).commit();
    }

    void getUserData() {
        progressDialog.show("Loading...");
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlString.URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("error", response);
                                JSONArray jsonArray = new JSONArray(response);
                                final int numberOfItemsInResp = jsonArray.length();
                                for (int x = 0; x < numberOfItemsInResp; x++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(x);
                                    if (recentCode.isEmpty()) {
                                        recentCode = jsonObject.getString("code");
                                    } else {
                                        switch (recentCode) {
                                            case "user_data":
                                                user.setUsername(jsonObject.getString("username"));
                                                user.setPassword(jsonObject.getString("password"));
                                                user.setFirstName(jsonObject.getString("first_name"));
                                                user.setMiddleName(jsonObject.getString("middle_name"));
                                                user.setLastName(jsonObject.getString("last_name"));
                                                user.setGender(jsonObject.getString("gender"));
                                                user.setContactNumber(jsonObject.getString("contact_number"));
                                                user.setEmailAddress(jsonObject.getString("email_address"));
                                                user.setImage(jsonObject.getString("image"));
                                                user.setActive(Boolean.valueOf(jsonObject.getString("active")));
                                                break;
                                            case "patient_data":
                                                user.setAddress(jsonObject.getString("address"));
                                                user.setBirthday(jsonObject.getString("birthday"));
                                                user.setOccupation(jsonObject.getString("occupation"));
                                                user.civilStatus = jsonObject.getString("civil_status");
                                                user.nationality = jsonObject.getString("nationality");
                                                user.religion = jsonObject.getString("religion");
                                                break;
                                            case "medicalStaff_data":
                                                user.setLicenseNumber(jsonObject.getString("license_number"));
                                                user.setMedical_staff_type(jsonObject.getString("user_type"));
                                                break;
                                        }
                                        recentCode = "";
                                    }
                                }
                                init();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "getUserData");
                    params.put("device", "mobile");
                    params.put("user_id", Integer.toString(user.getUser_data_id()));
                    if (user.getMedical_staff_id() != 0)
                        params.put("medicalStaff_id", Integer.toString(user.getMedical_staff_id()));
                    if (user.getPatient_id() != 0)
                        params.put("patient_id", Integer.toString(user.getPatient_id()));
                    return params;
                }
            };
            Custom_Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewer == null) {
            if (user.getMedical_staff_id() != 0 && user.getPatient_id() != 0) {
                getMenuInflater().inflate(R.menu.menu_profile_with_mode, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_profile, menu);
            }

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

            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    BusStation.getBus().post(new Bus_Search_Item(query, user.getUser_data_id(), mode));
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                Fragment_Search fragmentSearch = new Fragment_Search();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frameLayout, fragmentSearch);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.menu_mode:
                Mode_AlertDialog();
                break;

            case R.id.menu_edit:
                intent = new Intent(this, Settings.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;

            case R.id.menu_logout:
                intent = new Intent(getBaseContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("LOGIN_AUTHENTICATION", false);
                editor.putInt("user_id", 0);
                editor.putInt("patient_id", 0);
                editor.putInt("medicalStaff_id", 0);
                editor.apply();
                startActivity(intent);
                break;

            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void Mode_AlertDialog() {
        final CharSequence modes[] = {"Patient", "Medical Staff"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Choose Mode");
        builder.setItems(modes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "You are now in patient mode", Toast.LENGTH_SHORT).show();
                        if (mode != Mode.PATIENT) {
                            mode = Mode.PATIENT;
                            BusStation.getBus().post(new Bus_Mode(mode));
                        }
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "You are now in medical staff mode", Toast.LENGTH_SHORT).show();
                        if (mode != Mode.MEDICAL_STAFF) {
                            mode = Mode.MEDICAL_STAFF;
                            BusStation.getBus().post(new Bus_Mode(mode));
                        }

                        break;
                }
            }
        });
        builder.show();
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

    @Subscribe
    public void searchItemOnClick(Bus_Search_Item_OnClick busSearchItemOnClick) {
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("user_id", busSearchItemOnClick.user_id);
        intent.putExtra("patient_id", busSearchItemOnClick.patient_id);
        intent.putExtra("medicalStaff_id", busSearchItemOnClick.medicalStaff_id);
        intent.putExtra("viewer_name", user.getFullName());
        intent.putExtra("viewer_patient_id", user.getPatient_id());
        intent.putExtra("viewer_user_id", user.getUser_data_id());
        intent.putExtra("viewer_medicalStaff_id", user.getMedical_staff_id());
        intent.putExtra("viewer_ordinal", mode.ordinal());
        startActivity(intent);
    }

    @Subscribe
    public void imageClicked(Bus_Image_Click busImageClick){
        zoomImageFromThumb(busImageClick.imageButton, busImageClick.imageResId);
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.relativeLayout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
