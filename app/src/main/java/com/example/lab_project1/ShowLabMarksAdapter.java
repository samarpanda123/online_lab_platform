package com.example.lab_project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowLabMarksAdapter extends RecyclerView.Adapter<ShowLabMarksAdapter.ViewHolder> {
    List<ShowLabMarksModel>showLabMarksModels;

    public ShowLabMarksAdapter(List<ShowLabMarksModel> showLabMarksModels) {
        this.showLabMarksModels = showLabMarksModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showlabmarks_item_layout , parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShowLabMarksModel model = (ShowLabMarksModel) showLabMarksModels.get(position);
        holder.StudentName.setText(model.getStudentName());
        holder.StudentId.setText(model.getStudentId());
        holder.CourseName.setText(model.getCourseName());
        holder.CourseId.setText(model.getCourseId());
        holder.Marks.setText(model.getMarks());
        holder.Type.setText(model.getType());

    }

    @Override
    public int getItemCount() {
        return showLabMarksModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CourseId , CourseName , StudentId , StudentName , Type ,  Marks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CourseId = itemView.findViewById(R.id.label2_show_lab_marks);
            CourseName = itemView.findViewById(R.id.label4_show_lab_marks);
            StudentId = itemView.findViewById(R.id.label6_show_lab_marks);
            StudentName = itemView.findViewById(R.id.label8_show_lab_marks);
            Marks = itemView.findViewById(R.id.label10_show_lab_marks);
            Type = itemView.findViewById(R.id.label12_show_lab_marks);
        }
    }
}
