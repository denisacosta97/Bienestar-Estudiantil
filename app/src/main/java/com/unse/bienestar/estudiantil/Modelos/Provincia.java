package com.unse.bienestar.estudiantil.Modelos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Provincia {

    int codigo = -1;
    String titulo;

    public Provincia(int codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
    }

    public Provincia(String titulo) {
        Pattern pattern = Pattern.compile("[0-9]+ -");
        Matcher matcher = pattern.matcher(titulo);
        String mach = "";
        if (matcher.find()){
            try {
                String cod = matcher.group();
                mach = cod;
                this.codigo = Integer.parseInt(cod.replaceAll("-","").trim());
            }catch (Exception e) {

            }
        }
        try{
            this.titulo = titulo.substring(mach.length()).replaceAll("-", "")
                    .trim();
        }catch (Exception e){
            this.titulo = "";
        }

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
