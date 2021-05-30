package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Familiar;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.DocumentacionAdapter;

import java.util.ArrayList;

public class ListaDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    Familiar familiar;
    ArrayList<Documentacion> documentacions;
    ArrayList<Archivo> archivos;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    DocumentacionAdapter mAdapter;
    TextView txtNombre;
    int posicion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentacion_lista);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DATA_FAMILIAR) != null) {
            familiar = getIntent().getParcelableExtra(Utils.DATA_FAMILIAR);
        }

        if (getIntent().getSerializableExtra(Utils.DATA_DOCUM) != null) {
            documentacions = (ArrayList<Documentacion>) getIntent().getSerializableExtra(Utils.DATA_DOCUM);
        }

        if (getIntent().getSerializableExtra(Utils.TIPO_ARCHIVOS) != null) {
            archivos = (ArrayList<Archivo>) getIntent().getSerializableExtra(Utils.TIPO_ARCHIVOS);
        }

        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Utils.DATA_DOCUM, documentacions);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void loadViews() {
        btnBack = findViewById(R.id.imgFlecha);
        recycler = findViewById(R.id.recyclerg);
        txtNombre = findViewById(R.id.txtNombre);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Documentación");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {
        txtNombre.setText(familiar.getDescripcion());

        updateArchivos();

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);
        mAdapter = new DocumentacionAdapter(this, 1, archivos);
        recycler.setAdapter(mAdapter);
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SubirDocumentacionFamiliarActivity.class);
                posicion = position;
                intent.putExtra(Utils.ARCHIVO_NAME, archivos.get(position));
                intent.putExtra(Utils.DATA_DOCUM, getDocument(position));
                startActivityForResult(intent, Utils.UPLOAD_DOC);
            }
        });
        btnBack.setOnClickListener(this);
    }

    private Documentacion getDocument(int position) {
        Archivo archivo = archivos.get(position);
        for (Documentacion documentacion : documentacions) {
            if (archivo.getId() == documentacion.getIdArchivo()) {
                documentacion.setIdUsuario(familiar.getIdUsuario());
                documentacion.setIdBeca(familiar.getIdBeca());
                return documentacion;
            }
        }
        Documentacion documentacion = new Documentacion(familiar.getIdFamiliar(), familiar.getIdUsuario(), familiar.getIdBeca(),
                familiar.getAnio(), familiar.getDescripcion(), 0);
        documentacion.setIdArchivo(archivo.getId());
        return documentacion;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.UPLOAD_DOC) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getParcelableExtra(Utils.DATA_DOCUM) != null) {
                    if (posicion != -1) {
                        Documentacion documentacion = data.getParcelableExtra(Utils.DATA_DOCUM);
                        if (documentacion != null) {
                            int i = 0, pos = -1;
                            for (Documentacion docu : documentacions) {
                                if (docu.getIdArchivo() == documentacion.getIdArchivo()) {
                                    pos = i;
                                }
                                i++;
                            }
                            if (pos == -1)
                                documentacions.add(documentacion);
                            else
                                documentacions.set(pos, documentacion);
                            updateArchivos();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    private void updateArchivos() {
        for (Archivo archivo : archivos) {
            for (Documentacion documentacion : documentacions) {
                if (archivo.getId() == documentacion.getIdArchivo()) {
                    archivo.setValidez(1);
                }
            }
        }
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