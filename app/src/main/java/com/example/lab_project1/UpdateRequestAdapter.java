package com.example.lab_project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpdateRequestAdapter extends RecyclerView.Adapter<UpdateRequestAdapter.ViewHolder> {

    List<UpdateRequestModel> updateRequestModels;
    Request listener;
    public UpdateRequestAdapter(List<UpdateRequestModel> updateRequestModels , Request listener) {
        this.updateRequestModels = updateRequestModels;
        this.listener = listener;
    }
    public interface Request{
        void submitButton(String str);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_request_item_layout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UpdateRequestModel model = (UpdateRequestModel) updateRequestModels.get(position);
        holder.StudentId.setText(model.getStudent_Id());
        holder.Query.setText(model.getQuery());
        holder.Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.submitButton(model.getDocumentId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return updateRequestModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView StudentId;
        TextView Query;
        Button Submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            StudentId = itemView.findViewById(R.id.label2_update_request_item);
            Query = itemView.findViewById(R.id.label4_update_request_item);
            Submit = itemView.findViewById(R.id.btn1__update_request_item);
        }
    }
}
