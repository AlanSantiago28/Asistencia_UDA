package com.example.asistenciauda;

public class usuario {

    private String ID, NoControl, ApellidoPaterno, ApellidoMaterno, Nombre, Sexo, FechaNacimiento, Telefono, TelefonoEmergencia, Carrera, Turno, Campus, Observaciones;


    public usuario(String ID, String noControl, String apellidoPaterno, String apellidoMaterno, String nombre, String sexo, String fechaNacimiento, String telefono, String telefonoEmergencia, String carrera, String turno, String campus, String observaciones) {
        this.ID = ID;
        NoControl = noControl;
        ApellidoPaterno = apellidoPaterno;
        ApellidoMaterno = apellidoMaterno;
        Nombre = nombre;
        Sexo = sexo;
        FechaNacimiento = fechaNacimiento;
        Telefono = telefono;
        TelefonoEmergencia = telefonoEmergencia;
        Carrera = carrera;
        Turno = turno;
        Campus = campus;
        Observaciones = observaciones;


    }

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

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getTelefonoEmergencia() {
        return TelefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        TelefonoEmergencia = telefonoEmergencia;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String turno) {
        Turno = turno;
    }

    public String getCampus() {
        return Campus;
    }

    public void setCampus(String campus) {
        Campus = campus;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}

