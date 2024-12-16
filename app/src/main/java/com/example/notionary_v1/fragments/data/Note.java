package com.example.notionary_v1.fragments.data;

public class Note {
    private int id;
    private String title;
    private String description;
    private String date;
    private int idUsuario;
//    private int color;

    public Note(String title, String description, String date, int idUsuario) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.idUsuario = idUsuario;
    }

    public Note(int id, String title, String description, String date, int idUsuario) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.idUsuario = idUsuario;
    }

//    public Note(String title, String description, int color) {
//        this.id = System.currentTimeMillis();
//        this.title = title;
//        this.description = description;
//        this.color = color;
//    }
//
//
//    public Note(long id, String title, String description, int color) {
//        this.id = id;
//        this.title = title;
//        this.description = description;
//        this.color = color;
//    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }


//    public int getColor() {
//        return color;
//    }

//    public void setColor(int color) {
//        this.color = color;
//    }
}
