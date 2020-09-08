package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Polideportivo.EspaciosActivity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class PoliFragment extends Fragment {

    View view;
    Button btnReservas, btnCanchas;
    CardView cardOne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_poli, container, false);

        btnReservas = view.findViewById(R.id.btnReserva);
        btnReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EspaciosActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
