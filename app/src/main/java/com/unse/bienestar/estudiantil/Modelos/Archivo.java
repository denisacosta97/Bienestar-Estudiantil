package com.unse.bienestar.estudiantil.Modelos;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.unse.bienestar.estudiantil.Herramientas.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Archivo implements Parcelable {

    public static final int LOW = 1;

    private int id, validez, idArea, idUsuario;
    private String nombreArchivo, fechaCreacion, fechaModificacion, nombre, descripcion;
    private Uri mFile;

    public Archivo(int id, int validez, int idArea, int idUsuario, String nombreArchivo,
                   String fechaCreacion, String fechaModificacion, String nombre) {
        this.id = id;
        this.validez = validez;
        this.idArea = idArea;
        this.idUsuario = idUsuario;
        this.nombreArchivo = nombreArchivo;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.nombre = nombre;
    }

    public Archivo() {

    }

    public Archivo(int id, String nombreArchivo, String nombre) {
        this.id = id;
        this.nombreArchivo = nombreArchivo;
        this.nombre = nombre;
    }

    protected Archivo(Parcel in) {
        id = in.readInt();
        validez = in.readInt();
        idArea = in.readInt();
        idUsuario = in.readInt();
        nombreArchivo = in.readString();
        fechaCreacion = in.readString();
        fechaModificacion = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<Archivo> CREATOR = new Creator<Archivo>() {
        @Override
        public Archivo createFromParcel(Parcel in) {
            return new Archivo(in);
        }

        @Override
        public Archivo[] newArray(int size) {
            return new Archivo[size];
        }
    };

    public String getFileName(Activity context) {
        return Utils.getFileName(mFile, context);
    }

    public Uri getFile() {
        return mFile;
    }

    public void setFile(Uri file) {
        mFile = file;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getValidez() {
        return validez;
    }

    public void setValidez(int validez) {
        this.validez = validez;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreArchivo(boolean ext) {
        if (mFile == null) {
            return nombreArchivo;
        } else {
            if (ext)
                return nombreArchivo;
            if (!nombreArchivo.contains(".")) {
                return nombreArchivo;
            } else {
                //Aqui saco todo
                int index = nombreArchivo.indexOf("_");
                String n1 = nombreArchivo.substring(index + 1);
                int index2 = n1.indexOf(".");
                n1 = n1.substring(0, index2);
                return n1;

            }


        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public static Archivo toMapper(JSONObject object, int tipo) {
        Archivo archivo = null;

        int id, validez, idArea, idUsuario;
        String nombreArchivo, fechaCreacion, fechaModificacion, nombre, descripcion;
        Uri mFile;
        try {
            switch (tipo) {
                case LOW:
                    id = Integer.parseInt(object.getString("idarchivo"));
                    nombreArchivo = object.getString("nombrearchivo");
                    fechaModificacion = object.getString("fecharegistro");
                    descripcion = object.getString("descripcion");
                    nombre = object.getString("nombre");
                    archivo = new Archivo(id, nombreArchivo, descripcion);
                    archivo.setFechaCreacion(fechaModificacion);
                    archivo.setDescripcion(nombre);
                    break;
                case -1:
                    id = Integer.parseInt(object.getString("idarchivo"));
                    idArea = Integer.parseInt(object.getString("idArea"));
                    validez = Integer.parseInt(object.getString("validez"));
                    idUsuario = Integer.parseInt(object.getString("idUsuario"));
                    nombre = object.getString("nombre");
                    String fecha = object.getString("fechaCreacion");
                    String fechaModif = object.getString("fechaModificacion");
                    nombreArchivo = object.getString("nombreArchivo");
                    archivo = new Archivo(id, validez, idArea, idUsuario, nombreArchivo, fecha, fechaModif,
                            nombre);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return archivo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(validez);
        dest.writeInt(idArea);
        dest.writeInt(idUsuario);
        dest.writeString(nombreArchivo);
        dest.writeString(fechaCreacion);
        dest.writeString(fechaModificacion);
        dest.writeString(nombre);
        dest.writeString(descripcion);
    }
}
