package com.kmk.motatawera.admin.ui.fragment.youtube.control;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.ActivityUpdateYoutubeBinding;
import com.kmk.motatawera.admin.ui.MainActivity;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import java.util.HashMap;
import java.util.Map;

import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;

public class UpdateYoutubeActivity extends AppCompatActivity {

    private ActivityUpdateYoutubeBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_youtube);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String video_id = intent.getStringExtra("video_id");
        String video_name = intent.getStringExtra("video_name");
        id = intent.getStringExtra("id");

        binding.updateTxtYoutubeId.setText(video_id);
        binding.updateTxtYoutubeName.setText(video_name);

        binding.updateYoutube.setOnClickListener(v -> validation());


    }

    private void validation() {

        if (firebaseAuth.getCurrentUser() != null)
            if (new CheckInternetConn(this).isConnection()) {

                if (binding.updateTxtYoutubeId.getText().toString().trim().isEmpty()) {
                    SHOW_ALERT(this, "من فضلك ادخل ID الفديو");
                    return;
                }
                if (binding.updateTxtYoutubeName.getText().toString().trim().isEmpty()) {
                    SHOW_ALERT(this, "من فضلك ادخل اسم الفديو");
                    return;
                }

                update();

            } else SHOW_ALERT(this, "لا يوجد اتصال بالانترنت");

    }

    private void update() {

        //update here
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("رجاء الانتظار...جارى تحميل ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("video_id", binding.updateTxtYoutubeId.getText().toString().trim());
        map.put("video_name", binding.updateTxtYoutubeName.getText().toString().trim());

        firestore.collection("youtube")
                .document(id)
                .update(map)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "تم التعديل", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        SHOW_ALERT(this, task.getException().getMessage());
                    }
                });

    }

}