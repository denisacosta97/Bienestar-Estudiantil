package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Databases.AlumnoViewModel;
import com.unse.bienestar.estudiantil.Databases.EgresadoViewModel;
import com.unse.bienestar.estudiantil.Databases.ProfesorViewModel;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.UploadManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.Validador;
import com.unse.bienestar.estudiantil.Herramientas.VolleyMultipartRequest;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.Departamento;
import com.unse.bienestar.estudiantil.Modelos.Egresado;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.Modelos.Profesor;
import com.unse.bienestar.estudiantil.Modelos.Provincia;
import com.unse.bienestar.estudiantil.Modelos.Regularidad;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.RegularidadAdapter;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoActivarDesactivar;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoDirecciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoOpciones;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoRegularidadAlumno;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static android.view.View.VISIBLE;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.facultad;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.faya;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.fceyt;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.fcf;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.fcm;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.fhcys;

public class InfoUsuarioActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    ImageView btnBack;
    CircleImageView imgUser;
    LinearLayout latGeneral, latAlumno, latProfesor, latEgresado, latAdmin, latUser, latRegularidades;
    CardView fabEditar, fabPic;
    AppCompatImageView imgEditar;
    TextView edtProvincia, edtPais, edtLocalidad;
    EditText edtFechaNac, edtNombre, edtApellido, edtDNI, edtSexo, edtMail, edtProfesionProf, edtAnioIngresoProf,
            edtProfesionEgre, edtAnioEgresoEgre, edtAnioIngresoAlu, edtLegajoAlu, edtDomicilio, edtTelefono, edtBarrio, edtRegistro, edtModificacion;
    Button btnAltaBaja, btnAgregarReg;
    Spinner spinnerFacultad, spinnerCarrera;
    EditText[] campos;
    ArrayAdapter<String> carreraAdapter;
    ArrayAdapter<String> facultadAdapter;
    RecyclerView mRecyclerViewRegularidad;
    RecyclerView.LayoutManager mLayoutManager;
    RegularidadAdapter mRegularidadAdapter;
    ArrayList<Regularidad> mRegularidads;
    HashMap<String, String> param;

    ArrayList<Opciones> provincias = new ArrayList<>();
    ArrayList<Provincia> mProvincias;

    ArrayList<Opciones> departamentos = new ArrayList<>();
    ArrayList<Departamento> depart;

    ArrayList<Opciones> localidad = new ArrayList<>();
    ArrayList<Departamento> local;

    int idPais = -1, idProvinc = -1, idLocalidad = -1;
    int tipo = -1, posicionDep, posicionLocal;

    DialogoProcesamiento dialog;
    UsuarioViewModel mUsuarioViewModel;
    Usuario mUsuario = null;

    Object tipoUsuario = null;
    FragmentManager manager = null;
    Bitmap mBitmapFileSelect;
    Uri uriFileSelect;
    String nameFileSelect;

    boolean isEditMode = false, isReadyForLoad = false, isAdminMode = false;
    int facultadUser = 0, carreraUser = 0, mode = 0, tipoUsuer = -1, idUser = 0, validez = -1, position = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_usuario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isAdmin();

        loadViews();

        setToolbar();

        loadData();

        loadListener();

        editMode(0);

    }

    private void isAdmin() {
        if (getIntent().getBooleanExtra(Utils.IS_ADMIN_MODE, false)) {
            isAdminMode = getIntent().getBooleanExtra(Utils.IS_ADMIN_MODE, false);
        }
        if (isAdminMode) {
            if (getIntent().getParcelableExtra(Utils.USER_INFO) != null) {
                mUsuario = getIntent().getParcelableExtra(Utils.USER_INFO);
            }
        }
        if (getIntent().getSerializableExtra(Utils.LIST_REGULARIDAD) != null) {
            mRegularidads = (ArrayList<Regularidad>) getIntent().getSerializableExtra(Utils.LIST_REGULARIDAD);

        }
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("");
        DrawableCompat.setTint(btnBack.getDrawable(),
                ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
    }

    private void loadListener() {
        edtFechaNac.setOnClickListener(this);
        fabEditar.setOnClickListener(this);
        fabPic.setOnClickListener(this);
        imgUser.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnAltaBaja.setOnClickListener(this);
        btnAgregarReg.setOnClickListener(this);
        if (tipoUsuer == Utils.TIPO_ALUMNO) {
            spinnerFacultad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    spinnerFacultad.setSelection(position);
                    switch (position) {
                        case 0:
                            //FAyA
                            carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.style_spinner, faya);
                            carreraAdapter.setDropDownViewResource(R.layout.style_spinner);
                            spinnerCarrera.setAdapter(carreraAdapter);
                            break;
                        case 1:
                            //FCEyT
                            carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.style_spinner, fceyt);
                            carreraAdapter.setDropDownViewResource(R.layout.style_spinner);
                            spinnerCarrera.setAdapter(carreraAdapter);
                            break;
                        case 2:
                            //FCF
                            carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.style_spinner, fcf);
                            carreraAdapter.setDropDownViewResource(R.layout.style_spinner);
                            spinnerCarrera.setAdapter(carreraAdapter);
                            break;
                        case 3:
                            //FCM
                            carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.style_spinner, fcm);
                            carreraAdapter.setDropDownViewResource(R.layout.style_spinner);
                            spinnerCarrera.setAdapter(carreraAdapter);
                            break;
                        case 4:
                            //FHyCS
                            carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.style_spinner, fhcys);
                            carreraAdapter.setDropDownViewResource(R.layout.style_spinner);
                            spinnerCarrera.setAdapter(carreraAdapter);
                            break;
                    }
                    //Es para determinar que se cambio de carrera
                    if (facultadUser != position && mode == 1)
                        isEditMode = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }

            });
            spinnerCarrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mode == 0)
                        spinnerCarrera.setSelection(carreraUser);
                    if (carreraUser != position && mode == 1)
                        isEditMode = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerFacultad.setSelection(facultadUser);
        }
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerViewRegularidad);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                processClick(position);
            }
        });

        edtFechaNac.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edtFechaNac.setText(current);
                    edtFechaNac.setSelection(sel < current.length() ? sel : current.length());
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                edtFechaNac.setError(null);
            }
        });

    }

    private void processClick(int pos) {
        position = pos;
        Regularidad regularidad = mRegularidads.get(pos);
        DialogoActivarDesactivar dialogoActivarDesactivar = new DialogoActivarDesactivar();
        dialogoActivarDesactivar.setContext(getApplicationContext());
        dialogoActivarDesactivar.setFragmentManager(getSupportFragmentManager());
        dialogoActivarDesactivar.setIdUsuario(mUsuario.getIdUsuario());
        dialogoActivarDesactivar.setPosition(position);
        dialogoActivarDesactivar.setRegularidad(regularidad);
        dialogoActivarDesactivar.setOnClickListenerAdapter(new OnClickListenerAdapter() {
            @Override
            public void onClick(Object id) {
                mRegularidads.get(position).setValidez((Integer) id);
                mRegularidadAdapter.notifyDataSetChanged();
                determinarBoton();
            }
        });
        dialogoActivarDesactivar.show(getSupportFragmentManager(), "dialogo_act_desc");
    }

    private void loadViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtDNI = findViewById(R.id.edtDNI);
        edtSexo = findViewById(R.id.edtSexo);
        edtMail = findViewById(R.id.edtMail);
        edtProfesionProf = findViewById(R.id.edtProfesion1);
        edtAnioIngresoProf = findViewById(R.id.edtAnioIngProf);
        edtAnioIngresoAlu = findViewById(R.id.edtAnioIngrAlu);
        edtProfesionEgre = findViewById(R.id.edtProfesionEgre);
        edtAnioEgresoEgre = findViewById(R.id.edtAnioEgresoEgre);
        btnBack = findViewById(R.id.imgFlecha);
        spinnerCarrera = findViewById(R.id.spinner2);
        spinnerFacultad = findViewById(R.id.spinner1);
        edtProvincia = findViewById(R.id.edtProvincia);
        edtTelefono = findViewById(R.id.edtCelu);
        edtLocalidad = findViewById(R.id.edtLocalidad);
        edtDomicilio = findViewById(R.id.edtDomicilio);
        edtLegajoAlu = findViewById(R.id.edtLegajo);
        edtPais = findViewById(R.id.edtPais);
        edtBarrio = findViewById(R.id.edtBarrio);
        edtFechaNac = findViewById(R.id.edtFecha);
        fabEditar = findViewById(R.id.fab);
        imgEditar = findViewById(R.id.imgEditar);
        fabPic = findViewById(R.id.fabPic);
        latGeneral = findViewById(R.id.layRango);
        latProfesor = findViewById(R.id.layProfesor);
        latEgresado = findViewById(R.id.layEgresado);
        latAlumno = findViewById(R.id.layAlumno);
        imgUser = findViewById(R.id.imgUserPerfil);
        latAdmin = findViewById(R.id.latAdmin);
        latUser = findViewById(R.id.latDatos);
        btnAltaBaja = findViewById(R.id.btnAltaBaja);
        edtRegistro = findViewById(R.id.edtFechaRegistro);
        edtModificacion = findViewById(R.id.edtFechaMod);
        latRegularidades = findViewById(R.id.latRegularidad);
        mRecyclerViewRegularidad = findViewById(R.id.recycler);
        btnAgregarReg = findViewById(R.id.btnRegularidad);
    }

    private void loadData() {
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewRegularidad.setHasFixedSize(true);
        mRecyclerViewRegularidad.setLayoutManager(mLayoutManager);
        if (mRegularidads != null) {
            determinarBoton();
            mRegularidadAdapter = new RegularidadAdapter(getApplicationContext(), mRegularidads);
            mRecyclerViewRegularidad.setAdapter(mRegularidadAdapter);
        }
        mUsuarioViewModel = new UsuarioViewModel(getApplicationContext());
        if (!isAdminMode) {
            latAdmin.setVisibility(View.GONE);
            latRegularidades.setVisibility(View.GONE);
            latUser.setVisibility(VISIBLE);
        } else {
            latAdmin.setVisibility(VISIBLE);
            latUser.setVisibility(VISIBLE);
            if (mUsuario != null && mUsuario.getTipoUsuario() == Utils.TIPO_ALUMNO)
                latRegularidades.setVisibility(VISIBLE);
            else
                latRegularidades.setVisibility(View.GONE);
            edtRegistro.setEnabled(false);
        }
        campos = new EditText[]{edtNombre, edtApellido, edtSexo, edtMail, edtProfesionProf,
                edtAnioIngresoProf, edtAnioIngresoAlu, edtProfesionEgre, edtAnioEgresoEgre
                , edtTelefono, edtDomicilio, edtLegajoAlu, edtBarrio};
        manager = getSupportFragmentManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                facultadAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, facultad);
                facultadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFacultad.setAdapter(facultadAdapter);
                carreraAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, faya);
                carreraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCarrera.setAdapter(carreraAdapter);
            }
        }).start();
        loadInfo();
    }

    private void determinarBoton() {
        String anioActual = Utils.getFechaName(new Date(System.currentTimeMillis()));
        anioActual = anioActual.substring(0, 4);
        if (mRegularidads.size() > 0 &&
                mRegularidads.get(0).getAnio() == Integer.parseInt(anioActual)
                && mRegularidads.get(0).getValidez() == 1) {
            btnAgregarReg.setEnabled(false);
        } else {
            btnAgregarReg.setEnabled(true);
        }
    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        int idLocal = manager.getValueInt(Utils.MY_ID);
        if (!isAdminMode) {
            mUsuario = mUsuarioViewModel.getById(idLocal);
            edtDNI.setEnabled(false);
        }
        if (mUsuario != null) {
            loadLayout(mUsuario.getTipoUsuario());
            edtDNI.setText(String.valueOf(mUsuario.getIdUsuario()));
            edtNombre.setText(mUsuario.getNombre());
            edtApellido.setText(mUsuario.getApellido());
            edtSexo.setText(mUsuario.getSexo());
            edtFechaNac.setText(mUsuario.getFechaNac());
            edtDomicilio.setText(mUsuario.getDomicilio());
            edtBarrio.setText(mUsuario.getBarrio());

            if (mUsuario.getPais().equals("ARGENTINA") || mUsuario.getPais().equals("OTRO")
            || Character.isUpperCase(mUsuario.getPais().charAt(0))) {
                idPais = 54;
                edtPais.setText(mUsuario.getPais());
            } else {
                idPais = -1;
                edtPais.setText(" ");
            }
            Provincia provincia = new Provincia(mUsuario.getProvincia());
            if (provincia.getCodigo() == -1) {
                idProvinc = -1;
                edtProvincia.setText(" ");
                idLocalidad = -1;
                edtLocalidad.setText(" ");
            } else {
                idProvinc = provincia.getCodigo();
                edtProvincia.setText(provincia.getTitulo());
                idLocalidad = idProvinc;
                edtLocalidad.setText(mUsuario.getLocalidad());
            }

            edtMail.setText(mUsuario.getMail());
            edtTelefono.setText(mUsuario.getTelefono());
            edtRegistro.setText(Utils.getFechaFormat(mUsuario.getFechaRegistro()));
            edtModificacion.setText(Utils.getFechaFormat(mUsuario.getFechaModificacion()));
            //Cargo foto de perfil
            loadProfilePicture();
            //Cargo datos necesarios para operaciones
            tipoUsuer = mUsuario.getTipoUsuario();
            validez = mUsuario.getValidez();
            idUser = mUsuario.getIdUsuario();
            if (isAdminMode)
                changeButton();
            //Cargo datos en base al tipo de mUsuario
            if (mUsuario.getTipoUsuario() != 5 || mUsuario.getTipoUsuario() != 3) {
                //Alumnos
                if (mUsuario.getTipoUsuario() == 1) {
                    Alumno alumno;
                    //Si es modo Admin saco los datos del objeto
                    if (isAdminMode) {
                        edtLegajoAlu.setText(((Alumno) mUsuario).getLegajo());
                        edtAnioIngresoAlu.setText(((Alumno) mUsuario).getAnio());
                        loadCarrera(mUsuario);
                    } else {
                        AlumnoViewModel alumnoViewModel = new AlumnoViewModel(getApplicationContext());
                        alumno = alumnoViewModel.getById(idLocal);
                        if (alumno != null) {
                            edtLegajoAlu.setText(alumno.getLegajo());
                            edtAnioIngresoAlu.setText(alumno.getAnio());
                            loadCarrera(alumno);
                        }
                    }
                    //Egresado
                } else if (mUsuario.getTipoUsuario() == 4) {
                    Egresado egresado = null;
                    if (isAdminMode) {
                        edtProfesionEgre.setText(((Egresado) mUsuario).getProfesion());
                        edtAnioEgresoEgre.setText(((Egresado) mUsuario).getFechaEgreso());
                    } else {
                        EgresadoViewModel egresadoViewModel = new EgresadoViewModel(getApplicationContext());
                        egresado = egresadoViewModel.getById(idLocal);
                        if (egresado != null) {
                            edtProfesionEgre.setText(egresado.getProfesion());
                            edtAnioEgresoEgre.setText(egresado.getFechaEgreso());
                        }
                    }
                    //Profesor
                } else if (mUsuario.getTipoUsuario() == 2) {
                    Profesor profesor = null;
                    if (isAdminMode) {
                        edtProfesionProf.setText(((Profesor) mUsuario).getProfesion());
                        edtAnioIngresoProf.setText(((Profesor) mUsuario).getFechaIngreso());
                    } else {
                        ProfesorViewModel profesorViewModel = new ProfesorViewModel(getApplicationContext());
                        profesor = profesorViewModel.getById(idLocal);
                        if (profesor != null) {
                            edtProfesionProf.setText(profesor.getProfesion());
                            edtAnioIngresoProf.setText(profesor.getFechaIngreso());
                        }
                    }

                }
            }
        }
    }

    private void loadProfilePicture() {
        if (isAdminMode) {
            String URL = String.format("%s%s.jpg", Utils.URL_USUARIO_IMAGE_LOAD, mUsuario.getIdUsuario());
            Glide.with(imgUser.getContext()).load(URL).apply(new RequestOptions()
                    .error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_user)).into(imgUser);
        } else {
            Bitmap bitmap = FileStorageManager.getBitmap(getApplicationContext(), Utils.FOLDER,
                    String.format(Utils.PROFILE_PIC, mUsuario.getIdUsuario()), false);
            if (bitmap != null) {
                Glide.with(imgUser.getContext()).load(bitmap).into(imgUser);
            } else {
                String URL = String.format("%s%s.jpg", Utils.URL_USUARIO_IMAGE_LOAD, mUsuario.getIdUsuario());
                Glide.with(imgUser.getContext()).load(URL).apply(new RequestOptions()
                        .error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.ic_user)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        FileStorageManager.saveBitmap(getApplicationContext(), Utils.FOLDER,
                                String.format(Utils.PROFILE_PIC,
                                        mUsuario.getIdUsuario()),
                                ((BitmapDrawable) resource).getBitmap(), false);
                        return false;
                    }
                }).into(imgUser);
            }
        }
    }

    private void loadCarrera(Usuario mUsuario) {
        Alumno alumno = (Alumno) mUsuario;
        List<String> facultad = Arrays.asList(Utils.facultad);
        List<String> faya = Arrays.asList(Utils.faya);
        List<String> fceyt = Arrays.asList(Utils.fceyt);
        List<String> fcf = Arrays.asList(Utils.fcf);
        List<String> fcm = Arrays.asList(Utils.fcm);
        List<String> fhcys = Arrays.asList(Utils.fhcys);
        int index = facultad.indexOf(alumno.getFacultad());
        facultadUser = index;
        int index2 = -1;
        spinnerFacultad.setSelection(Math.max(index, 0));
        if (index != -1)
            switch (index) {
                case 0:
                    index2 = faya.indexOf(alumno.getCarrera());
                    break;
                case 1:
                    index2 = fceyt.indexOf(alumno.getCarrera());
                    break;
                case 2:
                    index2 = fcf.indexOf(alumno.getCarrera());
                    break;
                case 3:
                    index2 = fcm.indexOf(alumno.getCarrera());
                    break;
                case 4:
                    index2 = fhcys.indexOf(alumno.getCarrera());
                    break;

            }
        carreraUser = index2;
    }

    private void loadLayout(int tipoUsuario) {
        switch (tipoUsuario) {
            case 1:
                latGeneral.setVisibility(VISIBLE);
                latAlumno.setVisibility(VISIBLE);
                latEgresado.setVisibility(View.GONE);
                latProfesor.setVisibility(View.GONE);
                break;
            case 2:
                latGeneral.setVisibility(VISIBLE);
                latAlumno.setVisibility(View.GONE);
                latEgresado.setVisibility(View.GONE);
                latProfesor.setVisibility(VISIBLE);
                break;
            case 3:
            case 5:
                latGeneral.setVisibility(VISIBLE);
                latAlumno.setVisibility(View.GONE);
                latEgresado.setVisibility(View.GONE);
                latProfesor.setVisibility(View.GONE);
                break;
            case 4:
                latGeneral.setVisibility(VISIBLE);
                latAlumno.setVisibility(View.GONE);
                latEgresado.setVisibility(VISIBLE);
                latProfesor.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtPais:
                tipo = 0;
                openPais();
                break;
            case R.id.edtProvincia:
                tipo = 1;
                openProvincia();
                break;
            case R.id.edtLocalidad:
                tipo = 2;
                openLocalidad();
                break;
            case R.id.btnRegularidad:
                openBannerReg();
                break;
            case R.id.btnAltaBaja:
                altaBajaUser();
                break;
            case R.id.fabPic:
                checkPermission();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.fab:
                openModeEditor();
                break;
            case R.id.imgUserPerfil:
                openPicture();
                break;
        }
    }

    private void openLocalidad() {
        if (idProvinc == -1) {
            openDialogEdit("Escribe tu localidad", tipo);
        } else {
            PreferenceManager manager = new PreferenceManager(getApplicationContext());
            final String key = manager.getValueString(Utils.TOKEN);
            final int idLocal = manager.getValueInt(Utils.MY_ID);
            String URL = String.format("%s?key=%s&idU=%s&ir=%s", Utils.URL_USUARIO_DIRECCION, key, idLocal, idProvinc);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    procesarRespuestaDireccion(response, 2);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                    dialog.dismiss();

                }
            });
            //Abro dialogo para congelar pantalla
            dialog = new DialogoProcesamiento();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog_process");
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }

    private void openProvincia() {
        if (idPais == -1) {
            openDialogEdit("Escribe tu provincia", tipo);
        } else {
            PreferenceManager manager = new PreferenceManager(getApplicationContext());
            final String key = manager.getValueString(Utils.TOKEN);
            final int idLocal = manager.getValueInt(Utils.MY_ID);
            String URL = String.format("%s?key=%s&idU=%s&ip=%s", Utils.URL_USUARIO_DIRECCION, key, idLocal, idPais);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    procesarRespuestaDireccion(response, 1);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                    dialog.dismiss();

                }
            });
            //Abro dialogo para congelar pantalla
            dialog = new DialogoProcesamiento();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog_process");
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }

    private void procesarRespuestaDireccion(String response, int tipo) {
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
                        JSONArray jsonArray = jsonObject.getJSONArray("mensaje");
                        mProvincias = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String msj = object.getString("descripcion").toUpperCase();
                            int cod = Integer.parseInt(object.getString("idprovincia"));
                            mProvincias.add(new Provincia(cod, msj));
                        }
                        provincias = new ArrayList<>();
                        for (Provincia p : mProvincias) {
                            provincias.add(new Opciones(p.getTitulo()));
                        }
                        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
                            @Override
                            public void onClick(int pos) {
                                isEditMode = true;
                                for (Provincia p : mProvincias) {
                                    if (p.getTitulo().equals(provincias.get(pos).getTitulo())) {
                                        idProvinc = p.getCodigo();
                                        edtProvincia.setText(p.getTitulo());
                                        edtLocalidad.setText("");
                                        break;
                                    }
                                }
                            }
                        }, provincias, getApplicationContext());
                        dialogoOpciones.show(getSupportFragmentManager(), "dialog");
                    } else {
                        depart = new ArrayList<>();
                        JSONArray departa = jsonObject.getJSONArray("mensaje");
                        for (int i = 0; i < departa.length(); i++) {
                            JSONObject dep = departa.getJSONObject(i);
                            int codigo = Integer.parseInt(dep.getString("iddepartamento"));
                            String titulo = dep.getString("descripcion").toUpperCase();
                            depart.add(new Departamento(codigo, titulo));
                        }
                        departamentos = new ArrayList<>();
                        for (Departamento d : depart) {
                            departamentos.add(new Opciones(d.getDescripcion()));
                        }
                        local = new ArrayList<>();
                        JSONArray locali = jsonObject.getJSONArray("localidad");
                        for (int i = 0; i < locali.length(); i++) {
                            JSONObject dep = locali.getJSONObject(i);
                            int codigo = Integer.parseInt(dep.getString("iddepartamento"));
                            int codigo2 = Integer.parseInt(dep.has("idlocalidad") ? dep.getString("idlocalidad") : "");
                            String titulo = dep.getString("descripcion").toUpperCase();
                            local.add(new Departamento(codigo2, titulo, codigo));
                        }
                        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
                            @Override
                            public void onClick(int pos) {
                                isEditMode = true;
                                for (Departamento p : depart) {
                                    if (p.getDescripcion().equals(departamentos.get(pos).getTitulo())) {
                                        int codigo = p.getCodigo();
                                        posicionDep = pos;
                                        showLocalidad(codigo);
                                        break;
                                    }

                                }
                            }
                        }, departamentos, getApplicationContext());
                        dialogoOpciones.show(getSupportFragmentManager(), "dialog");

                    }

                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.noData));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void showLocalidad(int codigo) {
        localidad = new ArrayList<>();
        for (Departamento de : local) {
            if (de.getCodigoDep() == codigo) {
                localidad.add(new Opciones(de.getDescripcion()));
            }
        }
        DialogoOpciones dialogoOpciones = new DialogoOpciones(new OnClickOptionListener() {
            @Override
            public void onClick(int pos) {
                for (Departamento p : local) {
                    if (p.getDescripcion().equals(localidad.get(pos).getTitulo())) {
                        String depa = depart.get(posicionDep).getDescripcion().toUpperCase();
                        String loc = p.getDescripcion().toUpperCase();
                        edtLocalidad.setText(String.format("%s - %s", depa, loc));
                        break;
                    }

                }
            }
        }, localidad, getApplicationContext());
        dialogoOpciones.show(getSupportFragmentManager(), "dialog");

    }

    private void openPais() {
        ArrayList<Opciones> opciones = new ArrayList<>();
        opciones.add(new Opciones("ARGENTINA"));
        opciones.add(new Opciones("OTRO"));
        DialogoOpciones dialogo = new DialogoOpciones(new OnClickOptionListener() {
            @Override
            public void onClick(int pos) {
                isEditMode = true;
                if (pos == 1) {
                    openDialogEdit("Escribe tu pais", tipo);
                } else {
                    edtPais.setText("ARGENTINA");
                    edtLocalidad.setText("");
                    edtProvincia.setText("");
                    idProvinc = -1;
                    idPais = 54;
                }
            }
        }, opciones, getApplicationContext());
        dialogo.show(getSupportFragmentManager(), "dialogo");
    }

    private void openDialogEdit(String titulo, final int tipo) {
        DialogoDirecciones dialogoDirecciones = new DialogoDirecciones(titulo, new OnClickListenerAdapter() {
            @Override
            public void onClick(Object id) {
                if (tipo == 1) {
                    edtProvincia.setText(String.valueOf(id));
                    edtLocalidad.setText("");
                } else if (tipo == 2) {
                    edtLocalidad.setText(String.valueOf(id));
                }else if (tipo == 0) {
                    edtPais.setText(String.valueOf(id));
                    idPais = -1;
                    idProvinc = -1;
                    edtProvincia.setText("");
                    edtLocalidad.setText("");
                }

            }
        }, getApplicationContext());
        dialogoDirecciones.show(getSupportFragmentManager(), "dialog");
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(InfoUsuarioActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                    .setDescripcion(getString(R.string.permisosFoto))
                    .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                    .setListener(new YesNoDialogListener() {
                        @Override
                        public void yes() {
                            ActivityCompat.requestPermissions(InfoUsuarioActivity.this,
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

    private void openPicture() {
        Intent intent = new Intent(getApplicationContext(), ProfilePictureActivity.class);
        startActivity(intent);
    }

    private void openBannerReg() {
        DialogoRegularidadAlumno dialogoRegularidadAlumno = new DialogoRegularidadAlumno();
        dialogoRegularidadAlumno.setContext(getApplicationContext());
        dialogoRegularidadAlumno.setFragmentManager(getSupportFragmentManager());
        dialogoRegularidadAlumno.setIdUsuario(mUsuario.getIdUsuario());
        dialogoRegularidadAlumno.setRegularidadLista(mRegularidads);
        dialogoRegularidadAlumno.show(getSupportFragmentManager(), "dialogo_regularidad");
    }

    private void openModeEditor() {
        ObjectAnimator.ofFloat(fabEditar, "rotation", 0f, 360f).setDuration(600).start();
        activateEditMode();
    }

    private void altaBajaUser() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(R.string.advertencia))
                .setDescripcion(String.format(getString(R.string.usuarioEliminar), validez == 0 ? "alta" : "baja",
                        mUsuario.getNombre(),
                        mUsuario.getApellido()))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                        if (validez == 0)
                            validez = 1;
                        else
                            validez = 0;
                        baja(validez);
                    }

                    @Override
                    public void no() {

                    }
                })
                .setIcono(R.drawable.ic_advertencia)
                .setTipo(DialogoGeneral.TIPO_SI_NO);
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialog_ad");
    }

    private void baja(int val) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&id=%s&idU=%s&val=%s", Utils.URL_USUARIO_ELIMINAR, key,
                idLocal, mUsuario.getIdUsuario(), val);
        StringRequest requestImage = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaEliminar(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();
            }
        });
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void procesarRespuestaEliminar(String response) {
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
                    String texto;
                    if (validez == 0)
                        texto = getString(R.string.usuarioDeshabilitado);
                    else
                        texto = getString(R.string.usuarioHabilitado);
                    Utils.showToast(getApplicationContext(), texto);
                    changeButton();
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioNoExiste));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Seleccionar imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, Utils.PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    checkPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Utils.PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        Intent intent = new Intent(getApplicationContext(), CropImageActivity.class);
                        intent.putExtra(Utils.URI_IMAGE, uri);
                        startActivityForResult(intent, Utils.EDIT_IMAGE);
                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.imagenErrorLoad));
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Utils.showToast(getApplicationContext(), getString(R.string.noSelectImagen));
                }
                break;
            case Utils.EDIT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = null;
                    String name = null;
                    if (data != null && data.getParcelableExtra(Utils.URI_IMAGE) != null) {
                        uri = data.getParcelableExtra(Utils.URI_IMAGE);
                    }
                    if (data != null && data.getStringExtra(Utils.NAME_GENERAL) != null) {
                        name = data.getStringExtra(Utils.NAME_GENERAL);
                    }
                    if (uri != null && name != null) {
                        loadPic(uri, name);
                    } else {
                        Utils.showToast(getApplicationContext(), getString(R.string.imagenErrorCrop));
                    }
                } else if (resultCode == 2) {
                    Utils.showToast(getApplicationContext(), getString(R.string.imagenErrorCrop));
                } else if (resultCode == RESULT_CANCELED) {
                    Utils.showToast(getApplicationContext(), getString(R.string.cambioNoGuardado));
                }
                break;
        }

    }

    private void loadPic(final Uri uri, final String name) {
        FileStorageManager.resizeBitmapAndFile(name);
        mBitmapFileSelect = BitmapFactory.decodeFile(new File(name).getPath());
        uriFileSelect = uri;
        nameFileSelect = name;
        isReadyForLoad = true;

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isReadyForLoad) {
            uploadImage(mBitmapFileSelect);
            isReadyForLoad = false;
        }
    }

    private String getCarrera(int selectedItemPosition) {
        switch (selectedItemPosition) {
            case 0:
                return faya[spinnerCarrera.getSelectedItemPosition()];
            case 1:
                return fceyt[spinnerCarrera.getSelectedItemPosition()];
            case 2:
                return fcf[spinnerCarrera.getSelectedItemPosition()];
            case 3:
                return fcm[spinnerCarrera.getSelectedItemPosition()];
            case 4:
                return fhcys[spinnerCarrera.getSelectedItemPosition()];
        }
        return "";
    }

    private void uploadImage(final Bitmap bitmap) {
        VolleyMultipartRequest.DataPart dataPart = new VolleyMultipartRequest.DataPart(
                String.format(Utils.PROFILE_PIC, idUser),
                Utils.getFileDataFromDrawable(bitmap)
        );
        VolleyMultipartRequest volleyMultipartRequest = new UploadManager.Builder(getApplicationContext())
                .setMetodo(Request.Method.POST)
                .setURL(Utils.URL_USUARIO_IMAGE)
                .setOkListener(new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        procesarRespuestaImagen(new String(response.data), bitmap);
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Utils.showToast(getApplicationContext(), getString(R.string.imagenErrorLoad));
                        FileStorageManager.deleteFileFromUri(getApplicationContext(), uriFileSelect);
                    }
                })
                .setDato(dataPart)
                .setTipoDato(UploadManager.IMAGE)
                .setParams(null)
                .build();
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(volleyMultipartRequest);
    }

    private void procesarRespuestaImagen(String response, Bitmap bitmap) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case 1:
                    //Exito
                    Glide.with(imgUser.getContext()).load(bitmap).into(imgUser);
                    if (!isAdminMode)
                        FileStorageManager.saveBitmap(getApplicationContext(),
                                Utils.FOLDER,
                                String.format(Utils.PROFILE_PIC, idUser), bitmap,
                                false);
                    Utils.showToast(getApplicationContext(), getString(R.string.fotoPerfilExito));
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.fotoPerfilError));
                    break;
            }
            FileStorageManager.deleteFileFromUri(getApplicationContext(), uriFileSelect);

        } catch (JSONException e) {
            e.printStackTrace();
            FileStorageManager.deleteFileFromUri(getApplicationContext(), uriFileSelect);
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadProfilePicture();
    }


    @Override
    public void onBackPressed() {
        if (isEditMode) {
            Utils.showToast(getApplicationContext(), getString(R.string.cambioPrimeroGuardar));
        } else
            super.onBackPressed();
    }

    private void activateEditMode() {
        if (isEditMode) {
            save();
            return;
        }
        if (mode == 0)
            mode = 1;
        else
            mode = 0;
        editMode(mode);
    }

    private void editMode(int mode) {
        if (mode != 0) {
            Glide.with(imgEditar.getContext()).load(R.drawable.ic_save).into(imgEditar);
            edtFechaNac.setOnClickListener(this);
            edtPais.setOnClickListener(this);
            edtProvincia.setOnClickListener(this);
            edtLocalidad.setOnClickListener(this);
            spinnerFacultad.setEnabled(true);
            spinnerCarrera.setEnabled(true);
            if (!isAdminMode) {
                fabPic.setVisibility(VISIBLE);
            }
        } else {
            Glide.with(imgEditar.getContext()).load(R.drawable.ic_edit_).into(imgEditar);
            edtFechaNac.setOnClickListener(null);
            edtPais.setOnClickListener(null);
            edtProvincia.setOnClickListener(null);
            edtLocalidad.setOnClickListener(null);
            spinnerFacultad.setEnabled(false);
            spinnerCarrera.setEnabled(false);
            fabPic.setVisibility(View.INVISIBLE);
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

    private void save() {
        Validador validador = new Validador(getApplicationContext());

        String fechaParcial = edtFechaNac.getText().toString().trim();
        String fecha = Utils.parseDateString(fechaParcial);
        String nombre = edtNombre.getText().toString().trim();
        String apellido = edtApellido.getText().toString().trim();
        String dni = edtDNI.getText().toString().trim();
        String sexo = edtSexo.getText().toString().trim();
        String mail = edtMail.getText().toString().trim();
        String profesion = edtProfesionProf.getText().toString().trim();
        String anioIngreso = edtAnioIngresoProf.getText().toString().trim();
        String faculta = facultad[spinnerFacultad.getSelectedItemPosition()].trim();
        String carrera = getCarrera(spinnerFacultad.getSelectedItemPosition()).trim();
        String anioIngreso2 = edtAnioIngresoAlu.getText().toString().trim();
        String profesion2 = edtProfesionEgre.getText().toString().trim();
        String anioEgreso = edtAnioEgresoEgre.getText().toString().trim();
        String domicilio = edtDomicilio.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String pais = edtPais.getText().toString().trim();
        String provincia = edtProvincia.getText().toString().trim();
        String localidad = edtLocalidad.getText().toString().trim();
        String legajo = edtLegajoAlu.getText().toString().trim();
        String barrio = edtBarrio.getText().toString().trim();
        //Creo un modelo para posterior almacenarlo local
        int regularidad = 0;
        provincia = String.format("%s - %s", idProvinc, provincia);
        if (mUsuario instanceof Alumno)
            regularidad = ((Alumno) mUsuario).getIdRegularidad();
        mUsuario = new Usuario(Integer.parseInt(dni), nombre, apellido, fecha, pais, provincia, localidad,
                domicilio, barrio, telefono, sexo, mail, tipoUsuer, mUsuario.getFechaRegistro(), mUsuario.getFechaModificacion(),
                mUsuario.getValidez());
        idUser = Integer.parseInt(dni);



        if (validador.validarDNI(edtDNI) && validador.validarTelefono(edtTelefono) && validador.validarMail(edtMail)
                && validador.validarFecha(edtFechaNac) && validador.validarDomicilio(edtDomicilio)
                && validador.validarTexto(edtPais) && validador.validarTexto(edtLocalidad)
                && validador.validarTexto(edtProvincia)
                && !validador.validarNombresEdt(edtNombre, edtApellido, edtBarrio, edtSexo)) {
            switch (mUsuario.getTipoUsuario()) {
                case Utils.TIPO_ALUMNO:
                    if (!validador.noVacio(faculta) && !validador.noVacio(carrera) &&
                            validador.validarAnio(edtAnioIngresoAlu) && validador.validarLegajo(edtLegajoAlu)) {
                        sendServer(processString(dni, nombre, apellido, fecha, pais, provincia, localidad,
                                domicilio, barrio, telefono, sexo, mail, Utils.TIPO_ALUMNO, carrera, faculta, anioIngreso2
                                , legajo, null, null, regularidad));
                        tipoUsuario = new Alumno(mUsuario.getIdUsuario(), mUsuario.getNombre(), mUsuario.getApellido(),
                                mUsuario.getFechaNac(), mUsuario.getPais(), mUsuario.getProvincia(), mUsuario.getLocalidad(),
                                mUsuario.getDomicilio(), mUsuario.getBarrio(), mUsuario.getTelefono(), mUsuario.getSexo(),
                                mUsuario.getMail(), mUsuario.getTipoUsuario(), mUsuario.getFechaRegistro(), mUsuario.getFechaModificacion(),
                                mUsuario.getValidez(), Integer.parseInt(dni), carrera, faculta,
                                anioIngreso2, legajo, regularidad);
                    }
                    break;
                case Utils.TIPO_PROFESOR:
                    if (!validador.validarNombresEdt(edtProfesionProf)
                            && validador.validarAnio(edtAnioIngresoProf)) {
                        sendServer(processString(dni, nombre, apellido, fecha, pais, provincia, localidad,
                                domicilio, barrio, telefono, sexo, mail,
                                Utils.TIPO_PROFESOR, null, null, anioIngreso, null,
                                profesion, null, 0));
                        tipoUsuario = new Profesor(mUsuario.getIdUsuario(), mUsuario.getNombre(), mUsuario.getApellido(),
                                mUsuario.getFechaNac(), mUsuario.getPais(), mUsuario.getProvincia(), mUsuario.getLocalidad(),
                                mUsuario.getDomicilio(), mUsuario.getBarrio(), mUsuario.getTelefono(), mUsuario.getSexo(),
                                mUsuario.getMail(), mUsuario.getTipoUsuario(), mUsuario.getFechaRegistro(), mUsuario.getFechaModificacion(),
                                mUsuario.getValidez(), Integer.parseInt(dni), profesion, anioIngreso);
                    }
                    break;
                case Utils.TIPO_EGRESADO:
                    if (!validador.validarNombresEdt(edtProfesionEgre) && validador.validarAnio(edtAnioEgresoEgre)) {
                        sendServer(processString(dni, nombre, apellido, fecha, pais, provincia,
                                localidad, domicilio, barrio,
                                telefono, sexo, mail, Utils.TIPO_EGRESADO,
                                null, null, null, null,
                                profesion2, anioEgreso, 0));
                        tipoUsuario = new Egresado(mUsuario.getIdUsuario(), mUsuario.getNombre(), mUsuario.getApellido(),
                                mUsuario.getFechaNac(), mUsuario.getPais(), mUsuario.getProvincia(), mUsuario.getLocalidad(),
                                mUsuario.getDomicilio(), mUsuario.getBarrio(), mUsuario.getTelefono(), mUsuario.getSexo(),
                                mUsuario.getMail(), mUsuario.getTipoUsuario(), mUsuario.getFechaRegistro(), mUsuario.getFechaModificacion(),
                                mUsuario.getValidez(), Integer.parseInt(dni), profesion2, anioEgreso);
                    }
                    break;
                case Utils.TIPO_NODOCENTE:
                case Utils.TIPO_PARTICULAR:
                    sendServer(processString(dni, nombre, apellido, fecha, pais, provincia, localidad, domicilio,
                            barrio, telefono, sexo, mail, tipoUsuer,
                            null, null, null, null, null, null, 0));
                    break;
            }

        } else Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
    }

    public String processString(String dni, String nombre, String apellido, String
            fecha, String pais, String provincia, String localidad, String domicilio,
                                String barrio, String telefono, String sexo, String mail,
                                int tipo, String carrera, String facultad, String anioIng,
                                String legajo, String profesion, String anioEgreso, int regularidad) {
        param = new HashMap<>();
        param.put("idU", String.valueOf(dni));
        param.put("nom", nombre);
        param.put("ape", apellido);
        param.put("fechan", fecha);
        param.put("pais", pais);
        param.put("prov", provincia);
        param.put("local", localidad);
        param.put("dom", domicilio);
        param.put("sex", sexo);
        param.put("tipo", String.valueOf(tipo));
        param.put("mail", mail);
        param.put("tel", telefono);
        param.put("barr", barrio);
        if (tipo == 1) {
            /*resp = String.format(Utils.dataAlumno, dni, nombre, apellido, fecha, pais, provincia, localidad,
                    domicilio, sexo, carrera, facultad, anioIng, legajo, tipo, mail, telefono,
                    barrio, "", 0);*/
            param.put("car", carrera);
            param.put("fac", facultad);
            param.put("anio", anioIng);
            param.put("leg", legajo);
            param.put("idReg", "0");

        } else if (tipo == 2) {
            param.put("prof", profesion);
            param.put("fechain", anioIng);
            /*resp = String.format(Utils.dataProfesor, dni, nombre, apellido, fecha, pais, provincia,
                    localidad, domicilio, sexo, tipo, mail, telefono,
                    profesion, anioIng, barrio, "");*/

        } else if (tipo == 4) {
            /*resp = String.format(Utils.dataEgresado, dni, nombre, apellido, fecha, pais, provincia,
                    localidad, domicilio, sexo, tipo, mail, telefono,
                    profesion, anioEgreso, barrio, "");*/
            param.put("prof", profesion);
            param.put("fechaeg", anioEgreso);
        } else {
           /* resp = String.format(Utils.dataPartiNoDoc, dni, nombre, apellido, fecha, pais, provincia, localidad,
                    domicilio, sexo, tipo, mail, telefono, barrio, fecha);*/
        }
        param.put("pass", " ");
        //resp = String.format("%s&key=%s&pass=%s", resp, key, contrasenia);
        return "";
    }

    public void sendServer(String data) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s", Utils.URL_USUARIO_ACTUALIZAR);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                procesarRespuesta(response);


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
                param.put("key", key);
                param.put("id", String.valueOf(idLocal));
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
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
                    Utils.showToast(getApplicationContext(), getString(R.string.perfilActualizado));
                    isEditMode = false;
                    openModeEditor();
                    if (!isAdminMode)
                        updateInBD(mUsuario, tipoUsuario);
                    break;
                case 2:
                    Utils.showToast(getApplicationContext(), getString(R.string.actualizadoError));
                    break;
                case 4:
                    //Ya existe
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 5:
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioNoExiste));
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void updateInBD(Usuario mUsuario, Object tipo) {
        mUsuarioViewModel.update(mUsuario);
        if (tipo instanceof Alumno) {
            AlumnoViewModel alumnoViewModel = new AlumnoViewModel(getApplicationContext());
            alumnoViewModel.update((Alumno) tipo);
        } else if (tipo instanceof Profesor) {
            ProfesorViewModel profesorViewModel = new ProfesorViewModel(getApplicationContext());
            profesorViewModel.update((Profesor) tipo);
        } else if (tipo instanceof Egresado) {
            EgresadoViewModel egresadoViewModel = new EgresadoViewModel(getApplicationContext());
            egresadoViewModel.update((Egresado) tipo);

        }
        loadInfo();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mode == 1)
            isEditMode = true;
    }

    @Override
    public void afterTextChanged(Editable s) {


    }

    private void elegirFechaNacimiento() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String mes, dia;
                month = month + 1;
                if (month < 10) {
                    mes = "0" + month;
                } else
                    mes = String.valueOf(month);
                if (day < 10)
                    dia = "0" + day;
                else
                    dia = String.valueOf(day);
                final String selectedDate = year + "-" + mes + "-" + dia;
                if (!edtFechaNac.getText().toString().equals(selectedDate))
                    isEditMode = true;
                edtFechaNac.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");

    }

    private void changeButton() {
        if (validez == 0)
            btnAltaBaja.setText(getString(R.string.btnAlta));
        else btnAltaBaja.setText(getString(R.string.btnBaja));
    }
}
