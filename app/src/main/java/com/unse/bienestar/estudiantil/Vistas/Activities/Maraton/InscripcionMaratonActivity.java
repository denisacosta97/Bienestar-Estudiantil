package com.unse.bienestar.estudiantil.Vistas.Activities.Maraton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InscripcionMaratonActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter<String> categ, rango;
    String cat, rang;
    AppCompatImageView imgMaraton;
    Spinner spinnerCateg, spinnerRango;
    Button btnInsc;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_maraton);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();
    }

    private void loadData() {
        categ = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, Utils.catMaraton);
        categ.setDropDownViewResource(R.layout.style_spinner);
        spinnerCateg.setAdapter(categ);

        rango = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, Utils.rangoEdad);
        rango.setDropDownViewResource(R.layout.style_spinner);
        spinnerRango.setAdapter(rango);
        Glide.with(imgMaraton.getContext()).load(Utils.URL_IMAGEN_LOGO).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imgMaraton.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                imgMaraton.setVisibility(View.VISIBLE);
                return false;
            }
        }).apply(new RequestOptions().fitCenter())
                .into(imgMaraton);
    }

    private void loadListener() {
        btnInsc.setOnClickListener(this);
        spinnerCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = String.valueOf(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRango.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rang = String.valueOf(position + 1);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadViews() {
        imgMaraton = findViewById(R.id.imgIcon);
        spinnerCateg = findViewById(R.id.spinnerCateg);
        spinnerRango = findViewById(R.id.spinnerRango);
        btnInsc = findViewById(R.id.btnInsc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInsc:
                sendServer();
                break;
        }
    }

    private void sendServer() {
        final HashMap<String, String> map = new HashMap<>();
        String URL = Utils.URL_INSCRIPCION_MARATON;
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        final int id = preferenceManager.getValueInt(Utils.MY_ID);
        final String token = preferenceManager.getValueString(Utils.TOKEN);
        final String km = spinnerCateg.getSelectedItem().toString();
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
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
                map.put("iu", String.valueOf(id));
                map.put("ca", String.valueOf(rang));
                map.put("di", km);

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
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionExitosa));
                    finish();
                    break;
                case 2:
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.inscripcionYaRegistrada));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
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