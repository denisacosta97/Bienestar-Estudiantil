package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Profesor;

import java.util.List;

public class ProfesorRepository {

    private ProfesorDAO mProfesorDAO;

    public ProfesorRepository(Context application) {
        mProfesorDAO = new ProfesorDAO(application);
    }

    public void insert(final Profesor profesor) {
        mProfesorDAO.insert(profesor);
    }

    public void update(final Profesor profesor) {
        mProfesorDAO.update(profesor);
    }

    public Profesor getById(final int id) {
        return mProfesorDAO.get(id);

    }

    public boolean isExist(int id) {
        Profesor profesor = getById(id);
        return profesor != null;
    }

    public List<Profesor> getAll() {
        return mProfesorDAO.getAll();
    }

    public void deleteAll() {
        mProfesorDAO.deleteAll();
    }

}
