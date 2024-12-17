package com.example.notionary_v1.fragments.data;

public class Reminder {
    private int id;
    private String titulo;
    private String fechaHora;
    private int idUsuario;
    private int esFavorito;

    public Reminder(String titulo, String fechaHora, int idUsuario, int esFavorito) {
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
        this.esFavorito = esFavorito;
    }

    public Reminder(int id, String titulo, String fechaHora, int idUsuario, int esFavorito) {
        this.id = id;
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
        this.esFavorito = esFavorito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(int esFavorito) {
        this.esFavorito = esFavorito;
    }
}
