package com.example.notionary_v1.fragments.data;

import com.example.notionary_v1.interf.UsuarioApi;

import java.util.List;

public class ApiResponseUsuario {
    private Usuario data;
    private String message;
    private int status;

    public Usuario getData() {
        return data;
    }

    public void setData(Usuario data) {
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
