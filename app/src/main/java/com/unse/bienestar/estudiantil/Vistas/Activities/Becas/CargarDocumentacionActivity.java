package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
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
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Familiar;
import com.unse.bienestar.estudiantil.Modelos.TipoFamiliar;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.FamiliarAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoAgregarFamiliarBeca;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class CargarDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    ArrayList<TipoFamiliar> tipoFamiliars;
    ArrayList<Familiar> familiares;
    ArrayList<Documentacion> mDocumentacions;
    ArrayList<Archivo> mArchivos;
    ArrayList<Opciones> tipos;
    TextView txtEstado, txtAnio, txtFecha, txtNoData, txtObservacion, txtTipoBeca;
    LinearLayout latDatos, latModificacion;
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
        latModificacion = findViewById(R.id.latModificacion);
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
        if (inscripcion.getEstado() == 9) {
            txtEstado.setText(inscripcion.getEstadoDescripcion());
            latModificacion.setVisibility(View.GONE);
            PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
            preferenceManager.setValue(Utils.IS_EDIT_MODE, 1);
            addFamiliar.setVisibility(View.VISIBLE);
            btnEnviar.setEnabled(true);
        } else {
            addFamiliar.setVisibility(View.GONE);
            btnEnviar.setEnabled(false);
            txtEstado.setText(inscripcion.getEstadoDescripcion());
            latModificacion.setVisibility(View.VISIBLE);
            txtFecha.setText(Utils.getFechaFormat(inscripcion.getFechaModificacion()));
        }
        if (inscripcion.getDescripcion() != null && !inscripcion.getDescripcion().equals("")) {
            latDatos.setVisibility(View.VISIBLE);
            txtObservacion.setText(String.format("%s\n%s", inscripcion.getDescripcion(), inscripcion.getEstado() == 1 ? "" : Utils.getFechaOrder(Utils.getFechaDateWithHour(inscripcion.getFechaModificacion()))));

        } else latDatos.setVisibility(View.GONE);
        txtTipoBeca.setText(inscripcion.getIdBeca() == 7 ? "Beca de Apoyo Económico" : "");
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
                showDialogoFinalizar();
                break;
        }

    }

    public void showDialogoFinalizar() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(R.string.advertencia))
                .setDescripcion(getString(R.string.inscripcionFinalizar))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        finalizar();
                    }

                    @Override
                    public void no() {

                    }
                })
                .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                .setIcono(R.drawable.ic_advertencia);
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialogo");
    }


    private void finalizar() {
        final HashMap<String, String> map = new HashMap<>();
        String URL = Utils.URL_BECAS_INSCRIPCION_ACTUALIZAR;
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
                map.put("ib", String.valueOf(inscripcion.getIdBeca()));
                map.put("iu", String.valueOf(inscripcion.getIdUsuario()));
                map.put("an", String.valueOf(inscripcion.getAnio()));
                map.put("es", String.valueOf(1));
                return map;
            }
        };
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuesta(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case 1:
                    //Exito
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionEnviada));
                    isFinish();
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionNoActualizada));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionAceptada));
                    break;
                case 6:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionReenviada));
                    break;
                case 7:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionRecibido));
                    break;
                case 8:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionNoEditable));
                    break;
                case 9:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionNoEvaluada));
                    break;
                case 10:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionCancelada));
                    break;
                case 11:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionAdministrador));
                    break;
                case 100:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void isFinish() {
        addFamiliar.setVisibility(View.GONE);
        btnEnviar.setEnabled(false);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.setValue(Utils.IS_EDIT_MODE, 0);
    }

}