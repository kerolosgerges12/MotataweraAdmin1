package com.kmk.motatawera.admin.model;

public class SubjectModel {
    private String id;
    private String name;
    private int branch;
    private int department;
    private int grad;
    private String doctor_id;

    public SubjectModel() {
    }

    public SubjectModel(String id, String name, int branch, int department, int grad, String doctor_id) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.department = department;
        this.grad = grad;
        this.doctor_id = doctor_id;
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

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getGrad() {
        return grad;
    }

    public void setGrad(int grad) {
        this.grad = grad;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
