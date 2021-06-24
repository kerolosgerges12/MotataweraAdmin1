package com.kmk.motatawera.admin.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FragmentProfileBinding;
import com.kmk.motatawera.admin.ui.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.logout.setOnClickListener(v -> logout());
    }

    private void logout() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        getActivity().getSharedPreferences("login", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();

    }
}