package com.unse.bienestar.estudiantil.Vistas.Activities.UPA;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.ServiciosUPA;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.ServiciosUPAAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServiciosUPAActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    ArrayList<ServiciosUPA> mServicios;
    ServiciosUPAAdapter mAdapter;
    ImageView imgIcono;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_upa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        setToolbar();

    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?idU=%s&key=%s&ad=%s", Utils.URL_SERVICIOS, id, key, 0);
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
                    loadInfo(jsonObject);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.noData));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
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
        try {
            if (jsonObject.has("mensaje")) {
                JSONArray jsonArray = jsonObject.getJSONArray("mensaje");
                mServicios = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    ServiciosUPA serviciosUPA = ServiciosUPA.mapper(o, ServiciosUPA.BASIC);
                    mServicios.add(serviciosUPA);

                }
                if (mServicios.size() > 0) {
                    mAdapter = new ServiciosUPAAdapter(mServicios, getApplicationContext());
                    mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recycler.setNestedScrollingEnabled(true);
                    recycler.setLayoutManager(mLayoutManager);
                    recycler.setAdapter(mAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PerfilServicioActivity.class);
                i.putExtra(Utils.SERVICIO_NAME, mServicios.get(position));
                startActivity(i);
            }
        });
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Servicios");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadViews() {
        recycler = findViewById(R.id.recyclerBecas);
        imgIcono = findViewById(R.id.imgFlecha);
    }

    private void loadData() {
        loadInfo();

        /*mServicios.add(new ServiciosUPA(0, R.drawable.ic_medico, "Odontología", getString(R.string.descOdont), "Lunes a Viernes", "08:30hs a 11:30hs y 14:00hs a 17:00hs", "Odont. Carolina Salido y Odont. Ana Paula Carrizo", 0));
        mServicios.add(new ServiciosUPA(1, R.drawable.ic_medico, "Psicología", getString(R.string.descPsico), "Lunes a Viernes", "Turnos programados", "Lic. Silvia Gonzáles Corral", 0));
        mServicios.add(new ServiciosUPA(2, R.drawable.ic_medico, "Médico", getString(R.string.descServ), "Lunes a Viernes", "08:30hs a 11:30hs", "Dra. María Laura Silva", 1));
        mServicios.add(new ServiciosUPA(3, R.drawable.ic_medico, "Enfermería", getString(R.string.descServ), "Lunes a Viernes", "08:30hs a 12:00hs y 14:30hs a 18:30hs", "Lic. Patricia Farias y Lic. Celeste Segura", 1));
        mServicios.add(new ServiciosUPA(4, R.drawable.ic_medico, "Obstetricia SSyPR", getString(R.string.descServ), "Lunes a Viernes", "08:00hs a 13:00hs", "Lic. Exequiel Federico Duarte", 1));
        mServicios.add(new ServiciosUPA(5, R.drawable.ic_medico, "Educación para la Salud", getString(R.string.descServ), "Lunes a Viernes", "08:00hs a 13:00hs", "Dra. Adriana Barrera", 1));
       */
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