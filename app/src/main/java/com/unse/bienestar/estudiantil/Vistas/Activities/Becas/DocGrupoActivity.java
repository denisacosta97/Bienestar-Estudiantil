package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.GFamiliar;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoEnvioArchivos;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class DocGrupoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRec, btnSubir;
    ImageView btnBack;
    GFamiliar mGFamiliar;
    TextView txtDocumentacion;
    ImageView imgAddArchivo, imgArchivo;
    ArrayList<Archivo> mArchivos;
    int posicionArchivo = -1;
    Inscripcion mInscripcion;
    HashMap<String, Integer> cantidades = new HashMap<>();
    DialogoProcesamiento dialog;
    ArrayList<InfoBecas> mInfoBecas;
    LinearLayout layoutUpload, layoutPrevio;
    CardView cardModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_grupo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DATA_DOCUM) != null) {
            mGFamiliar = getIntent().getParcelableExtra(Utils.DATA_DOCUM);
        }

        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    private void loadViews() {
        btnRec = findViewById(R.id.btnRec);
        btnBack = findViewById(R.id.imgFlecha);
        btnSubir = findViewById(R.id.btnSubir);
        txtDocumentacion = findViewById(R.id.txtDocumentacion);
        imgAddArchivo = findViewById(R.id.imgAddArchivo);
        layoutPrevio = findViewById(R.id.layoutPrevio);
        layoutUpload = findViewById(R.id.layoutUpload);
        cardModificar = findViewById(R.id.cardModificar);
        imgArchivo = findViewById(R.id.imgArchivo);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Subir archivos");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {
        txtDocumentacion.setText(mGFamiliar.getNombre());
    }

    private void loadListener() {
        btnRec.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgAddArchivo.setOnClickListener(this);
        cardModificar.setOnClickListener(this);
        btnSubir.setOnClickListener(this);
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)) {
            loadFile();
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
                            ActivityCompat.requestPermissions(DocGrupoActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

    public void loadFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "Seleccionar archivo"), Utils.REQUEST_CHECK_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.REQUEST_CHECK_SETTINGS) {
            if(resultCode == Activity.RESULT_OK) {
                Uri result = data.getData();

                String extension = "";
                if (result.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    //If scheme is a content
                    final MimeTypeMap mime = MimeTypeMap.getSingleton();
                    extension = mime.getExtensionFromMimeType(getApplicationContext().getContentResolver().getType(result));
                    if (extension.toLowerCase().equals("pdf")){
                        updateView();
                        imgArchivo.setImageResource(R.drawable.ic_pdf);
                    } else {
                        updateView();
                        imgArchivo.setImageURI(result);
                    }
                } else { //Aquí pregunta si el archivo está en la memoria externa
                    extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(result.getPath())).toString());
                    updateView();
                    imgArchivo.setImageURI(result);

                    if (extension.toLowerCase().equals("pdf")){
                        updateView();
                        imgArchivo.setImageResource(R.drawable.ic_pdf);
                    }
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    } //onActivityResult

    private void updateView(){
        layoutUpload.setVisibility(View.GONE);
        layoutPrevio.setVisibility(View.VISIBLE);
    }

    private void procesarRespuesta(String response, int tipo) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case 1:
                    //Exito
                    if (tipo == 1) {
                        mArchivos.remove(posicionArchivo);
                        //checkFiles();
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoEliminado2));
                    } else if (tipo == 2) {
                        mInscripcion.setEstado(1);
                        mInscripcion.setEstadoDescripcion("EN EVALUACIÓN");
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionExitosa));
                        //updateView();
                    }
                    break;
                case 2:
                    if (tipo == 1)
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoErrorEliminar));
                    else if (tipo == 2)
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionErronea));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.archivoNoExiste));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private boolean isReady() {
        boolean isReady = true;
        for (Archivo archivo : mArchivos) {
            if (archivo.getValidez() == 0) {
                isReady = false;
                break;
            }
        }
        return isReady;
    }

    private void chequear() {
        boolean isReady = true, noLoad = false;
        for (Archivo a : mArchivos) {
            if (a.getValidez() == 0) {
                noLoad = true;
                if (a.getFile() == null)
                    isReady = false;
            }
        }
        if (noLoad) {
            if (isReady) {
                //Aqui envio
                DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                        .setTitulo(getString(R.string.advertencia))
                        .setDescripcion(getString(R.string.archivosEnviar))
                        .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                        .setIcono(R.drawable.ic_advertencia)
                        .setListener(new YesNoDialogListener() {
                            @Override
                            public void yes() {
                                sendFiles();
                            }

                            @Override
                            public void no() {

                            }
                        });
                DialogoGeneral dialogoGeneral = builder.build();
                dialogoGeneral.show(getSupportFragmentManager(), "dialog");

            } else {
                Utils.showToast(getApplicationContext(), getString(R.string.archivoNoSeleccionados));
            }

        } else {
            //Finaliza documentacion
            dialogoFin();
            //enviar();
        }

    }

    private void dialogoFin() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(R.string.advertencia))
                .setDescripcion(getString(R.string.inscripcionFinalizar))
                .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                .setIcono(R.drawable.ic_advertencia)
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        enviar();
                    }

                    @Override
                    public void no() {

                    }
                });
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialog");
    }

    private void enviar() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = Utils.URL_BECAS_INSCRIPCION_ACTUALIZAR;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));

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
                param.put("iu", String.valueOf(idLocal));
                param.put("an", String.valueOf(mInscripcion.getAnio()));
                param.put("de", "");
                param.put("es", String.valueOf(1));
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void sendFiles() {
        DialogoEnvioArchivos dialogoEnvioArchivos = new DialogoEnvioArchivos(new OnClickListenerAdapter() {
            @Override
            public void onClick(Object id) {
                if (!isReady()) {
                    Utils.showToast(getApplicationContext(), getString(R.string.archivosNoSubidos));
                }
                //checkFiles();
            }
        }, mArchivos, getApplicationContext(), this);
        dialogoEnvioArchivos.setInscripcion(mInscripcion);
        dialogoEnvioArchivos.show(getSupportFragmentManager(), "dialog");
    }

    /*@Override
    public void onBackPressed() {
        //Verifico si hay doc sin cargar
        if (!isReady()) {
            Utils.showToast(getApplicationContext(), getString(R.string.archivosPendiente));
        } else
            super.onBackPressed();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnRec:
                startActivity(new Intent(getApplicationContext(), RecomendacionesActivity.class));
                break;
            case R.id.imgAddArchivo:
            case R.id.cardModificar:
                checkPermission();
                break;
            case R.id.btnSubir:

                break;
        }
    }

}