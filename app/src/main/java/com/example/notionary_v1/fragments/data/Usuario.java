package com.example.notionary_v1.fragments.data;

public class Usuario {
    private String email;
    private String password;
    private String nombresCompletos;

    public Usuario(String email, String password, String nombresCompletos) {
        this.email = email;
        this.password = password;
        this.nombresCompletos = nombresCompletos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }
}
