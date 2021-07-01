package com.example.lab_project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<NoticeModel> noticeModelList;

    public Adapter(List<NoticeModel> noticeModelList) {
        this.noticeModelList = noticeModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item_layout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String published_by = noticeModelList.get(position).getPublished_by();
        String published_for = noticeModelList.get(position).getPublished_for();
        String notice = noticeModelList.get(position).getNotice();
        holder.setData(published_by , published_for , notice);
    }

    @Override
    public int getItemCount() {
        return noticeModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Published_by;
        private TextView Published_for;
        private TextView Notice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Published_by = itemView.findViewById(R.id.label2);
            Published_for= itemView.findViewById(R.id.label4);
            Notice = itemView.findViewById(R.id.label6);
        }
        private void setData(String published_by , String published_for , String notice)
        {
            Published_by.setText(published_by);
            Published_for.setText(published_for);
            Notice.setText(notice);
        }
    }

}









