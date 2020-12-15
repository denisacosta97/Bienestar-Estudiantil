package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.ItemBase;
import com.unse.bienestar.estudiantil.Modelos.ItemDato;
import com.unse.bienestar.estudiantil.Modelos.ItemFecha;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.CargarDocumentacionActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.ModificarInscripcionActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.FechasAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class InscripcionesActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FechasAdapter mFechasAdapter;
    ArrayList<ItemBase> mList, mListOficial;
    DialogoProcesamiento dialog;
    LinearLayout latError, latVacio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadData();

        setToolbar();

        loadListener();

        loadInfo(R.drawable.ic_error, getString(R.string.credencialesListError),
                R.drawable.ic_vacio, getString(R.string.credencialesListVacia));

        updateView(-1);

    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Inscripciones");
        Utils.changeColorDrawable(btnBack, getApplicationContext(), R.color.colorPrimaryDark);
    }

    private void loadListener() {
        btnBack.setOnClickListener(this);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {

                procesarClick(position);
            }
        });

    }

    private void procesarClick(int position) {
        Intent intent = null;
        ItemBase itemBase = mListOficial.get(position);
        if (itemBase instanceof ItemDato) {
            ItemDato itemDato = (ItemDato) itemBase;
            switch (itemDato.getInscripcion().getTipo()) {
                case Inscripcion.TIPO_BECA:
                    openInscripcion(itemDato.getInscripcion(), Inscripcion.TIPO_BECA);
                    break;
                case Inscripcion.TIPO_DEPORTE:
                    openInscripcion(itemDato.getInscripcion(), Inscripcion.TIPO_DEPORTE);
                    break;
            }
        }

    }

    private void openInscripcion(Inscripcion inscripcion, final int tipo) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = "";
        if (tipo == Inscripcion.TIPO_DEPORTE) {
            URL = String.format("%s?idU=%s&key=%s&ii=%s&aa=%s", Utils.URL_INSCRIPCION_BY_ID, id, key,
                    inscripcion.getIdInscripcion(),
                    inscripcion.getIdTemporada());
        } else if (tipo == Inscripcion.TIPO_BECA) {
            URL = String.format("%s?idU=%s&key=%s&iu=%s&an=%s&ib=%s", Utils.URL_BECAS_INSCRIPCION, id, key,
                    id,
                    inscripcion.getIdTemporada(), inscripcion.getIdBeca());
        }
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaInscripcion(response, tipo);
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

    private void procesarRespuestaInscripcion(String response, int tipo) {
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
                    loadInfoInscripcion(jsonObject, tipo);
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

    private void loadInfoInscripcion(JSONObject jsonObject, int tipo) {

        if (tipo == Inscripcion.TIPO_BECA) {
            try {
                Usuario usuario = null;
                if (jsonObject.has("datos")) {
                    usuario = Usuario.mapper(jsonObject.getJSONObject("mensaje"), Usuario.BASIC);
                }
                ArrayList<Documentacion> docs = new ArrayList<>();
                if (jsonObject.has("docs")) {
                    JSONArray documentos = jsonObject.getJSONArray("docs");
                    for (int i = 0; i < documentos.length(); i++) {
                        JSONObject object = documentos.getJSONObject(i);
                        Documentacion documentacion = Documentacion.toMapper(object, Documentacion.LOW);
                        docs.add(documentacion);

                    }
                }
                ArrayList<Archivo> archivos = new ArrayList<>();
                if (jsonObject.has("archivos")) {
                    JSONArray arch = jsonObject.getJSONArray("archivos");
                    for (int i = 0; i < arch.length(); i++) {
                        JSONObject object = arch.getJSONObject(i);
                        Archivo archivo = Archivo.toMapper(object, Archivo.LOW);
                        archivo.setValidez(1);
                        archivos.add(archivo);

                    }
                }
                Inscripcion inscripcion = null;
                if (jsonObject.has("mensaje")) {
                    JSONObject object = jsonObject.getJSONObject("mensaje");
                    inscripcion = Inscripcion.mapper(object, Inscripcion.HIGH);

                }
                Intent intent = new Intent(getApplicationContext(), CargarDocumentacionActivity.class);
                intent.putExtra(Utils.INFO_EXTRA, inscripcion);
                intent.putExtra(Utils.INFO_EXTRA_2, archivos);
                intent.putExtra(Utils.NOTICIA_INFO, docs);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (tipo == Inscripcion.TIPO_DEPORTE) {
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
                    intent.putExtra(Utils.CREDENCIAL, credencialDeporte);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void loadData() {
        mList = new ArrayList<>();
        mListOficial = new ArrayList<>();

        mFechasAdapter = new FechasAdapter(getApplicationContext(), mListOficial, FechasAdapter.TIPO_INSCRIPCIONES);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mFechasAdapter);

        loadInfo();


    }

    private void loadInfo(int iconError, String text, int iconVacio, String vacio) {
        ((ImageView) findViewById(R.id.imgIconError)).setBackground(getResources().getDrawable(iconError));
        ((ImageView) findViewById(R.id.imgIconVacio)).setBackground(getResources().getDrawable(iconVacio));
        ((TextView) findViewById(R.id.txtError)).setText(text);
        ((TextView) findViewById(R.id.txtVacio)).setText(vacio);
        ((Button) findViewById(R.id.btnError)).setOnClickListener(this);

    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?idU=%s&key=%s&iu=%s", Utils.URL_INSCRIPCIONES_GENERALES, id, key, id);
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
                    loadInfo(jsonObject);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.noData));
                    updateView(0);
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
            updateView(2);
        }
    }

    private void loadInfo(JSONObject jsonObject) {
        try {

            if (jsonObject.has("deportes")) {

                JSONArray jsonArray = jsonObject.isNull("deportes") ? null : jsonObject.getJSONArray("deportes");

                for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {

                    JSONObject o = jsonArray.getJSONObject(i);

                    Inscripcion inscripcion = Inscripcion.mapper(o, Inscripcion.PARCIAL);

                    ItemDato dato = new ItemDato();
                    dato.setInscripcion(inscripcion);
                    dato.setTipo(ItemDato.TIPO_INSCRIPCION);

                    mList.add(dato);

                }

            }

            if (jsonObject.has("becas")) {

                JSONArray jsonArray = jsonObject.isNull("becas") ? null : jsonObject.getJSONArray("becas");

                for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {

                    JSONObject o = jsonArray.getJSONObject(i);

                    Inscripcion inscripcion = Inscripcion.mapper(o, Inscripcion.LOW_BECA);

                    ItemDato dato = new ItemDato();
                    dato.setInscripcion(inscripcion);
                    dato.setTipo(ItemDato.TIPO_BECA);

                    mList.add(dato);

                }

            }
            /*if (jsonObject.get("becas") != null && jsonObject.get("becas") instanceof JSONArray) {

                JSONArray jsonArray = jsonObject.getJSONArray("becas");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);

                    int id = Integer.parseInt(o.getString("idInscripcion"));
                    int idUsuario = Integer.parseInt(o.getString("idUsuario"));
                    int idConvocatoria = Integer.parseInt(o.getString("idConvocatoria"));
                    int anio = Integer.parseInt(o.getString("anio"));
                    int validez = Integer.parseInt(o.getString("validez"));
                    int idEstado = Integer.parseInt(o.getString("idEstado"));
                    String estado = o.getString("nombreE");
                    String titulo = o.getString("nombreB");

                    Inscripcion inscripcion = new Inscripcion(id, titulo, anio, idUsuario, idConvocatoria,
                            validez, Inscripcion.TIPO_BECA, idEstado, estado);

                    ItemDato dato = new ItemDato();
                    dato.setInscripcion(inscripcion);
                    dato.setTipo(ItemDato.TIPO_INSCRIPCION);

                    mList.add(dato);

                }

            } else {

                JSONObject o = jsonObject.getJSONObject("becas");

                int id = Integer.parseInt(o.getString("idInscripcion"));
                int idUsuario = Integer.parseInt(o.getString("idUsuario"));
                int idConvocatoria = Integer.parseInt(o.getString("idConvocatoria"));
                int anio = Integer.parseInt(o.getString("anio"));
                int validez = Integer.parseInt(o.getString("validez"));
                String estado = o.getString("nombreE");
                int idEstado = Integer.parseInt(o.getString("idEstado"));
                String titulo = o.getString("nombreB");

                Inscripcion inscripcion = new Inscripcion(id, titulo, anio, idUsuario, idConvocatoria,
                        validez, Inscripcion.TIPO_BECA, idEstado, estado);

                ItemDato dato = new ItemDato();
                dato.setInscripcion(inscripcion);
                dato.setTipo(ItemDato.TIPO_INSCRIPCION);

                mList.add(dato);

            }

            if (jsonObject.get("deportes") != null && jsonObject.get("deportes") instanceof JSONArray) {



            } else {
                JSONObject o = jsonObject.getJSONObject("deportes");

                int id = Integer.parseInt(o.getString("idInscripcion"));
                int idUsuario = Integer.parseInt(o.getString("idUsuario"));
                int idConvocatoria = Integer.parseInt(o.getString("idTemporada"));
                int anio = Integer.parseInt(o.getString("anio"));
                int validez = Integer.parseInt(o.getString("validez"));
                String estado = o.getString("nombreE");
                int idEstado = Integer.parseInt(o.getString("idEstado"));

                String titulo = o.getString("nombre");

                Inscripcion inscripcion = new Inscripcion(id, titulo, anio, idUsuario, idConvocatoria,
                        validez, Inscripcion.TIPO_DEPORTE, idEstado, estado);

                ItemDato dato = new ItemDato();
                dato.setInscripcion(inscripcion);
                dato.setTipo(ItemDato.TIPO_INSCRIPCION);

                mList.add(dato);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
            updateView(2);
        }


        HashMap<String, List<ItemBase>> groupedHashMap = groupDataIntoHashMap(mList);
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };

        List<String> anios = new ArrayList<>();
        anios.addAll(groupedHashMap.keySet());

        Collections.sort(anios, comparator);

        for (String date : anios) {
            ItemFecha dateItem = new ItemFecha(date);
            mListOficial.add(dateItem);

            for (ItemBase item : groupedHashMap.get(date)) {
                ItemDato generalItem = (ItemDato) item;
                mListOficial.add(generalItem);
            }
        }
        mFechasAdapter.change(mListOficial);

        updateView(1);

    }

    private HashMap<String, List<ItemBase>> groupDataIntoHashMap(List<ItemBase> list) {

        HashMap<String, List<ItemBase>> groupedHashMap = new HashMap<>();

        for (ItemBase dato : list) {

            ItemDato itemDatoReserva = (ItemDato) dato;

            String key = String.valueOf(itemDatoReserva.getInscripcion().getIdTemporada());

            if (groupedHashMap.containsKey(key)) {
                groupedHashMap.get(key).add(dato);
            } else {
                List<ItemBase> nuevaLista = new ArrayList<>();
                nuevaLista.add(dato);
                groupedHashMap.put(key, nuevaLista);
            }
        }
        return groupedHashMap;
    }

    private void updateView(int b) {
        if (b == 1) {
            mRecyclerView.setVisibility(View.VISIBLE);
            latError.setVisibility(GONE);
            latVacio.setVisibility(GONE);
        } else if (b == 2) {
            mRecyclerView.setVisibility(View.GONE);
            latError.setVisibility(View.VISIBLE);
            latVacio.setVisibility(GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            latError.setVisibility(GONE);
            latVacio.setVisibility(View.VISIBLE);
        }
    }

    private void loadViews() {
        btnBack = findViewById(R.id.imgFlecha);
        mRecyclerView = findViewById(R.id.recycler);
        latError = findViewById(R.id.layoutError);
        latVacio = findViewById(R.id.layoutVacio);
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

