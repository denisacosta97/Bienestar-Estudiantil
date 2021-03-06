package com.unse.bienestar.estudiantil.Vistas.Activities.Polideportivo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.*;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.*;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReservaEspacioActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnReservar;
    Toolbar mToolbar;
    RecyclerView mRecyclerHoraQuincho, mRecyclerHoraSalon;
    RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    ArrayList<ReservaHorario> mListHorarios, mListHorario2;
    ImageView imgFlecha;
    TextView txtDesc;

    HorariosCanchaAdapter mHorariosCanchaAdapter, mHorariosCanchaAdapter2;

    DatePickerTimeline calendario;

    int tipo = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_espacio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(getIntent().getIntExtra(Utils.DATA_RESERVA,-1) != -1){
            tipo = getIntent().getIntExtra(Utils.DATA_RESERVA, -1);
        }
        if(tipo != -1){

            //Utils.setFont(getApplicationContext(), (ViewGroup) findViewById(android.R.id.content),"Montserrat-Regular.ttf");

            setToolbar();

            loadViews();

            loadData();

            loadListener();
        }else{
            Utils.showToast(getApplicationContext(), "Error al abrir la actividad");
        }

    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        String titulo = Utils.getAppName(getApplicationContext(), getComponentName());
        txtTitulo.setText(titulo);
    }

    private void loadData() {
        if(tipo == Utils.TIPO_CANCHA){
            txtDesc.setText(String.format(getResources().getString(R.string.txtReserva),"las canchas."));
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            mListHorarios = new ArrayList<>();
            mListHorarios.add(new ReservaHorario(1,1, "10:00","11:00"));
            mListHorarios.add(new ReservaHorario(2,1, "11:00","12:00"));
            mListHorarios.add(new ReservaHorario(1,2, "12:00","13:00"));
            mListHorarios.add(new ReservaHorario(1,1, "13:00","14:00"));
            mListHorarios.add(new ReservaHorario(2,3, "14:00","15:00"));
            mListHorarios.add(new ReservaHorario(1,1, "15:00","16:00"));
            mListHorarios.add(new ReservaHorario(2,2, "16:00","17:00"));
            mRecyclerHoraQuincho.setLayoutManager(mLayoutManager);
            mRecyclerHoraQuincho.setNestedScrollingEnabled(true);
            mHorariosCanchaAdapter = new HorariosCanchaAdapter(mListHorarios, getApplicationContext(), true);
            mRecyclerHoraQuincho.setAdapter(mHorariosCanchaAdapter);

        }else if(tipo == Utils.TIPO_QUINCHO){
            txtDesc.setText(String.format(getResources().getString(R.string.txtReserva),"el salon y quinchos."));
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            mListHorarios = new ArrayList<>();
            mListHorarios.add(new ReservaHorario(1,1, "10:00","18:00"));
            mListHorarios.add(new ReservaHorario(2,1, "20:00","02:00"));
            mRecyclerHoraQuincho.setLayoutManager(mLayoutManager);
            mRecyclerHoraQuincho.setNestedScrollingEnabled(true);
            mHorariosCanchaAdapter = new HorariosCanchaAdapter(mListHorarios, getApplicationContext(), false);
            mRecyclerHoraQuincho.setAdapter(mHorariosCanchaAdapter);

            mListHorario2 = new ArrayList<>();
            mListHorario2.add(new ReservaHorario(1, 2,"10:00","19:00"));
            mListHorario2.add(new ReservaHorario(2, 3,"21:00","05:00"));
            mLayoutManager2 = new GridLayoutManager(getApplicationContext(), 2);
            mRecyclerHoraSalon.setLayoutManager(mLayoutManager2);
            mRecyclerHoraQuincho.setNestedScrollingEnabled(true);
            mHorariosCanchaAdapter2 = new HorariosCanchaAdapter(mListHorario2, getApplicationContext(), false);
            mRecyclerHoraSalon.setAdapter(mHorariosCanchaAdapter2);

        }

        calendario.setLastVisibleDate(2029, Calendar.DECEMBER, 31);

    }

    private void loadListener() {
        imgFlecha.setOnClickListener(this);
        calendario.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {

            }
        });
        final Intent i = new Intent(getApplicationContext(), ConfirmarReservaActivity.class);
        if(tipo == Utils.TIPO_QUINCHO)
            i.putExtra(Utils.DATA_RESERVA, Utils.TIPO_QUINCHO);
        else
            i.putExtra(Utils.DATA_RESERVA, Utils.TIPO_CANCHA);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerHoraSalon);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                startActivity(i);
            }
        });

        ItemClickSupport itemClickSupport2 = ItemClickSupport.addTo(mRecyclerHoraQuincho);
        itemClickSupport2.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                startActivity(i);
            }
        });
    }

    private void loadViews() {
        mToolbar = findViewById(R.id.toolbar);
        calendario = findViewById(R.id.calendario);
       // btnReservar = findViewById(R.id.btnReservar);
        mRecyclerHoraSalon = findViewById(R.id.recycler2);
        mRecyclerHoraQuincho = findViewById(R.id.recycler);
        imgFlecha = findViewById(R.id.imgFlecha);
        txtDesc = findViewById(R.id.txtDescripcion);
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
