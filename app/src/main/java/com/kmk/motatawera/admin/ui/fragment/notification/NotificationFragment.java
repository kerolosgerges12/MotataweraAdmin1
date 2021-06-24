package com.kmk.motatawera.admin.ui.fragment.notification;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.data.Response;
import com.kmk.motatawera.admin.data.RetrofitClint;
import com.kmk.motatawera.admin.databinding.FragmentNotificationBinding;
import com.kmk.motatawera.admin.model.DataNotification;
import com.kmk.motatawera.admin.model.SenderNotification;
import com.kmk.motatawera.admin.model.StudentModel;

import retrofit2.Call;
import retrofit2.Callback;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private static final String TAG = "NotificationFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        binding.sendNotification.setOnClickListener(v -> {

            // run progress
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            getQuery(0).addSnapshotListener((value, error) -> {
                        if (value == null) {
                            Toast.makeText(getActivity(), "No Students found", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        if (value != null) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {

                                StudentModel model = documentChange.getDocument().toObject(StudentModel.class);
                                if (model.getToken() != null) {

                                    DataNotification data = new DataNotification(model.getId(),
                                            binding.txtNotificationBody.getText().toString().trim(),
                                            binding.txtNotificationTitle.getText().toString().trim(),
                                            firebaseAuth.getCurrentUser().getUid(),
                                            R.drawable.common_google_signin_btn_icon_dark_normal_background

                                    );
                                    SenderNotification sender = new SenderNotification(data, model.getToken());
                                    RetrofitClint.getInstance()
                                            .send(sender)
                                            .enqueue(new Callback<Response>() {
                                                @Override
                                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                                    if (response.isSuccessful()) {
                                                        boolean hasRun = false;
                                                        do {

                                                            // do some stuff
                                                            if (!hasRun) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getActivity(), "Notification Sent", Toast.LENGTH_SHORT).show();
                                                                // one time condition
                                                                hasRun = true;
                                                            }
                                                            // do some more stuff
                                                        } while (response.isSuccessful());


                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Response> call, Throwable t) {
                                                    Log.d(TAG, "onFailure: fail  " + t.getMessage());
                                                    progressDialog.dismiss();


                                                }

                                            });

                                    progressDialog.dismiss();
                                }

                            }
                        }
                    });

        });

    }

    private Query getQuery(int i) {
        Query query;
        switch (i) {
            case 1:
                query = firestore.collection("doctor");
                break;
            default:
                query = firestore.collection("student").whereEqualTo("disable", false).whereEqualTo("disable", false);
        }
        return query;
    }
}