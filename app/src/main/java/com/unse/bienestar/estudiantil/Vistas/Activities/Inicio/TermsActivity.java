package com.unse.bienestar.estudiantil.Vistas.Activities.Inicio;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;

public class TermsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        setToolbar();

    }

    private void loadData() {
    }

    private void loadListener() {
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Sobre nosotros");
    }

    private void loadViews() {
        imgIcono = findViewById(R.id.imgFlecha);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }
    }

}
