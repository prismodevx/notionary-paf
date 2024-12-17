package com.example.notionary_v1.fragments.data;

public class UsuarioNote {
    private String usuario;
    private int notaId;

    public UsuarioNote(String usuarioId, int notaId) {
        this.usuario = usuarioId;
        this.notaId = notaId;
    }

    public String getUsuarioId() {
        return usuario;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuario = usuarioId;
    }

    public int getNotaId() {
        return notaId;
    }

    public void setNotaId(int notaId) {
        this.notaId = notaId;
    }
}
