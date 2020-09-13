package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class CredencialDeporte implements Parcelable {

    private int idInscripcion, validez, idTemporada, idDeporte, anio, idUsuario;
    private String fechaCreacion, nombre, descripcion, nombreU, apellido, legajo, facultad;

    public static final int MEDIUM = 1;
    public static final int COMPLETE = 2;

    public CredencialDeporte(int idInscripcion, int validez, int idTemporada, String fechaCreacion) {
        this.idInscripcion = idInscripcion;
        this.validez = validez;
        this.idTemporada = idTemporada;
        this.fechaCreacion = fechaCreacion;
    }

    protected CredencialDeporte(Parcel in) {
        idInscripcion = in.readInt();
        validez = in.readInt();
        idTemporada = in.readInt();
        idDeporte = in.readInt();
        anio = in.readInt();
        fechaCreacion = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        nombreU = in.readString();
        apellido = in.readString();
        legajo = in.readString();
        facultad = in.readString();
        idUsuario = in.readInt();
    }

    public static final Creator<CredencialDeporte> CREATOR = new Creator<CredencialDeporte>() {
        @Override
        public CredencialDeporte createFromParcel(Parcel in) {
            return new CredencialDeporte(in);
        }

        @Override
        public CredencialDeporte[] newArray(int size) {
            return new CredencialDeporte[size];
        }
    };

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public static CredencialDeporte mapper(JSONObject object, int tipo) {
        CredencialDeporte credencialDeporte = null;
        String nombre, descripcion = "", titulo, legajo = null, facultad = null;
        int validez, anio, idTemporada, tipoUsuario, idInscripcion, idDeporte;
        try {
            switch (tipo) {
                case MEDIUM:
                     idInscripcion = Integer.parseInt(object.getString("idinscripcion"));
                     validez = Integer.parseInt(object.getString("validez"));
                     idTemporada = Integer.parseInt(object.getString("anio"));
                    String fechaCreacion = object.getString("fechacreacion");
                    credencialDeporte = new CredencialDeporte(idInscripcion, validez, idTemporada, fechaCreacion);
                    break;
                case COMPLETE:
                    nombre = object.getString("nombreu");
                    String apellido = object.getString("apellido");
                     idInscripcion = Integer.parseInt(object.getString("idinscripcion"));
                    validez = Integer.parseInt(object.getString("validez"));
                    idDeporte = Integer.parseInt(object.getString("iddeporte"));
                    String nombreDeporte = object.getString("nombre");
                    anio = Integer.parseInt(object.getString("anio"));
                    credencialDeporte = new CredencialDeporte(idInscripcion, validez, idDeporte, anio,
                            nombreDeporte, nombre, apellido);
                    break;
            }

        } catch (JSONException e) {

        }
        return credencialDeporte;
    }

    public CredencialDeporte(int idInscripcion, int validez, int idDeporte, int anio, String nombre, String nombreU, String apellido) {
        this.idInscripcion = idInscripcion;
        this.validez = validez;
        this.idDeporte = idDeporte;
        this.anio = anio;
        this.nombre = nombre;
        this.nombreU = nombreU;
        this.apellido = apellido;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getValidez() {
        return validez;
    }

    public void setValidez(int validez) {
        this.validez = validez;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getNombreU() {
        return nombreU;
    }

    public void setNombreU(String nombreU) {
        this.nombreU = nombreU;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(int idTemporada) {
        this.idTemporada = idTemporada;
    }

    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
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
        dest.writeInt(idInscripcion);
        dest.writeInt(validez);
        dest.writeInt(idTemporada);
        dest.writeInt(idDeporte);
        dest.writeInt(anio);
        dest.writeString(fechaCreacion);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(nombreU);
        dest.writeString(apellido);
        dest.writeString(legajo);
        dest.writeString(facultad);
        dest.writeInt(idUsuario);
    }
}
