package com.example.notionary_v1.fragments.data;

public class Note {
    private long id;
    private String title;
    private String description;
    private int color;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note(String title, String description, int color) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.description = description;
        this.color = color;
    }


    public Note(long id, String title, String description, int color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
