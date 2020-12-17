package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.DocumentacionAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoEnvioArchivos;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class CargarDocumentacionActivity extends AppCompatActivity implements View.OnClickListener {

    CardView btnAgregar, btnConsultar;
    Button btnCargar, btnRec;
    ImageView btnBack;
    ArrayList<Documentacion> mDocumentacions;
    ArrayList<Archivo> mArchivos;
    ArrayList<Opciones> tipos;
    TextView txtEstado, txtAnio, txtFecha, txtNoData, txtObservacion;
    LinearLayout latDatos;
    ArrayList<InfoBecas> mInfoBecas;
    HashMap<String, Integer> cantidades = new HashMap<>();

    int posicionArchivo = -1;
    Inscripcion mInscripcion;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycler;
    DocumentacionAdapter mAdapter;
    DialogoProcesamiento dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_documentacion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.INFO_EXTRA) != null) {
            mInscripcion = getIntent().getParcelableExtra(Utils.INFO_EXTRA);
        }
        if (getIntent().getSerializableExtra(Utils.INFO_EXTRA_2) != null) {
            mArchivos = (ArrayList<Archivo>) getIntent().getSerializableExtra(Utils.INFO_EXTRA_2);
        }

        if (getIntent().getSerializableExtra(Utils.NOTICIA_INFO) != null) {
            mDocumentacions = (ArrayList<Documentacion>) getIntent().getSerializableExtra(Utils.NOTICIA_INFO);
        }

        if (mInscripcion != null) {

            loadViews();

            setToolbar();

            loadData();

            loadListener();
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
            finish();
        }

    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Documentación");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    OnClickListenerAdapter agregar = new OnClickListenerAdapter() {
        @Override
        public void onClick(Object id) {
            checkPermission();
            posicionArchivo = Integer.parseInt(String.valueOf(id));


        }
    };

    OnClickListenerAdapter borrar = new OnClickListenerAdapter() {
        @Override
        public void onClick(Object id) {
            checkDelete(Integer.parseInt(String.valueOf(id)));
        }
    };

    private void checkDelete(int id) {
        posicionArchivo = id;
        if (mArchivos.get(id).getValidez() == 0) {
            mArchivos.remove(id);

            mAdapter.notifyDataSetChanged();
            checkFiles();

        } else {
            dialogoBorrar();
        }
    }

    private void dialogoBorrar() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(R.string.permisoNecesario))
                .setIcono(R.drawable.ic_advertencia)
                .setDescripcion(getString(R.string.archivoEliminar))
                .setTipo(DialogoGeneral.TIPO_SI_NO)
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        delete();
                    }

                    @Override
                    public void no() {
                    }
                });
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialogo_permiso");
    }

    private void delete() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        final int id = preferenceManager.getValueInt(Utils.MY_ID);
        final String token = preferenceManager.getValueString(Utils.TOKEN);
        String URL =
                Utils.URL_BECAS_DOCUMENTACION_ELIMINAR;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuesta(response, 1);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                param.put("idU", String.valueOf(id));
                param.put("ib", String.valueOf(mInscripcion.getIdBeca()));
                param.put("an", String.valueOf(mInscripcion.getAnio()));
                param.put("iu", String.valueOf(id));
                param.put("na", String.valueOf(mArchivos.get(posicionArchivo).getNombreArchivo(true)));
                param.put("key", token);
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
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
                        mAdapter.notifyDataSetChanged();
                        checkFiles();
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoEliminado2));
                    } else if (tipo == 2) {
                        mInscripcion.setEstado(1);
                        mInscripcion.setEstadoDescripcion("EN EVALUACIÓN");
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionExitosa));
                        updateView();
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
                            ActivityCompat.requestPermissions(CargarDocumentacionActivity.this,
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

    private void loadData() {
        txtAnio.setText(String.valueOf(mInscripcion.getAnio()));
        txtFecha.setText(mInscripcion.getFechaModificacion());
        txtEstado.setText(mInscripcion.getEstadoDescripcion());

        if (mInscripcion.getDescripcion() != null && !mInscripcion.getDescripcion().equals("")) {
            latDatos.setVisibility(View.VISIBLE);
            txtObservacion.setText(mInscripcion.getDescripcion());
        } else {
            latDatos.setVisibility(View.GONE);
            txtObservacion.setText("");
        }


        tipos = new ArrayList<>();
        for (Documentacion d : mDocumentacions) {
            tipos.add(new Opciones(d.getNombre()));
        }

        for (Archivo archivo : mArchivos) {
            Pattern pattern = Pattern.compile("[0-9]+");
            String nombre = archivo.getNombreArchivo(false);
            nombre = nombre.substring(nombre.indexOf("_") + 1);
            Matcher matcher = pattern.matcher(nombre);
            if (matcher.find()) {
                String num = matcher.group();
                checkCantidades(num, archivo.getDescripcion());
            }

        }

        mAdapter = new DocumentacionAdapter(mArchivos, agregar, borrar, this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recycler.setNestedScrollingEnabled(true);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setAdapter(mAdapter);

        checkFiles();

        updateView();

        mInfoBecas = new ArrayList<>();
        mInfoBecas.add(new InfoBecas(1, R.drawable.ic_becas, "Beca Comedor", getString(R.string.desc0), "pdf_comedor", getString(R.string.reqAcad0), getString(R.string.reqGen0)));
        mInfoBecas.add(new InfoBecas(2, R.drawable.ic_becas, "Beca Estímulo al Deporte", getString(R.string.desc1), "pdf_estimulo_deporte", getString(R.string.reqAcad1), getString(R.string.reqGen1)));
        mInfoBecas.add(new InfoBecas(3, R.drawable.ic_becas, "Beca Movilidad", getString(R.string.desc2), "pdf_movilidad", getString(R.string.reqAcad2), getString(R.string.reqGen2)));
        mInfoBecas.add(new InfoBecas(4, R.drawable.ic_becas, "Beca Residencia", getString(R.string.desc3), "pdf_residencia", getString(R.string.reqAcad3), getString(R.string.reqGen3)));
        mInfoBecas.add(new InfoBecas(5, R.drawable.ic_becas, "Beca Finalización de Estudios de Grado", getString(R.string.desc4), "pdf_finalizacion_estud", getString(R.string.reqAcad4), getString(R.string.reqGen4)));
        mInfoBecas.add(new InfoBecas(6, R.drawable.ic_becas, "Beca para el Apoyo al Ingreso y Permanencia de los Estudiantes en la UNSE", getString(R.string.desc5), "pdf_apoyo", getString(R.string.reqAcad5), getString(R.string.reqGen5)));
        mInfoBecas.add(new InfoBecas(7, R.drawable.ic_becas, "Beca de Apoyo Económico", getString(R.string.desc6), "pdf_apoyo_econ", getString(R.string.reqAcad6), getString(R.string.reqGen6)));
        mInfoBecas.add(new InfoBecas(8, R.drawable.ic_becas, "Beca Estímulo al Mérito Académico", getString(R.string.desc7), "pdf_estim_acad", getString(R.string.reqAcad7), getString(R.string.reqGen7)));


    }

    private void updateView() {
        if (mInscripcion.getEstado() != 9) {
            btnAgregar.setVisibility(View.GONE);
            btnConsultar.setVisibility(View.GONE);
            btnCargar.setVisibility(View.GONE);
            txtEstado.setText(mInscripcion.getEstadoDescripcion());
            recycler.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText("Documentación en proceso de revisión\n\n");

        } else {
            btnAgregar.setVisibility(View.VISIBLE);
            btnConsultar.setVisibility(View.VISIBLE);
        }
    }

    private void checkCantidades(String num, String nombreArchivo) {
        if (cantidades.containsKey(nombreArchivo)) {
            int n = cantidades.get(nombreArchivo);
            if (Integer.parseInt(num) > n)
                cantidades.put(nombreArchivo, Integer.parseInt(num));
        } else {
            cantidades.put(nombreArchivo, Integer.parseInt(num));
        }
    }

    private void loadListener() {
        btnBack.setOnClickListener(this);
        btnCargar.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnRec.setOnClickListener(this);

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
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uploadfileuri = data.getData();
                    Cursor cursor = getContentResolver().query(uploadfileuri, null, null, null, null);
                    int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                    int index2 = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    long size = cursor.getLong(index);
                    if (size <= (5 * (1024 * 1024))) {
                        String ext = cursor.getString(index2);
                        ext = ext.substring(ext.indexOf(".") + 1);
                        if ((ext.toLowerCase().equals("pdf")
                                || ext.toLowerCase().equals("jpg")
                                || ext.toLowerCase().equals("jpeg")
                                || ext.toLowerCase().equals("png")
                        )
                        ) {
                            if (posicionArchivo != -1) {
                                mArchivos.get(posicionArchivo).setFile(uploadfileuri);
                                mArchivos.get(posicionArchivo).setValidez(0);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(getApplicationContext(), getString(R.string.archivosInvalido));
                        }

                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoGrande2));
                    }


                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.errorArchivo));
                }
            } else {
                Utils.showToast(getApplicationContext(), getString(R.string.errorArchivo));
            }
        }
    }

    private void loadViews() {
        btnBack = findViewById(R.id.imgFlecha);
        latDatos = findViewById(R.id.latDatos);
        txtObservacion = findViewById(R.id.txtObs);
        txtNoData = findViewById(R.id.txtCuerpo);
        txtAnio = findViewById(R.id.txtDesc);
        txtEstado = findViewById(R.id.txtEstado);
        txtFecha = findViewById(R.id.txtFecha);
        btnCargar = findViewById(R.id.btnCargar);
        recycler = findViewById(R.id.recycler);
        btnConsultar = findViewById(R.id.cardConsultar);
        btnAgregar = findViewById(R.id.cardAgregar);
        btnRec = findViewById(R.id.btnRec);
    }

    private void openDialogArchivos() {
        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
            @Override
            public void onClick(int pos) {
                Documentacion documentacion = mDocumentacions.get(pos);
                int number = getNumber(documentacion);
                mArchivos.add(0, new Archivo(documentacion.getCodigo(),
                        String.format("%s_%s", documentacion.getNombreArchivo(), number),
                        documentacion.getNombre()
                ));

                mAdapter.notifyDataSetChanged();
                checkFiles();
            }
        }, tipos, getApplicationContext());
        dialogoOpciones.show(getSupportFragmentManager(), "archivos");
    }

    private int getNumber(Documentacion documentacion) {
        if (!cantidades.containsKey(documentacion.getNombreArchivo())) {
            cantidades.put(documentacion.getNombreArchivo(), 1);
            return 1;
        } else {
            cantidades.put(documentacion.getNombreArchivo(),
                    cantidades.get(documentacion.getNombreArchivo()) + 1);
            return cantidades.get(documentacion.getNombreArchivo());
        }
    }

    private void checkFiles() {
        if (mArchivos.size() > 0) {
            txtNoData.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }

        if (isReady() && mArchivos.size() > 0) {
            btnCargar.setText("FINALIZAR INSCRIPCIÓN");
        } else {
            btnCargar.setText("ENVIAR DOCUMENTACIÓN");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.cardAgregar:
                openDialogArchivos();
                break;
            case R.id.cardConsultar:
                openDialogDatos();
                break;
            case R.id.btnCargar:
                //chequear documentación
                chequear();
                break;
            case R.id.btnRec:
                startActivity(new Intent(getApplicationContext(), RecomendacionesActivity.class));
                break;
        }

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
            enviar();
        }

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
                mAdapter.notifyDataSetChanged();
                if (!isReady()) {
                    Utils.showToast(getApplicationContext(), getString(R.string.archivosNoSubidos));
                }
                checkFiles();
            }
        }, mArchivos, getApplicationContext(), this);
        dialogoEnvioArchivos.setInscripcion(mInscripcion);
        dialogoEnvioArchivos.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onBackPressed() {
        //Verifico si hay doc sin cargar
        if (!isReady()) {
            Utils.showToast(getApplicationContext(), getString(R.string.archivosPendiente));
        } else
            super.onBackPressed();
    }

    private void openDialogDatos() {
        Intent intent = new Intent(getApplicationContext(), PerfilBecasActivity.class);
        intent.putExtra(Utils.BECA_NAME, mInfoBecas.get(mInscripcion.getIdBeca() -1));
        intent.putExtra(Utils.IS_EDIT_MODE, true);
        startActivity(intent);
    }

}