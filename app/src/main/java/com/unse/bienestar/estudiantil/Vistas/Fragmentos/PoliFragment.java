package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Polideportivo.EspaciosActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class PoliFragment extends Fragment {

    View view;
    Button btnReservas;
    CardView cardMapa;
    AppCompatActivity activity;

    public PoliFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_poli, container, false);

        final Context context = activity.getApplicationContext();

        btnReservas = view.findViewById(R.id.btnReserva);
        cardMapa = view.findViewById(R.id.card_mapa);
        cardMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoGeneral.Builder builder = new DialogoGeneral.Builder(context)
                        .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                        .setTitulo(getString(R.string.advertencia))
                        .setDescripcion(getString(R.string.abrirMapa))
                        .setIcono(R.drawable.ic_advertencia)
                        .setListener(new YesNoDialogListener() {
                            @Override
                            public void yes() {
                                LatLng latLng = new LatLng(-27.763612, -64.281770);
                                Utils.openMap(activity, latLng);
                            }

                            @Override
                            public void no() {

                            }
                        });
                DialogoGeneral dialogoGeneral = builder.build();
                dialogoGeneral.show(activity.getSupportFragmentManager(), "dialogo_pregunta");

            }
        });
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
