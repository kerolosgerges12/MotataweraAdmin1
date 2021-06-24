package com.kmk.motatawera.admin.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.adapter.AddSubjectAdapter;
import com.kmk.motatawera.admin.databinding.ActivityAddSubjectDoctorBinding;
import com.kmk.motatawera.admin.model.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class AddSubjectDoctorActivity extends AppCompatActivity {
    private String doctor_id;
    private ActivityAddSubjectDoctorBinding binding;
    private FirebaseFirestore db;
    private List<SubjectModel> list;
    private AddSubjectAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_subject_doctor);
        db = FirebaseFirestore.getInstance();
        doctor_id = getIntent().getStringExtra("doctor_id");


        list = new ArrayList<>();
        adapter = new AddSubjectAdapter(list,this,doctor_id);

        recyclerView = binding.rvSubject;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.progressBar.setVisibility(View.VISIBLE);

        getList();

    }

    private void getList() {

        db.collection("subject").addSnapshotListener((value, error) -> {
            if (error == null) {
                binding.progressBar.setVisibility(View.GONE);
                if (value == null) {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                        binding.rvSubject.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);

                    }
                }
            } else {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void onDocumentAdded(DocumentChange change) {
        try {
            SubjectModel model = change.getDocument().toObject(SubjectModel.class);
            list.add(model);
            adapter.notifyItemInserted(list.size() - 1);
            adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onDocumentModified(DocumentChange change) {
        try {
            SubjectModel model = change.getDocument().toObject(SubjectModel.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}