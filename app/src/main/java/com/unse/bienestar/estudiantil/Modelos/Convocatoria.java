package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Convocatoria implements Parcelable {

    public static final int MEDIUM = 2;

    int idBeca, anio;
    String nombreBeca, fechaInicio, fechaFin;

    public Convocatoria(int idBeca, int anio, String nombreBeca, String fechaInicio, String fechaFin) {
        this.idBeca = idBeca;
        this.anio = anio;
        this.nombreBeca = nombreBeca;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    protected Convocatoria(Parcel in) {
        idBeca = in.readInt();
        anio = in.readInt();
        nombreBeca = in.readString();
        fechaInicio = in.readString();
        fechaFin = in.readString();
    }

    public static final Creator<Convocatoria> CREATOR = new Creator<Convocatoria>() {
        @Override
        public Convocatoria createFromParcel(Parcel in) {
            return new Convocatoria(in);
        }

        @Override
        public Convocatoria[] newArray(int size) {
            return new Convocatoria[size];
        }
    };

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

    public String getNombreBeca() {
        return nombreBeca;
    }

    public void setNombreBeca(String nombreBeca) {
        this.nombreBeca = nombreBeca;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public static Convocatoria mapper(JSONObject object, int tipo) {
        Convocatoria convocatoria = null;
        try {
            int idBeca, anio;
            String nombreBeca, fechaInicio, fechaFin;
            switch (tipo) {
                case MEDIUM:
                    idBeca = Integer.parseInt(object.getString("idbeca"));
                    anio = Integer.parseInt(object.getString("anio"));
                    nombreBeca = object.getString("nombre");
                    fechaInicio = object.getString("fechainicio");
                    fechaFin = object.getString("fechafin");

                    convocatoria = new Convocatoria(idBeca, anio, nombreBeca, fechaInicio, fechaFin);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convocatoria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBeca);
        dest.writeInt(anio);
        dest.writeString(nombreBeca);
        dest.writeString(fechaInicio);
        dest.writeString(fechaFin);
    }
}
