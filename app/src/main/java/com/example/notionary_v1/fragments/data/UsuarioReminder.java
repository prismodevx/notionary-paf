package com.example.notionary_v1.fragments.data;

public class UsuarioReminder {
    private String usuario;
    private int recordatorioId;

    public UsuarioReminder(String usuario, int recordatorioId) {
        this.usuario = usuario;
        this.recordatorioId = recordatorioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getRecordatorioId() {
        return recordatorioId;
    }

    public void setRecordatorioId(int recordatorioId) {
        this.recordatorioId = recordatorioId;
    }
}
