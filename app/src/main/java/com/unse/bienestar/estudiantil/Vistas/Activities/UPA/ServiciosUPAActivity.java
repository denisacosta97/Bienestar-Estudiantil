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

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.ServiciosUPA;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.ServiciosUPAAdapter;

import java.util.ArrayList;

public class ServiciosUPAActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    ArrayList<ServiciosUPA> mServicios;
    ServiciosUPAAdapter mAdapter;
    ImageView imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_upa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadDataRecycler();

        setToolbar();

    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Servicios");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);

    }

    private void loadViews() {
        recycler = findViewById(R.id.recyclerBecas);
        imgIcono = findViewById(R.id.imgFlecha);
    }

    private void loadDataRecycler() {
        mServicios = new ArrayList<>();
        mServicios.add(new ServiciosUPA(0, R.drawable.ic_medico, "Odontología", getString(R.string.lorem), "Lunes a Viernes", "08:30hs a 11:30hs y 14:00hs a 17:00hs", "Dra. Ana Carrizo", 0));
        mServicios.add(new ServiciosUPA(1, R.drawable.ic_medico, "Psicología", getString(R.string.lorem), "Lunes a Viernes", "Turnos programados", "Dra. Ana Carrizo", 0));
        mServicios.add(new ServiciosUPA(2, R.drawable.ic_medico, "Medicina Familiar", getString(R.string.lorem), "Lunes a Viernes", "08:30hs a 11:30hs", "Dra. Ana Carrizo", 1));
        mServicios.add(new ServiciosUPA(3, R.drawable.ic_medico, "Enfermería", getString(R.string.lorem), "Lunes a Viernes", "08:30hs a 12:00hs y 14:30hs a 18:30hs", "Dra. Ana Carrizo", 1));
        mServicios.add(new ServiciosUPA(4, R.drawable.ic_medico, "Obstetricia", getString(R.string.lorem), "Lunes a Viernes", "08:00hs a 13:00hs", "Dra. Ana Carrizo", 1));
        mServicios.add(new ServiciosUPA(5, R.drawable.ic_medico, "Educación para la Salud", getString(R.string.lorem), "Lunes a Viernes", "08:00hs a 13:00hs", "Dra. Ana Carrizo", 1));

        mAdapter = new ServiciosUPAAdapter(mServicios, getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recycler.setNestedScrollingEnabled(true);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setAdapter(mAdapter);

        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PerfilServicioActivity.class);
                i.putExtra(Utils.SERVICIO_NAME, mServicios.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }
    }
}