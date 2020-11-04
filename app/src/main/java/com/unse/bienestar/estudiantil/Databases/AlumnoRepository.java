package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Alumno;

import java.util.List;

public class AlumnoRepository {
    
    private AlumnoDAO mAlumnoDAO;
    
    public AlumnoRepository(Context context){
        mAlumnoDAO = new AlumnoDAO(context);

    }

    public void insert(final Alumno alumno) {
        mAlumnoDAO.insert(alumno);
    }

    public void update(final Alumno alumno) {
        mAlumnoDAO.update(alumno);
    }

    public Alumno getById(final int id) {
        return mAlumnoDAO.get(id);

    }

    public boolean isExist(int id) {
        Alumno alumno = getById(id);
        return alumno != null;
    }

    public List<Alumno> getAll() {
        return mAlumnoDAO.getAll();
    }

    public void deleteAll(){
        mAlumnoDAO.deleteAll();
    }

}
