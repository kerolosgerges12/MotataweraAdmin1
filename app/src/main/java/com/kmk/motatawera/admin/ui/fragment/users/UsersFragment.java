package com.kmk.motatawera.admin.ui.fragment.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FragmentUsersBinding;


public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setAdapter(new SectionPagerAdapter(getChildFragmentManager(), 0));
        binding.tabLayout.setupWithViewPager(binding.pager);
    }
}