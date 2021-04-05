package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiciosUPA implements Parcelable {

    private int idServicio, icon, categ, turnos;
    private String name, desc, dias, hora, nomApMed;

    public ServiciosUPA(int idServicio, int icon, String name, String desc, String dias,
                        String hora, String nomApMed, int categ) {
        this.idServicio = idServicio;
        this.icon = icon;
        this.categ = categ;
        this.name = name;
        this.desc = desc;
        this.dias = dias;
        this.hora = hora;
        this.nomApMed = nomApMed;
    }

    protected ServiciosUPA(Parcel in) {
        idServicio = in.readInt();
        icon = in.readInt();
        name = in.readString();
        desc = in.readString();
        dias = in.readString();
        hora = in.readString();
        nomApMed = in.readString();
        categ = in.readInt();
        turnos = in.readInt();
    }

    public static final Creator<ServiciosUPA> CREATOR = new Creator<ServiciosUPA>() {
        @Override
        public ServiciosUPA createFromParcel(Parcel in) {
            return new ServiciosUPA(in);
        }

        @Override
        public ServiciosUPA[] newArray(int size) {
            return new ServiciosUPA[size];
        }
    };

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNomApMed() {
        return nomApMed;
    }

    public void setNomApMed(String nomApMed) {
        this.nomApMed = nomApMed;
    }

    public int getCateg() {
        return categ;
    }

    public void setCateg(int categ) {
        this.categ = categ;
    }

    public int getTurnos() {
        return 1;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idServicio);
        dest.writeInt(icon);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(dias);
        dest.writeString(hora);
        dest.writeString(nomApMed);
        dest.writeInt(categ);
        dest.writeInt(turnos);
    }
}
