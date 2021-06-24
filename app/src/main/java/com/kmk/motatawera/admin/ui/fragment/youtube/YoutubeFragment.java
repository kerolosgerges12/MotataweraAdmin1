package com.kmk.motatawera.admin.ui.fragment.youtube;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.adapter.YoutubeAdapter;
import com.kmk.motatawera.admin.databinding.FragmentYoutubeBinding;
import com.kmk.motatawera.admin.model.YoutubeModel;
import com.kmk.motatawera.admin.ui.fragment.youtube.control.AddYoutubeActivity;

import java.util.ArrayList;
import java.util.List;

import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;


public class YoutubeFragment extends Fragment {

    private FragmentYoutubeBinding binding;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private List<YoutubeModel> list;
    private YoutubeAdapter adapter;
    private boolean isFirstLoad = true;
    private RecyclerView recyclerView;

    private List<String> listSubjectID, listSubjectName;

    public YoutubeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        isFirstLoad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_youtube, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddYoutubeActivity.class)));

        recyclerView = binding.recyclerYoutube;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        list = new ArrayList<>();
        adapter = new YoutubeAdapter(list, getActivity());

        listSubjectID = new ArrayList<>();
        listSubjectName = new ArrayList<>();

        binding.spinnerDepartmentHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listSubjectID.clear();
                listSubjectName.clear();
                list.clear();
                adapter.notifyDataSetChanged();
                getSubject(position, binding.spinnerGradHome.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerGradHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listSubjectID.clear();
                listSubjectName.clear();
                list.clear();
                adapter.notifyDataSetChanged();
                getSubject(binding.spinnerDepartmentHome.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerSubjectHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                list.clear();
                adapter.notifyDataSetChanged();
                getYoutube();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    void getSubject(int stateDepartment, int stateGrad) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "لا توجد مواد مسجلة لهذه الفرقة", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }

    }

    private void setSubjectSpinner(List<String> listName) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.format_spinner, listName);
        adapter.setDropDownViewResource(R.layout.format_spinner);
        binding.spinnerSubjectHome.setAdapter(adapter);
    }


    void getYoutube() {

        if (firebaseAuth.getCurrentUser() != null) {

            binding.progressYoutube.setVisibility(View.VISIBLE);

            Query query = firestore.collection("youtube")
                    .whereEqualTo("subject_id", listSubjectID.get(binding.spinnerSubjectHome.getSelectedItemPosition()))
                    .whereEqualTo("video_department", binding.spinnerDepartmentHome.getSelectedItemPosition() + 1)
                    .whereEqualTo("video_grad", binding.spinnerGradHome.getSelectedItemPosition() + 1);

            query.addSnapshotListener((value, error) -> {

                if (error == null) {

                    assert value != null;
                    if (!value.isEmpty()) {
                        if (isFirstLoad) {
                            list.clear();
                        }

                        for (DocumentChange doc : value.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                YoutubeModel model = doc.getDocument().toObject(YoutubeModel.class);

                                if (isFirstLoad) list.add(model);
                                else list.add(0, model);

                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else if (doc.getType() == DocumentChange.Type.MODIFIED)
                                Toast.makeText(getActivity(), "MODIFIED", Toast.LENGTH_SHORT).show();

                        }
                        isFirstLoad = false;
                        binding.progressYoutube.setVisibility(View.GONE);
                    }else{
                        binding.progressYoutube.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "!لا توجد فديوهات مسجلة لهذه الماده", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressYoutube.setVisibility(View.GONE);
                    SHOW_ALERT(getActivity(), error.getMessage());
                    Log.d("TAG", "getYoutube: " + error.getMessage());
                }

            });

        }
    }
}