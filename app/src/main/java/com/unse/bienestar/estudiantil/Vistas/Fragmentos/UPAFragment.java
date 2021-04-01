package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.UPA.MedicamentosActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.UPA.ServiciosUPAActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class UPAFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardTurnos, cardServ, card_medicamentos;
    Context mContext;

    public UPAFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


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
        card_medicamentos.setOnClickListener(this);
    }

    private void loadViews() {
        cardTurnos = view.findViewById(R.id.cardTurnos);
        cardServ = view.findViewById(R.id.card_servicios);
        card_medicamentos = view.findViewById(R.id.card_medicamentos);
    }

    @Override
    public void onClick(View v) {
        PreferenceManager manager = new PreferenceManager(getContext());
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        switch (v.getId()) {
            case R.id.cardTurnos:
                if (isLogin)
                    Utils.showToast(getContext(), getString(R.string.noDisponible));
                else
                    Utils.showToast(getContext(), getString(R.string.primeroRegistrar));
                //startActivity(new Intent(getContext(), TurnosUPAActivity.class));
                break;
            case R.id.card_servicios:
                startActivity(new Intent(getContext(), ServiciosUPAActivity.class));
                break;
            case R.id.card_medicamentos:
                startActivity(new Intent(getContext(), MedicamentosActivity.class));
                break;
        }
    }

}