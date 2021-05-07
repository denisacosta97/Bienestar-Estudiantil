package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.Modelos.PuntoConectividad;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.PuntosConectividad.SelectorFechaPCActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.OpcionesSimpleAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class PConectividadFragment extends Fragment {

    View view;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recicler;
    ArrayList<Opciones> mOpciones;
    ArrayList<PuntoConectividad> mPuntos;
    OpcionesSimpleAdapter mSimpleAdapter;
    DialogoProcesamiento dialog;
    FragmentManager mFragmentManager;
    LinearLayout latError;
    Context mContext;
    Button btnError;

    public PConectividadFragment(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pconectividad, container, false);

        loadViews();

        loadData();

        loadListener();

        updateView(0);

        return view;
    }

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recicler.setNestedScrollingEnabled(true);
        recicler.setLayoutManager(mLayoutManager);

        loadInfo();
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recicler);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                processClick(position);

            }
        });
    }

    private void processClick(int position) {
        PuntoConectividad puntoConectividad = mPuntos.get(position);
        if (puntoConectividad.getId() == mOpciones.get(position).getId()) {
            if (puntoConectividad.getDisponible() == 0){
                dialogoPronto();
            }else if (puntoConectividad.getTurno() == 0){
                dialogoWsp(position);
            }else{
                Intent i = new Intent(getContext(), SelectorFechaPCActivity.class);
                i.putExtra(Utils.DATA_OPCION, mOpciones.get(position));
                startActivity(i);
            }
        }
    }

    private void dialogoWsp(final int position) {
        PuntoConectividad puntoConectividad = mPuntos.get(position);
        DialogoGeneral.Builder dialogoGeneral = new DialogoGeneral.Builder(getContext())
                .setTipo(DialogoGeneral.TIPO_ACEPTAR)
                .setTitulo(getString(R.string.advertencia))
                .setIcono(R.drawable.ic_advertencia)
                .setDescripcion(getString(R.string.puntoCWsp, puntoConectividad.getContacto()))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        openWSP(position);
                    }

                    @Override
                    public void no() {

                    }
                });
        DialogoGeneral dialogo = dialogoGeneral.build();
        dialogo.show(getFragmentManager(), "dialog");
    }

    private void openWSP(int position) {
        try{
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            UsuarioViewModel usuarioViewModel = new UsuarioViewModel(getContext());
            PreferenceManager manager = new PreferenceManager(getContext());
            int id = manager.getValueInt(Utils.MY_ID);
            Usuario usuario = usuarioViewModel.getById(id);
            String mensaje = String.format("Hola, soy %s estudiante de la UNSE, solicito información acerca de los turnos del punto de conectividad", usuario != null ? usuario.getNombre() : "nombreAlumno");
            String url = "https://api.whatsapp.com/send?phone="+ mPuntos.get(position).getContacto() +"&text="
                    + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Utils.showToast(getContext(), getString(R.string.wspNoIntalado));
            }
        } catch(Exception e) {
            Utils.showToast(getContext(), getString(R.string.errorTelefono));
        }

    }

    private void dialogoPronto() {
        DialogoGeneral.Builder dialogoGeneral = new DialogoGeneral.Builder(getContext())
                .setTipo(DialogoGeneral.TIPO_ACEPTAR)
                .setTitulo(getString(R.string.advertencia))
                .setIcono(R.drawable.ic_advertencia)
                .setDescripcion(getString(R.string.puntoCNoDisponible))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {

                    }

                    @Override
                    public void no() {

                    }
                });
        DialogoGeneral dialogo = dialogoGeneral.build();
        dialogo.show(getFragmentManager(), "dialog");
        
                
    }

    private void loadViews() {
        recicler = view.findViewById(R.id.recycler);
        btnError = view.findViewById(R.id.btnError);
        latError = view.findViewById(R.id.layoutError);
    }

    public void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getContext());
        String key = manager.getValueString(Utils.TOKEN);
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        int id = manager.getValueInt(Utils.MY_ID);
        if (id == 0)
            id = 1;
        String URL = String.format("%s?key=%s&idU=%s&iu=%s", Utils.URL_PC_ZONAS, isLogin ? key : "1", id, id);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getContext(), getString(R.string.servidorOff));
                updateView(1);
                dialog.dismiss();
            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(mFragmentManager, "dialog_process");
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void updateView(int i) {
        switch (i) {
            case 0:
                latError.setVisibility(View.GONE);
                break;
            case 1:
                latError.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void procesarRespuesta(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getContext(), getString(R.string.errorInternoAdmin));
                    updateView(1);
                    break;
                case 1:
                    loadInfo(jsonObject);
                    break;
                case 2:
                    updateView(1);
                    break;
                case 3:
                    Utils.showToast(getContext(), getString(R.string.tokenInvalido));
                    updateView(1);
                    break;
                case 4:
                    Utils.showToast(getContext(), getString(R.string.camposInvalidos));
                    updateView(1);
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getContext(), getString(R.string.tokenInexistente));
                    updateView(1);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            updateView(2);
            Utils.showToast(getContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadInfo(JSONObject mensaje) {
        if (mensaje.has("mensaje")) {
            try {
                JSONArray datos = mensaje.getJSONArray("mensaje");

                mOpciones = new ArrayList<>();

                mPuntos = new ArrayList<>();

                for (int i = 0; i < datos.length(); i++) {

                    JSONObject o = datos.getJSONObject(i);

                    PuntoConectividad puntoConectividad = PuntoConectividad.mapper(o, PuntoConectividad.LUGAR);
                    Opciones op = Opciones.mapper(o, Opciones.BASIC);
                    mOpciones.add(op);
                    mPuntos.add(puntoConectividad);

                }
                if (mOpciones.size() > 0) {
                    mSimpleAdapter = new OpcionesSimpleAdapter(mOpciones, getContext());
                    recicler.setAdapter(mSimpleAdapter);
                } else {
                    updateView(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                updateView(1);
            }
        } else {
            updateView(1);
        }
    }

}