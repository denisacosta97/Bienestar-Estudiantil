package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.annotation.SuppressLint;
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
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.CredencialBeca;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.CredencialSocio;
import com.unse.bienestar.estudiantil.Modelos.CredencialTorneo;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.CredencialesAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class ListaCredencialesActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecycler;
    RecyclerView.LayoutManager mManager;
    CredencialesAdapter mAdapter;
    ArrayList<Object> mList;
    ImageView btnBack;
    Alumno alumno;

    DialogoProcesamiento dialog;
    LinearLayout latError, latVacio;

    int tipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_credencial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getIntExtra(Utils.TIPO_CREDENCIAL, -1) != -1) {
            tipo = getIntent().getIntExtra(Utils.TIPO_CREDENCIAL, -1);
        }

        if (tipo != -1) {

            loadViews();

            setToolbar();

            loadData();

            loadListener();

            updateView(-1);

            loadInfo();


        } else
            Utils.showToast(getApplicationContext(), "Error, intente nuevamente");


    }

    private void loadInfo() {
        String vacio = null;
        switch (tipo) {
            case 1:
                vacio = "No posee ninguna credencial de becas, primero debe solicitar una en Área Becas";
                break;
            case 2:
                vacio = "No posee ninguna credencial deportiva, primero debe registrarse a alguna actividad";
                break;
            case 3:
                vacio = "No posee ninguna credencial de torneos, primero debe participar en alguno de ellos";
                break;
            case 4:
                vacio = "No posee ninguna credencial de socio, primero debe asociarse en Bienestar Estudiantil";
                break;
        }
        loadInfo(R.drawable.ic_error, "Error al obtener sus credenciales, por favor reintenta",
                R.drawable.ic_vacio, vacio);
    }

    private void setToolbar() {
        String titulo = "Credenciales";
        switch (tipo) {
            case 1:
                titulo = "Credenciales Becas";
                break;
            case 2:
                titulo = "Credenciales Deportes";
                break;
            case 3:
                titulo = "Credenciales Torneos";
                break;
            case 4:
                titulo = "Credenciales Socio";
                break;
        }
        ((TextView) findViewById(R.id.txtTitulo)).setText(titulo);
        Utils.changeColorDrawable(btnBack, getApplicationContext(), R.color.colorPrimaryDark);
    }

    private void loadListener() {
        btnBack.setOnClickListener(this);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecycler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                procesarClick(position);
            }
        });
    }

    private void loadViews() {
        mRecycler = findViewById(R.id.recycler);
        btnBack = findViewById(R.id.imgFlecha);
        latVacio = findViewById(R.id.layoutVacio);
        latError = findViewById(R.id.layoutError);
    }


    private void loadData() {
        mList = new ArrayList<>();

        mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mManager);

        if (tipo == 2)
            loadCredenciales();
        else
            updateView(0);
    }

    private void procesarClick(int id) {
        Intent intent = null;
        switch (tipo) {
            case 1:
                CredencialBeca beca = (CredencialBeca) mList.get(id);
                intent = new Intent(getApplicationContext(), TarjetaCredencialActivity.class);
                intent.putExtra(Utils.TIPO_CREDENCIAL_DATO, beca);
                intent.putExtra(Utils.TIPO_CREDENCIAL, tipo);
                startActivity(intent);
                break;
            case 2:
                CredencialDeporte deporte = (CredencialDeporte) mList.get(id);
                if (deporte.getValidez() == 0) {
                    Utils.showToast(getApplicationContext(), getString(R.string.credencialNoDisponible));
                    break;

                }
                intent = new Intent(getApplicationContext(), TarjetaCredencialActivity.class);
                intent.putExtra(Utils.TIPO_CREDENCIAL_DATO, deporte);
                intent.putExtra(Utils.TIPO_CREDENCIAL, tipo);
                intent.putExtra(Utils.ALUMNO_NAME, alumno);
                startActivity(intent);
                break;
            case 3:
                CredencialTorneo torneo = (CredencialTorneo) mList.get(id);
                intent = new Intent(getApplicationContext(), TarjetaCredencialActivity.class);
                intent.putExtra(Utils.TIPO_CREDENCIAL_DATO, torneo);
                intent.putExtra(Utils.TIPO_CREDENCIAL, tipo);
                startActivity(intent);
                break;
            case 4:
                CredencialSocio socio = (CredencialSocio) mList.get(id);
                intent = new Intent(getApplicationContext(), TarjetaCredencialActivity.class);
                intent.putExtra(Utils.TIPO_CREDENCIAL_DATO, socio);
                intent.putExtra(Utils.TIPO_CREDENCIAL, tipo);
                startActivity(intent);
                break;


        }
    }

    public void loadCredenciales() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = "";
        switch (tipo) {
            case 1:
                URL = String.format("%s?id=%s&key=%s", Utils.URL_BECAS_CREDENCIAL, id, key);
                break;
            case 2:
                URL = String.format("%s?idU=%s&key=%s&iu=%s", Utils.URL_CREDENCIAL_DEPORTE, id, key, id);
                break;
            case 3:
                URL = String.format("%s?id=%s&key=%s", Utils.URL_TORNEO_CREDENCIAL, id, key);
                break;
            case 4:
                URL = String.format("%s?id=%s&key=%s", Utils.URL_SOCIO_CREDENCIAL, id, key);
                break;
        }
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
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    switch (tipo) {
                        case 1:
                            loadInfoBeca(jsonObject.getJSONArray("mensaje"), jsonObject);
                            break;
                        case 2:
                            loadInfoDeporte(jsonObject);
                            break;
                        case 3:
                            loadInfoTorneo(jsonObject.getJSONArray("mensaje"));
                            break;
                        case 4:
                            loadInfoSocios(jsonObject.getJSONArray("mensaje"));
                            break;
                    }
                    updateView(1);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.noCredenciales));
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
            updateView(2);
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadInfoBeca(JSONArray mensaje, JSONObject object) {
        for (int i = 0; i < mensaje.length(); i++) {
            try {
                JSONObject jsonObject = mensaje.getJSONObject(i);

                String legajo = "", carrera = "", facultad = "";

                int id = Integer.parseInt(jsonObject.getString("idAutorizacion"));
                int validez = Integer.parseInt(jsonObject.getString("validez"));
                int idUsuario = Integer.parseInt(jsonObject.getString("idUsuario"));
                int idConvocatoria = Integer.parseInt(jsonObject.getString("idConvocatoria"));
                int tipoBeca = Integer.parseInt(jsonObject.getString("idTipoBeca"));
                int anio = Integer.parseInt(jsonObject.getString("anio"));
                int tipoUsuario = Integer.parseInt(jsonObject.getString("tipoUsuario"));

                if (tipoUsuario == 1) {
                    JSONObject o = object.getJSONObject("dato");
                    legajo = o.getString("legajo");
                    carrera = o.getString("carrera");
                    facultad = o.getString("facultad");
                }

                String nombreBeca = jsonObject.getString("nombreB");
                String nombreU = jsonObject.getString("nombreU");
                String apellidoU = jsonObject.getString("apellidoU");
                String fecha = jsonObject.getString("fechaValidez");

                String titulo = String.format("%s - %s", nombreBeca, anio);

                CredencialBeca socio = new CredencialBeca(id, titulo, validez, idUsuario, idConvocatoria,
                        tipoBeca, anio, tipoUsuario, nombreBeca, nombreU, apellidoU, fecha);
                socio.setCarrera(carrera);
                socio.setFacultad(facultad);
                socio.setLegajo(legajo);

                mList.add(socio);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mAdapter.notifyDataSetChanged();
    }

    private void loadInfoTorneo(JSONArray mensaje) {
        for (int i = 0; i < mensaje.length(); i++) {
            try {
                JSONObject jsonObject = mensaje.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.getString("idInscripcion"));
                int idUsuario = Integer.parseInt(jsonObject.getString("idUsuario"));
                int validez = Integer.parseInt(jsonObject.getString("validez"));
                int anio = Integer.parseInt(jsonObject.getString("anio"));
                int tipoUsuario = Integer.parseInt(jsonObject.getString("tipoUsuario"));
                int idTorneo = Integer.parseInt(jsonObject.getString("idTorneo"));
                int idDeporte = Integer.parseInt(jsonObject.getString("idDeporte"));

                String nombreTorneo = jsonObject.getString("nombreTorneo");
                String lugar = jsonObject.getString("lugar");
                String fechaInicio = jsonObject.getString("fechaInicio");
                String fechaFin = jsonObject.getString("fechaFin");
                String nombreDeporte = jsonObject.getString("nombreDeporte");
                String descripcion = jsonObject.getString("descripcion");
                String nombreUsuario = jsonObject.getString("nombreUsuario");
                String apellido = jsonObject.getString("apellido");
                String fechaNac = jsonObject.getString("fechaNac");
                String sexo = jsonObject.getString("sexo");

                String titulo = String.format("%s - %s", nombreTorneo, nombreDeporte);

                CredencialTorneo socio = new CredencialTorneo(id, idUsuario, titulo, validez, idTorneo, idDeporte,
                        anio, tipoUsuario, nombreTorneo, lugar, fechaInicio, fechaFin, nombreDeporte, descripcion, nombreUsuario,
                        apellido, fechaNac, sexo);

                mList.add(socio);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        mAdapter.notifyDataSetChanged();
    }

    private void loadInfoSocios(JSONArray mensaje) {
        for (int i = 0; i < mensaje.length(); i++) {
            try {
                JSONObject jsonObject = mensaje.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.getString("idCredencialSocio"));
                int idUsuario = Integer.parseInt(jsonObject.getString("idUsuario"));
                int validez = Integer.parseInt(jsonObject.getString("validez"));
                int anio = Integer.parseInt(jsonObject.getString("anio"));
                int tipoUsuario = Integer.parseInt(jsonObject.getString("tipoUsuario"));

                String nombre = jsonObject.getString("nombre");
                String apellido = jsonObject.getString("apellido");
                String fechaRegistro = jsonObject.getString("fechaRegistro");
                String sexo = jsonObject.getString("sexo");

                String titulo = String.format("%s %s", "Socio", anio);

                CredencialSocio socio = new CredencialSocio(id, titulo, validez, idUsuario, anio,
                        tipoUsuario, nombre, apellido, fechaRegistro, sexo);

                mList.add(socio);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        mAdapter.notifyDataSetChanged();
    }

    private void loadInfo(int iconError, String text, int iconVacio, String vacio) {
        (findViewById(R.id.imgIconError)).setBackground(AppCompatResources.getDrawable(getApplicationContext(), iconError));
        (findViewById(R.id.imgIconVacio)).setBackground(AppCompatResources.getDrawable(getApplicationContext(), iconVacio));
        ((TextView) findViewById(R.id.txtError)).setText(text);
        ((TextView) findViewById(R.id.txtVacio)).setText(vacio);
        (findViewById(R.id.btnError)).setOnClickListener(this);

    }

    private void updateView(int b) {
        if (b == 1) {
            mRecycler.setVisibility(View.VISIBLE);
            latError.setVisibility(GONE);
            latVacio.setVisibility(GONE);
        } else if (b == 2) {
            mRecycler.setVisibility(View.GONE);
            latError.setVisibility(View.VISIBLE);
            latVacio.setVisibility(GONE);
        } else {
            mRecycler.setVisibility(View.GONE);
            latError.setVisibility(GONE);
            latVacio.setVisibility(View.VISIBLE);
        }
    }

    private void loadInfoDeporte(JSONObject obj) {
        if (obj.has("mensaje")) {
            try {
                JSONArray mensaje = obj.getJSONArray("mensaje");

                for (int i = 0; i < mensaje.length(); i++) {

                    JSONObject o = mensaje.getJSONObject(i);

                    CredencialDeporte credencialDeporte = CredencialDeporte.mapper(o, CredencialDeporte.COMPLETE);

                    mList.add(credencialDeporte);

                }
                if (obj.has("datos")) {
                    Usuario usuario = new Usuario();
                    alumno = Alumno.mapper(obj, usuario);
                }
                if (mList.size() > 0) {
                    mAdapter = new CredencialesAdapter(mList, getApplicationContext(),
                            false, CredencialesAdapter.DEPORTE);
                    mRecycler.setAdapter(mAdapter);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnError:
                loadCredenciales();
                break;
        }

    }
}
