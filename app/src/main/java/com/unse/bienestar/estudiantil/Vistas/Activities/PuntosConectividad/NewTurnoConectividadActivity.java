package com.unse.bienestar.estudiantil.Vistas.Activities.PuntosConectividad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.DatePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class NewTurnoConectividadActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter<String> puntos;
    ImageView imgIcon;
    Spinner spinner;
    Button btnTurno;
    String punto;
    DialogoProcesamiento dialog;
    TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_turno_conectividad);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        setToolbar();
    }

    private void loadData() {
        puntos = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, Utils.puntosConect);
        puntos.setDropDownViewResource(R.layout.style_spinner);
        spinner.setAdapter(puntos);
    }

    private void loadListener() {
        btnTurno.setOnClickListener(this);
        txtFecha.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                punto = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadViews() {
        imgIcon = findViewById(R.id.imgvIcon);
        btnTurno = findViewById(R.id.btnTurno);
        spinner = findViewById(R.id.spinner);
        txtFecha = findViewById(R.id.txtFecha);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Puntos de Conectividad");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void selectFecha() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String mes, dia;
                month = month + 1;
                if (month < 10) {
                    mes = "0" + month;
                } else
                    mes = String.valueOf(month);
                if (day < 10)
                    dia = "0" + day;
                else
                    dia = String.valueOf(day);
                final String selectedDate = year + "-" + mes + "-" + dia;
                txtFecha.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTurno:
                sendServer();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.txtFecha:
                selectFecha();
                break;
        }
    }

    private void sendServer() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int id = preferenceManager.getValueInt(Utils.MY_ID);
        String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL = String.format("%s?idU=%s&key=%s&ti=%s&iu=%s",
                Utils.URL_MEDICAM_INSERT, id, token, punto, id);
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