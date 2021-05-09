package com.unse.bienestar.estudiantil.Vistas.Activities.Maraton;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Maraton;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilMaratonActivity extends AppCompatActivity {

    TextView txtNombre, txtDistancia, txtEdad, txtNumero;
    ImageView imgIcono;
    Maraton mMaraton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maraton_perfil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.USER_NAME) != null) {
            mMaraton = getIntent().getParcelableExtra(Utils.USER_NAME);
        }

        if (mMaraton != null) {

            loadView();

            loadData();

            loadListener();

            setToolbar();

        } else {
            finish();
        }

    }

    private void loadListener() {
        imgIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) findViewById(R.id.txtTitulo)).setText(String.format("Inscripción #%s", mMaraton.getIdInscripcion()));
        Utils.changeColorDrawable(imgIcono, getApplicationContext(), R.color.colorPrimary);
    }

    private void loadData() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        final int id = preferenceManager.getValueInt(Utils.MY_ID);
        txtNumero.setText(String.format("#%s", mMaraton.getIdInscripcion()));
        txtDistancia.setText(String.format("%s KM", mMaraton.getCarrera()));
        UsuarioViewModel usuarioViewModel = new UsuarioViewModel(getApplicationContext());
        Usuario usuario = usuarioViewModel.getById(id);
        txtEdad.setText(String.valueOf(Utils.getEdad(Utils.getFechaDate(usuario.getFechaNac()))));
        txtNombre.setText(String.format("%s %s", usuario.getNombre(), usuario.getApellido()));

    }

    private void loadView() {
        imgIcono = findViewById(R.id.imgFlecha);
        txtDistancia = findViewById(R.id.txtDistancia);
        txtNombre = findViewById(R.id.txtNombre);
        txtNumero = findViewById(R.id.txtId);
        txtEdad = findViewById(R.id.txtEdad);
    }
}

