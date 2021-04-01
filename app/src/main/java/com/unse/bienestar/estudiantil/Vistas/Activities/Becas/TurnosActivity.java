package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

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
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.TurnosAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TurnosActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    TurnosAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Turno> mList;
    LinearLayout latTurnos, latVacio;
    ImageView imgIcono;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s&iu=%s", Utils.URL_TURNO_POR_USUARIO, key,
                idLocal, idLocal);
        StringRequest requestImage = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();
            }
        });
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
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
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadInfo(JSONObject jsonObject) {
        try {
            if (jsonObject.has("mensaje")) {

                mList = new ArrayList<>();

                JSONArray datos = jsonObject.getJSONArray("mensaje");
                for (int i = 0; i < datos.length(); i++) {
                    JSONObject object = datos.getJSONObject(i);

                    Turno turno = Turno.mapper(object, Turno.MEDIUM);
                    if (turno != null)
                        turno.setTipo(Turno.TIPO_BECA);

                    mList.add(turno);
                }


            }
            if (jsonObject.has("uapu")) {

                if (mList == null)
                    mList = new ArrayList<>();

                JSONArray datos = jsonObject.getJSONArray("uapu");
                for (int i = 0; i < datos.length(); i++) {
                    JSONObject object = datos.getJSONObject(i);

                    Turno turno = Turno.mapper(object, Turno.UAPU);
                    if (turno != null)
                        turno.setTipo(Turno.TIPO_UPA);

                    mList.add(turno);
                }

            }
            Collections.sort(mList, new Comparator<Turno>() {
                @Override
                public int compare(Turno o1, Turno o2) {
                    Date date1 = Utils.getFechaDateWithHour(o1.getFechaRegistro());
                    Date date2 = Utils.getFechaDateWithHour(o2.getFechaRegistro());
                    return date2.compareTo(date1);
                }
            });
            if (mList.size() > 0) {
                adapter = new TurnosAdapter(mList, getApplicationContext());
                mRecyclerView.setAdapter(adapter);
                latTurnos.setVisibility(View.VISIBLE);
                latVacio.setVisibility(View.GONE);
            } else {
                latVacio.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {

        }

    }

    private void loadData() {
        mList = new ArrayList<>();

        loadInfo();

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        latVacio.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                procresClick(position);
            }
        });
    }

    private void procresClick(int position) {
        Turno turno = mList.get(position);
        Intent intent = new Intent(getApplicationContext(), InfoTurnoActivity.class);
        intent.putExtra(Utils.DATA_TURNO, turno);
        startActivityForResult(intent, 500);
    }

    private void loadViews() {
        latTurnos = findViewById(R.id.latDatos);
        latVacio = findViewById(R.id.latVacio);
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

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Historial de Turnos");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }
}
