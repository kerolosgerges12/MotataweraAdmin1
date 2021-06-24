package com.kmk.motatawera.admin.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.ActivityAddSubjectBinding;
import com.kmk.motatawera.admin.model.SubjectModel;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import static com.kmk.motatawera.admin.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;

public class AddSubjectActivity extends AppCompatActivity {
    private ActivityAddSubjectBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_subject);
        binding.setLifecycleOwner(this);
        db = FirebaseFirestore.getInstance();

        binding.btnAddSubject.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) validation();

            else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

    }

    private void validation() {

        String name = binding.etSubjectFullname.getText().toString();

        if (name.isEmpty()) {
            SHOW_ALERT(this, "Please Enter Subject Name");
            return;
        }


        hideKeyboard(this);


        int branch = binding.spBranch.getSelectedItemPosition() + 1;
        int department = binding.spDepartment.getSelectedItemPosition() + 1;
        int grad = binding.spGrad.getSelectedItemPosition() + 1;

        CreateNewSubject(name, branch, department, grad);
    }

    private void CreateNewSubject(String name, int branch, int department, int grad) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait");
        progressDialog.setCancelable(false);
        progressDialog.show();


        DocumentReference documentReference = db.collection("subject").document();
        SubjectModel model = new SubjectModel();
        model.setId(documentReference.getId());
        model.setName(name);
        model.setBranch(branch);
        model.setDepartment(department);
        model.setGrad(grad);
        documentReference.set(model).addOnCompleteListener(task -> {
            Toast.makeText(this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
            progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}