package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Inscripcion implements Parcelable {

    public static final int TIPO_BECA = 1;
    public static final int TIPO_DEPORTE = 2;

    public static final int COMPLETE = 1;
    public static final int HIGH = 4;
    public static final int LOW_BECA = 5;
    public static final int PARCIAL = 2;
    public static final int LOW = 3;

    private int idInscripcion;
    private int idEstado;
    private int idUsuario;
    private int idTemporada;
    private int wsp;
    private int cantMaterias;
    private String facebook;
    private String instagram;
    private String objetivo;
    private String peso;
    private String altura;
    private String fechaRegistro;
    private String fechaModificacion;
    private int disponible;
    private int validez;
    private String cuales;
    private int intensidad;
    private String lugar;

    private int idBeca;
    private int anio;
    private int estado;
    private String descripcion, nombre, apellido, nombreBeca, estadoDescripcion;

    private String nombreEstado, nombreDeporte, titulo;
    private int tipoInscripcion, idDeporte;



    public Inscripcion(int idUsuario, String fechaModificacion, int validez, int idBeca, int anio, int estado) {
        this.idUsuario = idUsuario;
        this.fechaModificacion = fechaModificacion;
        this.validez = validez;
        this.idBeca = idBeca;
        this.anio = anio;
        this.estado = estado;
    }

    //Inscripcion principal
    public Inscripcion(int idInscripcion, int idEstado, int idUsuario, int idTemporada, int wsp,
                       int cantMaterias, String facebook, String instagram, String objetivo,
                       String peso, String altura, String fechaRegistro, String fechaModificacion,
                       int disponible, int validez, String cuales, int intensidad,
                       String lugar) {
        this.idInscripcion = idInscripcion;
        this.idEstado = idEstado;
        this.idUsuario = idUsuario;
        this.idTemporada = idTemporada;
        this.wsp = wsp;
        this.cantMaterias = cantMaterias;
        this.facebook = facebook;
        this.instagram = instagram;
        this.objetivo = objetivo;
        this.peso = peso;
        this.altura = altura;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.disponible = disponible;
        this.validez = validez;
        this.cuales = cuales;
        this.intensidad = intensidad;
        this.lugar = lugar;
    }

    //Inscripcion para el perfil, muestra inscripciones generales
    public Inscripcion(int idInscripcion, String titulo, int anio, int validez, int tipo,
                       String estado, String fechaRegistro) {
        this.idInscripcion = idInscripcion;
        this.titulo = titulo;
        this.fechaRegistro = fechaRegistro;
        this.idTemporada = anio;
        this.validez = validez;
        this.tipoInscripcion = tipo;
        this.nombreEstado = estado;
    }

    protected Inscripcion(Parcel in) {
        idInscripcion = in.readInt();
        idEstado = in.readInt();
        idUsuario = in.readInt();
        idTemporada = in.readInt();
        wsp = in.readInt();
        cantMaterias = in.readInt();
        facebook = in.readString();
        instagram = in.readString();
        objetivo = in.readString();
        peso = in.readString();
        altura = in.readString();
        fechaRegistro = in.readString();
        fechaModificacion = in.readString();
        disponible = in.readInt();
        validez = in.readInt();
        cuales = in.readString();
        intensidad = in.readInt();
        lugar = in.readString();
        idBeca = in.readInt();
        anio = in.readInt();
        estado = in.readInt();
        descripcion = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        nombreBeca = in.readString();
        estadoDescripcion = in.readString();
        nombreEstado = in.readString();
        nombreDeporte = in.readString();
        titulo = in.readString();
        tipoInscripcion = in.readInt();
        idDeporte = in.readInt();
    }


    public static final Creator<Inscripcion> CREATOR = new Creator<Inscripcion>() {
        @Override
        public Inscripcion createFromParcel(Parcel in) {
            return new Inscripcion(in);
        }

        @Override
        public Inscripcion[] newArray(int size) {
            return new Inscripcion[size];
        }
    };

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipoInscripcion() {
        return tipoInscripcion;
    }

    public void setTipoInscripcion(int tipoInscripcion) {
        this.tipoInscripcion = tipoInscripcion;
    }


    public int getIdDeporte() {
        return idDeporte;
    }

    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String nombreDeporte) {
        this.nombreDeporte = nombreDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(int idTemporada) {
        this.idTemporada = idTemporada;
    }

    public int getWsp() {
        return wsp;
    }

    public void setWsp(int wsp) {
        this.wsp = wsp;
    }

    public int getCantMaterias() {
        return cantMaterias;
    }

    public void setCantMaterias(int cantMaterias) {
        this.cantMaterias = cantMaterias;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public int getValidez() {
        return validez;
    }

    public void setValidez(int validez) {
        this.validez = validez;
    }

    public String getCuales() {
        return cuales;
    }

    public void setCuales(String cuales) {
        this.cuales = cuales;
    }

    public int getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(int intensidad) {
        this.intensidad = intensidad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreBeca() {
        return nombreBeca;
    }

    public void setNombreBeca(String nombreBeca) {
        this.nombreBeca = nombreBeca;
    }

    public int getTipo() {
        return tipoInscripcion;
    }

    public void setTipo(int tipo) {
        this.tipoInscripcion = tipo;
    }

    public Inscripcion(int idBeca, int anio, String descripcion, String nombreBeca) {
        this.idBeca = idBeca;
        this.anio = anio;
        this.idTemporada = anio;
        this.descripcion = descripcion;
        this.nombreBeca = nombreBeca;
    }

    public static Inscripcion mapper(JSONObject o, int tipo) {
        Inscripcion inscripcion = null;
        int idInscripcion, disponible, idEstado, idUsuario, idTemporada, wsp,
                inten, cantMaterias, validez;
        String facebook, instagram, objetivo, altura, peso, cuales, lugar, fechaRegistro, fechaModificacion;
        try {
            switch (tipo) {
                case HIGH:
                    idUsuario = Integer.parseInt(o.getString("idusuario"));
                    int idBeca = Integer.parseInt(o.getString("idbeca"));
                    String descripcion = o.getString("descripcionbeca");
                    String observaciones = o.isNull("observaciones") ? "" : o.getString("observaciones");
                    idEstado = Integer.parseInt(o.getString("estado"));
                    idTemporada = Integer.parseInt(o.getString("anio"));
                    fechaModificacion = o.getString("fechamodificacion");
                    fechaRegistro = o.getString("fecharegistro");
                    String nombreBeca = o.getString("nombrebeca");
                    String nombre2 = o.getString("nombre");
                    String apellido2 = o.getString("apellido");
                    validez = Integer.parseInt(o.getString("validez"));
                    int tipoUsuario = Integer.parseInt(o.getString("tipousuario"));
                    inscripcion = new Inscripcion(idUsuario, fechaModificacion, validez, idBeca,
                            idTemporada, idEstado);
                    inscripcion.setFechaRegistro(fechaRegistro);
                    inscripcion.setEstadoDescripcion(descripcion);
                    inscripcion.setIntensidad(tipoUsuario);
                    inscripcion.setNombre(nombre2);
                    inscripcion.setApellido(apellido2);
                    inscripcion.setNombreBeca(nombreBeca);
                    inscripcion.setDescripcion(observaciones);
                    break;
                case LOW_BECA:
                    idBeca = Integer.parseInt(o.getString("idbeca"));
                    descripcion = o.getString("descripcionbeca");
                    idTemporada = Integer.parseInt(o.getString("anio"));
                    nombreBeca = o.getString("nombrebeca");
                    inscripcion = new Inscripcion(idBeca, idTemporada, descripcion, nombreBeca);
                    inscripcion.setTipo(Inscripcion.TIPO_BECA);
                    break;
                case COMPLETE:
                    idEstado = Integer.parseInt(o.getString("estado"));
                    int idDeporte = Integer.parseInt(o.getString("iddeporte"));
                    String estadodescripcion = o.getString("estadodescripcion");
                    String nombredeporte = o.getString("nombredeporte");
                    idInscripcion = Integer.parseInt(o.getString("idinscripcion"));
                    idUsuario = Integer.parseInt(o.getString("idusuario"));
                    idTemporada = Integer.parseInt(o.getString("anio"));
                    wsp = Integer.parseInt(o.getString("iswhatsapp"));
                    inten = Integer.parseInt(o.getString("intensidad"));
                    cantMaterias = Integer.parseInt(o.getString("cantmaterias"));
                    disponible = Integer.parseInt(o.getString("disponible"));
                    facebook = o.getString("facebook");
                    instagram = o.getString("instagram");
                    objetivo = o.getString("objetivo");
                    altura = o.getString("altura");
                    peso = o.getString("peso");
                    cuales = o.getString("cuales");
                    lugar = o.getString("lugar");
                    fechaModificacion = o.getString("fechamodificacion");
                    fechaRegistro = o.getString("fecharegistro");
                    validez = Integer.parseInt(o.getString("validez"));
                    inscripcion = new Inscripcion(idInscripcion, idEstado, idUsuario, idTemporada,
                            wsp, cantMaterias, facebook,
                            instagram, objetivo, peso, altura, fechaRegistro, fechaModificacion,
                            disponible, validez, cuales, inten, lugar);
                    inscripcion.setNombreEstado(estadodescripcion);
                    inscripcion.setIdDeporte(idDeporte);
                    inscripcion.setNombreDeporte(nombredeporte);
                    break;
                case PARCIAL:
                    int id = Integer.parseInt(o.getString("idinscripcion"));
                    idEstado = Integer.parseInt(o.getString("idestado"));
                    int anio = Integer.parseInt(o.getString("anio"));
                    String nombreDeporte = o.getString("nombredeporte");
                    validez = Integer.parseInt(o.getString("validez"));
                    fechaRegistro = o.getString("fecharegistro");
                    String estado = o.getString("nombree");

                    inscripcion = new Inscripcion(id, nombreDeporte, anio,
                            validez, Inscripcion.TIPO_DEPORTE, estado, fechaRegistro);
                    inscripcion.setIdEstado(idEstado);
                case LOW:
                    idInscripcion = Integer.parseInt(o.getString("idinscripcion"));
                    idEstado = Integer.parseInt(o.getString("idestado"));
                    idUsuario = Integer.parseInt(o.getString("idusuario"));
                    validez = Integer.parseInt(o.getString("validez"));
                    fechaRegistro = o.getString("fecharegistro");
                    estado = o.getString("nombree");
                    String nombre = o.getString("nombre");
                    String apellido = o.getString("apellido");

                    inscripcion = new Inscripcion(idInscripcion, String.format("%s %s", nombre, apellido), 0,
                            validez, Inscripcion.TIPO_DEPORTE, estado, fechaRegistro);
                    inscripcion.setIdEstado(idEstado);
                    inscripcion.setIdUsuario(idUsuario);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inscripcion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idInscripcion);
        dest.writeInt(idEstado);
        dest.writeInt(idUsuario);
        dest.writeInt(idTemporada);
        dest.writeInt(wsp);
        dest.writeInt(cantMaterias);
        dest.writeString(facebook);
        dest.writeString(instagram);
        dest.writeString(objetivo);
        dest.writeString(peso);
        dest.writeString(altura);
        dest.writeString(fechaRegistro);
        dest.writeString(fechaModificacion);
        dest.writeInt(disponible);
        dest.writeInt(validez);
        dest.writeString(cuales);
        dest.writeInt(intensidad);
        dest.writeString(lugar);
        dest.writeInt(idBeca);
        dest.writeInt(anio);
        dest.writeInt(estado);
        dest.writeString(descripcion);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(nombreBeca);
        dest.writeString(estadoDescripcion);
        dest.writeString(nombreEstado);
        dest.writeString(nombreDeporte);
        dest.writeString(titulo);
        dest.writeInt(tipoInscripcion);
        dest.writeInt(idDeporte);
    }
}
