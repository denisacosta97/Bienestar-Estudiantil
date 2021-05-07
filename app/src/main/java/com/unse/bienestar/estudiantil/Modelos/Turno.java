package com.unse.bienestar.estudiantil.Modelos;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Turno implements Parcelable {

    public static final int LOW_2 = 0;
    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int UAPU = 3;
    public static final int UAPU_TURNOS = 4;

    public static final int TIPO_BECA = 1;
    public static final int TIPO_UPA_MEDICAMENTO = 2;
    public static final int TIPO_UPA_TURNOS = 3;
    public static final int TIPO_PC_TURNOS = 5;

    int id, receptor, dia, mes, anio, idEstado;
    String titulo, descripcion, nombre, apellido, dni, dniEncripted;
    String estado, fechaInicio, fechaFin, fecha, receptorString, fechaRegistro, horario;
    int tipo;

    public Turno(String titulo, String descripcion, String estado, String fechaInicio, String fechaFin,
                 String fecha, String dni, String nombre, String apellido) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fecha = fecha;
    }

    public Turno(int receptor, String fechaInicio) {
        this.receptor = receptor;
        this.fechaInicio = fechaInicio;
    }

    public Turno(int id, String dni, int idEstado, int dia, int mes, int anio, String horario, String estado, String titulo) {
        this.id = id;
        this.dni = dni;
        this.idEstado = idEstado;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.horario = horario;
        this.estado = estado;
        this.titulo = titulo;
    }

    protected Turno(Parcel in) {
        id = in.readInt();
        receptor = in.readInt();
        dia = in.readInt();
        mes = in.readInt();
        anio = in.readInt();
        idEstado = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        dni = in.readString();
        estado = in.readString();
        fechaInicio = in.readString();
        fechaFin = in.readString();
        fecha = in.readString();
        receptorString = in.readString();
        fechaRegistro = in.readString();
        horario = in.readString();
        tipo = in.readInt();
        dniEncripted = in.readString();
    }

    public static final Creator<Turno> CREATOR = new Creator<Turno>() {
        @Override
        public Turno createFromParcel(Parcel in) {
            return new Turno(in);
        }

        @Override
        public Turno[] newArray(int size) {
            return new Turno[size];
        }
    };

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }


    public int getReceptor() {
        return receptor;
    }

    public String getReceptorString() {
        return receptorString;
    }

    public void setReceptor(int receptor) {
        this.receptor = receptor;
    }

    public void setReceptor(String receptor) {
        this.receptorString = receptor;
    }

    public String getFecha() {
        return fecha != null ? fecha : String.format("%s/%s/%s", getDia(), getMes(), getAnio());
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        if (tipo == TIPO_UPA_MEDICAMENTO) {
            return String.format("Retiro Medicamentos: %s", getMedicamentos(Integer.parseInt(descripcion)));
        } else if (tipo == TIPO_UPA_TURNOS) {
            return descripcion != null ? descripcion : "ERROR";
        }
        return titulo != null ? titulo : "ERROR";
    }

    public String getMedicamentos(int i) {
        return i == 0 ? "Clínica Médica" : "Salud Sexual y Reprod.";
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getFechaRegistro() {
        if (fechaRegistro != null)
            return fechaRegistro;
        else {
            return String.format("%s-%s-%s %s:%s:%s", getAnio(), getMes(), getDia(),
                    "00", "00", "00");
        }
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public Turno(int id, String descripcion, String estado, String fechaRegistro) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public static Turno mapper(JSONObject object, int tipo) {
        Turno turno = null;
        try {
            int id, receptor, dia, mes, anio, idEstado;
            String titulo, descripcion, nombre, apellido, dni, horario;
            String estado, fechaInicio, fechaFin, fecha, fechRegistro, lugar;
            switch (tipo) {
                case UAPU:
                    id = Integer.parseInt(object.getString("idusuario"));
                    estado = object.getString("descripcion");
                    dia = Integer.parseInt(object.getString("dia"));
                    mes = Integer.parseInt(object.getString("mes"));
                    anio = Integer.parseInt(object.getString("anio"));
                    horario = object.getString("horario");
                    descripcion = object.getString("tipomedicamento");
                    fechRegistro = object.getString("fecharegistro");
                    turno = new Turno(id, descripcion, estado, fechRegistro);
                    turno.setFechaInicio(horario);
                    turno.setDia(dia);
                    turno.setMes(mes);
                    turno.setAnio(anio);
                    break;
                case UAPU_TURNOS:
                    id = Integer.parseInt(object.getString("idturno"));
                    dia = Integer.parseInt(object.getString("dia"));
                    mes = Integer.parseInt(object.getString("mes"));
                    anio = Integer.parseInt(object.getString("anio"));
                    estado = object.getString("estado");
                    titulo = object.getString("titulo");
                    horario = object.getString("horario");
                    fechRegistro = object.getString("fecharegistro");
                    turno = new Turno(id, titulo, estado, fechRegistro);
                    turno.setDia(dia);
                    turno.setMes(mes);
                    turno.setAnio(anio);
                    turno.setFechaInicio(horario);
                    break;
                case LOW_2:
                    horario = object.getString("horario");
                    turno = new Turno(0, horario);
                    break;
                case LOW:
                    receptor = Integer.parseInt(object.getString("receptor"));
                    fechaInicio = object.getString("horario");
                    turno = new Turno(receptor, fechaInicio);
                    break;
                case MEDIUM:
                    titulo = object.getString("nombre");
                    estado = object.getString("descripcion");
                    fechaInicio = object.getString("horario");
                    dia = Integer.parseInt(object.getString("dia"));
                    mes = Integer.parseInt(object.getString("mes"));
                    anio = Integer.parseInt(object.getString("anio"));
                    receptor = Integer.parseInt(object.getString("receptor"));
                    fechRegistro = object.getString("fecharegistro");
                    turno = new Turno(dia, mes, anio, titulo, estado, fechaInicio);
                    turno.setReceptor(receptor);
                    turno.setReceptor(String.format("Receptor %s", receptor));
                    turno.setFechaRegistro(fechRegistro);
                    break;
                case TIPO_PC_TURNOS:
                    id = Integer.parseInt(object.getString("id"));
                    dni = object.getString("idusuario");
                    idEstado = Integer.parseInt(object.getString("idestado"));
                    dia = Integer.parseInt(object.getString("dia"));
                    mes = Integer.parseInt(object.getString("mes"));
                    anio = Integer.parseInt(object.getString("anio"));
                    horario = object.getString("horario");
                    estado = object.getString("descripcion");
                    titulo = object.getString("nombrelugar");

                    turno = new Turno(id, dni, idEstado, dia, mes, anio, horario, estado, titulo);
                    turno.setFechaInicio(horario);
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return turno;
    }

    public String getEncripted() {
        return dniEncripted;
    }

    public void setEncripted(String dniEncripted) {
        this.dniEncripted = dniEncripted;
    }

    public Turno(int dia, int mes, int anio, String titulo, String estado, String fechaInicio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.titulo = titulo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(receptor);
        dest.writeInt(dia);
        dest.writeInt(mes);
        dest.writeInt(anio);
        dest.writeInt(idEstado);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(dni);
        dest.writeString(estado);
        dest.writeString(fechaInicio);
        dest.writeString(fechaFin);
        dest.writeString(fecha);
        dest.writeString(receptorString);
        dest.writeString(fechaRegistro);
        dest.writeString(horario);
        dest.writeInt(tipo);
        dest.writeString(dniEncripted);
    }

}
