package com.example.notionary_v1.fragments.data;

import java.util.List;

public class ApiResponse {
    private List<Note> data;
    private String message;
    private int status;

    public List<Note> getData() {
        return data;
    }

    public void setData(List<Note> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
