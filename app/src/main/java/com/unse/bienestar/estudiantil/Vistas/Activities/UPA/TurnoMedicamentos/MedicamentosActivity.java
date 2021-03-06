package com.unse.bienestar.estudiantil.Vistas.Activities.UPA.TurnoMedicamentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.unse.bienestar.estudiantil.Modelos.Convocatoria;
import com.unse.bienestar.estudiantil.Modelos.Deporte;
import com.unse.bienestar.estudiantil.Modelos.Horario;
import com.unse.bienestar.estudiantil.Modelos.Medicamento;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.ResumenTurnoActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.InfoDeporteActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.InscribirDeporteActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.UPA.ListMedicamentoActivity;
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
    String med;
    Spinner spinner;
    Button btnMed;
    CardView cardContinuar;
    DialogoProcesamiento dialog;

    int[] mCalendar;
    String horarios;

    public static MedicamentosActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        instance = this;

        if (getIntent().getStringExtra(Utils.DATA_RESERVA) != null) {
            horarios = getIntent().getStringExtra(Utils.DATA_RESERVA);
        }

        if (getIntent().getIntArrayExtra(Utils.DATA_FECHA) != null) {
            mCalendar = getIntent().getIntArrayExtra(Utils.DATA_FECHA);
        }

        loadViews();

        loadListener();

        loadData();
    }

    private void loadData() {
        medic = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, Utils.med);
        medic.setDropDownViewResource(R.layout.style_spinner);
        spinner.setAdapter(medic);
    }

    private void loadListener() {
        cardContinuar.setOnClickListener(this);
        btnMed.setOnClickListener(this);

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
        cardContinuar = findViewById(R.id.cardContinuar);
        spinner = findViewById(R.id.spinner);
        btnMed = findViewById(R.id.btnMed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardContinuar:
                openFinal();
                break;
            case R.id.btnMed:
                startActivity(new Intent(getApplicationContext(), ListMedicamentoActivity.class));
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

    private void openFinal() {
        Intent intent = new Intent(getApplicationContext(), ResumenTurnoMedActivity.class);
        intent.putExtra(Utils.DATA_FECHA, mCalendar);
        intent.putExtra(Utils.DATA_RESERVA, horarios);
        intent.putExtra(Utils.DATA_MEDICAMENTO, med);
        startActivity(intent);
    }

}