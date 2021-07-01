package com.example.lab_project1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.List;

public class PseudoCodeAdapter extends RecyclerView.Adapter<PseudoCodeAdapter.ViewHolder> {

    List<PseudoCodeModel>pseudoCodeModels;
    Myclick listener;


    public PseudoCodeAdapter(List<PseudoCodeModel> pseudoCodeModels  , Myclick listener) {
        this.pseudoCodeModels = pseudoCodeModels;
        this.listener = listener;
    }

    public interface Myclick{
        void secondButton(String str);
        void firstButton(String str);



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pseudocode_item_layout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PseudoCodeModel model = (PseudoCodeModel) pseudoCodeModels.get(position);
        holder.Course_Id.setText(model.getCourseId());
        holder.Test_Name.setText(model.getTestName());
        holder.Student_Id.setText(model.getStudentId());
        holder.Time_submit.setText(model.getTime_submit());


        holder.Status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.secondButton(model.getDocumentId());
            }
        });
        holder.Download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.firstButton(model.getUrl());
            }
        });


    }

    @Override
    public int getItemCount() {
        return pseudoCodeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Course_Id;
        TextView Test_Name;
        TextView Student_Id;
        TextView Time_submit;
        Button Download_button;
        Button Status_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Course_Id = itemView.findViewById(R.id.label2_pseudocode_item);
            Test_Name = itemView.findViewById(R.id.label4_pseudocode_item);
            Student_Id = itemView.findViewById(R.id.label6_pseudocode_item);
            Time_submit = itemView.findViewById(R.id.label8_pseudocode_item);
            Download_button = itemView.findViewById(R.id.btn1_pseudocode_item);
            Status_button = itemView.findViewById(R.id.button_pseudocode_item);




        }
    }
}
