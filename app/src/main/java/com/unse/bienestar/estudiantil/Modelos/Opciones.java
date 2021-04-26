package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import com.unse.bienestar.estudiantil.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Opciones implements Parcelable {

    public static final int BASIC = 1;

    private String titulo;
    private int icon, orientation;
    private int color, colorText = 0, sizeText =0;
    private int id;
    private boolean disponibility = true;

    public Opciones(int orientation, int id,String titulo, int icon, int color) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.orientation = orientation;
    }

    public Opciones(boolean dis, int orientation, int id,String titulo, int icon, int color) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.orientation = orientation;
        this.disponibility = dis;
    }

    public Opciones(int orientation,int id,String titulo, int icon, int color, int colorText) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.colorText = colorText;
        this.orientation = orientation;
    }

    public Opciones(boolean dis,int orientation,int id,String titulo, int icon, int color, int colorText) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.colorText = colorText;
        this.orientation = orientation;
        this.disponibility = dis;
    }

    public Opciones(int orientation,int id,String titulo, int icon, int color, int colorText, int sizeText) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.colorText = colorText;
        this.sizeText = sizeText;
        this.orientation = orientation;
    }

    public Opciones(int orientation, boolean disponibility, int id,String titulo, int icon, int color, int colorText, int sizeText) {
        this.titulo = titulo;
        this.icon = icon;
        this.color = color;
        this.id = id;
        this.colorText = colorText;
        this.sizeText = sizeText;
        this.orientation = orientation;
        this.disponibility = disponibility;
    }

    public Opciones(int id, String titulo) {
        this.titulo = titulo;
        this.id = id;
    }

    public Opciones(String titulo) {
        this.titulo = titulo;
    }

    protected Opciones(Parcel in) {
        titulo = in.readString();
        icon = in.readInt();
        orientation = in.readInt();
        color = in.readInt();
        colorText = in.readInt();
        sizeText = in.readInt();
        id = in.readInt();
        disponibility = in.readByte() != 0;
    }

    public static final Creator<Opciones> CREATOR = new Creator<Opciones>() {
        @Override
        public Opciones createFromParcel(Parcel in) {
            return new Opciones(in);
        }

        @Override
        public Opciones[] newArray(int size) {
            return new Opciones[size];
        }
    };

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(boolean disponibility) {
        this.disponibility = disponibility;
    }

    public int getSizeText() {
        return sizeText;
    }

    public void setSizeText(int sizeText) {
        this.sizeText = sizeText;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static Opciones mapper(JSONObject o, int tipo) {
        String titulo;
        int id, disponibility;
        boolean disp = false;
        Opciones opciones = null;
        try {

            switch (tipo) {
                case BASIC:
                    titulo = o.getString("descripcion");
                    id = Integer.parseInt(o.getString("id"));

                    opciones = new Opciones(id, titulo);

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return opciones;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeInt(icon);
        dest.writeInt(orientation);
        dest.writeInt(color);
        dest.writeInt(colorText);
        dest.writeInt(sizeText);
        dest.writeInt(id);
        dest.writeByte((byte) (disponibility ? 1 : 0));
    }
}
