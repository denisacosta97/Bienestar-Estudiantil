package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.PDF.DownloadPDF;
import com.unse.bienestar.estudiantil.Herramientas.PDF.LoadInfoPDF;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

public class InfoTurnoActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cardEstado;
    TextView txtTitulo, txtEstado, txtHorario, txtReceptor, txtFecha;
    Button btnCancelar, btnPDF;
    Turno mTurno;
    DialogoProcesamiento dialog;
    boolean change = false;
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_turno);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DATA_TURNO) != null) {
            mTurno = getIntent().getParcelableExtra(Utils.DATA_TURNO);
        }

        setToolbar();

        loadViews();

        loadData();

        loadListener();

        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    private void cancelar() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = Utils.URL_TURNO_CANCELAR;
        StringRequest requestImage = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();
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
                param.put("di", String.valueOf(mTurno.getDia()));
                param.put("me", String.valueOf(mTurno.getMes()));
                param.put("an", String.valueOf(mTurno.getAnio()));
                param.put("ir", String.valueOf(mTurno.getReceptor()));
                param.put("ho", mTurno.getFechaInicio());
                return param;
            }
        };
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
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
                    cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPink));
                    txtEstado.setText("CANCELADO");
                    btnCancelar.setEnabled(false);
                    mTurno.setEstado("CANCELADO");
                    change = true;
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoErrorCancelar));
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

    private void loadData() {

        txtTitulo.setText(mTurno.getTitulo());
        txtReceptor.setText(mTurno.getReceptorString());
        txtHorario.setText(mTurno.getFechaInicio());
        txtFecha.setText(String.format("%s/%s/%s", mTurno.getDia(), mTurno.getMes(), mTurno.getAnio()));
        txtEstado.setText(mTurno.getEstado());

        switch (mTurno.getEstado()) {
            case "PENDIENTE":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorOrange));
                break;
            case "CONFIRMADO":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
                break;
            case "AUSENTE":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                break;
            case "CANCELADO":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPink));
                break;
        }

        if (!mTurno.getEstado().equals("PENDIENTE")) {
            btnCancelar.setEnabled(false);
        }


    }

    private void loadListener() {
        btnPDF.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private void loadViews() {
        cardEstado = findViewById(R.id.cardEstado);
        txtTitulo = findViewById(R.id.txtDescripcion);
        txtEstado = findViewById(R.id.txtEstado);
        txtFecha = findViewById(R.id.txtFecha);
        txtHorario = findViewById(R.id.txtHoraIni);
        txtReceptor = findViewById(R.id.txtReceptor);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnPDF = findViewById(R.id.btnPDF);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnCancelar:
                cancelar();
                break;
            case R.id.btnPDF:
                downloadPDF();
                break;
        }
    }

    private void downloadPDF() {
        final Archivo archivo = new Archivo();
        archivo.setNombreArchivo(String.format("COMPROBANTE_TURNO_%s_%s.pdf",
                mTurno.getTitulo().replaceAll(" ","_").toUpperCase(),
                mTurno.getFecha().replaceAll("/", "_")));
        DownloadPDF downloadPDF = new DownloadPDF(getApplicationContext(), archivo.getNombreArchivo(),
                getSupportFragmentManager(), new YesNoDialogListener() {
            @Override
            public void yes() {
                completeFile(archivo);
            }

            @Override
            public void no() {
                Utils.showToast(getApplicationContext(), getString(R.string.archivosErrorDescarga));
            }

        }, true);
        String URL = String.format("%s%s", Utils.URL_ARCHIVO_TURNO, "COMPROBANTE_TURNO.pdf");
        downloadPDF.execute(URL);
    }

    public void completeFile(final Archivo archivo) {
        LoadInfoPDF loadInfoPDF = new LoadInfoPDF(getApplicationContext(), archivo, new YesNoDialogListener() {
            @Override
            public void yes() {
                openFile(archivo);
            }

            @Override
            public void no() {
                Utils.showToast(getApplicationContext(), getString(R.string.documentoNoCompletado));
            }

        }, getSupportFragmentManager());
        loadInfoPDF.setTurno(mTurno);
        loadInfoPDF.execute();
    }

    private void openFile(Archivo archivo) {

        File file = new File(Utils.getDirectoryPath(true, getApplicationContext()) + archivo.getNombreArchivo());

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Utils.showToast(getApplicationContext(), getString(R.string.archivoErrorNoAppPDF));

            }
        } else
            Utils.showToast(getApplicationContext(), getString(R.string.archivoEliminado));
    }

    @Override
    public void onBackPressed() {
        if (change)
            setResult(RESULT_OK);
        else setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Estado del Turno");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }
}
