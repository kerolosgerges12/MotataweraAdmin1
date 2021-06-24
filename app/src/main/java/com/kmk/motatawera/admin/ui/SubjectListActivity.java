package com.kmk.motatawera.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.adapter.SubjectAdapter;
import com.kmk.motatawera.admin.databinding.ActivitySubjectListBinding;
import com.kmk.motatawera.admin.model.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectListActivity extends AppCompatActivity {
    private ActivitySubjectListBinding binding;
    private SubjectAdapter adapter;
    private List<SubjectModel> list;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_list);
        binding.setLifecycleOwner(this);

        list = new ArrayList<>();
        adapter = new SubjectAdapter(this, list);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(this, AddSubjectActivity.class)));
        int b = getIntent().getIntExtra("b", 0);
        int d = getIntent().getIntExtra("d", 0);
        int g = getIntent().getIntExtra("g", 0);

        firebaseFirestore = FirebaseFirestore.getInstance();
        getList(b, d, g);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getList(int b, int d, int g) {

        binding.progressbar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("subject")
                .whereEqualTo("grad", g)
                .whereEqualTo("department", d)
                .whereEqualTo("branch", b)
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        binding.progressbar.setVisibility(View.GONE);
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





                                binding.recyclerview.setAdapter(adapter);
                                binding.progressbar.setVisibility(View.GONE);

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

}