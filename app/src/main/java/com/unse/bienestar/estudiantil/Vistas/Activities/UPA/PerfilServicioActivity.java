package com.unse.bienestar.estudiantil.Vistas.Activities.UPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.Modelos.ServiciosUPA;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

public class PerfilServicioActivity extends AppCompatActivity implements View.OnClickListener {

    ServiciosUPA mServicio;
    Button btnTurno;
    TextView txtName, txtNameDoc, txtDia, txtHorarios, txtDesc;
    ImageView imgIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_servicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.SERVICIO_NAME) != null) {
            mServicio = getIntent().getParcelableExtra(Utils.SERVICIO_NAME);
        }

        if (mServicio != null) {
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
        ((TextView) findViewById(R.id.txtTitulo)).setText("Servicio");
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorAccent));
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadData() {
        btnTurno.setVisibility(View.GONE);
        txtName.setText(mServicio.getName());
        txtNameDoc.setText(mServicio.getNomApMed());
        txtDia.setText(mServicio.getDias());
        txtHorarios.setText(mServicio.getHora());
        txtDesc.setText(mServicio.getDesc());
        if(mServicio.getCateg() == 0){
            btnTurno.setVisibility(View.VISIBLE);
        }

    }

    private void loadListener() {
        btnTurno.setOnClickListener(this);
        imgIcono.setOnClickListener(this);
    }

    private void loadViews() {
        btnTurno = findViewById(R.id.btnTurno);
        txtName = findViewById(R.id.txtName);
        txtNameDoc = findViewById(R.id.txtNameDoc);
        txtDia = findViewById(R.id.txtDia);
        txtHorarios = findViewById(R.id.txtHorarios);
        txtDesc = findViewById(R.id.txtDesc);
        imgIcono = findViewById(R.id.imgFlecha);

    }

    private void showDialogs() {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setDescripcion(getString(R.string.generalFunciones))
                .setIcono(R.drawable.ic_enojado)
                .setTipo(DialogoGeneral.TIPO_ACEPTAR).setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {

                    }

                    @Override
                    public void no() {

                    }

                });
        final DialogoGeneral mensaje = builder.build();
        mensaje.setCancelable(false);
        mensaje.show(getSupportFragmentManager(), "dialog_error");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTurno:
                showDialogs();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }

    }
}