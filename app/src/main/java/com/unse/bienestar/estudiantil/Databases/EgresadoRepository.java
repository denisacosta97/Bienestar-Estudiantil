package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Egresado;

import java.util.List;

public class EgresadoRepository {
    
    private EgresadoDAO mEgresadoDAO;

    public EgresadoRepository(Context application) {
        mEgresadoDAO = new EgresadoDAO(application);
    }

    public void insert(final Egresado egresado) {
        mEgresadoDAO.insert(egresado);
    }


    public void update(final Egresado egresado) {
        mEgresadoDAO.update(egresado);
    }

    public Egresado getById(final int id) {
        return mEgresadoDAO.get(id);

    }

    public boolean isExist(int id) {
        Egresado egresado = getById(id);
        return egresado != null;
    }

    public List<Egresado> getAll() {
        return mEgresadoDAO.getAll();
    }

    public void deleteAll(){
        mEgresadoDAO.deleteAll();
    }

}
