package com.example.kinit.e_medicalrecord.Fragments.Laboratory;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinit.e_medicalrecord.Classes.User.Viewer;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_View_Lab_Result extends Fragment {
    //View
    View rootView;
    ViewPager viewPager;

    //Enum
    Laboratory_Tests enum_laboratoryTests;
    //Classes
    Viewer viewer;

    //Fragments
    Fragment_View_Laboratory fragmentLab;
    Fragment_View_Lab_Tagged fragmentViewLabTagged;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_lab_result, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {
        initializeViewPager();
    }

    public void setLabResultAndViewer(Laboratory_Tests enum_laboratoryTests, Object object, Viewer viewer) {
        this.viewer = viewer;
        this.enum_laboratoryTests = enum_laboratoryTests;
        fragmentLab = new Fragment_View_Laboratory();
        fragmentLab.setLab(enum_laboratoryTests, object);
        fragmentViewLabTagged = new Fragment_View_Lab_Tagged();
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
        Fragment_View_Lab_Result.ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(fragmentLab, "Laboratory Test");
        viewPagerAdapter.addFragment(fragmentViewLabTagged, "Tagged Physicians");
        this.viewPager.setAdapter(viewPagerAdapter);
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
