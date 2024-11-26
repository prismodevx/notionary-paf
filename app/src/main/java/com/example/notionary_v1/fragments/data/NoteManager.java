package com.example.notionary_v1.fragments.data;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    private static final List<Note> notes = new ArrayList<>();

    public static void addNote(Note note) {
        notes.add(note);
    }

    public static List<Note> getNotes() {
        return notes;
    }
}
