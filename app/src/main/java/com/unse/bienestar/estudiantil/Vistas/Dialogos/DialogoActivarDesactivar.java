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
import android.widget.LinearLayout;
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
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.Regularidad;
import com.unse.bienestar.estudiantil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogoActivarDesactivar extends DialogFragment {

    View view;
    Button btnOk;
    TextView txtTitulo, txtFecha, txtEstado, txtTituloPrin;
    SwitchCompat switchEstado;
    LinearLayout latEstado;
    Context mContext;
    DialogoProcesamiento dialog;
    FragmentManager mFragmentManager;
    Regularidad mRegularidad;
    Object mCredencial;
    int position, idUsuario;
    boolean isActive = false;
    OnClickListenerAdapter mOnClickListenerAdapter;

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

    public OnClickListenerAdapter getOnClickListenerAdapter() {
        return mOnClickListenerAdapter;
    }

    public void setOnClickListenerAdapter(OnClickListenerAdapter onClickListenerAdapter) {
        mOnClickListenerAdapter = onClickListenerAdapter;
    }


    public Object getCredencial() {
        return mCredencial;

    }

    public void setCredencial(CredencialDeporte credencial) {
        mCredencial = (CredencialDeporte) credencial;
    }

    public Regularidad getRegularidad() {
        return mRegularidad;
    }

    public void setRegularidad(Regularidad regularidad) {
        mRegularidad = regularidad;
    }

    public CredencialDeporte getCredencialDeporte() {
        try {
            return (CredencialDeporte) mCredencial;
        } catch (ClassCastException e) {
        }
        return null;

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialogo_estado_switch, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        return view;
    }

    private void loadListener() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListenerAdapter != null) {
                    mOnClickListenerAdapter.onClick(isActive ? 1 : 0);
                    dismiss();
                }
            }
        });
        switchEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar();
            }
        });
        latEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiar();
            }
        });
    }

    private void cambiar() {
        isActive = !isActive;
        PreferenceManager manager = new PreferenceManager(getContextDialog());
        final String key = manager.getValueString(Utils.TOKEN);
        final int id = manager.getValueInt(Utils.MY_ID);
        String URL = mRegularidad != null ? Utils.URL_REGULARIDAD_CAMBIAR : Utils.URL_CREDENCIAL_CAMBIAR;
                /*mRegularidad != null ?
                        String.format("%s?id=%s&key=%s&idU=%s&est=%s&idReg=%s",
                                Utils.URL_REGULARIDAD_CAMBIAR,
                                id, key, getIdUsuario(), isActive ? 1 : 0, mRegularidad.getIdRegularidad())
                        : !(mCredencial instanceof CredencialSocio) ?
                        String.format("%s?id=%s&key=%s&idC=%s&est=%s",
                                Utils.URL_CREDENCIAL_CAMBIAR, id, key, ((CredencialSocio) mCredencial).getId(), isActive ? 1 : 0)
                        : String.format("%s?id=%s&key=%s&idC=%s&est=%s",
                        Utils.URL_CREDENCIAL_SOCIO_CAMBIAR, id, key, ((CredencialSocio) mCredencial).getIdCredencialSocio(), isActive ? 1 : 0);*/
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuesta(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isActive = !isActive;
                Utils.showToast(getContextDialog(), getString(R.string.servidorOff));
                dialog.dismiss();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> datos = new HashMap<>();
                datos.put("key", key);
                datos.put("idU", String.valueOf(id));
                datos.put("val", String.valueOf(isActive ? 1 : 0));
                if (getCredencial() != null) {
                    if (getCredencialDeporte() != null) {
                        datos.put("ii", String.valueOf(getCredencialDeporte().getIdInscripcion()));
                        datos.put("aa", String.valueOf(getCredencialDeporte().getIdTemporada()));
                    }
                }
                //datos.put("iu", String.valueOf(getCredencialDeporte().getIdUsuario()));
                return datos;
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
                    isActive = !isActive;
                    break;
                case 1:
                    //Exito
                    Utils.showToast(getContextDialog(), mRegularidad != null ? getString(R.string.regularidadActualizada) :
                            getString(R.string.credencialActualizada));
                    updateReg(isActive);
                    break;
                case 2:
                    Utils.showToast(getContextDialog(), getString(R.string.regularidadNoCambiada));
                    isActive = !isActive;
                    break;
                case 3:
                    Utils.showToast(getContextDialog(), getString(R.string.tokenInvalido));
                    isActive = !isActive;
                    break;
                case 4:
                    Utils.showToast(getContextDialog(), getString(R.string.camposInvalidos));
                    isActive = !isActive;
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getContextDialog(), getString(R.string.tokenInexistente));
                    isActive = !isActive;
                    break;
            }

        } catch (JSONException e) {
            isActive = !isActive;
            e.printStackTrace();
            Utils.showToast(getContextDialog(), getString(R.string.errorInternoAdmin));
        }
    }

    private void updateReg(boolean reg) {
        if (reg) {
            txtEstado.setText("ACTIVADO");
            switchEstado.setChecked(true);
            Utils.changeColor(latEstado.getBackground(), getContextDialog(), R.color.colorGreen);
        } else {
            txtEstado.setText("DESACTIVADO");
            switchEstado.setChecked(false);
            Utils.changeColor(latEstado.getBackground(), getContextDialog(), R.color.colorPrimaryDark);
        }
    }


    private void loadData() {
        txtTituloPrin.setText(mRegularidad != null ? "Info Regularidad" : "Info Credencial");
        isActive = mRegularidad != null ? mRegularidad.getValidez() == 1 :
                getCredencialDeporte().getValidez() == 1;
        txtTitulo.setText(mRegularidad != null ?
                String.format("Regularidad #%s - %s", mRegularidad.getIdRegularidad(), mRegularidad.getAnio())
                : String.format("%s - %s", getCredencialDeporte().getNombre(),
                getCredencialDeporte().getIdTemporada()));
        updateReg(isActive);
        txtFecha.setText(Utils.getFechaFormat(mRegularidad != null ?
                mRegularidad.getFechaOtorg()
                : getCredencialDeporte().getFechaCreacion()));


    }

    private void loadViews() {
        txtTituloPrin = view.findViewById(R.id.txtTitulo2);
        txtEstado = view.findViewById(R.id.txtDescripcion);
        txtFecha = view.findViewById(R.id.txtFecha);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        switchEstado = view.findViewById(R.id.switchCred);
        latEstado = view.findViewById(R.id.latSwitch);
        btnOk = view.findViewById(R.id.btnAceptar);
    }
}