package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.PDF.DownloadPDF;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class InfoTurnoActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cardEstado;
    TextView txtTitulo, txtEstado, txtHorario, txtReceptor, txtFecha, txtFechaRegistro, txtOpcion;
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

    private void loadData() {

        if (mTurno.getTipo() == Turno.TIPO_BECA) {
            txtOpcion.setText("Receptor:");
            txtReceptor.setText(mTurno.getReceptorString());
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_MEDICAMENTO) {
            txtOpcion.setText("Tipo de Medicamento:");
            txtReceptor.setText(mTurno.getMedicamentos(Integer.parseInt(mTurno.getDescripcion())));
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_TURNOS) {
            txtOpcion.setText("Especialidad:");
            txtReceptor.setText(mTurno.getDescripcion());
        }

        txtTitulo.setText(mTurno.getTitulo());
        txtHorario.setText(mTurno.getFechaInicio());
        txtFechaRegistro.setText(mTurno.getFechaRegistro());
        txtFecha.setText(String.format("%02d/%02d/%s", mTurno.getDia(), mTurno.getMes(), mTurno.getAnio()));
        txtEstado.setText(mTurno.getEstado());

        switch (mTurno.getEstado()) {
            case "PENDIENTE":
            case "RESERVADO":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorOrange));
                break;
            case "CONFIRMADO":
            case "RETIRADO":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGreen));
                break;
            case "AUSENTE":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                break;
            case "CANCELADO":
                cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPink));
                break;
        }

        if (mTurno.getEstado().equals("PENDIENTE") || mTurno.getEstado().equals("RESERVADO")) {
            btnCancelar.setEnabled(true);
        } else {
            btnCancelar.setEnabled(false);
        }


    }

    private void loadListener() {
        btnPDF.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private void loadViews() {
        txtFechaRegistro = findViewById(R.id.txtFechaRegistro);
        txtOpcion = findViewById(R.id.txtOpcion);
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
                preguntar();
                break;
            case R.id.btnPDF:
                checkPermission();
                break;
        }
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)) {
            generatePDF();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_GROUP_PERMISSIONS_LOCATION);
            Utils.showToast(getApplicationContext(), "Por favor, autoriza el permiso de almacenamiento");

        } else {
            DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                    .setTitulo(getString(R.string.permisoNecesario))
                    .setIcono(R.drawable.ic_advertencia)
                    .setDescripcion(getString(R.string.permisoAlmacenamiento))
                    .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                    .setListener(new YesNoDialogListener() {
                        @Override
                        public void yes() {
                            ActivityCompat.requestPermissions(InfoTurnoActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_ALL);
                        }

                        @Override
                        public void no() {
                        }
                    });
            DialogoGeneral dialogoGeneral = builder.build();
            dialogoGeneral.show(getSupportFragmentManager(), "dialogo_permiso");
        }
    }

    private void generatePDF() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = Utils.URL_GENERATE_PDF;
        StringRequest requestImage = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 2);

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
                param.put("iu", String.valueOf(idLocal));
                if (mTurno.getTipo() == Turno.TIPO_UPA_MEDICAMENTO) {
                    param.put("tipo", String.valueOf(2));
                    param.put("di", String.valueOf(mTurno.getDia()));
                    param.put("me", String.valueOf(mTurno.getMes()));
                    param.put("an", String.valueOf(mTurno.getAnio()));
                    param.put("ho", String.valueOf(mTurno.getFechaInicio()));
                } else if (mTurno.getTipo() == Turno.TIPO_UPA_TURNOS) {
                    param.put("tipo", String.valueOf(3));
                    param.put("it", String.valueOf(mTurno.getId()));
                } else {
                    param.put("tipo", String.valueOf(1));
                    param.put("di", String.valueOf(mTurno.getDia()));
                    param.put("me", String.valueOf(mTurno.getMes()));
                    param.put("an", String.valueOf(mTurno.getAnio()));
                    param.put("ho", String.valueOf(mTurno.getFechaInicio()));
                    param.put("rec", String.valueOf(mTurno.getReceptor()));
                }
                return param;
            }
        };
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void preguntar() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTipo(DialogoGeneral.TIPO_SI_NO)
                .setTitulo(getString(R.string.advertencia))
                .setDescripcion(getString(R.string.becaTurnoCancelar))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        cancelar();
                    }

                    @Override
                    public void no() {

                    }
                });
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialog");
    }

    private void cancelar() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = "";
        if (mTurno.getTipo() == Turno.TIPO_BECA) {
            URL = Utils.URL_TURNO_CANCELAR;
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_MEDICAMENTO) {
            URL = Utils.URL_MEDICAM_CANCELAR;
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_TURNOS) {
            URL = Utils.URL_TURNO_UPA_CANCELAR;
        }
        StringRequest requestImage = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response,1);

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
                if (mTurno.getTipo() == Turno.TIPO_UPA_MEDICAMENTO) {
                    param.put("es", String.valueOf(2));
                    param.put("iu", String.valueOf(idLocal));
                    param.put("fr", String.valueOf(mTurno.getFechaRegistro()));
                } else if (mTurno.getTipo() == Turno.TIPO_UPA_TURNOS) {
                    param.put("es", String.valueOf(2));
                    param.put("it", String.valueOf(mTurno.getId()));
                } else {
                    param.put("di", String.valueOf(mTurno.getDia()));
                    param.put("me", String.valueOf(mTurno.getMes()));
                    param.put("re", String.valueOf(mTurno.getReceptor()));
                    param.put("an", String.valueOf(mTurno.getAnio()));
                    param.put("ir", String.valueOf(mTurno.getReceptor()));
                    param.put("ho", mTurno.getFechaInicio());
                }
                return param;
            }
        };
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void procesarRespuesta(String response, int tipo) {
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
                    if (tipo == 1) {
                        cardEstado.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPink));
                        txtEstado.setText("CANCELADO");
                        btnCancelar.setEnabled(false);
                        mTurno.setEstado("CANCELADO");
                        change = true;
                    } else if (tipo == 2) {
                        String name = jsonObject.getString("archivo");
                        downloadPDF(name);
                    }

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

    private void downloadPDF(String name) {
        final Archivo archivo = new Archivo();
        Date date = new Date(System.currentTimeMillis());
        String fecha = Utils.getFechaName(date);
        String nombre = null;
        if (mTurno.getTipo() == Turno.TIPO_BECA) {
            nombre = "T_BECAS";
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_MEDICAMENTO) {
            nombre = "T_MEDICAM";
        } else if (mTurno.getTipo() == Turno.TIPO_UPA_TURNOS) {
            nombre = "T_UAPU";
        }
        archivo.setNombreArchivo(String.format("%s_%s",nombre, fecha));
        DownloadPDF downloadPDF = new DownloadPDF(getApplicationContext(), archivo.getNombreArchivo(true),
                getSupportFragmentManager(), new YesNoDialogListener() {
            @Override
            public void yes() {
                openFile(archivo);
            }

            @Override
            public void no() {
                Utils.showToast(getApplicationContext(), getString(R.string.archivosErrorDescarga));
            }

        }, true);
        String URL = String.format("%s%s", Utils.URL_PDF_TURNO, name);
        downloadPDF.execute(URL);
    }

    private void openFile(Archivo archivo) {

        File file = new File(Utils.getDirectoryPath(true, getApplicationContext()) + archivo.getNombreArchivo(true));

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
