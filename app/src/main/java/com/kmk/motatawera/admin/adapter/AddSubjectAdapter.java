package com.kmk.motatawera.admin.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FormatAddDoctorSubjectBinding;
import com.kmk.motatawera.admin.model.SubjectModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSubjectAdapter extends RecyclerView.Adapter<AddSubjectAdapter.ViewHolder> {

    private List<SubjectModel> list;
    private Context context;
    private String Doctor_id;
    private FirebaseFirestore db;


    public AddSubjectAdapter(List<SubjectModel> list, Context context, String doctor_id) {
        this.list = list;
        this.context = context;
        Doctor_id = doctor_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormatAddDoctorSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.format_add_doctor_subject, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        SubjectModel model = list.get(position);

        Resources res = context.getResources();
        String[] Branch = res.getStringArray(R.array.branchlist);
        String[] Department = res.getStringArray(R.array.departmentlist);
        String[] grad = res.getStringArray(R.array.gradlist);

        if (model.getDoctor_id() == null) {
            holder.binding.checkBox.setChecked(false);
            holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AddSubject(model.getId());
                }
            });
        } else {
            if (model.getDoctor_id().equals(Doctor_id)) {
                holder.binding.checkBox.setChecked(true);
                holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        RemoveSubject(model.getId());
                    }
                });
            } else {

                holder.binding.checkBox.setChecked(true);
                holder.binding.subjectItemName.setTextColor(context.getResources().getColor(R.color.MainRedColor));
                holder.binding.checkBox.setClickable(false);
            }


        }


        holder.binding.subjectItemName.setText(model.getName());
        holder.binding.subjectItemBranch.setText(Branch[model.getBranch() - 1]);
        holder.binding.subjectItemDepartment.setText(Department[model.getDepartment() - 1]);
        holder.binding.subjectItemGrad.setText(grad[model.getGrad() - 1]);


    }


    private void AddSubject(String Subject_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("doctor_id", Doctor_id);
        db.collection("subject")
                .document(Subject_id)
                .update(map)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Subject Added", Toast.LENGTH_SHORT).show();
                });

    }

    private void RemoveSubject(String Subject_id) {
        db.collection("subject").document(Subject_id)
                .collection("doctor_id")
                .document()
                .delete()
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Subject Removed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        FormatAddDoctorSubjectBinding binding;

        public ViewHolder(@NonNull FormatAddDoctorSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}



