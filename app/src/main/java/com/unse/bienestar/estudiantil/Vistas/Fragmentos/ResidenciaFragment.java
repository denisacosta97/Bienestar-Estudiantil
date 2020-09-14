package com.unse.bienestar.estudiantil.Vistas.Fragmentos;


import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unse.bienestar.estudiantil.R;

public class ResidenciaFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_residencia, container, false);

        loadViews();

        loadData();

        return view;
    }

    private void loadData() {

    }

    private void loadViews() {

    }

}