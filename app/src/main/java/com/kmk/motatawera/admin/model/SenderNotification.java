package com.kmk.motatawera.admin.model;

public class SenderNotification {

    private DataNotification data;
    private String to;

    public SenderNotification(DataNotification data, String to) {
        this.data = data;
        this.to = to;
    }
}
