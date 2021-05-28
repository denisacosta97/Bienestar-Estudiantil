package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Documentacion implements Parcelable {

    public static final int LOW = 1;
    public static final int BECAS = 2;

    private String nombre, nombreArchivo, descripcion;
    private int codigo, idFamiliar, idBeca, anio, validez, idUsuario;


    public Documentacion(String nombre, String nombreArchivo, int codigo) {
        this.nombre = nombre;
        this.nombreArchivo = nombreArchivo;
        this.codigo = codigo;
    }

    public Documentacion(int idFamiliar, int idUsuario, int idBeca, int anio, String descripcion,
                         int validez) {
        this.descripcion = descripcion;
        this.idFamiliar = idFamiliar;
        this.idBeca = idBeca;
        this.anio = anio;
        this.validez = validez;
        this.idUsuario = idUsuario;
    }

    protected Documentacion(Parcel in) {
        nombre = in.readString();
        nombreArchivo = in.readString();
        descripcion = in.readString();
        codigo = in.readInt();
        idFamiliar = in.readInt();
        idBeca = in.readInt();
        anio = in.readInt();
        validez = in.readInt();
        idUsuario = in.readInt();
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

    public static Documentacion mapper(JSONObject object, int tipo) {
         String nombre, nombreArchivo, descripcion;
         int codigo, idFamiliar, idBeca, anio, validez, idUsuario;
         Documentacion documentacion = null;
        try {
            switch (tipo) {
                case LOW:
                    codigo = Integer.parseInt(object.getString("idarchivo"));
                    nombreArchivo = object.getString("nombrearchivo");
                    nombre = object.getString("descripcion");
                    documentacion = new Documentacion(nombre, nombreArchivo, codigo);
                    break;
                case BECAS:
                    idFamiliar = Integer.parseInt(object.getString("id"));
                    idUsuario = Integer.parseInt(object.getString("idusuario"));
                    idBeca = Integer.parseInt(object.getString("idbeca"));
                    anio = Integer.parseInt(object.getString("anio"));
                    descripcion = object.getString("descripcion");
                    validez = Integer.parseInt(object.getString("validez"));
                    documentacion = new Documentacion(idFamiliar, idUsuario, idBeca, anio, descripcion, validez);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(nombreArchivo);
        dest.writeString(descripcion);
        dest.writeInt(codigo);
        dest.writeInt(idFamiliar);
        dest.writeInt(idBeca);
        dest.writeInt(anio);
        dest.writeInt(validez);
        dest.writeInt(idUsuario);
    }
}
