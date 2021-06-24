package com.kmk.motatawera.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.model.YoutubeModel;

import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {

    private List<YoutubeModel> list;
    private Context context;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    public YoutubeAdapter(List<YoutubeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.format_subject_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.youtube_name.setText(list.get(position).getVideo_name());








    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView youtube_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            youtube_name = itemView.findViewById(R.id.youtube_name);

        }


        }

}
