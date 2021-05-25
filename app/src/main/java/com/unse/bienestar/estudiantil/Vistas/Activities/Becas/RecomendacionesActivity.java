package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecomendacionesActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    ImageView imgUno, imgDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

    }

    private void loadData() {
        Glide.with(imgUno.getContext()).load(Utils.URL_IMAGEN_REC_NO).into(imgUno);
        Glide.with(imgDos.getContext()).load(Utils.URL_IMAGEN_REC_SI).into(imgDos);

    }

    private void loadViews() {
        imgDos = findViewById(R.id.imgReco2);
        imgUno = findViewById(R.id.imgReco1);
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtTitulo)).setText("Recomendaciones");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }
    }

}