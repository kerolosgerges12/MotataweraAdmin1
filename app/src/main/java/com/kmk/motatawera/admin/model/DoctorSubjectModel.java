package com.kmk.motatawera.admin.model;

public class DoctorSubjectModel {
    private String doctor_id;
    private String subject_id;

    public DoctorSubjectModel() {
    }

    public DoctorSubjectModel(String doctor_id, String subject_id) {
        this.doctor_id = doctor_id;
        this.subject_id = subject_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
