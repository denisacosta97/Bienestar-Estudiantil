package com.unse.bienestar.estudiantil.Vistas.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilBecasActivity extends AppCompatActivity {

    InfoBecas mInfoBecas;
    TextView nameBeca, fechaIni, fechaFin, desc, pdf;

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

    }

    private void loadData() {
        nameBeca.setText(mInfoBecas.getNameBeca());
        fechaIni.setText(mInfoBecas.getFechaInicio());
        fechaFin.setText(mInfoBecas.getFechaFin());
        desc.setText(mInfoBecas.getDesc());
        pdf.setText(mInfoBecas.getPdf());
    }

    private void loadListener() {

    }

    private void loadViews() {
        nameBeca = findViewById(R.id.txtNameBeca);
        fechaIni = findViewById(R.id.txtFechaIni);
        fechaFin = findViewById(R.id.txtFechaFin);
        desc = findViewById(R.id.txtDesc);
        pdf = findViewById(R.id.txtPdf);

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnRegisterDep:
//                Intent i = new Intent(getApplicationContext(), RegistroDeporteActivity.class);
//                i.putExtra(Utils.DEPORTE_NAME, mDeporte);
//                startActivity(i);
//                break;
//            case R.id.btnBack:
//                onBackPressed();
//                break;
//        }
//
//    }
}
