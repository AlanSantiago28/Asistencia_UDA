package com.example.asistenciauda;

import androidx.appcompat.app.AppCompatActivity;

public class modeloAsistencias extends AppCompatActivity {
    private String ID, NoControl, FechaRegistro;
    private String Nombre, Apellido;

    public modeloAsistencias(String ID, String noControl, String fechaRegistro, String nombre, String apellido) {
        this.ID = ID;
        this.NoControl = noControl;
        this.FechaRegistro = fechaRegistro;
        this.Nombre = nombre;
        this.Apellido = apellido;
    }

    // Getters y Setters

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNoControl() {
        return NoControl;
    }

    public void setNoControl(String noControl) {
        NoControl = noControl;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        this.Apellido = apellido;
    }
}

