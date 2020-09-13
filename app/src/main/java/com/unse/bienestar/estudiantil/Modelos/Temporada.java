package com.unse.bienestar.estudiantil.Modelos;

import org.json.JSONException;
import org.json.JSONObject;

public class Temporada {

    private int idDeporte;
    private int anio;
    private int disponible;
    private String nombre;

    public static final int DEPORTE = 1;

    public Temporada() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Temporada(int idDeporte, int anio, String nombre) {
        this.idDeporte = idDeporte;
        this.anio = anio;
        this.nombre = nombre;
    }

    public static Temporada mapper(JSONObject j, int tipo) {
        Temporada temporada = new Temporada();
        try {
            switch (tipo) {
                case DEPORTE:
                    int idDeporte = Integer.parseInt(j.getString("iddeporte"));
                    int anio = Integer.parseInt(j.getString("anio"));
                    String nombreDeporte = j.getString("nombre");

                    temporada = new Temporada(idDeporte, anio, nombreDeporte);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temporada;
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

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }
}
