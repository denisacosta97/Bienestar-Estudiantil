package com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.GestionDeportes.Inscripciones;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.ModificarInscripcionActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.InscripcionesAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaInscriptosActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    InscripcionesAdapter mAdapter;
    ArrayList<Inscripcion> mInscripcions;
    ImageView imgIcono;

    CardView mCardView;
    EditText mEditText;
    LinearLayout mLayoutError, mLayoutVacio;
    DialogoProcesamiento dialog;

    int idDeporte = -1, anio = -1;
    String deporteName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscriptos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getData();

        setToolbar();

        loadViews();

        updateView(0);

        loadData();

        loadListener();

        loadInfo(R.drawable.ic_error, String.format(getString(R.string.listError), "inscripciones"),
                R.drawable.ic_vacio, String.format(getString(R.string.listVacio), "inscripciones"));

    }

    private void updateView(int i) {
        switch (i) {
            case 0:
                mRecyclerView.setVisibility(View.GONE);
                mCardView.setVisibility(View.GONE);
                mLayoutError.setVisibility(View.GONE);
                mLayoutVacio.setVisibility(View.VISIBLE);
                break;
            case 1:
                mRecyclerView.setVisibility(View.VISIBLE);
                mCardView.setVisibility(View.VISIBLE);
                mLayoutError.setVisibility(View.GONE);
                mLayoutVacio.setVisibility(View.GONE);
                break;
            case 2:
                mRecyclerView.setVisibility(View.GONE);
                mCardView.setVisibility(View.GONE);
                mLayoutError.setVisibility(View.VISIBLE);
                mLayoutVacio.setVisibility(View.GONE);
                break;
        }
    }

    private void loadInfo(int iconError, String text, int iconVacio, String vacio) {
        ((ImageView) findViewById(R.id.imgIconError)).setBackground(getResources().getDrawable(iconError));
        ((ImageView) findViewById(R.id.imgIconVacio)).setBackground(getResources().getDrawable(iconVacio));
        ((TextView) findViewById(R.id.txtError)).setText(text);
        ((TextView) findViewById(R.id.txtVacio)).setText(vacio);
        ((Button) findViewById(R.id.btnError)).setOnClickListener(this);

    }

    private void getData() {
        if (getIntent().getIntExtra(Utils.DEPORTE_ID, -1) != -1) {
            idDeporte = getIntent().getIntExtra(Utils.DEPORTE_ID, -1);
        }
        if (getIntent().getIntExtra(Utils.ANIO, -1) != -1) {
            anio = getIntent().getIntExtra(Utils.ANIO, -1);
        }
        if (getIntent().getStringExtra(Utils.DEPORTE_NAME) != null) {
            deporteName = getIntent().getStringExtra(Utils.DEPORTE_NAME);
        }
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText(
                String.format("%s %s", deporteName != null ? deporteName : "Temporada",
                        anio != -1 ? anio : ""));

    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Inscripcion inscripcion = mInscripcions.get(position);
                if (inscripcion != null) {
                    openInscripcion(inscripcion);
                }
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void openInscripcion(Inscripcion inscripcion) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?idU=%s&key=%s&ii=%s&aa=%s", Utils.URL_INSCRIPCION_BY_ID, id, key,
                inscripcion.getIdInscripcion(),
                anio);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaInscripcion(response);
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

    private void procesarRespuestaInscripcion(String response) {
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
                    loadInfoInscripcion(jsonObject);
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

    private void loadInfoInscripcion(JSONObject jsonObject) {
        try {
            if (jsonObject.has("mensaje") && jsonObject.has("cred")) {

                JSONObject o = jsonObject.getJSONObject("mensaje");

                Inscripcion mInscripcion = Inscripcion.mapper(o, Inscripcion.COMPLETE);
                Usuario mUsuario = Usuario.mapper(o, Usuario.MEDIUM);
                Alumno alumno = jsonObject.has("datos") ? Alumno.mapper(jsonObject, mUsuario) : null;
                CredencialDeporte credencialDeporte = (jsonObject.has("cred") &&
                        !(jsonObject.get("cred") instanceof Boolean))
                        ? CredencialDeporte.mapper(jsonObject.getJSONObject("cred"), CredencialDeporte.MEDIUM) : null;

                Intent intent = new Intent(getApplicationContext(), ModificarInscripcionActivity.class);
                intent.putExtra(Utils.INSCRIPCION_ID, mInscripcion);
                intent.putExtra(Utils.USER_INFO, mUsuario);
                intent.putExtra(Utils.ALUMNO_NAME, alumno);
                intent.putExtra(Utils.IS_ADMIN_MODE, true);
                intent.putExtra(Utils.CREDENCIAL, credencialDeporte);
                startActivity(intent);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mInscripcions = new ArrayList<>();
        mAdapter = new InscripcionesAdapter(getApplicationContext(), mInscripcions);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadInfo();

    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        imgIcono = findViewById(R.id.imgFlecha);
        mCardView = findViewById(R.id.cardSearch);
        mEditText = findViewById(R.id.edtBuscar);
        mLayoutVacio = findViewById(R.id.layoutVacio);
        mLayoutError = findViewById(R.id.layoutError);
    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s&it=%s&id=%s",
                Utils.URL_INSCRIPCIONES_POR_DEPORTE, key, id, anio, idDeporte);
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
                updateView(2);
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
                    updateView(0);
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
                JSONArray datos = jsonObject.getJSONArray("mensaje");
                for (int i = 0; i < datos.length(); i++) {

                    JSONObject j = datos.getJSONObject(i);

                    Inscripcion inscripcion = Inscripcion.mapper(j, Inscripcion.LOW);

                    mInscripcions.add(inscripcion);
                }
                if (mInscripcions.size() > 0) {
                    mAdapter.setList(mInscripcions);
                    updateView(1);
                } else {
                    updateView(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            updateView(0);
        }


    }

    @Override
    public void onBackPressed() {
        if (mEditText.getText().length() > 0) {
            mEditText.setText("");
        } else super.onBackPressed();
    }

    private void buscar(String txt) {
        Pattern pattern = Pattern.compile(Utils.PATRON_NUMEROS);
        Matcher matcher = pattern.matcher(txt);
        if (matcher.find()) {
            mAdapter.filtrar(txt, Utils.LIST_DNI);
            return;
        }
        pattern = Pattern.compile(Utils.PATRON_NOMBRES);
        matcher = pattern.matcher(txt);
        if (matcher.find()) {
            mAdapter.filtrar(txt, Utils.LIST_NOMBRE);
        } else {
            mAdapter.filtrar(txt, Utils.LIST_RESET);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnError:
                loadInfo();
                break;
        }
    }

}
