package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Perfil.InscripcionesActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilBecasActivity extends AppCompatActivity implements View.OnClickListener {

    InfoBecas mInfoBecas;
    TextView nameBeca, reqGeneral, reqAcad, desc, pdf;
    ImageView imgIcono;
    LinearLayout latDatos;
    Button btnCargar;
    DialogoProcesamiento
            dialog;
    String anio;
    boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_becas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.BECA_NAME) != null) {
            mInfoBecas = getIntent().getParcelableExtra(Utils.BECA_NAME);
        }
        if (getIntent().getBooleanExtra(Utils.IS_EDIT_MODE, false)) {
            isShow = getIntent().getBooleanExtra(Utils.IS_EDIT_MODE, false);
        }

        if (mInfoBecas != null) {
            loadViews();

            loadListener();

            loadData();
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
            finish();
        }

        setToolbar();
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Información de Becas");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {
        nameBeca.setText(mInfoBecas.getNameBeca());
        reqAcad.setText(mInfoBecas.getReqAcad());
        reqGeneral.setText(mInfoBecas.getReqGeneral());
        desc.setText(mInfoBecas.getDesc());
        pdf.setText(mInfoBecas.getPdf());

        if (!isShow) {
            latDatos.setVisibility(View.VISIBLE);
        } else {
            latDatos.setVisibility(View.GONE);
        }
    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        btnCargar.setOnClickListener(this);
    }

    private void loadViews() {
        latDatos = findViewById(R.id.latDatos);
        nameBeca = findViewById(R.id.txtNameBeca);
        reqGeneral = findViewById(R.id.txtReqGeneral);
        reqAcad = findViewById(R.id.txtReqAcad);
        desc = findViewById(R.id.txtDesc);
        pdf = findViewById(R.id.txtPdf);
        imgIcono = findViewById(R.id.imgFlecha);
        btnCargar = findViewById(R.id.btnCargar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnCargar:
                openInscripcion();
                break;
        }

    }

    private void checkDisponibility() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int id = preferenceManager.getValueInt(Utils.MY_ID);
        String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL = String.format("%s?idU=%s&key=%s&iu=%s&ib=%s",
                Utils.URL_BECAS_DISPONIBILIDAD, id, token, id, mInfoBecas.getId());
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();

            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuesta(String response, int tipo) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    Inscripcion inscripcion = Inscripcion.mapper(jsonObject.getJSONObject("datos"), Inscripcion.HIGH);
                    Intent intent = new Intent(getApplicationContext(), CargarDocumentacionActivity.class);
                    intent.putExtra(Utils.INFO_EXTRA, inscripcion);
                    //intent.putExtra(Utils.INFO_EXTRA_2, new ArrayList<Archivo>());
                    startActivity(intent);
                    break;
                case 2:
                    if (tipo == 1)
                        Utils.showToast(getApplicationContext(), getString(R.string.becaConvocatoriaNo));
                    else {
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionErronea));
                    }
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionYaRegistrada));
                    //Utils.showToast(getApplicationContext(), getString(R.string.inscripcionPerfil));
                    startActivity(new Intent(getApplicationContext(), InscripcionesActivity.class));
                    break;
                case 6:
                    Utils.showToast(getApplicationContext(), getString(R.string.deporteYaInscripto));
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionPerfil));
                    break;
                case 7:
                    Utils.showToast(getApplicationContext(), getString(R.string.deporteUsuario));
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

    private void openInscripcion() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(R.string.advertencia))
                .setDescripcion(getString(R.string.inscripcionRegistrar))
                .setTipo(DialogoGeneral.TIPO_SI_NO)
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        register();
                    }

                    @Override
                    public void no() {

                    }
                })
                .setIcono(R.drawable.ic_advertencia);
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialog");
    }

    private void register() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        final int id = preferenceManager.getValueInt(Utils.MY_ID);
        final String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL = Utils.URL_BECAS_INSCRIPCION_DOC;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 2);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("idU", String.valueOf(id));
                param.put("ib", String.valueOf(mInfoBecas.getId()));
                param.put("an", "2021");
                param.put("iu", String.valueOf(id));
                param.put("key", token);
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
