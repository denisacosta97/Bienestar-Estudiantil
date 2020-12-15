package com.unse.bienestar.estudiantil.Vistas.Dialogos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.UploadManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleyMultipartRequest;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoEnvioArchivos extends DialogFragment {

    View view;
    OnClickListenerAdapter mListener;
    ArrayList<Archivo> archivos;
    Context mContext;
    ProgressBar mProgressBar, mProgressBarTotal;
    TextView txtNombre;
    Activity mActivity;
    Inscripcion mInscripcion;

    int progres = -1, dni = 0;

    public DialogoEnvioArchivos(OnClickListenerAdapter listener, ArrayList<Archivo> archivos, Context context, Activity activity) {
        mListener = listener;
        this.archivos = archivos;
        mContext = context;
        mActivity = activity;
    }

    public Inscripcion getInscripcion() {
        return mInscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        mInscripcion = inscripcion;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialogo_envio_archivos, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Esto es lo nuevoooooooo, evita los bordes cuadrados
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        return view;
    }

    private void loadViews() {
        txtNombre = view.findViewById(R.id.txtDescripcion);
        mProgressBar = view.findViewById(R.id.progres);
        mProgressBarTotal = view.findViewById(R.id.progresTotal);
    }

    private void loadData() {
        PreferenceManager manager = new PreferenceManager(mContext);
        dni = manager.getValueInt(Utils.MY_ID);
        checkAndSend();
    }

    private void send(int number) {
        HashMap<String, String> datos = new HashMap<>();
        datos.put("na", archivos.get(number).getNombreArchivo(false));
        datos.put("iu", String.valueOf(dni));
        datos.put("ia", String.valueOf(archivos.get(number).getId()));
        datos.put("ib", String.valueOf(mInscripcion.getIdBeca()));
        datos.put("an", String.valueOf(mInscripcion.getAnio()));
        VolleyMultipartRequest.DataPart dataPart = new VolleyMultipartRequest.DataPart(
                archivos.get(number).getFileName(mActivity),
                Utils.getFileDataFromUri(mContext, archivos.get(number).getFile())
        );
        VolleyMultipartRequest volleyMultipartRequest = new UploadManager.Builder(mContext)
                .setMetodo(Request.Method.POST)
                .setURL(Utils.URL_BECAS_DOCUMENTACION)
                .setOkListener(new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        procesarRespuesta(new String(response.data));
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkAndSend();
                    }
                })
                .setDato(dataPart)
                .setTipoDato(UploadManager.FILE)
                .setParams(datos)
                .build();
        VolleySingleton.getInstance(mContext).addToRequestQueue(volleyMultipartRequest);
    }

    private void procesarRespuesta(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case 1:
                    //Exito
                    archivos.get(progres).setValidez(1);
                    String nombre = jsonObject.getString("id");
                    archivos.get(progres).setNombreArchivo(nombre);
                    break;
                case 2:
                case 3:
                    archivos.get(progres).setValidez(0);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            archivos.get(progres).setValidez(0);
            Utils.showToast(mContext, getString(R.string.errorInternoAdmin));
        }
        checkAndSend();
    }

    private void checkAndSend() {
        try {
            do {
                progres++;
            } while (archivos.get(progres).getValidez() == 1);
        } catch (Exception e) {

        }

        if (archivos.size() <= progres) {
            mListener.onClick(archivos);
            dismiss();
        } else {
            updateProgres(progres);
            send(progres);
        }
    }

    private void updateProgres(int i) {
        txtNombre.setText(String.format("Enviando: %s - %s", archivos.get(i).getNombre(), archivos.get(i).getFileName(mActivity)));
        int porc = ((i + 1) * 100) / archivos.size();
        mProgressBarTotal.setProgress(porc);
    }


    private void loadListener() {


    }

}

