package com.example.lab_project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GetAssignmentAdapter  extends RecyclerView.Adapter<GetAssignmentAdapter.ViewHolder> {

    List<GetAssignmentModel> getAssignmentModelList;

    Assignment ass;

    public GetAssignmentAdapter(List<GetAssignmentModel> getAssignmentModelList, Assignment ass) {
        this.getAssignmentModelList = getAssignmentModelList;
        this.ass = ass;
    }

    public interface Assignment {
        void downloadAssignment(String url);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_assignment_item_list , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GetAssignmentModel model = (GetAssignmentModel) getAssignmentModelList.get(position);

        holder.StudentId.setText(model.getStudentId());
        holder.CourseId.setText(model.getCourseId());
        holder.AssignmentName.setText(model.getAssignmentName());
        holder.Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ass.downloadAssignment(model.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return getAssignmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView StudentId , CourseId , AssignmentName;
        Button Submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            StudentId = itemView.findViewById(R.id.label2_get_assign);
            CourseId = itemView.findViewById(R.id.label4_get_assign);
            AssignmentName = itemView.findViewById(R.id.label6_get_assign);
            Submit = itemView.findViewById(R.id.btn1_get_assignment);

        }
    }
}
