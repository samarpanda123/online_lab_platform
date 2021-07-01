package com.example.lab_project1;

public class GetAssignmentModel {

    private String studentId;
    private String courseId;
    private String assignmentName;
    private String url;

    public GetAssignmentModel() {
    }

    public GetAssignmentModel(String studentId, String courseId, String assignmentName, String url) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.assignmentName = assignmentName;
        this.url = url;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getUrl() {
        return url;
    }
}
