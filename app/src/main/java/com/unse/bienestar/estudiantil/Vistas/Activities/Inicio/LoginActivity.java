package com.unse.bienestar.estudiantil.Vistas.Activities.Inicio;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.unse.bienestar.estudiantil.Databases.AlumnoViewModel;
import com.unse.bienestar.estudiantil.Databases.EgresadoViewModel;
import com.unse.bienestar.estudiantil.Databases.ProfesorViewModel;
import com.unse.bienestar.estudiantil.Databases.RolViewModel;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.Egresado;
import com.unse.bienestar.estudiantil.Modelos.Profesor;
import com.unse.bienestar.estudiantil.Modelos.Rol;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Perfil.RecuperarContraseniaActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button mInicio;
    ImageView btnBack;
    DialogoProcesamiento dialog;
    EditText edtUser, edtPass;
    TextView txtWelcome;
    AppCompatImageView latFondo;
    UsuarioViewModel mUsuarioViewModel;
    int dniNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        changeTextWelcome();

    }

    public void changeTextWelcome() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 6 && timeOfDay <= 12) {
            txtWelcome.setText("¡Buen día!");
        } else if (timeOfDay >= 13 && timeOfDay <= 19) {
            txtWelcome.setText("¡Buenas tardes!");
        } else if (timeOfDay >= 20)
            txtWelcome.setText("¡Buenas noches!");
    }

    private void loadData() {
        Glide.with(latFondo.getContext()).load(Utils.URL_IMAGEN_INICIO).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                latFondo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black_alpha_40));
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(new RequestOptions().centerCrop())
                .into(latFondo);
        mUsuarioViewModel = new UsuarioViewModel(getApplicationContext());
        // Uri uri = Uri.parse("android.resource://".concat(getPackageName()).concat("/raw/").concat(String.valueOf(R.raw.video_bacl)));
        //mVideoView.setVideoURI(uri);
        //mVideoView.start();

        /*mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadListener() {
        //btnBack.setOnClickListener(this);
        mInicio.setOnClickListener(this);
    }

    private void loadViews() {
        latFondo = findViewById(R.id.imgFondo);
        mInicio = findViewById(R.id.sesionOn);
        edtPass = findViewById(R.id.edtPass);
        edtUser = findViewById(R.id.edtUser);
        txtWelcome = findViewById(R.id.txtWelcome);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sesionOn:
                login();
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.txtPassMissed:
                startActivity(new Intent(LoginActivity.this, RecuperarContraseniaActivity.class));
                break;
        }
    }

    private void login() {
        String dniN = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        if (!dniN.equals("") && !pass.equals("")) {
            //pass = Utils.crypt(pass);
            try {
                dniNumber = Integer.parseInt(dniN);
            } catch (NumberFormatException e) {

            }
            String url = String.format("%s?id=%s&pass=%s", Utils.URL_USUARIO_LOGIN, dniN, pass);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
            });
            dialog = new DialogoProcesamiento();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog_process");
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        } else {
            Utils.showToast(getApplicationContext(), "Por favor complete los campos");
        }

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
                    //Utils.showToast(getApplicationContext(), getString(R.string.sesionIniciada));
                    String token = jsonObject.getJSONObject("mensaje").getString("token");

                    //Insertar BD
                    //Usuario user = guardarDatos(jsonObject);
                    //Roles
                    //saveRoles(jsonObject, user.getIdUsuario());
                    PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                    //preferenceManager.setValue(Utils.IS_LOGIN, true);
                    //int dni = user.getIdUsuario();
                    preferenceManager.setValue(Utils.MY_ID, dniNumber);
                    preferenceManager.setValue(Utils.TOKEN, token);
                    //preferenceManager.setValue(Utils.IS_VISIT, false);
                    //Main
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //finishAffinity();
                    getData(token);
                    break;
                case 2:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioInvalido));
                    break;
                case 5:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioInhabilitado));
                    break;
                case 4:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    public void getData(String token) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String url = String.format("%s?idU=%s&key=%s&id=%s", Utils.URL_USUARIO_LOGIN_DATA, id, key, dniNumber);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();

            }
        });
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuestaLogin(String response) {
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
                    Utils.showToast(getApplicationContext(), getString(R.string.sesionIniciada));
                    //Insertar BD
                    Usuario user = guardarDatos(jsonObject);
                    //Roles
                    saveRoles(jsonObject, user.getIdUsuario());
                    PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                    preferenceManager.setValue(Utils.IS_LOGIN, true);
                    int dni = user.getIdUsuario();
                    preferenceManager.setValue(Utils.MY_ID, dniNumber);
                    preferenceManager.setValue(Utils.IS_VISIT, false);
                    //Main
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finishAffinity();
                    break;
                case 2:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioInvalido));
                    break;
                case 5:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.usuarioInhabilitado));
                    break;
                case 4:
                    //Usuario invalido
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }


    private void saveRoles(JSONObject jsonObject, int id) {
        RolViewModel rolViewModel = new RolViewModel(getApplicationContext());
        if (jsonObject.has("roles")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("roles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String rol = object.getString("idrol");

                    Rol ro = new Rol(Integer.parseInt(rol), id, "test");

                    rolViewModel.insert(ro);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private Usuario guardarDatos(JSONObject datos) {
        Usuario usuario = null;
        try {
            usuario = Usuario.mapper(datos, Usuario.COMPLETE);
            mUsuarioViewModel.insert(usuario);
            switch (usuario.getTipoUsuario()) {
                case 1:
                    Alumno alumno = Alumno.mapper(datos, usuario);
                    AlumnoViewModel alumnoViewModel = new AlumnoViewModel(getApplicationContext());
                    alumnoViewModel.insert(alumno);
                    break;
                case 2:
                    Profesor profesor = Profesor.mapper(datos, usuario);
                    ProfesorViewModel profesorViewModel = new ProfesorViewModel(getApplicationContext());
                    profesorViewModel.insert(profesor);
                    break;
                case 4:
                    Egresado egresado = Egresado.mapper(datos, usuario);
                    EgresadoViewModel egresadoViewModel = new EgresadoViewModel(getApplicationContext());
                    egresadoViewModel.insert(egresado);
                    break;
                case 3:
                case 5:
                    //Nadin jeje
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

}
