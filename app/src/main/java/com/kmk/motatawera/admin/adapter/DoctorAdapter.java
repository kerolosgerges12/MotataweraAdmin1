package com.kmk.motatawera.admin.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FormatDoctorBinding;
import com.kmk.motatawera.admin.model.DoctorModel;
import com.kmk.motatawera.admin.ui.AddSubjectDoctorActivity;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;


public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private Context context;
    private List<DoctorModel> list;
    private FirebaseFirestore db;

    public DoctorAdapter(Context context, List<DoctorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        db = FirebaseFirestore.getInstance();
        FormatDoctorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.format_doctor, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorModel model = list.get(position);

        setText(holder.binding, model);
        onclick(holder.binding, model);
        if (model.isApproved()) {
            holder.binding.doctorItemConfirm.setVisibility(View.GONE);

            if (model.isDisable()) {
                holder.binding.doctorItemDisable.setVisibility(View.GONE);
                holder.binding.doctorItemUnDisable.setVisibility(View.VISIBLE);

                holder.binding.doctorItemDelete.setVisibility(View.GONE);
                holder.binding.doctorItemUnDelete.setVisibility(View.GONE);
            } else {
                holder.binding.doctorItemDisable.setVisibility(View.VISIBLE);
                holder.binding.doctorItemUnDisable.setVisibility(View.GONE);
            }

            if (model.isDeleted()) {
                holder.binding.doctorItemDelete.setVisibility(View.GONE);
                holder.binding.doctorItemUnDelete.setVisibility(View.VISIBLE);

                holder.binding.doctorItemDisable.setVisibility(View.GONE);
                holder.binding.doctorItemUnDisable.setVisibility(View.GONE);
            } else {
                holder.binding.doctorItemDelete.setVisibility(View.VISIBLE);
                holder.binding.doctorItemUnDelete.setVisibility(View.GONE);
            }


        } else {
            holder.binding.doctorItemConfirm.setVisibility(View.VISIBLE);
        }


    }

    private void onclick(FormatDoctorBinding binding, DoctorModel model) {

        binding.doctorItemAddSubject.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddSubjectDoctorActivity.class);
            intent.putExtra("doctor_id", model.getId());
            context.startActivity(intent);
        });

        binding.doctorItemConfirm.setOnClickListener(v -> {
            if (new CheckInternetConn(context).isConnection()) {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("هل تريد قبول هذا الحساب؟")
                        .setPositiveButton("لا", null)
                        .setNegativeButton("نعم", (dialog, which) -> confirmDoctor(model.getId()))
                        .create().show();
            } else
                SHOW_ALERT(context, context.getString(R.string.noInternet));
        });
        binding.doctorItemDisable.setOnClickListener(v -> {
            if (new CheckInternetConn(context).isConnection()) {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("هل تريد تعطيل هذا الحساب؟")
                        .setPositiveButton("لا", null)
                        .setNegativeButton("نعم", (dialog, which) -> disableDoctor(model.getId()))
                        .create().show();
            } else
                SHOW_ALERT(context, context.getString(R.string.noInternet));
        });
        binding.doctorItemDelete.setOnClickListener(v -> {
            if (new CheckInternetConn(context).isConnection()) {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("هل تريد حذف هذا الحساب؟")
                        .setPositiveButton("لا", null)
                        .setNegativeButton("نعم", (dialog, which) -> deleteDoctor(model.getId()))
                        .create().show();
            } else
                SHOW_ALERT(context, context.getString(R.string.noInternet));
        });
        binding.doctorItemUnDisable.setOnClickListener(v -> {
            if (new CheckInternetConn(context).isConnection()) {

                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("هل تريد استرجاع تعطيل هذا الحساب؟")
                        .setPositiveButton("لا", null)
                        .setNegativeButton("نعم", (dialog, which) -> unDisableDoctor(model.getId()))
                        .create().show();
            } else
                SHOW_ALERT(context, context.getString(R.string.noInternet));
        });
        binding.doctorItemUnDelete.setOnClickListener(v -> {
            if (new CheckInternetConn(context).isConnection()) {

                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("هل تريد استرجاع حذف هذا الحساب؟")
                        .setPositiveButton("لا", null)
                        .setNegativeButton("نعم", (dialog, which) -> unDeleteDoctor(model.getId()))
                        .create().show();
            } else
                SHOW_ALERT(context, context.getString(R.string.noInternet));
        });
    }

    private void setText(FormatDoctorBinding binding, DoctorModel model) {
        Resources res = context.getResources();
        String[] Branch = res.getStringArray(R.array.branchlist);
        binding.doctorItemName.setText(model.getName());
        binding.doctorItemEmail.setText(model.getName());
        binding.doctorItemBranch.setText(Branch[model.getBranch() - 1]);

        if (model.isDoctor())
            binding.doctorItemIsDoctor.setText("Doctor");
        else
            binding.doctorItemIsDoctor.setText("Assistant");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FormatDoctorBinding binding;

        public ViewHolder(@NonNull FormatDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


    void updateQuery(String userID, Map<String, Object> map, String msg) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("رجاء الانتظار...جارى تحميل ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection("doctor")
                .document(userID)
                .update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void confirmDoctor(String ID) {
        Map<String, Object> map = new HashMap<>();
        map.put("approved", true);
        updateQuery(ID, map, "تم قبول الحساب");
        notifyDataSetChanged();
    }

    void disableDoctor(String ID) {
        Map<String, Object> map = new HashMap<>();
        map.put("disable", true);
        updateQuery(ID, map, "تم تعطيل الحساب");
        notifyDataSetChanged();
    }

    void deleteDoctor(String ID) {
        Map<String, Object> map = new HashMap<>();
        map.put("deleted", true);

        updateQuery(ID, map, "تم حذف الحساب");
        notifyDataSetChanged();
    }

    void unDisableDoctor(String ID) {
        Map<String, Object> map = new HashMap<>();

        map.put("disable", false);
        updateQuery(ID, map, "تم استرجاع التعطيل");
        notifyDataSetChanged();
    }

    void unDeleteDoctor(String ID) {
        Map<String, Object> map = new HashMap<>();
        map.put("deleted", false);
        updateQuery(ID, map, "تم استرجاع الحذف");
        notifyDataSetChanged();
    }


}
