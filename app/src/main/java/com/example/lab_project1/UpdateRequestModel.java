package com.example.lab_project1;

public class UpdateRequestModel {

    private String student_Id;
    private String query;
    private String documentId;

    public UpdateRequestModel() {
    }

    public UpdateRequestModel(String student_Id, String query, String documentId) {
        this.student_Id = student_Id;
        this.query = query;
        this.documentId = documentId;
    }

    public String getStudent_Id() {
        return student_Id;
    }

    public String getQuery() {
        return query;
    }

    public String getDocumentId() {
        return documentId;
    }
}

