package com.unse.bienestar.estudiantil.Vistas.Dialogos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.TipoFamiliar;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogoAgregarFamiliarBeca extends DialogFragment {

    View view;
    Button btnAceptar;
    EditText edtNombre;
    TextView txtFamiliar;
    Context mContext;
    DialogoProcesamiento dialog;
    FragmentManager mFragmentManager;
    TipoFamiliar familiar;
    OnClickListenerAdapter mListener;
    Inscripcion mInscripcion;

    public OnClickListenerAdapter getListener() {
        return mListener;
    }

    public void setListener(OnClickListenerAdapter listener) {
        mListener = listener;
    }

    public Inscripcion getInscripcion() {
        return mInscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        mInscripcion = inscripcion;
    }

    public TipoFamiliar getFamiliar() {
        return familiar;
    }

    public void setFamiliar(TipoFamiliar familiar) {
        TipoFamiliar fam = new TipoFamiliar(familiar.getId(), familiar.getNombre());
        this.familiar = fam;
    }

    public Context getContextDialog() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public FragmentManager getFragmentManagerDialog() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialogo_agregar_familiar_beca, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        return view;
    }

    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString();
                if (nombre.equals(""))
                    nombre = familiar.getNombre();
                else
                    nombre = String.format("%s - %s", familiar.getNombre(), nombre);
                familiar.setNombre(nombre);
                insertar(nombre);
            }
        });
    }

    private void insertar(final String nombre) {
        PreferenceManager manager = new PreferenceManager(getContextDialog());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = Utils.URL_INSERT_FAMILIAR;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                Utils.showToast(getContextDialog(), getString(R.string.servidorOff));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("key", key);
                param.put("idU", String.valueOf(idLocal));
                param.put("ib", String.valueOf(mInscripcion.getIdBeca()));
                param.put("iu", String.valueOf(mInscripcion.getIdUsuario()));
                param.put("an", String.valueOf(mInscripcion.getAnio()));
                param.put("if", String.valueOf(familiar.getId()));
                param.put("no", nombre);
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getFragmentManagerDialog(), "dialog_process");
        VolleySingleton.getInstance(getContextDialog()).addToRequestQueue(request);
    }

    private void procesarRespuesta(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getContextDialog(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    Utils.showToast(getContextDialog(), getString(R.string.agregado));
                    mListener.onClick(familiar);
                    dismiss();
                    break;
                case 2:
                    Utils.showToast(getContextDialog(), getString(R.string.yaExisteFamiliar));
                    break;
                case 3:
                    Utils.showToast(getContextDialog(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getContextDialog(), getString(R.string.camposInvalidos));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getContextDialog(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getContextDialog(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadData() {
        txtFamiliar.setText(familiar.getNombre());
    }

    private void loadViews() {
        edtNombre = view.findViewById(R.id.edtNombre);
        txtFamiliar = view.findViewById(R.id.txtFamiliar);
        btnAceptar = view.findViewById(R.id.btnAceptar);
    }

}
