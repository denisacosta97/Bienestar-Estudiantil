package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Categoria;
import com.unse.bienestar.estudiantil.Modelos.Noticia;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Gestion.GestionNoticias.NoticiaLectorActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Maraton.InscripcionMaratonActivity;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.CategoriasAdapter;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.NoticiasAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InicioFragmento extends Fragment {

    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    RecyclerView recyclerCategorias, recyclerNoticias;
    ArrayList<Categoria> mCategorias;
    CategoriasAdapter mAdapter;
    View view;
    NoticiasAdapter mNoticiasAdapter;
    ArrayList<Noticia> mListNoticias;
    Context mContext;
    FragmentManager mFragmentManager;
    CardView cardMaraton;

    public void setContext(Context context) {
        mContext = context;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    DialogoProcesamiento dialog;

    public InicioFragmento() {
        // Metodo necesario
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public InicioFragmento(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Crea la vista de Inicio
        view = inflater.inflate(R.layout.fragmento_inicio, container, false);

        loadViews();

        loadDataRecycler();

        return view;
    }

    private void loadViews() {
        recyclerCategorias = view.findViewById(R.id.recyclerCategorias);
        recyclerNoticias = view.findViewById(R.id.recyclerNoticias);
        cardMaraton = view.findViewById(R.id.cardMaraton);
    }

    private void loadDataRecycler() {
        loadCategorias();
        mAdapter = new CategoriasAdapter(mCategorias, getContext());
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerCategorias.setLayoutManager(mLayoutManager);
        recyclerCategorias.setAdapter(mAdapter);

        recyclerCategorias.setVisibility(View.GONE);

        loadInfo();

        mListNoticias = new ArrayList<>();

        mLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerNoticias.setLayoutManager(mLayoutManager2);

        recyclerNoticias.setNestedScrollingEnabled(false);

        ItemClickSupport itemClickSupport2 = ItemClickSupport.addTo(recyclerNoticias);
        itemClickSupport2.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), NoticiaLectorActivity.class);
                i.putExtra(Utils.NOTICIA, mListNoticias.get(position));
                startActivity(i);
            }
        });

        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerCategorias);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                resetear();
                mCategorias.get(position).setEstado(true);
                mNoticiasAdapter.filtrarNoticias((int) id);
                mAdapter.notifyDataSetChanged();
            }
        });

        cardMaraton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), InscripcionMaratonActivity.class);
                startActivity(i);
            }
        });
    }

    private void resetear() {
        for (Categoria c : mCategorias) {
            c.setEstado(false);
        }
    }

    private void loadInfo() {

        PreferenceManager manager = new PreferenceManager(getContext());
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        if (id == 0)
            id = 1;
        String URL = String.format("%s?idU=%s&key=%s", Utils.URL_LISTA_NOTICIA, id, isLogin ? key : "1");
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
                dialog.dismiss();

            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(mFragmentManager, "dialog_process");
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void procesarRespuesta(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    loadInfo(jsonObject);
                    break;
                case 2:
                    Utils.showToast(getContext(), getString(R.string.noData));
                    //updateView(0);
                    break;
                case 3:
                    Utils.showToast(getContext(), getString(R.string.tokenInvalido));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getContext(), getString(R.string.errorInternoAdmin));
            //updateView(2);
        }
    }

    private void loadInfo(JSONObject jsonObject) {
        try {
            if (jsonObject.has("mensaje")) {

                JSONArray jsonArray = jsonObject.getJSONArray("mensaje");

                mListNoticias = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject o = jsonArray.getJSONObject(i);

                    Noticia noticia = Noticia.mapper(o, Noticia.COMPLETE);

                    mListNoticias.add(noticia);

                }
                if (mListNoticias.size() > 0) {
                    mNoticiasAdapter = new NoticiasAdapter(mListNoticias, getContext(), 0);
                    recyclerNoticias.setAdapter(mNoticiasAdapter);
                    recyclerCategorias.setVisibility(View.VISIBLE);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
            //updateView(2);
        }

    }


    private void loadCategorias() {
        mCategorias = new ArrayList<>();
        mCategorias.add(new Categoria(9, "Todo"));
        mCategorias.add(new Categoria(1, "Comedor Universitario"));
        mCategorias.add(new Categoria(2, "Deportes"));
        mCategorias.add(new Categoria(3, "Transporte"));
        mCategorias.add(new Categoria(4, "Becas"));
        mCategorias.add(new Categoria(5, "Residencia"));
        mCategorias.add(new Categoria(6, "Ciber Estudiantil"));
        mCategorias.add(new Categoria(7, "UPA"));
        mCategorias.add(new Categoria(8, "Polideportivo"));
        mCategorias.get(0).setEstado(true);
    }
}
