package com.kmk.motatawera.admin.ui.fragment.users;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kmk.motatawera.admin.ui.fragment.users.doctor.DoctorFragment;
import com.kmk.motatawera.admin.ui.fragment.users.student.StudentFragment;



public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DoctorFragment();
            case 1:
            default:
                return new StudentFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Doctor";
            case 1:
            default:
                return "Student";
        }
    }
}