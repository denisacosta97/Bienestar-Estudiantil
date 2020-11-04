package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;

import java.io.Serializable;

public class Rol implements Serializable {

    public static final String TABLE = "roles";
    public static final String KEY_USER = "idUsuario";
    public static final String KEY_ROL = "idRol";
    public static final String KEY_DESCRIPCON = "descripcion";
    public static final String KEY_ID_PADRE = "idRolPadre";


    private int idRol;
    private int idUsuario;
    private String descripcion;
    private int idRolPadre;

    public Rol() {
        descripcion = "";

    }

    public Rol(int idRol, int idUsuario) {
        this.idRol = idRol;
        this.idUsuario = idUsuario;
    }

    public Rol(int idRol, int idUsuario, String descripcion) {
        this.idRol = idRol;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
    }


    public Rol(int idRol, int idRolPadre, String descripcion, int f) {
        this.idRol = idRol;
        this.idRolPadre = idRolPadre;
        this.descripcion = descripcion;
    }


    public Rol(int idRol, int idUsuario, int idRolPadre) {
        this.idRol = idRol;
        this.idUsuario = idUsuario;
        this.idRolPadre = idRolPadre;
    }


    protected Rol(Parcel in) {
        idRol = in.readInt();
        idUsuario = in.readInt();
        idRolPadre = in.readInt();
        descripcion = in.readString();
    }


    public int getIdRol() {
        return idRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRolPadre() {
        return idRolPadre;
    }

    public void setIdRolPadre(int idRolPadre) {
        this.idRolPadre = idRolPadre;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        builder.append(idRol);
        builder.append("\n");
        builder.append("Desc: ");
        builder.append(descripcion);
        return builder.toString();
    }
}
