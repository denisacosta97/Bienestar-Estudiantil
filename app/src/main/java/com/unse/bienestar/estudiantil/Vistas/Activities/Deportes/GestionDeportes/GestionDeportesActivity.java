package com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.GestionDeportes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Databases.RolViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.Modelos.Rol;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.GestionDeportes.Inscripciones.InscripcionesActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.GestionDeportes.Inscripciones.InscripcionesTemporadasActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.OpcionesAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GestionDeportesActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    OpcionesAdapter mAdapter;
    ArrayList<Opciones> mOpciones, mOpcionesFinal;
    ImageView imgIcono;
    List<Rol> rols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gestion_deportes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadListener();

        loadData();

    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Gestión Deportes");
    }


    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                switch ((int) id) {
                    case 101:
                        startActivity(new Intent(getApplicationContext(), InscripcionesTemporadasActivity.class));
                        break;
                    case 106:
                        startActivity(new Intent(getApplicationContext(), InscripcionesActivity.class));
                        break;

                }
                Utils.showToast(getApplicationContext(), "Item: " + mOpciones.get(position).getTitulo());
            }
        });
        imgIcono.setOnClickListener(this);

    }

    private void loadData() {
        mOpciones = new ArrayList<>();
        mOpcionesFinal = new ArrayList<>();
        mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 101, "Inscripciones", R.drawable.ic_usuarios, R.color.colorFCEyT));
        mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 106, "Gestión de Inscripciones", R.drawable.ic_usuarios, R.color.colorFCEyT));
        //mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 102, "Gestión de Becados", R.drawable.ic_becas, R.color.colorFCEyT));
        //mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 103, "Gestión de Deportes", R.drawable.ic_deportes, R.color.colorFCEyT));
        //mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 104, "Gestión de Profesores", R.drawable.ic_entrenador, R.color.colorFCEyT));
        //mOpciones.add(new Opciones(true, LinearLayout.VERTICAL, 105, "Gestión de Torneos", R.drawable.ic_cup, R.color.colorFCEyT));

        filtrarOpciones();

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OpcionesAdapter(mOpcionesFinal, getApplicationContext(), 1);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void filtrarOpciones() {
        RolViewModel rolViewModel = new RolViewModel(getApplicationContext());
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int id = preferenceManager.getValueInt(Utils.MY_ID);
        rols = rolViewModel.getAllByUsuario(id);
        for (Opciones e : mOpciones) {
            for (Rol rol : rols) {
                if (rol.getIdRol() == e.getId())
                    mOpcionesFinal.add(e);
            }
        }
    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        imgIcono = findViewById(R.id.imgFlecha);
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
