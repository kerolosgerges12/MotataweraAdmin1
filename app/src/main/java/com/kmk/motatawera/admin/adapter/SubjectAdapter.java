package com.kmk.motatawera.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FormatSubjectBinding;
import com.kmk.motatawera.admin.model.SubjectModel;
import com.kmk.motatawera.admin.ui.ChapterListActivity;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<SubjectModel> list;

    private Context context;

    public SubjectAdapter(Context context, List<SubjectModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormatSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.format_subject, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectModel model = list.get(position);
        Resources res = context.getResources();

        String[] Branch = res.getStringArray(R.array.branchlist);
        String[] Department = res.getStringArray(R.array.departmentlist);
        String[] grad = res.getStringArray(R.array.gradlist);

        holder.binding.subjectItemName.setText(model.getName());
        holder.binding.subjectItemBranch.setText(Branch[model.getBranch() - 1]);
        holder.binding.subjectItemDepartment.setText(Department[model.getDepartment() - 1]);
        holder.binding.subjectItemGrad.setText(grad[model.getGrad() - 1]);

        holder.binding.btnAddChapter.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChapterListActivity.class);
            intent.putExtra("subject_name", model.getName());
            intent.putExtra("subject_id", model.getId());
            context.startActivity(intent);
        });
        holder.binding.btnDeleteSubject.setOnClickListener(v -> {

            AlertDialog.Builder b = new AlertDialog.Builder(context)
                    .setTitle("Confirm")
                    .setMessage("Are You Sure you Want to Delete This Subject : " + model.getName())
                    .setPositiveButton("Delete",
                            (dialog, whichButton) -> {
                                FirebaseFirestore.getInstance().collection("subject").document(model.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Subject Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                    )
                    .setNegativeButton("Cancel",
                            (dialog, whichButton) -> dialog.dismiss()
                    );
            b.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<SubjectModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        FormatSubjectBinding binding;

        public ViewHolder(@NonNull FormatSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


}

