package com.unse.bienestar.estudiantil.Vistas.Activities.UPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Deporte;
import com.unse.bienestar.estudiantil.Modelos.Medicamento;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.InfoDeporteActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.InscribirDeporteActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.helpers.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unse.bienestar.estudiantil.Herramientas.Utils.faya;

public class MedicamentosActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter<String> medic;
    Spinner spinner;
    ImageView imgIcon, btnBack;
    Button btnSent;
    DialogoProcesamiento dialog;
    String med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        setToolbar();
    }

    private void loadData() {
        medic = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, Utils.med);
        medic.setDropDownViewResource(R.layout.style_spinner);
        spinner.setAdapter(medic);
    }

    private void loadListener() {
        btnSent.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                med = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadViews() {
        imgIcon = findViewById(R.id.imgvIcon);
        btnSent = findViewById(R.id.btnSent);
        spinner = findViewById(R.id.spinner);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Medicamentos");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSent:
                sendServer();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }

    }

    private void sendServer() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int id = preferenceManager.getValueInt(Utils.MY_ID);
        String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL = String.format("%s?idU=%s&key=%s&ti=%s&iu=%s",
                Utils.URL_MEDICAM_INSERT, id, token, med, id);
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
                dialog.dismiss();

            }
        });
        //Abro dialogo para congelar pantalla
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
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioRegistrado));
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorCrearUsuario));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 6:
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

}