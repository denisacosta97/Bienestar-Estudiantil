package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Inicio.RegisterActivity;

import androidx.fragment.app.Fragment;

public class AccesoDenegadoFragment extends Fragment implements View.OnClickListener {

    View view;
    Button btnRegistrarse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_acceso_denegado, container, false);

        loadViews();


        loadListener();


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnregister:
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loadListener() {
        btnRegistrarse.setOnClickListener(this);
    }

    private void loadViews() {
        btnRegistrarse = view.findViewById(R.id.btnregister);
    }

}