package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class TipoFamiliar implements Parcelable {

    public static final int LOW = 1;

    private int id, estado;
    private String nombre, descripcion;

    public TipoFamiliar(int id, int estado, String nombre, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public TipoFamiliar() {
        this.id = -1;
        this.estado = -1;
        this.nombre = "";
        this.descripcion = "";
    }

    public TipoFamiliar(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    protected TipoFamiliar(Parcel in) {
        id = in.readInt();
        estado = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<TipoFamiliar> CREATOR = new Creator<TipoFamiliar>() {
        @Override
        public TipoFamiliar createFromParcel(Parcel in) {
            return new TipoFamiliar(in);
        }

        @Override
        public TipoFamiliar[] newArray(int size) {
            return new TipoFamiliar[size];
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

    public static TipoFamiliar toMapper(JSONObject object, int tipo) {
        int id, estado;
        String nombre, descripcion;
        TipoFamiliar familiar = null;
        try {
            switch (tipo) {
                case LOW:
                    id = Integer.parseInt(object.getString("id"));
                    nombre = object.getString("nombre");
                    familiar = new TipoFamiliar(id, nombre);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return familiar;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(estado);
        dest.writeString(nombre);
        dest.writeString(descripcion);
    }
}
