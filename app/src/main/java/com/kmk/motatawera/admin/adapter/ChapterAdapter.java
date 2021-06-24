package com.kmk.motatawera.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.FormatChapterBinding;
import com.kmk.motatawera.admin.model.ChapterModel;
import com.kmk.motatawera.admin.ui.AddEditChapterActivity;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private List<ChapterModel> list;

    private Context context;

    public ChapterAdapter(List<ChapterModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormatChapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.format_chapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChapterModel model = list.get(position);

        holder.binding.chapterItemName.setText(model.getName());

        holder.binding.chapterItemEdit.setOnClickListener(v -> {
            boolean isEdit = true;
            Intent intent = new Intent(context  , AddEditChapterActivity.class);
            intent.putExtra("chapter_id", model.getId());
            intent.putExtra("chapter_name", model.getName());
            intent.putExtra("chapter_pdf", model.getPdf_url());
            intent.putExtra("chapter_video", model.getVideo_url());
            intent.putExtra("isEdit",isEdit);
            context.startActivity(intent);
        });


        if (model.getPdf_url().isEmpty()){
            holder.binding.chapterItemPDF.setText("PDF False");
        }else {
            holder.binding.chapterItemPDF.setText("PDF True");
        }

        if (model.getVideo_url().isEmpty()){
            holder.binding.chapterItemVideo.setText("Video False");
        }else {
            holder.binding.chapterItemVideo.setText("Video True");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ChapterModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        FormatChapterBinding binding;
        public ViewHolder(@NonNull FormatChapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


}

