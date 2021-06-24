package com.kmk.motatawera.admin.model;

public class ChapterModel {
    private String id;
    private String name;
    private String subject_id;
    private String video_url;
    private String pdf_url;

    public ChapterModel() {
    }

    public ChapterModel(String id, String name, String subject_id, String video_url, String pdf_url) {
        this.id = id;
        this.name = name;
        this.subject_id = subject_id;
        this.video_url = video_url;
        this.pdf_url = pdf_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }
}
