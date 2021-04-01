package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Medicamento implements Parcelable {

    public static final int COMPLETE = 1;

    private int idUsuario, dia, mes, anio, validez, estado;
    private String tipoMedicamento;

    public Medicamento(int idUsuario, int dia, int mes, int anio, int validez, int estado, String tipoMedicamento) {
        this.idUsuario = idUsuario;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.validez = validez;
        this.estado = estado;
        this.tipoMedicamento = tipoMedicamento;
    }

    protected Medicamento(Parcel in) {
        idUsuario = in.readInt();
        dia = in.readInt();
        mes = in.readInt();
        anio = in.readInt();
        validez = in.readInt();
        estado = in.readInt();
        tipoMedicamento = in.readString();
    }

    public static final Creator<Medicamento> CREATOR = new Creator<Medicamento>() {
        @Override
        public Medicamento createFromParcel(Parcel in) {
            return new Medicamento(in);
        }

        @Override
        public Medicamento[] newArray(int size) {
            return new Medicamento[size];
        }
    };

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public int getValidez() {
        return validez;
    }

    public void setValidez(int validez) {
        this.validez = validez;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getTipoMedicamento() {
        return tipoMedicamento;
    }

    public void setTipoMedicamento(String tipoMedicamento) {
        this.tipoMedicamento = tipoMedicamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUsuario);
        dest.writeInt(dia);
        dest.writeInt(mes);
        dest.writeInt(anio);
        dest.writeInt(validez);
        dest.writeInt(estado);
        dest.writeString(tipoMedicamento);
    }
}
