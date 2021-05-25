package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class GFamiliar implements Parcelable {

    private int id, estado;
    private String nombre, descripcion;

    public GFamiliar(int id, int estado, String nombre, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public GFamiliar() {
        this.id = -1;
        this.estado = -1;
        this.nombre = "";
        this.descripcion = "";
    }

    protected GFamiliar(Parcel in) {
        id = in.readInt();
        estado = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<GFamiliar> CREATOR = new Creator<GFamiliar>() {
        @Override
        public GFamiliar createFromParcel(Parcel in) {
            return new GFamiliar(in);
        }

        @Override
        public GFamiliar[] newArray(int size) {
            return new GFamiliar[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(estado);
        dest.writeString(nombre);
        dest.writeString(descripcion);
    }
}
