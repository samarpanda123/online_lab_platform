package com.example.lab_project1;

public class uploadPDF {

    private String name;
    private String url;

    public uploadPDF() {
    }

    public uploadPDF(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
