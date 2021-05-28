package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

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
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Modelos.GFamiliar;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.GFamiliarAdapter;

import java.util.ArrayList;

public class DocumentacionListaActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    ArrayList<GFamiliar> mGFamiliars;
    GFamiliar mGFamiliar;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    GFamiliarAdapter mAdapter;
    TextView txtNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentacion_lista);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DATA_INTEG) != null) {
            mGFamiliar = getIntent().getParcelableExtra(Utils.DATA_INTEG);
        }

        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    private void loadViews() {
        btnBack = findViewById(R.id.imgFlecha);
        recycler = findViewById(R.id.recyclerg);
        txtNombre = findViewById(R.id.txtNombre);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Inscripción");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData(){
        txtNombre.setText(mGFamiliar.getNombre());

        mGFamiliars = new ArrayList<>();
        mGFamiliars.add(new GFamiliar(0, 0, "D.N.I Frontal", "Incompleto"));
        mGFamiliars.add(new GFamiliar(1, 0, "D.N.I Dorsal", "Incompleto"));
        mGFamiliars.add(new GFamiliar(2, 0, "Certificación negativa de ANSES", "Incompleto"));
        mGFamiliars.add(new GFamiliar(3, 0, "Número de cuenta Santander", "Incompleto"));
        mGFamiliars.add(new GFamiliar(4, 0, "Certificado de discapacidad", "Incompleto"));
        mGFamiliars.add(new GFamiliar(5, 0, "Certificado de no convivencia", "Incompleto"));
        mGFamiliars.add(new GFamiliar(6, 0, "Constancia de ingresos", "Incompleto"));
        mGFamiliars.add(new GFamiliar(7, 0, "Acta de nacimiento de hijos", "Incompleto"));
        mGFamiliars.add(new GFamiliar(8, 0, "Acta de defunción", "Incompleto"));

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);
        mAdapter = new GFamiliarAdapter(mGFamiliars, getApplicationContext());
        recycler.setAdapter(mAdapter);
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DocGrupoActivity.class);
                intent.putExtra(Utils.DATA_DOCUM, mGFamiliars.get(position));
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnRec:
                startActivity(new Intent(getApplicationContext(), RecomendacionesActivity.class));
                break;
        }
    }

}