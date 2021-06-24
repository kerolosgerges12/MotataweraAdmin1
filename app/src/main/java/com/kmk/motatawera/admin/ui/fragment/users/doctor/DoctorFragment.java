package com.kmk.motatawera.admin.ui.fragment.users.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.adapter.DoctorAdapter;
import com.kmk.motatawera.admin.databinding.FragmentDoctorBinding;
import com.kmk.motatawera.admin.model.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorFragment extends Fragment {

    private FragmentDoctorBinding binding;

    private FirebaseFirestore db;
    private static final String TAG = "DoctorFragment";
    private DoctorAdapter adapter;
    private List<DoctorModel> list;
    private RecyclerView recyclerView;
    private Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false);

        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recyclerDoctor;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();



        binding.spinnerDoctorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getUsers(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getUsers(int i) {


        if (adapter != null & list != null) {
            list.clear();
            adapter.notifyDataSetChanged();

        }


        checkQuery(i).addSnapshotListener((value, error) -> {
            if (error == null) {

                binding.progressbar.setVisibility(View.GONE);
                if (value == null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    for (DocumentChange documentChange : value.getDocumentChanges()) {

                        switch (documentChange.getType()) {

                            case ADDED:
                                onDocumentAdded(documentChange);
                                break;
                            case MODIFIED:
                                onDocumentModified(documentChange);
                                break;
                            case REMOVED:
                                onDocumentRemoved(documentChange);
                                break;

                        }
                        adapter = new DoctorAdapter(getActivity(), list);
                        binding.recyclerDoctor.setAdapter(adapter);
                        binding.progressbar.setVisibility(View.GONE);

                    }
                }
            } else {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private Query checkQuery(int i) {

        switch (i) {

            case 1:
                query = db.collection("doctor")
                        .whereEqualTo("approved", false)
                        .whereEqualTo("disable", false)
                        .whereEqualTo("deleted", false);
                break;
            case 2:
                query = db.collection("doctor")
                        .whereEqualTo("approved", true)
                        .whereEqualTo("disable", false)
                        .whereEqualTo("deleted", false);
                break;
            case 3:
                query = db.collection("doctor")
                        .whereEqualTo("approved", true)
                        .whereEqualTo("doctor", true);
                break;
            case 4:
                query = db.collection("doctor")
                        .whereEqualTo("approved", true)
                        .whereEqualTo("doctor", false);
                break;
            case 5:
                query = db.collection("doctor")
                        .whereEqualTo("approved", true)
                        .whereEqualTo("deleted", true);
                break;
            case 6:
                query = db.collection("doctor")
                        .whereEqualTo("approved", true)
                        .whereEqualTo("disable", true);
                break;

            case 0:
            default:
                query = db.collection("doctor");

        }
        return query;
    }

    private void onDocumentAdded(DocumentChange change) {
        try {
            DoctorModel model = change.getDocument().toObject(DoctorModel.class);
            list.add(model);
            adapter.notifyItemInserted(list.size() - 1);
            adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onDocumentModified(DocumentChange change) {
        try {
            DoctorModel model = change.getDocument().toObject(DoctorModel.class);
            if (change.getOldIndex() == change.getNewIndex()) {
                // Item changed but remained in same position
                list.set(change.getOldIndex(), model);
                adapter.notifyItemChanged(change.getOldIndex());
            } else {
                // Item changed and changed position
                list.remove(change.getOldIndex());
                list.add(change.getNewIndex(), model);
                adapter.notifyItemRangeChanged(0, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDocumentRemoved(DocumentChange change) {
        try {
            list.remove(change.getOldIndex());
            adapter.notifyItemRemoved(change.getOldIndex());
            adapter.notifyItemRangeChanged(0, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}