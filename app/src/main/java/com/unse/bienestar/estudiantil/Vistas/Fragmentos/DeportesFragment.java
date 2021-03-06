package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Context;
import android.content.Intent;
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
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Deporte;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Deportes.InfoDeporteActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.DeportesAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeportesFragment extends Fragment {

    View view;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView reciclerDeportes;
    ArrayList<Deporte> mDeportes;
    DeportesAdapter mDeportesAdapter;
    DialogoProcesamiento dialog;
    FragmentManager mFragmentManager;
    LinearLayout latError;
    Context mContext;
    Button btnError;

    public DeportesFragment(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deportes, container, false);


        loadViews();

        loadData();

        loadListener();

        updateView(0);

        return view;
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(reciclerDeportes);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), InfoDeporteActivity.class);
                i.putExtra(Utils.DEPORTE_NAME, mDeportes.get(position));
                startActivity(i);
            }
        });
    }

    private void loadViews() {
        btnError = view.findViewById(R.id.btnError);
        latError = view.findViewById(R.id.layoutError);
        reciclerDeportes = view.findViewById(R.id.recyclerDeportes);
    }

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        reciclerDeportes.setNestedScrollingEnabled(true);
        reciclerDeportes.setLayoutManager(mLayoutManager);

        loadInfo();


    }

    public void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getContext());
        String key = manager.getValueString(Utils.TOKEN);
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        int id = manager.getValueInt(Utils.MY_ID);
        if (id == 0)
            id = 1;
        String URL = String.format("%s?key=%s&idU=%s", Utils.URL_DEPORTE_LISTA, isLogin ? key : "1", id);
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

                mDeportes = new ArrayList<>();

                for (int i = 0; i < datos.length(); i++) {
                    try {
                        JSONObject o = datos.getJSONObject(i);

                        Deporte deporte = Deporte.mapper(o, Deporte.COMPLETE);
                        deporte.setIcon();

                        mDeportes.add(deporte);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        updateView(1);
                    }
                }
                if (mDeportes.size() > 0) {
                    mDeportesAdapter = new DeportesAdapter(mDeportes, getContext(), false);
                    reciclerDeportes.setAdapter(mDeportesAdapter);
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