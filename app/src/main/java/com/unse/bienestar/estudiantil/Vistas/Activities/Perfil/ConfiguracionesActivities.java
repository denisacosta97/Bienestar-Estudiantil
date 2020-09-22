package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionesActivities extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;

    LinearLayout latContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuraciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Configuraciones");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {

    }


    private void loadListener() {
        latContrasenia.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    private void openActivity(int id) {
        Intent intent = null;
        switch (id) {
            case 1:
                intent = new Intent(getApplicationContext(), CambiarContraseniaActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void loadViews() {
        latContrasenia = findViewById(R.id.latContrasenia);
        btnBack = findViewById(R.id.imgFlecha);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.latContrasenia:
                openActivity(1);
                break;
        }
    }
}
