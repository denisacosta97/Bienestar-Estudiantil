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
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Familiar;
import com.unse.bienestar.estudiantil.Modelos.GFamiliar;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.DocumentacionAdapter;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.GFamiliarAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoAgregarFamiliarBeca;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoBuscaUsuario;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class CargarDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    ArrayList<GFamiliar> mGFamiliars;
    ArrayList<Documentacion> mDocumentacions;
    ArrayList<GFamiliar> listadoFamilia;
    ArrayList<Archivo> mArchivos;
    ArrayList<Opciones> tipos;
    TextView txtEstado, txtAnio, txtFecha, txtNoData, txtObservacion, txtTipoBeca;
    LinearLayout latDatos;
    ArrayList<InfoBecas> mInfoBecas;
    HashMap<String, Integer> cantidades = new HashMap<>();
    CardView addFamiliar;

    int posicionArchivo = -1;
    Inscripcion mInscripcion;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    GFamiliarAdapter mAdapter;
    DialogoProcesamiento dialog;
    Inscripcion inscripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_documentacion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.INFO_EXTRA) != null) {
            inscripcion = getIntent().getParcelableExtra(Utils.INFO_EXTRA);
        }

        if (getIntent().getSerializableExtra(Utils.DATA_FAMILIAR) != null) {
            listadoFamilia = (ArrayList<GFamiliar>) getIntent().getSerializableExtra(Utils.DATA_FAMILIAR);
        }

        if (getIntent().getSerializableExtra(Utils.DATA_DOCUM) != null) {
            mDocumentacions = (ArrayList<Documentacion>) getIntent().getSerializableExtra(Utils.DATA_DOCUM);
        }

        /*if (getIntent().getParcelableExtra(Utils.INFO_EXTRA) != null) {
            mInscripcion = getIntent().getParcelableExtra(Utils.INFO_EXTRA);
        }
        if (getIntent().getSerializableExtra(Utils.INFO_EXTRA_2) != null) {
            mArchivos = (ArrayList<Archivo>) getIntent().getSerializableExtra(Utils.INFO_EXTRA_2);
        }*/

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
        addFamiliar = findViewById(R.id.cardAddFamiliar);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Inscripción");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData(){
        mGFamiliars = new ArrayList<>();
        for(Documentacion doc : mDocumentacions){
            mGFamiliars.add(new GFamiliar(doc.getIdFamiliar(), doc.getDescripcion()));
        }
        /*mGFamiliars.add(new GFamiliar(0, 0, "Postulante", "Incompleto"));
        mGFamiliars.add(new GFamiliar(1, 0, "Madre", "Incompleto"));
        mGFamiliars.add(new GFamiliar(2, 0, "Padre", "Incompleto"));*/

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
        addFamiliar.setOnClickListener(this);
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

    private void openDialogArchivos() {
        tipos = new ArrayList<>();
        for (GFamiliar d : listadoFamilia) {
            tipos.add(new Opciones(d.getNombre()));
        }
        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
            @Override
            public void onClick(int pos) {
                //BASE DE DATOS SI O SI...
                GFamiliar familiar = listadoFamilia.get(pos);
                DialogoAgregarFamiliarBeca beca = new DialogoAgregarFamiliarBeca();
                beca.setContext(getApplicationContext());
                beca.setListener(new OnClickListenerAdapter() {
                    @Override
                    public void onClick(Object id) {
                        GFamiliar f = (GFamiliar) id;
                        mGFamiliars.add(new GFamiliar(f.getId(), f.getNombre()));
                        mAdapter.notifyDataSetChanged();
                    }
                });
                beca.setFragmentManager(getSupportFragmentManager());
                beca.setFamiliar(familiar);
                beca.setInscripcion(inscripcion);
                beca.show(getSupportFragmentManager(), "dialog");



            }
        }, tipos, getApplicationContext());
        dialogoOpciones.show(getSupportFragmentManager(), "archivos");
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
            case R.id.cardAddFamiliar:
                openDialogArchivos();
                break;
        }

    }

}