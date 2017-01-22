package com.example.kinit.e_medicalrecord.Fragments.Profile;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Bus_Mode;
import com.example.kinit.e_medicalrecord.BusStation.Profile.Bus_Image_Click;
import com.example.kinit.e_medicalrecord.Classes.User.User;
import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Mode;
import com.example.kinit.e_medicalrecord.Fragments.Medical_History.Fragment_Medical_History;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Glide_ImgLoader;
import com.example.kinit.e_medicalrecord.Request.UrlString;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Profile extends Fragment implements View.OnClickListener {
    //View
    View rootView;
    ViewPager viewPager;

    //Fragment
    Fragment_Profile_BasicInfo fragment_profile;
    Fragment_Medical_History fragment_medicalInfo;

    //Classes
    User user;
    Mode mode;
    Viewer viewer;

    ImageButton ib_profPic;
    Bitmap profPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        //Fragment
        fragment_profile = new Fragment_Profile_BasicInfo();
        fragment_medicalInfo = new Fragment_Medical_History();

        //Fragment Pass Data
        fragment_profile.setUser(user);
        fragment_medicalInfo.setUser(user, viewer);

        ib_profPic = (ImageButton) rootView.findViewById(R.id.ib_profPic);

        Glide.with(getActivity())
                .load(UrlString.getImageUrl(user.image))
                .error(R.mipmap.icon_user_default)
                .into(ib_profPic);

        initializeViewPager();
    }

    void initializeViewPager() {
        //ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        if (viewPager != null) {
            setupViewPager();
        }
        //TabLayout
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(fragment_profile, "About");
        if (mode == Mode.PATIENT) {
            viewPagerAdapter.addFragment(fragment_medicalInfo, "Medical History");
        }
        this.viewPager.setAdapter(viewPagerAdapter);
    }

    public void setUser(User user, Mode mode, Viewer viewer, Bitmap profPic) {
        this.user = user;
        this.mode = mode;
        this.viewer = viewer;
        this.profPic = profPic;
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
    public void changeMode(Bus_Mode busMode) {
        mode = busMode.mode;
        initializeViewPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_profPic:
                BusStation.getBus().post(new Bus_Image_Click(ib_profPic, R.mipmap.kinit));
                break;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
