package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Familiar implements Parcelable {

    public static final int LOW = 1;

    private int idUsuario, idFamiliar, idBeca, anio, validez, cantidad;
    private String descripcion;

    public Familiar(int idUsuario, int idFamiliar, int idBeca, int anio, int validez, String descripcion) {
        this.idUsuario = idUsuario;
        this.idFamiliar = idFamiliar;
        this.idBeca = idBeca;
        this.anio = anio;
        this.validez = validez;
        this.descripcion = descripcion;
    }

    public Familiar() {

    }

    protected Familiar(Parcel in) {
        idUsuario = in.readInt();
        idFamiliar = in.readInt();
        idBeca = in.readInt();
        anio = in.readInt();
        validez = in.readInt();
        descripcion = in.readString();
        cantidad = in.readInt();
    }

    public static final Creator<Familiar> CREATOR = new Creator<Familiar>() {
        @Override
        public Familiar createFromParcel(Parcel in) {
            return new Familiar(in);
        }

        @Override
        public Familiar[] newArray(int size) {
            return new Familiar[size];
        }
    };

    public static Familiar mapper(JSONObject object, int tipo) {
        int idUsuario, idFamiliar, idBeca, anio, validez;
        String descripcion;
        Familiar familiar = null;
        try {
            switch (tipo) {
                case LOW:
                    idUsuario = Integer.parseInt(object.getString("idusuario"));
                    idFamiliar = Integer.parseInt(object.getString("id"));
                    idBeca = Integer.parseInt(object.getString("idbeca"));
                    anio = Integer.parseInt(object.getString("anio"));
                    validez = Integer.parseInt(object.getString("validez"));
                    descripcion = object.getString("descripcion");
                    familiar = new Familiar(idUsuario, idFamiliar, idBeca, anio, validez, descripcion);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return familiar;

    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdFamiliar() {
        return idFamiliar;
    }

    public void setIdFamiliar(int idFamiliar) {
        this.idFamiliar = idFamiliar;
    }

    public int getIdBeca() {
        return idBeca;
    }

    public void setIdBeca(int idBeca) {
        this.idBeca = idBeca;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getValidez() {
        return validez;
    }

    public void setValidez(int validez) {
        this.validez = validez;
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
        dest.writeInt(idUsuario);
        dest.writeInt(idFamiliar);
        dest.writeInt(idBeca);
        dest.writeInt(anio);
        dest.writeInt(validez);
        dest.writeString(descripcion);
        dest.writeInt(cantidad);
    }
}
