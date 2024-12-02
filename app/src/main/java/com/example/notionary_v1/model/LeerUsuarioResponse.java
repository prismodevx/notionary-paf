package com.example.notionary_v1.model;

public class LeerUsuarioResponse {
    private DataLeerUsuarioResponse data;
    private String message;
    private int status;

    public DataLeerUsuarioResponse getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
