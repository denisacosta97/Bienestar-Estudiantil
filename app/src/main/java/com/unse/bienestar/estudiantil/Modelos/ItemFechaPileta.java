package com.unse.bienestar.estudiantil.Modelos;

public class ItemFechaPileta extends ItemListado {
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public int getTipo() {
        return TIPO_FECHA;
    }
}
