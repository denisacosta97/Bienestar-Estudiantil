package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.InfoBecasAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InfoBecasActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerBecas;
    ArrayList<InfoBecas> mInfoBecas;
    InfoBecasAdapter mInfoBecasAdapter;
    ImageView mIconBecas, imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_becas);
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
        ((TextView) findViewById(R.id.txtTitulo)).setText("Información de Becas");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);

    }

    private void loadViews() {
        recyclerBecas = findViewById(R.id.recyclerBecas);
        mIconBecas = findViewById(R.id.imgvIconB);
        imgIcono = findViewById(R.id.imgFlecha);
    }

    private void loadDataRecycler() {
        mInfoBecas = new ArrayList<>();

        mInfoBecas.add(new InfoBecas(1, R.drawable.ic_becas, "Beca Comedor", getString(R.string.desc0), "pdf_comedor", getString(R.string.reqAcad0), getString(R.string.reqGen0)));
        mInfoBecas.add(new InfoBecas(2, R.drawable.ic_becas, "Beca Estímulo al Deporte", getString(R.string.desc1), "pdf_estimulo_deporte", getString(R.string.reqAcad1), getString(R.string.reqGen1)));
        mInfoBecas.add(new InfoBecas(3, R.drawable.ic_becas, "Beca Movilidad", getString(R.string.desc2), "pdf_movilidad", getString(R.string.reqAcad2), getString(R.string.reqGen2)));
        mInfoBecas.add(new InfoBecas(4, R.drawable.ic_becas, "Beca Residencia", getString(R.string.desc3), "pdf_residencia", getString(R.string.reqAcad3), getString(R.string.reqGen3)));
        mInfoBecas.add(new InfoBecas(5, R.drawable.ic_becas, "Beca Finalización de Estudios de Grado", getString(R.string.desc4), "pdf_finalizacion_estud", getString(R.string.reqAcad4), getString(R.string.reqGen4)));
        mInfoBecas.add(new InfoBecas(6, R.drawable.ic_becas, "Beca para el Apoyo al Ingreso y Permanencia de los Estudiantes en la UNSE", getString(R.string.desc5), "pdf_apoyo", getString(R.string.reqAcad5), getString(R.string.reqGen5)));
        mInfoBecas.add(new InfoBecas(7, R.drawable.ic_becas, "Beca de Apoyo Económico", getString(R.string.desc6), "pdf_apoyo_econ", getString(R.string.reqAcad6), getString(R.string.reqGen6)));
        mInfoBecas.add(new InfoBecas(8, R.drawable.ic_becas, "Beca Estímulo al Mérito Académico", getString(R.string.desc7), "pdf_estim_acad", getString(R.string.reqAcad7), getString(R.string.reqGen7)));


        mInfoBecasAdapter = new InfoBecasAdapter(mInfoBecas, getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerBecas.setNestedScrollingEnabled(true);
        recyclerBecas.setLayoutManager(mLayoutManager);
        recyclerBecas.setAdapter(mInfoBecasAdapter);

        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerBecas);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PerfilBecasActivity.class);
                i.putExtra(Utils.BECA_NAME, mInfoBecas.get(position));
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
