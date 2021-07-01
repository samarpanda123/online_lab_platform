package com.example.lab_project1;

import com.google.firebase.Timestamp;

public class PseudoCodeModel {



    private String courseId;
    private String testName;
    private String studentId;
    private String time_submit;
    private String url;
    private String documentId;

    public PseudoCodeModel() {
    }

    public PseudoCodeModel(String courseId, String testName, String studentId, String time_submit, String url, String documentId) {
        this.courseId = courseId;
        this.testName = testName;
        this.studentId = studentId;
        this.time_submit = time_submit;
        this.url = url;
        this.documentId = documentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTestName() {
        return testName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTime_submit(){ return time_submit;}


    public String getUrl() {
        return url;
    }

    public String getDocumentId() {
        return documentId;
    }
}
