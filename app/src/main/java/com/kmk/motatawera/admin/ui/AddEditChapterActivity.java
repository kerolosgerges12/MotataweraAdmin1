package com.kmk.motatawera.admin.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.ActivityAddEditChapterBinding;
import com.kmk.motatawera.admin.model.ChapterModel;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import java.util.HashMap;
import java.util.Map;

import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;

public class AddEditChapterActivity extends AppCompatActivity {
    private ActivityAddEditChapterBinding binding;
    private FirebaseFirestore db;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_chapter);
        binding.setLifecycleOwner(this);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        String chapter_name = getIntent().getStringExtra("chapter_name");
        String chapter_pdf = getIntent().getStringExtra("chapter_pdf");
        String chapter_video = getIntent().getStringExtra("chapter_video");

        if (isEdit) {

            binding.textBtnChapter.setText("Update Chapter");

            if (chapter_name.isEmpty()) {
                binding.txtChapterName.setHint("Update Name");
            } else {
                binding.txtChapterName.setText(chapter_name);
            }


            if (chapter_pdf.isEmpty()) {
                binding.txtChapterPdf.setHint("Update PDF");
            } else {
                binding.txtChapterPdf.setText(chapter_pdf);
            }

            if (chapter_video.isEmpty()) {
                binding.txtChapterVideo.setHint("Update Video");
            } else {
                binding.txtChapterVideo.setText(chapter_video);
            }

        }
        binding.btnChapter.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) validation();
            else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });
    }

    private void validation() {
        String video = binding.txtChapterVideo.getText().toString();
        String pdf = binding.txtChapterPdf.getText().toString();
        String name = binding.txtChapterName.getText().toString();

        if (name.isEmpty()) {
            SHOW_ALERT(this, "Please Enter Chapter Name");
            return;
        }

        if (isEdit) {
            EditChapter(name, pdf, video);
        } else {
            AddChapter(name, pdf, video);
        }

    }

    private void EditChapter(String name, String pdf, String video) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String subject_id = getIntent().getStringExtra("subject_id");
        String chapter_id = getIntent().getStringExtra("chapter_id");

        db = FirebaseFirestore.getInstance();


        Map<String, Object> map = new HashMap<>();
        map.put("approved", true);


        map.put("name",name);
        map.put("pdf_url",pdf);
        map.put("video_url",video);


        db.collection("subject").document(subject_id).collection("chapter").document(chapter_id).update(map).addOnCompleteListener(task1 -> {
            Toast.makeText(AddEditChapterActivity.this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();

        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    private void AddChapter(String name, String pdf, String video) {
        //run progress
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String subject_id = getIntent().getStringExtra("subject_id");
        db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("subject").document(subject_id).collection("chapter").document();
        ChapterModel model = new ChapterModel();
        model.setId(documentReference.getId());
        model.setSubject_id(subject_id);
        model.setName(name);
        model.setPdf_url(pdf);
        model.setVideo_url(video);

        documentReference.set(model).addOnCompleteListener(task1 -> {
            Toast.makeText(AddEditChapterActivity.this, "Chapter Added Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();

        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }
}


