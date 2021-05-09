package com.unse.bienestar.estudiantil.Vistas.Activities.Polideportivo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class EspaciosActivity extends AppCompatActivity implements View.OnClickListener {

    Button linearCancha, linearQuincho;
    ImageView imgIcon, imgSum;
    AppCompatImageView imgCancha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espacios);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

    }

    private void loadData() {
        Glide.with(imgCancha.getContext()).load(Utils.URL_IMAGEN_CANCHA).into(imgCancha);
        Glide.with(imgSum.getContext()).load(Utils.URL_IMAGEN_SUM).into(imgSum);
        ((TextView) findViewById(R.id.txtTitulo)).setText(Utils.getAppName(getApplicationContext(), getComponentName()));
    }

    private void loadListener() {
        linearQuincho.setOnClickListener(this);
        linearCancha.setOnClickListener(this);
        imgIcon.setOnClickListener(this);
    }

    private void loadViews() {
        imgSum = findViewById(R.id.imgSum);
        imgCancha = findViewById(R.id.imgCancha);
        linearCancha = findViewById(R.id.layCancha);
        linearQuincho = findViewById(R.id.laySalon);
        imgIcon = findViewById(R.id.imgFlecha);
    }

    @Override
    public void onClick(View v) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        switch (v.getId()) {
            case R.id.layCancha:
                if (isLogin)
                    Utils.showToast(getApplicationContext(), getString(R.string.noDisponible));
                else Utils.showToast(

                        getApplicationContext(), getString(R.string.primeroRegistrar));
                //startActivity(new Intent(getApplicationContext(), CanchasActivity.class));
                break;
            case R.id.laySalon:
                if (isLogin)
                    startActivity(new Intent(getApplicationContext(), CalendarReservasActivity.class));
                else Utils.showToast(

                        getApplicationContext(), getString(R.string.primeroRegistrar));
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }


    }
}
