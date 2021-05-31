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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.FileUtil;
import com.unse.bienestar.estudiantil.Herramientas.UploadManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleyMultipartRequest;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.TipoFamiliar;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class SubirDocumentacionFamiliarActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRec, btnSubir;
    EditText edtDescripcion;
    ImageView btnBack;
    Documentacion documentacion;
    TextView txtDocumentacion;
    ImageView imgAddArchivo, imgArchivo;
    ArrayList<Archivo> mArchivos;
    Archivo archivo;
    LinearLayout latPDF;
    int posicionArchivo = -1;
    Inscripcion mInscripcion;
    DialogoProcesamiento dialog;
    LinearLayout layoutUpload, layoutPrevio;
    CardView cardModificar;
    String nombreArchivo = "";
    Uri uriFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_grupo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.DATA_DOCUM) != null) {
            documentacion = getIntent().getParcelableExtra(Utils.DATA_DOCUM);
        }

        if (getIntent().getParcelableExtra(Utils.ARCHIVO_NAME) != null) {
            archivo = getIntent().getParcelableExtra(Utils.ARCHIVO_NAME);
        }

        loadViews();

        setToolbar();

        loadData();

        loadListener();
    }

    private void loadViews() {
        edtDescripcion = findViewById(R.id.edtDesc);
        latPDF = findViewById(R.id.latPDF);
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
        ((TextView) findViewById(R.id.txtTitulo)).setText("Subir archivo");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    public void updateButton() {
        if (uriFile == null)
            btnSubir.setEnabled(false);
        else btnSubir.setEnabled(true);
        if (documentacion.getUrl() != null)
            if (uriFile == null)
                btnSubir.setEnabled(false);
            else btnSubir.setEnabled(true);
    }

    private void loadData() {
        if (documentacion.getObservacion() != null)
            edtDescripcion.setText(documentacion.getObservacion());
        if (documentacion.getUrl() != null) {
            updateView();
            if (!documentacion.getUrl().contains(".pdf"))
                Glide.with(imgArchivo.getContext())
                        .applyDefaultRequestOptions(new RequestOptions()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)).load(documentacion.getUrl()).into(imgArchivo);
            else
                Glide.with(imgArchivo.getContext()).load(R.drawable.ic_pdf).into(imgArchivo);
        }
        txtDocumentacion.setText(String.format("%s - %S", archivo.getNombre(), documentacion.getDescripcion()));
        updateButton();
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        int edit = preferenceManager.getValueInt(Utils.IS_EDIT_MODE);
        if (edit == 0) {
            btnSubir.setEnabled(false);
            cardModificar.setVisibility(View.GONE);
            cardModificar.setEnabled(false);
            edtDescripcion.setEnabled(false);
            imgAddArchivo.setEnabled(false);
            imgArchivo.setEnabled(false);
        }

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
                            Utils.showPermission(SubirDocumentacionFamiliarActivity.this);

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
            if (resultCode == Activity.RESULT_OK) {
                Uri result = data.getData();

                Cursor cursor = getContentResolver().query(result, null, null, null, null);
                int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                cursor.moveToFirst();
                long size = cursor.getLong(index);
                if (size <= (5 * (1024 * 1024))) {
                    String extension = "";
                    if (result.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                        //If scheme is a content
                        final MimeTypeMap mime = MimeTypeMap.getSingleton();
                        extension = mime.getExtensionFromMimeType(getApplicationContext().getContentResolver().getType(result));
                    } else { //Aquí pregunta si el archivo está en la memoria externa
                        extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(result.getPath())).toString());
                    }

                    extension = extension.toLowerCase();
                    nombreArchivo = FileUtil.getFileName(getApplicationContext(), result);
                    uriFile = result;
                    if (extension.equals("pdf") || extension.equals("jpg") | extension.equals("jpeg")
                            || extension.equals("png")) {
                        updateView();
                        updateButton();
                        if (extension.equals("pdf")) {
                            Glide.with(imgArchivo.getContext()).load(R.drawable.ic_pdf).into(imgArchivo);
                        } else {
                            Glide.with(imgArchivo.getContext()).load(result).into(imgArchivo);
                        }

                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.imagenNoFormat));

                    }

                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.archivoGrande2));
                }


            }
        }
    }


    private void updateView() {
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
                        mInscripcion.setEstadoDescripcion("RECIBIDO");
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionExitosa));
                        //updateView();
                    } else if (tipo == 3) {
                        Intent intent = new Intent();
                        String url = jsonObject.getString("id");
                        documentacion.setUrl(url);
                        documentacion.setObservacion(edtDescripcion.getText().toString());
                        documentacion.setValidez(1);
                        intent.putExtra(Utils.DATA_DOCUM, documentacion);
                        setResult(Activity.RESULT_OK, intent);
                        Utils.showToast(getApplicationContext(), getString(R.string.documentoSubido));
                        finish();
                    }
                    break;
                case 2:
                    if (tipo == 1)
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoErrorEliminar));
                    else if (tipo == 2)
                        Utils.showToast(getApplicationContext(), getString(R.string.inscripcionErronea));
                    else if (tipo == 3)
                        Utils.showToast(getApplicationContext(), getString(R.string.archivoError));
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

    private void send() {
        String descrip = edtDescripcion.getText().toString();
        if (descrip.equals(""))
            descrip = " ";
        HashMap<String, String> datos = new HashMap<>();
        datos.put("iu", String.valueOf(documentacion.getIdUsuario()));
        datos.put("if", String.valueOf(documentacion.getIdFamiliar()));
        datos.put("de", documentacion.getDescripcion());
        datos.put("obs", descrip);
        datos.put("ia", String.valueOf(archivo.getId()));
        datos.put("ib", String.valueOf(documentacion.getIdBeca()));
        datos.put("an", String.valueOf(documentacion.getAnio()));
        datos.put("na", archivo.getNombreArchivo(false));
        try {
            VolleyMultipartRequest.DataPart dataPart = new VolleyMultipartRequest.DataPart(
                    nombreArchivo,
                    Utils.getFileDataFromUri(getApplicationContext(), uriFile)
            );
            VolleyMultipartRequest volleyMultipartRequest = new UploadManager.Builder(getApplicationContext())
                    .setMetodo(Request.Method.POST)
                    .setURL(Utils.URL_BECAS_DOCUMENTACION)
                    .setOkListener(new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            procesarRespuesta(new String(response.data), 3);
                        }
                    })
                    .setErrorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            error.printStackTrace();
                            Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                        }
                    })
                    .setDato(dataPart)
                    .setTipoDato(UploadManager.FILE)
                    .setParams(datos)
                    .build();
            dialog = new DialogoProcesamiento();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog_process");
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(volleyMultipartRequest);
        } catch (Exception e) {
            Utils.showToast(getApplicationContext(), getString(R.string.archivoMalo));
        }


    }


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
                send();
                break;
        }
    }

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            latPDF.removeView(imageView);
                            File file = null;
                            try {
                                file = FileUtil.from(getApplicationContext(), result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final File f = file;
                            imageView = new SubsamplingScaleImageView(getApplicationContext());
                            imageView.setMinimumTileDpi(120);
                            try{
                                imageView.setBitmapDecoderFactory(new DecoderFactory<ImageDecoder>() {
                                    @Override
                                    public ImageDecoder make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
                                        return new PDFDecoder(1, f, 8);
                                    }
                                });
                                imageView.setRegionDecoderFactory(new DecoderFactory<ImageRegionDecoder>() {
                                    @Override
                                    public ImageRegionDecoder make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
                                        return new PDFRegionDecoder(1, f, 8);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            ImageSource source = ImageSource.uri(file.getAbsolutePath());
                            imageView.setImage(source);

                            latPDF.invalidate();
                            latPDF.addView(imageView);

                            //layoutPrevio.findViewById(R.id.imgArchivo).setVisibility(View.GONE);
                        } else {
                            layoutPrevio.findViewById(R.id.imgArchivo).setVisibility(View.VISIBLE);
                            latPDF.setVisibility(View.GONE);
                            Glide.with(imgArchivo.getContext()).load(R.drawable.ic_pdf).into(imgArchivo);
                        }*/


}