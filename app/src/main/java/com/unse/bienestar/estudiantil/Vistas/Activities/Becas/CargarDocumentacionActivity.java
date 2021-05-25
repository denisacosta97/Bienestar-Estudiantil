package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.GFamiliar;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.GFamiliarAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class CargarDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    ArrayList<GFamiliar> mGFamiliars;
    ArrayList<Archivo> mArchivos;
    TextView txtEstado, txtAnio, txtFecha, txtNoData, txtObservacion, txtTipoBeca;
    LinearLayout latDatos;

    int posicionArchivo = -1;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    GFamiliarAdapter mAdapter;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_documentacion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    private void loadViews() {
        txtTipoBeca = findViewById(R.id.txtTipoBeca);
        btnBack = findViewById(R.id.imgFlecha);
        latDatos = findViewById(R.id.latDatos);
        txtObservacion = findViewById(R.id.txtObs);
        txtNoData = findViewById(R.id.txtCuerpo);
        txtAnio = findViewById(R.id.txtDesc);
        txtEstado = findViewById(R.id.txtEstado);
        txtFecha = findViewById(R.id.txtFecha);
        recycler = findViewById(R.id.recyclerg);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Inscripción");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData(){
        mGFamiliars = new ArrayList<>();
        mGFamiliars.add(new GFamiliar(0, 0, "Postulante", "Incompleto"));
        mGFamiliars.add(new GFamiliar(1, 0, "Madre", "Incompleto"));
        mGFamiliars.add(new GFamiliar(2, 0, "Padre", "Incompleto"));
        mGFamiliars.add(new GFamiliar(3, 0, "Grupo familiar", "Incompleto"));

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
                Intent intent = new Intent(getApplicationContext(), DocumentacionListaActivity.class);
                intent.putExtra(Utils.DATA_INTEG, mGFamiliars.get(position));
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uploadfileuri = data.getData();
                    Cursor cursor = getContentResolver().query(uploadfileuri, null, null, null, null);
                    int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                    int index2 = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    long size = cursor.getLong(index);
                    if (size <= (5 * (1024 * 1024))) {
                        String ext = cursor.getString(index2);
                        boolean contiene = ext.toLowerCase().contains("pdf") || ext.toLowerCase().contains("jpg")
                                || ext.toLowerCase().contains("jpeg") || ext.toLowerCase().contains("png");
                        ext = ext.substring(ext.indexOf(".") + 1);
                        boolean extension = (ext.toLowerCase().equals("pdf")
                                || ext.toLowerCase().equals("jpg")
                                || ext.toLowerCase().equals("jpeg")
                                || ext.toLowerCase().equals("png")
                        );
                        if (extension || contiene
                        ) {
                            if (posicionArchivo != -1) {
                                mArchivos.get(posicionArchivo).setFile(uploadfileuri);
                                mArchivos.get(posicionArchivo).setValidez(0);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(getApplicationContext(), getString(R.string.archivosInvalido));
                        }

                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoGrande2));
                    }


                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.errorArchivo));
                }
            } else {
                Utils.showToast(getApplicationContext(), getString(R.string.errorArchivo));
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