package com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.GestionDeportes.Inscripciones;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.Modelos.Temporada;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.OpcionesAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InscripcionesTemporadasActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    OpcionesAdapter mAdapter;
    ArrayList<Opciones> mOpciones;
    ArrayList<Temporada> mTemporadas;
    ImageView imgIcono;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gestion_deportes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadListener();

        loadData();

    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Temporada de Deportes");
    }


    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                try {
                    Temporada temporada = mTemporadas.get(position);
                    if (temporada != null) {
                        Intent i = new Intent(getApplicationContext(), ListaInscriptosActivity.class);
                        i.putExtra(Utils.DEPORTE_ID, temporada.getIdDeporte());
                        i.putExtra(Utils.ANIO, temporada.getAnio());
                        i.putExtra(Utils.DEPORTE_NAME, temporada.getNombre());
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        imgIcono.setOnClickListener(this);

    }

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadInfo();
    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s&ie=%s",
                Utils.URL_INSCRIPCIONES_TEMPORADA_DEPORTE, key, id, id);
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
                case 1:
                    //Exito
                    loadInfo(jsonObject);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.noData));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadInfo(JSONObject jsonObject) {
        if (jsonObject.has("mensaje")) {

            try {
                mOpciones = new ArrayList<>();
                mTemporadas = new ArrayList<>();
                JSONArray datos = jsonObject.getJSONArray("mensaje");
                for (int i = 0; i < datos.length(); i++) {

                    JSONObject j = datos.getJSONObject(i);

                    Temporada temporada = Temporada.mapper(j, Temporada.DEPORTE);

                    mTemporadas.add(temporada);

                    mOpciones.add(new Opciones(
                            true, LinearLayout.VERTICAL, i,
                            String.format("%s - %s", temporada.getNombre(), temporada.getAnio()),
                            R.drawable.ic_deportes, R.color.colorFCEyT));


                }
                if (mOpciones.size() > 0) {
                    mAdapter = new OpcionesAdapter(mOpciones, getApplicationContext(), 1);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.noData));
                }
            } catch (JSONException e) {
                Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                e.printStackTrace();
            }

        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.noData));
        }


    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        imgIcono = findViewById(R.id.imgFlecha);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }
    }
}
