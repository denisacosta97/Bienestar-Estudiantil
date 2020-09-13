package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.InfoBecasActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.TurnosActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Inicio.MainActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.UPA.ServiciosUPAActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.UPA.TurnosUPAActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

public class UPAFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardTurnos, cardServ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upa, container, false);

        loadViews();

        loadListener();

        return view;
    }

    private void loadListener() {
        cardTurnos.setOnClickListener(this);
        cardServ.setOnClickListener(this);
    }

    private void loadViews() {
        cardTurnos = view.findViewById(R.id.cardTurnos);
        cardServ = view.findViewById(R.id.card_servicios);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardTurnos:
                startActivity(new Intent(getContext(), TurnosUPAActivity.class));
                break;
            case R.id.card_servicios:
                startActivity(new Intent(getContext(), ServiciosUPAActivity.class));
                break;
        }
    }

}