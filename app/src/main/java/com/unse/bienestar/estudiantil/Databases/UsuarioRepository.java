package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Usuario;

import java.util.List;

public class UsuarioRepository {

    private UsuarioDAO mUsuarioDAO;

    public UsuarioRepository(Context application) {
        mUsuarioDAO = new UsuarioDAO(application);
    }

    public void insert(final Usuario usuario) {
        mUsuarioDAO.insert(usuario);
    }

    public void delete(final Usuario usuario) {
        mUsuarioDAO.delete(usuario);
    }

    public void update(final Usuario usuario) {
        mUsuarioDAO.update(usuario);
    }

    public Usuario getById(final int id) {
        return mUsuarioDAO.get(id);

    }

    public boolean isExist(int id) {
        Usuario usuario = getById(id);
        return usuario != null;
    }

    public List<Usuario> getAll() {
        return mUsuarioDAO.getAll();
    }

    public void deleteAll(){
        mUsuarioDAO.deleteAll();
    }
}
