package com.example.lab_project1;

public class ShowLabMarksModel {

    private String courseId;
    private String courseName;
    private String studentId;
    private String studentName;
    private String type;
    private String marks;

    public ShowLabMarksModel() {
    }

    public ShowLabMarksModel(String courseId, String courseName, String studentId, String studentName, String type, String marks) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.type = type;
        this.marks = marks;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getType() {
        return type;
    }

    public String getMarks() {
        return marks;
    }
}
