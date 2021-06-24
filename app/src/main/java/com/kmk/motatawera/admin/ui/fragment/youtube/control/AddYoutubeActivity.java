package com.kmk.motatawera.admin.ui.fragment.youtube.control;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.ActivityAddYoutubeBinding;
import com.kmk.motatawera.admin.ui.MainActivity;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kmk.motatawera.admin.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;

public class AddYoutubeActivity extends AppCompatActivity {

    private ActivityAddYoutubeBinding binding;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private List<String> listSubjectID, listSubjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_youtube);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        listSubjectID = new ArrayList<>();
        listSubjectName = new ArrayList<>();

        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listSubjectID.clear();
                listSubjectName.clear();
                getSubject(position, binding.spinnerGrad.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerGrad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listSubjectID.clear();
                listSubjectName.clear();
                getSubject(binding.spinnerDepartment.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.addYoutube.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) validation();
            else SHOW_ALERT(this, getString(R.string.noInternet));
        });

    }

    private void validation() {
        String youtubeID = binding.txtYoutubeId.getText().toString().trim();
        String youtubeName = binding.txtYoutubeName.getText().toString().trim();

        if (youtubeID.isEmpty()) {
            SHOW_ALERT(this, "من فضلك ادخل ID الفديو");
            return;
        }
        if (youtubeName.isEmpty()) {
            SHOW_ALERT(this, "من فضلك ادخل اسم الفديو");
            return;
        }
        new AlertDialog.Builder(this)
                .setMessage("هل انت متاكد من هذه البيانات؟؟")
                .setPositiveButton("لا", null)
                .setNegativeButton("نعم", (dialog, which) -> addData(youtubeID, youtubeName))
                .create()
                .show();

    }

    private void addData(String youtubeID, String youtubeName) {

        hideKeyboard(this);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("رجاء الانتظار...جارى تحميل ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DocumentReference db = firestore.collection("youtube").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id", db.getId());
        map.put("video_id", youtubeID);
        map.put("video_name", youtubeName);
        map.put("video_branch", binding.spinnerBranch.getSelectedItemPosition() + 1);
        map.put("video_department", binding.spinnerDepartment.getSelectedItemPosition() + 1);
        map.put("video_grad", binding.spinnerGrad.getSelectedItemPosition() + 1);
        map.put("subject_id", listSubjectID.get(binding.spinnerSubject.getSelectedItemPosition()));
        map.put("subject_name", listSubjectName.get(binding.spinnerSubject.getSelectedItemPosition()));
        map.put("time", FieldValue.serverTimestamp());

        db.set(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                new AlertDialog.Builder(this)
                        .setMessage("تم الاضافه بنجاح")
                        .setCancelable(false)
                        .setPositiveButton("موافق", (dialog, which) -> startActivity(new Intent(this, MainActivity.class)))
                        .create()
                        .show();
            } else {
                progressDialog.dismiss();
                SHOW_ALERT(this, task.getException().getMessage());
            }
        });

    }


    void getSubject(int stateDepartment, int stateGrad) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("رجاء الانتظار...جارى تحميل المواد");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (firebaseAuth.getCurrentUser() != null) {

            Query query = firestore.collection("subject")
                    .whereEqualTo("department", stateDepartment + 1)
                    .whereEqualTo("grad", stateGrad + 1);

            query.addSnapshotListener((value, error) -> {

                if (error == null) {
                    assert value != null;
                    if (!value.isEmpty()) {

                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                listSubjectID.add(doc.getDocument().getString("id"));
                                listSubjectName.add(doc.getDocument().getString("name"));
                            }
                        }
                        setSubjectSpinner(listSubjectName);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "لا توجد مواد مسجلة لهذه الفرقة", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSubjectSpinner(List<String> listName) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.format_spinner, listName);
        adapter.setDropDownViewResource(R.layout.format_spinner);
        binding.spinnerSubject.setAdapter(adapter);
    }

}