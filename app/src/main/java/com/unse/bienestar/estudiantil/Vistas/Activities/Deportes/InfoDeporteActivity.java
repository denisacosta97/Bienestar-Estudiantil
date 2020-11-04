package com.unse.bienestar.estudiantil.Vistas.Activities.Deportes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Deporte;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class InfoDeporteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    Deporte mDeporte;
    TextView txtHorario, txtDia, txtEntrenador, txtNombre, txtLugar;
    ImageView imgIcon, btnBack;
    GoogleMap maps;
    Button btnRegister;
    DialogoProcesamiento dialog;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_deporte);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DEPORTE_NAME) != null) {
            mDeporte = getIntent().getParcelableExtra(Utils.DEPORTE_NAME);
        }

        if (mDeporte != null) {

            loadViews();

            loadListener();

            loadData();
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.noData));
            finish();
        }

        setToolbar();

    }

    private void loadData() {
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        if (mDeporte.getProfesor().getIdProfesor() == 0)
            txtEntrenador.setText(getString(R.string.noAsignado));
        else txtEntrenador.setText(String.format("%s %s", mDeporte.getProfesor().getNombre(),
                mDeporte.getProfesor().getApellido()));
        txtHorario.setText(mDeporte.getHorario());
        txtDia.setText(mDeporte.getDias());
        txtLugar.setText(mDeporte.getLugar());
        txtNombre.setText(mDeporte.getName());
        Glide.with(imgIcon.getContext()).load(mDeporte.getIconDeporte()).into(imgIcon);
    }

    private void loadListener() {
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    private void loadViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        txtHorario = findViewById(R.id.txtHorarios);
        txtLugar = findViewById(R.id.txtLugar);
        txtDia = findViewById(R.id.txtDia);
        txtEntrenador = findViewById(R.id.txtEntrenador);
        txtNombre = findViewById(R.id.txtNameDeporte);
        imgIcon = findViewById(R.id.imgvIcon);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.imgFlecha);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Información del Deporte");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                PreferenceManager manager = new PreferenceManager(getApplicationContext());
                boolean isLogin = manager.getValue(Utils.IS_LOGIN);
                if (isLogin)
                    checkDisponibility();
                else
                    Utils.showToast(getApplicationContext(), getString(R.string.primeroRegistrar));
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }

    }

    private void checkDisponibility() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int id = preferenceManager.getValueInt(Utils.MY_ID);
        String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL = String.format("%s?idU=%s&key=%s&id=%s",
                Utils.URL_DEPORTE_TEMPORADA, id, token, mDeporte.getIdDep());
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
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
                    Intent i = new Intent(getApplicationContext(), InscribirDeporteActivity.class);
                    i.putExtra(Utils.DEPORTE_NAME, mDeporte);
                    int anio = jsonObject.getInt("id");
                    i.putExtra(Utils.DEPORTE_TEMPORADA, anio);
                    startActivity(i);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.deporteCerroConvocatoria));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.deportesSinConvocatoria));
                    break;
                case 6:
                    Utils.showToast(getApplicationContext(), getString(R.string.deporteYaInscripto));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;
        maps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                        .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                        .setTitulo(getString(R.string.advertencia))
                        .setDescripcion(getString(R.string.abrirMapa))
                        .setIcono(R.drawable.ic_advertencia)
                        .setListener(new YesNoDialogListener() {
                            @Override
                            public void yes() {
                                LatLng latLng = new LatLng(Double.parseDouble(mDeporte.getLat()), Double.parseDouble(mDeporte.getLon()));
                                Utils.openMap(InfoDeporteActivity.this, latLng);
                            }

                            @Override
                            public void no() {

                            }
                        });
                DialogoGeneral dialogoGeneral = builder.build();
                dialogoGeneral.show(getSupportFragmentManager(), "dialogo_pregunta");
            }
        });
        LatLng latLng = new LatLng(Double.parseDouble(mDeporte.getLat()), Double.parseDouble(mDeporte.getLon()));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(mDeporte.getLugar()).
                        icon(BitmapDescriptorFactory.defaultMarker());
        maps.addMarker(markerOptions);
        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }
}
