package com.unse.bienestar.estudiantil.Vistas.Activities.Polideportivo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.Validador;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.ReservaCancha;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CanchasSelectActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Button btnCancelar, btnReservar;
    TextView txtCancha, txtHora, txtFecha, txtPrecio;
    EditText edtNombre, edtDni;
    Spinner spinnerCat;
    ReservaCancha reservaCancha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canchas_select);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.CANCHA) != null) {
            reservaCancha = getIntent().getParcelableExtra(Utils.CANCHA);
        }

        setToolbar();

        loadView();

        loadData();

        loadListener();
    }

    private void loadListener() {
        btnReservar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                spinnerCat.setSelection(position);
                float precio = calcularPrecio(position);
                String p = String.valueOf(precio);
                txtPrecio.setText("$" + p);
        }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    private void loadView() {
        btnCancelar = findViewById(R.id.btnCancelar);
        btnReservar = findViewById(R.id.btnReservar);
        txtHora = findViewById(R.id.txtHora);
        txtCancha = findViewById(R.id.txtCancha);
        txtFecha = findViewById(R.id.txtFecha);
        txtPrecio = findViewById(R.id.txtPrecio);
        edtNombre = findViewById(R.id.edtNombre);
        edtDni = findViewById(R.id.edtDni);
        spinnerCat = findViewById(R.id.spinnerCateg);

    }

    private void loadData() {
        txtCancha.setText(reservaCancha.getCancha());
        txtHora.setText(reservaCancha.getHora());
        txtFecha.setText(reservaCancha.getFechaReserva());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.style_spinner, Utils.categorias);
        dataAdapter.setDropDownViewResource(R.layout.style_spinner);
        spinnerCat.setAdapter(dataAdapter);

    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Reserva");
    }

    private float calcularPrecio(int categ) {
        float precioTotal = 0;
        int turno = reservaCancha.getTurno();

        switch (categ) {
            case 1:
            case 0:
            case 7:
            case 6: //Estudiante
                //Afiliados
                if(turno == 0){
                    precioTotal = 350;
                } else
                    precioTotal = 700;
                break;
            case 5:
                //Particular
                if(turno == 0){
                    precioTotal = 600;
                } else
                    precioTotal = 1200;
                break;
            default:
                //Docentes, Nodocentes y Egresados
                if(turno == 0){
                    precioTotal = 450;
                } else
                    precioTotal = 900;
                break;
        }
        return precioTotal;
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

                    @Override
                    public void aceptar() {

                    }
                });
        final DialogoGeneral mensaje = builder.build();
        mensaje.setCancelable(false);
        mensaje.show(getSupportFragmentManager(), "dialog_error");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnCancelar:
                finish();
                break;
            case R.id.btnReservar:
                showDialogs();
                //save();
                break;
        }
    }

    private void save() {
        Validador validador = new Validador(getApplicationContext());

        String nombre = edtNombre.getText().toString().trim();
        String dni = edtDni.getText().toString().trim();
        String cancha = txtCancha.getText().toString().trim();
        String hora = txtHora.getText().toString().trim();
        String fechaReserva = txtFecha.getText().toString().trim();
        String precio = txtPrecio.getText().toString().trim();
        String categ = Utils.categorias[spinnerCat.getSelectedItemPosition()].trim();

        String fecha = Utils.getFechaNameWithinHour(new Date(System.currentTimeMillis()));
        String token = new PreferenceManager(getApplicationContext()).getValueString(Utils.TOKEN);
    }
}