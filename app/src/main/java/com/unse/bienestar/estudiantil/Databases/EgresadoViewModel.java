package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;

import com.unse.bienestar.estudiantil.Modelos.Egresado;

import java.util.List;

public class EgresadoViewModel {
    private Context mContext;
    private EgresadoRepository mRepository;

    public EgresadoViewModel(Context context) {
        mContext = context;
        mRepository = new EgresadoRepository(context);
    }

    public void insert(Egresado egresado) {
        mRepository.insert(egresado);
    }

    public void delete(Egresado egresado) {
    }

    public void update(Egresado egresado) {
        mRepository.update(egresado);
    }

    public Egresado getById(int id) {
        return mRepository.getById(id);
    }

    public boolean isExist(int id) {
        Egresado egresado = getById(id);
        return egresado != null;
    }

    public List<Egresado> getAll() {
        return mRepository.getAll();
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }
}
