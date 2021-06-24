package com.kmk.motatawera.admin.ui.fragment.subject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FragmentFilterSubjectBinding;
import com.kmk.motatawera.admin.ui.SubjectListActivity;


public class SubjectFilterFragment extends Fragment {
    private FragmentFilterSubjectBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_subject, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SubjectListActivity.class);
            intent.putExtra("b", binding.spinnerSubjectBranch.getSelectedItemPosition() + 1);
            intent.putExtra("d", binding.spinnerSubjectDepartment.getSelectedItemPosition() + 1);
            intent.putExtra("g", binding.spinnerSubjectGrad.getSelectedItemPosition() + 1);

            startActivity(intent);
        });


    }


}