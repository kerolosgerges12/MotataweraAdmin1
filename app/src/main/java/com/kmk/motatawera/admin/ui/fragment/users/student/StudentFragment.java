package com.kmk.motatawera.admin.ui.fragment.users.student;

import android.content.Context;
import android.content.Intent;
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
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.adapter.StudentAdapter;
import com.kmk.motatawera.admin.databinding.FragmentStudentBinding;
import com.kmk.motatawera.admin.model.StudentModel;
import com.kmk.motatawera.admin.ui.AddStudentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentFragment extends Fragment implements StudentAdapter.OnStudentListener {


    //firebase
    private FirebaseFirestore db;

    private StudentAdapter adapter;
    private RecyclerView recyclerView;
    private FragmentStudentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false);
        recyclerView = binding.recyclerStudents;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddStudentActivity.class));
        });

        binding.spinnerStudentBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<StudentModel> list = new ArrayList<>();
                adapter = new StudentAdapter(list, getActivity(), StudentFragment.this);
                getStudent(list);
                binding.recyclerStudents.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.spinnerStudentDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<StudentModel> list2 = new ArrayList<>();
                adapter = new StudentAdapter(list2, getActivity(), StudentFragment.this);
                getStudent(list2);
                binding.recyclerStudents.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.spinnerStudentGrad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<StudentModel> list3 = new ArrayList<>();
                adapter = new StudentAdapter(list3, getActivity(), StudentFragment.this);
                getStudent(list3);
                binding.recyclerStudents.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getStudent(List<StudentModel> list) {

        if (adapter != null & list != null) {
            list.clear();
            adapter.notifyDataSetChanged();
        }

        int b = binding.spinnerStudentBranch.getSelectedItemPosition() + 1;
        int d = binding.spinnerStudentDepartment.getSelectedItemPosition() + 1;
        int g = binding.spinnerStudentGrad.getSelectedItemPosition() + 1;

        binding.progressStudents.setVisibility(View.VISIBLE);
        db.collection("student")
                .whereEqualTo("branch", binding.spinnerStudentBranch.getSelectedItemPosition() + 1)
                .whereEqualTo("department", binding.spinnerStudentDepartment.getSelectedItemPosition() + 1)
                .whereEqualTo("grad", binding.spinnerStudentGrad.getSelectedItemPosition() + 1)
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        binding.progressStudents.setVisibility(View.GONE);
                        if (value == null) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {

                            for (DocumentChange documentChange : value.getDocumentChanges()) {

                                switch (documentChange.getType()) {

                                    case ADDED:
                                        onDocumentAdded(documentChange, list);
                                        break;
                                    case MODIFIED:
                                        onDocumentModified(documentChange, list);
                                        break;
                                    case REMOVED:
                                        onDocumentRemoved(documentChange, list);
                                        break;


                                }


                                binding.progressStudents.setVisibility(View.GONE);

                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void onDocumentAdded(DocumentChange change, List<StudentModel> list) {
        try {
            StudentModel model = change.getDocument().toObject(StudentModel.class);
            list.add(model);
            adapter.notifyItemInserted(list.size() - 1);
            adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onDocumentModified(DocumentChange change, List<StudentModel> list) {
        try {
            StudentModel model = change.getDocument().toObject(StudentModel.class);
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

    private void onDocumentRemoved(DocumentChange change, List<StudentModel> list) {
        try {
            list.remove(change.getOldIndex());
            adapter.notifyItemRemoved(change.getOldIndex());
            adapter.notifyItemRangeChanged(0, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStudentDeleteClick(int position, List<StudentModel> studentModelList, Context context) {
        StudentModel model = studentModelList.get(position);
        boolean d;
        if (model.isDeleted()) {
            d = false;
        } else {
            d = true;
        }
        Map<String, Object> map = new HashMap<>();

        map.put("deleted", d);

        db.collection("student")
                .document(model.getId())
                .update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (d) {
                    Toast.makeText(context, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Account unDeleted Successfully", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStudentDisableClick(int position, List<StudentModel> studentModelList, Context context) {
        StudentModel model = studentModelList.get(position);
        boolean d;
        if (model.isDisable()) {
            d = false;
        } else {
            d = true;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("disable", d);

        db.collection("student")
                .document(model.getId())
                .update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (d) {
                    Toast.makeText(context, "Account Disable Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Account unDisable Successfully", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}