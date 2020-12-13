package com.unse.bienestar.estudiantil.Modelos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Departamento {

    int codigo;
    String descripcion;
    int codigoDep;

    public Departamento(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Departamento(int codigo, String descripcion, int codigoDep) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.codigoDep = codigoDep;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoDep() {
        return codigoDep;
    }

    public void setCodigoDep(int codigoDep) {
        this.codigoDep = codigoDep;
    }
}
