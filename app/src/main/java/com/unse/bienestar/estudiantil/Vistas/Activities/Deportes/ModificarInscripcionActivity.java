package com.unse.bienestar.estudiantil.Vistas.Activities.Deportes;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.Validador;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.Deporte;
import com.unse.bienestar.estudiantil.Modelos.Estado;
import com.unse.bienestar.estudiantil.Modelos.Inscripcion;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoActivarDesactivar;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoListaEstados;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ModificarInscripcionActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText edtDni, edtNombre, edtApellido, edtEdad, edtBarrio, edtLocalidad, edtProvincia,
            edtPais, edtDomicilio, edtMail, edtAnioIngreso, edtMaterias, edtFace, edtInsta, edtPeso,
            edtAltura, edtTelefono, edtLugar, edtObj, edtCuales, edtLegajo, edtCarrera, edtFacultad,
            edtIMC, edtIMCEstado;
    EditText[] campos, noEditables;
    CheckBox[] noCheck;
    FloatingActionButton fabEditar;
    CheckBox chIsWsp, echSiActividad, chNoActividad, chBaja, chMedia, chAlta;
    LinearLayout linearActividades, linearAdmin, linearCredencial, latIMC, latIMCEstado;
    TextView txtTitulo, txtDeporte, edtFechaNac, txtFechaIns, txtFechaModi;
    TextView txtId, txtDescripcio, txtEstado;
    ImageView imgIcon;
    LinearLayout latCredencial;
    ImageView btnBack, imgIcono;
    Button btnBajaAlta, btnEstado, btnCarnet, btnPDF;
    //CredencialesAdapter mAdapter;


    Inscripcion mInscripcion;
    Usuario mUsuario;
    Alumno alumno;
    CredencialDeporte credencialDeporte;

    DialogoProcesamiento dialog;

    boolean isWsp = false, isActividad = false, isEdit = false, isAdmin = false, isEstadoEdit = false;
    int modeUI = 0, positionReg = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_inscripcion_deporte);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isAdmin();

        if (mInscripcion != null) {

            loadViews();

            setToolbar();

            loadData();

            loadListener();

            editMode(0);

        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.noData));
            finish();
        }


    }

    public void isAdmin() {
        if (getIntent().getParcelableExtra(Utils.INSCRIPCION_ID) != null) {
            mInscripcion = getIntent().getParcelableExtra(Utils.INSCRIPCION_ID);
        }
        if (getIntent().getParcelableExtra(Utils.USER_INFO) != null) {
            mUsuario = getIntent().getParcelableExtra(Utils.USER_INFO);
        }
        if (getIntent().getParcelableExtra(Utils.ALUMNO_NAME) != null) {
            alumno = getIntent().getParcelableExtra(Utils.ALUMNO_NAME);
        }
        if (getIntent().getParcelableExtra(Utils.CREDENCIAL) != null) {
            credencialDeporte = getIntent().getParcelableExtra(Utils.CREDENCIAL);
        }
        if (getIntent().getBooleanExtra(Utils.IS_ADMIN_MODE, false)) {
            isAdmin = getIntent().getBooleanExtra(Utils.IS_ADMIN_MODE, false);
        }
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("");
        Utils.changeColorDrawable(btnBack, getApplicationContext(), R.color.colorPrimaryDark);
    }

    private void loadData() {
        if (isAdmin) {
            latIMCEstado.setVisibility(View.VISIBLE);
            latIMC.setVisibility(View.VISIBLE);
            linearAdmin.setVisibility(View.VISIBLE);
            linearCredencial.setVisibility(View.VISIBLE);

        } else {
            latIMCEstado.setVisibility(View.GONE);
            latIMC.setVisibility(View.GONE);
            linearAdmin.setVisibility(View.GONE);
            linearCredencial.setVisibility(View.GONE);
        }
        btnPDF.setEnabled(false);

        if (mInscripcion.getIdEstado() == 2 || mInscripcion.getDisponible() == 0 || isAdmin)
            fabEditar.setVisibility(View.GONE);

        edtIMC.setText(Utils.obtainIMC(mInscripcion.getPeso(), mInscripcion.getAltura()));
        edtIMCEstado.setText(Utils.obtainEstado(edtIMC.getText().toString().trim()).toUpperCase());
        edtIMCEstado.setTextColor(getApplicationContext().getResources().getColor(
                Utils.getColorIMC(edtIMC.getText().toString().trim())));
        txtFechaIns.setText(Utils.getFechaFormat(mInscripcion.getFechaRegistro()));
        txtFechaModi.setText(Utils.getFechaFormat(mInscripcion.getFechaModificacion()));
        edtEdad.setText(String.valueOf(Utils.getEdad(Utils.getFechaDate(mUsuario.getFechaNac()))));
        edtDni.setText(String.valueOf(mUsuario.getIdUsuario()));
        edtNombre.setText(mUsuario.getNombre());
        edtApellido.setText(mUsuario.getApellido());
        edtFechaNac.setText(mUsuario.getFechaNac());
        edtBarrio.setText(mUsuario.getBarrio());
        edtLocalidad.setText(mUsuario.getLocalidad());
        edtProvincia.setText(mUsuario.getProvincia());
        edtPais.setText(mUsuario.getPais());
        edtDomicilio.setText(mUsuario.getDomicilio());
        edtMail.setText(mUsuario.getMail());
        edtAnioIngreso.setText(String.valueOf(alumno != null ? alumno.getAnio() : 0));
        edtFace.setText(mInscripcion.getFacebook());
        edtInsta.setText(mInscripcion.getInstagram());
        edtPeso.setText(mInscripcion.getPeso());
        edtAltura.setText(mInscripcion.getAltura());
        edtTelefono.setText(mUsuario.getTelefono());
        edtLugar.setText(mInscripcion.getLugar());
        edtObj.setText(mInscripcion.getObjetivo());
        edtCuales.setText(mInscripcion.getCuales());
        edtMaterias.setText(String.valueOf(mInscripcion.getCantMaterias()));
        txtDeporte.setText(mInscripcion.getNombreDeporte());
        txtTitulo.setText(String.format("Inscripción #%s/%s", mInscripcion.getIdInscripcion(),
                String.valueOf(mInscripcion.getIdTemporada()).substring(2)));
        if (alumno != null) {
            edtCarrera.setText(alumno.getCarrera());
            edtFacultad.setText(alumno.getFacultad());
            edtLegajo.setText(alumno.getLegajo());
        } else {
            edtCarrera.setText(" ");
            edtFacultad.setText(" ");
            edtLegajo.setText(" ");
        }
        if (mInscripcion.getWsp() == 1) {
            chIsWsp.setChecked(true);
            isWsp = true;
        } else {
            isWsp = false;
            chIsWsp.setChecked(false);

        }
        updateButton(mInscripcion.getValidez());
        if (mInscripcion.getIntensidad() == -1) {
            linearActividades.setVisibility(View.GONE);
            chNoActividad.setChecked(true);
            echSiActividad.setChecked(false);
            isActividad = false;
        } else {
            linearActividades.setVisibility(View.VISIBLE);
            echSiActividad.setChecked(true);
            chNoActividad.setChecked(false);
            isActividad = true;
            switch (mInscripcion.getIntensidad()) {
                case 1:
                    chMedia.setChecked(false);
                    chAlta.setChecked(false);
                    chBaja.setChecked(true);
                    break;
                case 2:
                    chMedia.setChecked(false);
                    chAlta.setChecked(true);
                    chBaja.setChecked(false);
                    break;
                case 3:
                    chMedia.setChecked(true);
                    chAlta.setChecked(false);
                    chBaja.setChecked(false);
                    break;
            }
        }
        Deporte deporte = new Deporte();
        deporte.setName(mInscripcion.getNombreDeporte());
        deporte.setIcon();
        Glide.with(imgIcono.getContext()).load(deporte.getIconDeporte()).into(imgIcono);
        updateButtonCarnet();

        if (credencialDeporte != null) {

            txtId.setText(String.format("%s/%s", credencialDeporte.getIdInscripcion(),
                    String.valueOf(credencialDeporte.getIdTemporada()).substring(2)));
            imgIcon.setVisibility(View.VISIBLE);
            Glide.with(imgIcon.getContext()).load(credencialDeporte.getValidez() == 1 ? R.drawable.ic_chek :
                    R.drawable.ic_error).into(imgIcon);
            txtEstado.setText(credencialDeporte.getValidez() == 1 ? getString(R.string.activo) :
                    getString(R.string.inactivo));
            txtDescripcio.setText(String.format("%s - %s", mInscripcion.getNombreDeporte(),
                    credencialDeporte.getIdTemporada()));

            btnCarnet.setEnabled(false);

        } else {
            txtDescripcio.setText(getString(R.string.credencialNo));
            txtId.setVisibility(View.INVISIBLE);
            txtEstado.setVisibility(View.INVISIBLE);
            imgIcon.setVisibility(View.INVISIBLE);
            btnCarnet.setEnabled(true);
        }

        // mAdapter = new CredencialesAdapter(mList, getApplicationContext(), true);
        /*ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                positionReg = position;

            }
        });*/

    }


    private void procesarRespuestaUpdate(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    if (isEstadoEdit) {
                        Utils.showToast(getApplicationContext(), getString(R.string.estadoActualizado));
                        isEstadoEdit = false;
                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.actualizado));
                        isEdit = false;
                        edit();
                    }
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.actualizadoError));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    isEdit = false;
                    edit();
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 6:
                    Utils.showToast(getApplicationContext(), getString(R.string.noExiste));
                    break;
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.noActualizableForm));
                    isEdit = false;
                    edit();

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


    private void updateButtonCarnet() {
        if (credencialDeporte != null) {
            btnCarnet.setEnabled(false);
        } else {
            btnCarnet.setEnabled(true);
        }
    }

    private void updateButton(int val) {
        if (val == 0)
            btnBajaAlta.setText("HABILITAR INSCRIPCION");
        else btnBajaAlta.setText("DESHABILITAR INSCRIPCION");
    }

    private void loadListener() {
        fabEditar.setOnClickListener(this);
        chIsWsp.setOnClickListener(this);
        chNoActividad.setOnClickListener(this);
        echSiActividad.setOnClickListener(this);
        chBaja.setOnClickListener(this);
        chMedia.setOnClickListener(this);
        chAlta.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnEstado.setOnClickListener(this);
        btnPDF.setOnClickListener(this);
        btnCarnet.setOnClickListener(this);
        btnBajaAlta.setOnClickListener(this);
        latCredencial.setOnClickListener(this);

    }

    private void loadViews() {
        edtIMC = findViewById(R.id.edtIMC);
        edtIMCEstado = findViewById(R.id.edtIMCEstado);
        latIMC = findViewById(R.id.latIMC);
        latIMCEstado = findViewById(R.id.latIMCEstado);
        imgIcono = findViewById(R.id.imgIcon);
        edtDni = findViewById(R.id.edtDNI);
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEdad = findViewById(R.id.edtxEdad);
        edtFechaNac = findViewById(R.id.edtFecha);
        edtBarrio = findViewById(R.id.edtBarrio);
        edtLocalidad = findViewById(R.id.edtLocalidad);
        edtProvincia = findViewById(R.id.edtProvincia);
        edtPais = findViewById(R.id.edtPais);
        edtDomicilio = findViewById(R.id.edtDomicilio);
        edtMail = findViewById(R.id.edtMail);
        edtAnioIngreso = findViewById(R.id.edtAnioIngrAlu);
        edtFace = findViewById(R.id.edtFacebook);
        edtInsta = findViewById(R.id.edtxInst);
        edtPeso = findViewById(R.id.edxtPeso);
        edtAltura = findViewById(R.id.edtxAltura);
        edtTelefono = findViewById(R.id.edtCelu);
        edtLugar = findViewById(R.id.edtxPreg3);
        edtObj = findViewById(R.id.edtObjetivo);
        edtCuales = findViewById(R.id.edtxPreg2);
        edtMaterias = findViewById(R.id.edtxCantMat);
        edtCarrera = findViewById(R.id.edtCarrera);
        edtLegajo = findViewById(R.id.edtLegajo);
        edtFacultad = findViewById(R.id.edtxFacultad);
        txtFechaIns = findViewById(R.id.edtFechaRegistro);
        txtFechaModi = findViewById(R.id.edtFechaMod);
        txtDescripcio = findViewById(R.id.txtDescripcion);
        txtId = findViewById(R.id.txtId);
        txtEstado = findViewById(R.id.txtEstado);
        imgIcon = findViewById(R.id.imgEstado);
        latCredencial = findViewById(R.id.layout);
        linearCredencial = findViewById(R.id.latCredencial);

        campos = new EditText[]{edtMaterias, edtFace, edtInsta, edtPeso, edtAltura,
                edtLugar, edtObj, edtCuales};

        noEditables = new EditText[]{edtDni, edtNombre, edtApellido, edtEdad, edtBarrio, edtLocalidad, edtProvincia,
                edtPais, edtDomicilio, edtMail, edtAnioIngreso, edtTelefono, edtLegajo, edtCarrera, edtFacultad};


        btnBack = findViewById(R.id.imgFlecha);
        echSiActividad = findViewById(R.id.chbxSi);
        chNoActividad = findViewById(R.id.chbxNo);
        chBaja = findViewById(R.id.chbxBaja);
        chAlta = findViewById(R.id.chbxContin);
        chMedia = findViewById(R.id.chbxMedia);
        fabEditar = findViewById(R.id.fab);
        chIsWsp = findViewById(R.id.chbxWhats);
        btnBajaAlta = findViewById(R.id.btnAltaBaja);
        btnCarnet = findViewById(R.id.btnCarnet);
        btnPDF = findViewById(R.id.btnPDF);
        btnEstado = findViewById(R.id.btnEstado);
        linearAdmin = findViewById(R.id.latAdmin);

        noCheck = new CheckBox[]{echSiActividad, chNoActividad, chMedia, chAlta, chBaja, chIsWsp};

        linearActividades = findViewById(R.id.disablePreg);
        txtDeporte = findViewById(R.id.txtDeporte);
        txtTitulo = findViewById(R.id.txtTituloI);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAltaBaja:
                if (mInscripcion.getValidez() == 0)
                    mInscripcion.setValidez(1);
                else mInscripcion.setValidez(0);
                bajaAlta();
                break;
            case R.id.btnEstado:
                openDialogEstado();
                break;
            case R.id.btnPDF:
                break;
            case R.id.btnCarnet:
                generarCredencial();
                break;
            case R.id.fab:
                edit();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.chbxBaja:
                isEdit = true;
                chMedia.setChecked(false);
                chAlta.setChecked(false);
                chBaja.setChecked(true);
                mInscripcion.setIntensidad(1);
                break;
            case R.id.chbxContin:
                isEdit = true;
                chMedia.setChecked(false);
                chAlta.setChecked(true);
                chBaja.setChecked(false);
                mInscripcion.setIntensidad(2);
                break;
            case R.id.chbxMedia:
                isEdit = true;
                chMedia.setChecked(true);
                chAlta.setChecked(false);
                chBaja.setChecked(false);
                mInscripcion.setIntensidad(3);
                break;
            case R.id.chbxNo:
                isEdit = true;
                chNoActividad.setChecked(true);
                echSiActividad.setChecked(false);
                isActividad = false;
                linearActividades.setVisibility(View.GONE);
                break;
            case R.id.chbxSi:
                if (!isActividad) {
                    isEdit = true;
                    chNoActividad.setChecked(false);
                    isActividad = true;
                    linearActividades.setVisibility(View.VISIBLE);
                    chBaja.setChecked(true);
                    mInscripcion.setIntensidad(1);
                }
                echSiActividad.setChecked(true);
                break;
            case R.id.chbxWhats:
                isEdit = true;
                if (isWsp) {
                    isWsp = false;
                    chIsWsp.setChecked(isWsp);
                } else {
                    isWsp = true;
                    chIsWsp.setChecked(isWsp);
                }
                break;
            case R.id.layout:
                changeEstadoCredencial();
                break;
        }
    }

    private void changeEstadoCredencial() {
        DialogoActivarDesactivar dialogoActivarDesactivar = new DialogoActivarDesactivar();
        dialogoActivarDesactivar.setContext(getApplicationContext());
        dialogoActivarDesactivar.setFragmentManager(getSupportFragmentManager());
        credencialDeporte.setIdUsuario(mInscripcion.getIdUsuario());
        credencialDeporte.setNombre(mInscripcion.getNombreDeporte());
        dialogoActivarDesactivar.setCredencial(credencialDeporte);
        dialogoActivarDesactivar.setOnClickListenerAdapter(new OnClickListenerAdapter() {
            @Override
            public void onClick(Object id) {
                updateCredencial((int) id);
                updateButtonCarnet();
            }
        });
        dialogoActivarDesactivar.show(getSupportFragmentManager(), "dialogo_act_desc");
    }

    private void updateCredencial(int id) {
        credencialDeporte.setValidez(id);
        txtEstado.setText(getString(id == 1 ? R.string.activo : R.string.inactivo));
        Glide.with(imgIcon.getContext()).load(id == 1 ? R.drawable.ic_chek :
                R.drawable.ic_error).into(imgIcon);
    }

    private void generarCredencial() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String token = manager.getValueString(Utils.TOKEN);
        final int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s",
                Utils.URL_INSCRIPCION_AGREGAR_CREDENCIAL);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuestaCarnet(response);


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
                HashMap<String, String> datos = new HashMap<>();
                datos.put("key", token);
                datos.put("idU", String.valueOf(id));
                datos.put("iu", String.valueOf(id));
                datos.put("ii", String.valueOf(mInscripcion.getIdInscripcion()));
                datos.put("aa", String.valueOf(mInscripcion.getIdTemporada()));
                return datos;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuestaCarnet(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    Utils.showToast(getApplicationContext(), getString(R.string.credencialCreada));
                    btnCarnet.setEnabled(false);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorCrearCredencial));
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

    private void bajaAlta() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?idU=%s&key=%s&ii=%s&val=%s&aa=%s",
                Utils.URL_INSCRIPCION_PARTICULAR_ELIMIAR, id, key, mInscripcion.getIdInscripcion(),
                mInscripcion.getValidez(),
                mInscripcion.getIdTemporada());
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuestaAltaBaja(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));

            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void procesarRespuestaAltaBaja(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                    break;
                case 1:
                    String mensaje = "";
                    if (mInscripcion.getValidez() == 0)
                        mensaje = "Inscripción deshabilitada";
                    else mensaje = "Inscripción habilitada";
                    updateButton(mInscripcion.getValidez());
                    Utils.showToast(getApplicationContext(), mensaje);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.actualizadoError));
                    mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                    break;
                case 100:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            mInscripcion.setValidez(mInscripcion.getValidez() == 1 ? 0 : 1);
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void openDialogEstado() {
        final DialogoListaEstados dialogoListaEstados = new DialogoListaEstados();
        dialogoListaEstados.setContextEstado(getApplicationContext());
        final ArrayList<Estado> list = new ArrayList<>();
        list.add(new Estado(1, "EN EVALUACION", false));
        list.add(new Estado(2, "ACEPTADO", false));
        list.add(new Estado(3, "RECHAZADO", false));
        list.get(mInscripcion.getIdEstado() - 1).setSelect(true);
        dialogoListaEstados.setListener(new OnClickListenerAdapter() {
            @Override
            public void onClick(Object id) {
                dialogoListaEstados.dismiss();
                if (mInscripcion.getIdEstado() == (Integer) id + 1)
                    return;
                isEstadoEdit = true;
                mInscripcion.setIdEstado((Integer) id + 1);
                if (mInscripcion.getIdEstado() == 1)
                    mInscripcion.setDisponible(0);
                else mInscripcion.setDisponible(1);
                update();
            }
        });
        dialogoListaEstados.setList(list);
        dialogoListaEstados.setId(mInscripcion.getIdEstado() - 1);
        dialogoListaEstados.show(getSupportFragmentManager(), "dialog_estado");
    }

    private void edit() {
        if (isEdit) {
            update();
            return;
        }
        if (modeUI == 0)
            modeUI = 1;
        else
            modeUI = 0;
        editMode(modeUI);
    }

    private void update() {
        Validador validador = new Validador(getApplicationContext());
        String cantM = edtMaterias.getText().toString().trim();
        String face = edtFace.getText().toString().trim();
        String ins = edtInsta.getText().toString().trim();
        String obj = edtObj.getText().toString().trim();
        String cual = edtCuales.getText().toString().trim();
        String lugar = edtLugar.getText().toString().trim();
        String peso = edtPeso.getText().toString().trim();
        String altura = edtAltura.getText().toString().trim();

        if (validador.validarDNI(edtDni) && validador.validarNombres(edtNombre)
                && validador.validarNombres(edtApellido) && validador.validarNumero(edtEdad)
                && validador.validarTexto(edtDomicilio) && validador.validarTexto(edtBarrio)
                && validador.validarTexto(edtPais) && validador.validarTexto(edtCarrera)
                && validador.validarLegajo(edtLegajo) && validador.validarNumero(edtAnioIngreso)
                && validador.validarNumero(edtMaterias)
                && validador.validarTexto(edtFacultad) && validador.validarMail(edtMail)
                && validador.validarTelefono(edtTelefono) && validador.validarTexto(edtObj)
        ) {
            if (face.equals("")) {
                face = " ";
            }
            if (ins.equals("")) {
                ins = " ";
            }
            HashMap<String, String> datos = new HashMap<>();
            datos.put("iu", "");
            datos.put("id", String.valueOf(mInscripcion.getIdDeporte()));
            datos.put("cm", cantM);
            datos.put("fa", face);
            datos.put("in", ins);
            datos.put("ws", String.valueOf(isWsp ? 1 : 2));
            datos.put("ob", obj);
            datos.put("e", String.valueOf(mInscripcion.getIdEstado()));
            datos.put("ii", String.valueOf(mInscripcion.getIdInscripcion()));
            datos.put("pe", peso);
            datos.put("al", altura);
            datos.put("cu", cual.equals("") ? " " : cual);
            datos.put("int", String.valueOf(mInscripcion.getIntensidad()));
            datos.put("lu", lugar.equals("") ? " " : lugar);
            if (isActividad) {
                if (validador.validarTexto(edtLugar) && validador.validarTexto(edtCuales)) {
                    sendServer(datos);

                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.noActividadCompleta));
                }
            } else {
                sendServer(datos);
            }


        }

    }

    private void sendServer(final HashMap<String, String> datos) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final int id = manager.getValueInt(Utils.MY_ID);
        final String token = manager.getValueString(Utils.TOKEN);
        String URL = String.format("%s", Utils.URL_INSCRIPCION_ACTUALIZAR);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuestaUpdate(response);


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
                datos.put("key", token);
                datos.put("idU", String.valueOf(id));
                datos.put("iu", String.valueOf(id));
                if (isAdmin) {
                    datos.put("ie", String.valueOf(id));
                    datos.put("ad", "1");
                }
                return datos;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void editMode(int mode) {
        if (mode != 0) {
            fabEditar.setImageResource(R.drawable.ic_save);
        } else {
            fabEditar.setImageResource(R.drawable.ic_edit_);
        }

        for (EditText e2 : noEditables) {
            e2.setEnabled(false);
        }

        if (mode != 0) {
            btnPDF.setEnabled(false);
            btnEstado.setEnabled(false);
            btnBajaAlta.setEnabled(false);
        } else {
            //btnPDF.setEnabled(true);
            btnEstado.setEnabled(true);
            btnBajaAlta.setEnabled(true);
        }

        for (CheckBox c : noCheck) {
            if (mode == 0)
                c.setEnabled(false);
            else c.setEnabled(true);
        }

        for (EditText e : campos) {
            if (mode == 0) {
                e.setEnabled(false);
                e.setBackgroundColor(getResources().getColor(R.color.transparente));
                e.removeTextChangedListener(null);
            } else {
                e.setEnabled(true);
                e.setBackground(getResources().getDrawable(R.drawable.edit_text_logreg));
                e.addTextChangedListener(this);
            }
        }


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isEdit = true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
