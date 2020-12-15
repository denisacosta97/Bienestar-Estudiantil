package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Documentacion implements Parcelable {

    public static final int LOW = 1;

    private String nombre, nombreArchivo;
    private int codigo;


    public Documentacion(String nombre, String nombreArchivo, int codigo) {
        this.nombre = nombre;
        this.nombreArchivo = nombreArchivo;
        this.codigo = codigo;
    }

    protected Documentacion(Parcel in) {

        nombre = in.readString();
        nombreArchivo = in.readString();
        codigo = in.readInt();
    }

    public static Documentacion toMapper(JSONObject object, int tipo) {
         String nombre, nombreArchivo;
         int codigo;
         Documentacion documentacion = null;
        try {
            switch (tipo) {
                case LOW:
                    codigo = Integer.parseInt(object.getString("idarchivo"));
                    nombreArchivo = object.getString("nombrearchivo");
                    nombre = object.getString("descripcion");
                    documentacion = new Documentacion(nombre, nombreArchivo, codigo);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return documentacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public static final Creator<Documentacion> CREATOR = new Creator<Documentacion>() {
        @Override
        public Documentacion createFromParcel(Parcel in) {
            return new Documentacion(in);
        }

        @Override
        public Documentacion[] newArray(int size) {
            return new Documentacion[size];
        }
    };

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Documentacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nombre);
        dest.writeString(nombreArchivo);
        dest.writeInt(codigo);
    }
}
