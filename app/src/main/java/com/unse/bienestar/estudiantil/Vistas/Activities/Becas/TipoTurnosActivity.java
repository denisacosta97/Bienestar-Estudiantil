package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Convocatoria;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.OpcionesSimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TipoTurnosActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TextView txtDescripcion;
    OpcionesSimpleAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Opciones> mList;
    ArrayList<Convocatoria> mConvocatorias;
    ProgressBar mProgressBar;


    public static TipoTurnosActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos_turnos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        instance = this;

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {
        mList = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable progress = mProgressBar.getIndeterminateDrawable();
            progress.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mProgressBar.setIndeterminateDrawable(progress);
        }
        loadInfo();

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s", Utils.URL_BECAS_CONVOCATORIA, key,
                idLocal);
        StringRequest requestImage = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                mProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void procesarRespuesta(String response) {
        try {
            mProgressBar.setVisibility(View.GONE);
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
                    Utils.showToast(getApplicationContext(), getString(R.string.becaConvocatoriaNo));
                    txtDescripcion.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
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

    private void loadInfo(JSONObject response) {
        try {
            if (response.has("mensaje")) {
                JSONArray jsonArray = response.getJSONArray("mensaje");

                mConvocatorias = new ArrayList<>();
                mList = new ArrayList<>();

                for(int i = 0; i<jsonArray.length(); i++){

                    JSONObject object = jsonArray.getJSONObject(i);

                    Convocatoria convocatoria = Convocatoria.mapper(object, Convocatoria.MEDIUM);

                    mConvocatorias.add(convocatoria);

                    mList.add(new Opciones(LinearLayout.VERTICAL, convocatoria.getIdBeca(),
                            String.format("%s %s", convocatoria.getNombreBeca(), convocatoria.getAnio()).toUpperCase(), 0, 0));

                }

                if (mList.size() > 0){
                    adapter = new OpcionesSimpleAdapter(mList, getApplicationContext());
                    mRecyclerView.setAdapter(adapter);
                }else{
                    txtDescripcion.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SelectorFechaActivity.class);
                intent.putExtra(Utils.DATA_RESERVA, mConvocatorias.get(position));
                startActivity(intent);
            }
        });
    }

    private void loadViews() {
        txtDescripcion = findViewById(R.id.txtDescripcion);
        mRecyclerView = findViewById(R.id.recycler);
        mProgressBar = findViewById(R.id.progres);
    }


}
