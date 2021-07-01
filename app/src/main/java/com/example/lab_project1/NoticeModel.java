package com.example.lab_project1;

public class NoticeModel {
    private String published_by;
    private String published_for;
    private String notice;


    public NoticeModel() {
    }

    public NoticeModel(String published_by, String published_for, String notice) {
        this.published_by = published_by;
        this.published_for = published_for;
        this.notice = notice;
    }

    public String getPublished_by() {
        return published_by;
    }

    public String getPublished_for() {
        return published_for;
    }

    public String getNotice() {
        return notice;
    }
}
