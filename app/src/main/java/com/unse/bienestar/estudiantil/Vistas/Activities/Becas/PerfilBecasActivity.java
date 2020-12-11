package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilBecasActivity extends AppCompatActivity implements View.OnClickListener {

    InfoBecas mInfoBecas;
    TextView nameBeca, reqGeneral, reqAcad, desc, pdf;
    ImageView imgIcono;
    Button btnCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_becas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.BECA_NAME) != null) {
            mInfoBecas = getIntent().getParcelableExtra(Utils.BECA_NAME);
        }

        if (mInfoBecas != null) {
            loadViews();

            loadListener();

            loadData();
        } else {
            Utils.showToast(getApplicationContext(), "ERROR al abrir, vuelva a intentar");
            finish();
        }

        setToolbar();
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Información de Becas");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {
        nameBeca.setText(mInfoBecas.getNameBeca());
        reqAcad.setText(mInfoBecas.getReqAcad());
        reqGeneral.setText(mInfoBecas.getReqGeneral());
        desc.setText(mInfoBecas.getDesc());
        pdf.setText(mInfoBecas.getPdf());
    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        btnCargar.setOnClickListener(this);
    }

    private void loadViews() {
        nameBeca = findViewById(R.id.txtNameBeca);
        reqGeneral = findViewById(R.id.txtReqGeneral);
        reqAcad = findViewById(R.id.txtReqAcad);
        desc = findViewById(R.id.txtDesc);
        pdf = findViewById(R.id.txtPdf);
        imgIcono = findViewById(R.id.imgFlecha);
        btnCargar = findViewById(R.id.btnCargar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnCargar:
                startActivity(new Intent(getApplicationContext(), CargarDocumentacionActivity.class));
                break;
        }

    }
}
