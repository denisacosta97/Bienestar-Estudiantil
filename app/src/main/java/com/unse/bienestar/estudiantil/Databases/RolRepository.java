package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Rol;

import java.util.List;

public class RolRepository {

    private RolDAO mRolDAO;

    public RolRepository(Context application) {
        mRolDAO = new RolDAO(application);
    }

    public void insert(final Rol rol) {
        mRolDAO.insert(rol);
    }


    public void update(final Rol rol) {
        mRolDAO.update(rol);
    }

    public List<Rol> getAllByUsuario(int id) {
        return mRolDAO.getAllByUser(id);
    }

    public Rol get(int id) {
        return mRolDAO.get(id);
    }

    public void deleteAll(){
        mRolDAO.deleteAll();
    }
}
