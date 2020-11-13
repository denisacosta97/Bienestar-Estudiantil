package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Convocatoria;
import com.unse.bienestar.estudiantil.Modelos.Opciones;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.OpcionesSimpleAdapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorReceptoresActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    OpcionesSimpleAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Opciones> mList;
    ArrayList<Turno> horarios;
    ArrayList<String> receptores;
    int[] mCalendar;
    Convocatoria mConvocatoria;

    public static SelectorReceptoresActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_receptores);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        instance = this;

        if (getIntent().getSerializableExtra(Utils.DATA_RESERVA) != null) {
            horarios = (ArrayList<Turno>) getIntent().getSerializableExtra(Utils.DATA_RESERVA);
        }
        if (getIntent().getSerializableExtra(Utils.DATA_TURNO) != null) {
            receptores = (ArrayList<String>) getIntent().getSerializableExtra(Utils.DATA_TURNO);
        }
        if (getIntent().getIntArrayExtra(Utils.DATA_FECHA) != null) {
            mCalendar = getIntent().getIntArrayExtra(Utils.DATA_FECHA);

        }
        if (getIntent().getParcelableExtra(Utils.DATA_CONVOCATORIA) != null) {
            mConvocatoria = (Convocatoria) getIntent().getParcelableExtra(Utils.DATA_CONVOCATORIA);

        }

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {
        mList = new ArrayList<>();

        for (String s : receptores) {
            Pattern pattern = Pattern.compile("[0-9]");
            Matcher matcher = pattern.matcher(s);
            String num = "";
            boolean is = true;
            if (matcher.find())
                num = matcher.group();
            for (Turno t : horarios) {
                if (t.getReceptor() == Integer.parseInt(num)) {
                    is = false;
                    break;
                }
            }
            if (is)
                mList.add(new Opciones(LinearLayout.VERTICAL, Integer.parseInt(num),
                        s, 0, 0));
        }

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new OpcionesSimpleAdapter(mList, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                openResumen(position);
            }
        });
    }

    private void openResumen(int position) {
        String s = mList.get(position).getTitulo();
        Intent intent = new Intent(getApplicationContext(), ResumenTurnoActivity.class);
        intent.putExtra(Utils.DATA_TURNO, s);
        intent.putExtra(Utils.DATA_FECHA, mCalendar);
        intent.putExtra(Utils.DATA_RESERVA, horarios.get(0).getFechaInicio());
        intent.putExtra(Utils.DATA_CONVOCATORIA, mConvocatoria);
        startActivity(intent);
    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
    }


}