package com.kmk.motatawera.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FormatStudentBinding;
import com.kmk.motatawera.admin.model.StudentModel;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentModel> studentModelList;
    private Context context;
    private OnStudentListener onStudentListener;


    public StudentAdapter(List<StudentModel> studentModelList, Context context, OnStudentListener onStudentListener) {
        this.studentModelList = studentModelList;
        this.context = context;
        this.onStudentListener = onStudentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormatStudentBinding formatStudentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.format_student, parent, false);
        return new ViewHolder(formatStudentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel studentModel = studentModelList.get(position);

        if (studentModel.isDeleted()) {
            holder.formatStudentBinding.studentItemDisable.setVisibility(View.GONE);
            holder.formatStudentBinding.txtDeleteStudent.setText("unDelete");
        } else {
            holder.formatStudentBinding.txtDeleteStudent.setText("Delete");
        }

        if (studentModel.isDisable()) {
            holder.formatStudentBinding.studentItemDelete.setVisibility(View.GONE);
            holder.formatStudentBinding.txtDisableStudent.setText("unDisable");
        } else {
            holder.formatStudentBinding.txtDisableStudent.setText("Disable");
        }


        holder.formatStudentBinding.txtNameStudent.setText("Name"+"  "+":"+"  "+studentModel.getName());
        holder.formatStudentBinding.txtIdStudent.setText("ID"+"  "+":"+"  "+studentModel.getId());


        holder.formatStudentBinding.studentItemDelete.setOnClickListener(v -> {
            //delete student
            onStudentListener.onStudentDeleteClick(position, studentModelList, context);
        });

        holder.formatStudentBinding.studentItemDisable.setOnClickListener(v -> {
            //disable student
            onStudentListener.onStudentDisableClick(position, studentModelList, context);
        });

    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FormatStudentBinding formatStudentBinding;

        public ViewHolder(@NonNull FormatStudentBinding formatStudentBinding) {
            super(formatStudentBinding.getRoot());
            this.formatStudentBinding = formatStudentBinding;
        }

    }

    public interface OnStudentListener {

        void onStudentDeleteClick(int position, List<StudentModel> studentModelList, Context context);

        void onStudentDisableClick(int position, List<StudentModel> studentModelList, Context context);

    }

}
