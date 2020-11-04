package com.unse.bienestar.estudiantil.Vistas.Activities.UPA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.TipoTurnosActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.TurnosAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

import java.util.ArrayList;

public class TurnosUPAActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    TurnosAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Turno> mList;
    FloatingActionButton fabMas;
    ImageView imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos_upa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {
        mList = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new TurnosAdapter(mList, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        fabMas.setOnClickListener(this);
    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        fabMas = findViewById(R.id.floatingMas);
        imgIcono = findViewById(R.id.imgFlecha);
    }

    private void showDialogs() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setDescripcion(getString(R.string.generalFunciones))
                .setIcono(R.drawable.ic_enojado)
                .setTipo(DialogoGeneral.TIPO_ACEPTAR).setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {

                    }

                    @Override
                    public void no() {

                    }
                });
        final DialogoGeneral mensaje = builder.build();
        mensaje.setCancelable(false);
        mensaje.show(getSupportFragmentManager(), "dialog_error");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.floatingMas:
                showDialogs();
                break;
        }
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Mis Turnos");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }
}