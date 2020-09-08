package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.InfoBecasActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.TurnosActivity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class BecasFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardTurnos, cardInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_becas, container, false);

        loadViews();

        loadListener();

        return view;
    }

    private void loadListener() {
        cardTurnos.setOnClickListener(this);
        cardInfo.setOnClickListener(this);
    }

    private void loadViews() {
        cardTurnos = view.findViewById(R.id.cardTurnos);
        cardInfo = view.findViewById(R.id.card_infoBecas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardTurnos:
                startActivity(new Intent(getContext(), TurnosActivity.class));
                break;
            case R.id.card_infoBecas:
                startActivity(new Intent(getContext(), InfoBecasActivity.class));
                break;
        }
    }

}
