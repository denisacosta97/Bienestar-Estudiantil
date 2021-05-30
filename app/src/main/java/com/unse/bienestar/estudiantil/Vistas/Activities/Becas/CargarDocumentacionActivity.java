package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Familiar;
import com.unse.bienestar.estudiantil.Modelos.TipoFamiliar;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.FamiliarAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoAgregarFamiliarBeca;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CargarDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    ArrayList<TipoFamiliar> tipoFamiliars;
    ArrayList<Familiar> familiares;
    ArrayList<Documentacion> mDocumentacions;
    ArrayList<Archivo> mArchivos;
    ArrayList<Opciones> tipos;
    TextView txtEstado, txtAnio, txtFecha, txtNoData, txtObservacion, txtTipoBeca;
    LinearLayout latDatos;
    CardView addFamiliar;
    Button btnEnviar;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    FamiliarAdapter mAdapter;
    DialogoProcesamiento dialog;
    Inscripcion inscripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_documentacion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.INSCRIPCION_ID) != null) {
            inscripcion = getIntent().getParcelableExtra(Utils.INSCRIPCION_ID);
        }

        if (getIntent().getSerializableExtra(Utils.TIPO_ARCHIVOS) != null) {
            mArchivos = (ArrayList<Archivo>) getIntent().getSerializableExtra(Utils.TIPO_ARCHIVOS);
        }

        if (getIntent().getSerializableExtra(Utils.DATA_FAMILIAR) != null) {
            familiares = (ArrayList<Familiar>) getIntent().getSerializableExtra(Utils.DATA_FAMILIAR);
        }

        if (getIntent().getSerializableExtra(Utils.TIPO_FAMILIA) != null) {
            tipoFamiliars = (ArrayList<TipoFamiliar>) getIntent().getSerializableExtra(Utils.TIPO_FAMILIA);
        }

        if (getIntent().getSerializableExtra(Utils.DATA_DOCUM) != null) {
            mDocumentacions = (ArrayList<Documentacion>) getIntent().getSerializableExtra(Utils.DATA_DOCUM);
        }


        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    private void loadViews() {
        btnEnviar = findViewById(R.id.btnCargar);
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

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);
        updateFamiliars();
        mAdapter = new FamiliarAdapter(familiares, getApplicationContext());
        recycler.setAdapter(mAdapter);
    }

    private void updateFamiliars() {
        for (Familiar familiar : familiares) {
            int total = 0;
            for (Documentacion doc : mDocumentacions) {
                if (doc.getIdFamiliar() == familiar.getIdFamiliar())
                    total++;
            }
            familiar.setCantidad(total);
        }
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                procesarClick(position);
            }
        });
        btnEnviar.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        addFamiliar.setOnClickListener(this);
    }

    private void procesarClick(int position) {
        Familiar familiar = familiares.get(position);
        ArrayList<Documentacion> documentacions = new ArrayList<>();
        for (Documentacion documentacion : mDocumentacions) {
            if (documentacion.getIdFamiliar() == familiar.getIdFamiliar() &&
                    documentacion.getDescripcion().equals(familiar.getDescripcion())) {
                documentacions.add(documentacion);
            }
        }
        Intent intent = new Intent(getApplicationContext(), ListaDocumentacionActivity.class);
        intent.putExtra(Utils.DATA_FAMILIAR, familiares.get(position));
        intent.putExtra(Utils.DATA_DOCUM, documentacions);
        intent.putExtra(Utils.TIPO_ARCHIVOS, mArchivos);
        startActivityForResult(intent, Utils.UPDATE_POST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.UPDATE_POST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getSerializableExtra(Utils.DATA_DOCUM) != null) {
                    ArrayList<Documentacion> docs = (ArrayList<Documentacion>) data.getSerializableExtra(Utils.DATA_DOCUM);
                    if (docs != null)
                        for (Documentacion nuevos : docs) {
                            boolean is = false;
                            int pos = -1, i = 0;
                            for (Documentacion actual : mDocumentacions) {
                                if (nuevos.getIdFamiliar() == actual.getIdFamiliar() &&
                                        nuevos.getDescripcion().equals(actual.getDescripcion())
                                        && actual.getIdArchivo() == nuevos.getIdArchivo()) {
                                    is = true;
                                    pos = i;
                                } else
                                    i++;
                            }
                            if (!is)
                                mDocumentacions.add(nuevos);
                            else
                                mDocumentacions.set(pos, nuevos);
                        }
                    updateFamiliars();

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
        for (TipoFamiliar d : tipoFamiliars) {
            tipos.add(new Opciones(d.getNombre()));
        }
        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
            @Override
            public void onClick(int pos) {
                TipoFamiliar familiar = tipoFamiliars.get(pos);
                DialogoAgregarFamiliarBeca dialogoAgregarFamiliarBeca = new DialogoAgregarFamiliarBeca();
                dialogoAgregarFamiliarBeca.setContext(getApplicationContext());
                dialogoAgregarFamiliarBeca.setListener(new OnClickListenerAdapter() {
                    @Override
                    public void onClick(Object id) {
                        TipoFamiliar f = (TipoFamiliar) id;
                        familiares.add(new Familiar(inscripcion.getIdUsuario(), f.getId(), inscripcion.getIdBeca(),
                                inscripcion.getAnio(), 1, f.getNombre()));
                        mAdapter.notifyDataSetChanged();
                    }
                });
                dialogoAgregarFamiliarBeca.setFragmentManager(getSupportFragmentManager());
                dialogoAgregarFamiliarBeca.setFamiliar(familiar);
                dialogoAgregarFamiliarBeca.setInscripcion(inscripcion);
                dialogoAgregarFamiliarBeca.show(getSupportFragmentManager(), "dialog");


            }
        }, tipos, getApplicationContext());
        dialogoOpciones.show(getSupportFragmentManager(), "tipoFamilia");
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
            case R.id.btnCargar:
               // finalizar();
                break;
        }

    }

    /*private void finalizar() {
        final HashMap<String, String> map = new HashMap<>();
        String URL = null;
        if (!isUPA)
            URL = Utils.URL_TURNO_NUEVO;
        else
            URL = Utils.URL_TURNO_UAPU_NUEVO;
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        final int id = preferenceManager.getValueInt(Utils.MY_ID);
        final String token = preferenceManager.getValueString(Utils.TOKEN);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuesta(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                loadError();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("key", token);
                map.put("idU", String.valueOf(id));
                map.put("di", String.valueOf(mCalendar[0]));
                map.put("me", String.valueOf(mCalendar[1]));
                map.put("an", String.valueOf(mCalendar[2]));
                map.put("ho", horarios);
                map.put("iu", String.valueOf(id));
                if (isUPA) {
                    map.put("is", String.valueOf(mConvocatoria.getIdBeca()));
                } else {
                    map.put("ib", String.valueOf(mConvocatoria.getIdBeca()));
                    Pattern pattern = Pattern.compile("[0-9]");
                    Matcher matcher = pattern.matcher(receptores);
                    String num = "";
                    if (matcher.find())
                        num = matcher.group();
                    map.put("ir", num);
                }
                return map;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }*/

}