package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.TurnosAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TurnosActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    TurnosAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Turno> mList;
    FloatingActionButton fabMas;
    ImageView imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {
        mList = new ArrayList<>();
        /*mList.add(new Turno("BECAS COMEDOR 2019", "Presentación de papeles para becas comedor 2019", Turno.ESTADO.CONFIRMADO.name(),
                "09:05:00", "09:10:00", "24/02/2019", "39986583", "Cristian", "Ledesma"));
        mList.add(new Turno("BECAS UNSE 2020", "Presentación de papeles para becas comedor 2019", Turno.ESTADO.AUSENTE.name(),
                "09:05:00", "09:10:00", "24/02/2019", "39986583", "Cristian", "Ledesma"));
        mList.add(new Turno("BECAS COMEDOR 2020", "Presentación de papeles para becas comedor 2019", Turno.ESTADO.CONFIRMADO.name(),
                "09:05:00", "09:10:00", "2/10/2020", "39986583", "Cristian", "Ledesma"));
        mList.add(new Turno("DEPORTES 2020", "Presentación de papeles para becas comedor 2019", Turno.ESTADO.AUSENTE.name(),
                "09:05:00", "09:10:00", "24/04/2020", "39986583", "Cristian", "Ledesma"));
        mList.add(new Turno("AYUDA ECONOMICA 2020", "Presentación de papeles para becas comedor 2019", Turno.ESTADO.PENDIENTE.name(),
                "09:05:00", "09:10:00", "24/03/2020", "39986583", "Cristian", "Ledesma"));
*/
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.floatingMas:
                startActivity(new Intent(getApplicationContext(), TipoTurnosActivity.class));
                break;
        }
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText(Utils.getAppName(getApplicationContext(), getComponentName()));
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }
}
