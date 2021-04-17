package com.unse.bienestar.estudiantil.Vistas.Activities.Becas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.matrix.Vector3;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Convocatoria;
import com.unse.bienestar.estudiantil.Modelos.Horario;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Adaptadores.HorariosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFechaActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    HorariosAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Horario> mList;
    ArrayList<Horario> copiaHorarios;
    CalendarView mCalendarView;
    CardView mCardView;
    ProgressBar mProgressBar, mProgressBarHorario;
    LinearLayout latDatos;
    Convocatoria mConvocatoria;
    ArrayList<Vector3> fechas;
    HashMap<String, ArrayList<Turno>> turnos;
    ArrayList<String> receptores;
    ArrayList<Turno> horarios;
    boolean errorHorario = false;
    int[] fecha = new int[3];
    int posicionHorario = -1, count = 0;

    public static SelectorFechaActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_fecha);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        instance = this;

        if (getIntent().getParcelableExtra(Utils.DATA_RESERVA) != null)
            mConvocatoria = getIntent().getParcelableExtra(Utils.DATA_RESERVA);

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {
        mList = new ArrayList<>();
        fecha[0] = -1;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable progress = mProgressBar.getIndeterminateDrawable();
            progress.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mProgressBar.setIndeterminateDrawable(progress);

            Drawable progress2 = mProgressBarHorario.getIndeterminateDrawable();
            progress2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mProgressBarHorario.setIndeterminateDrawable(progress2);
        }

        latDatos.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarHorario.setVisibility(View.GONE);
        mCardView.setVisibility(View.INVISIBLE);

        loadHorarios();

        loadFechas();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        mCalendarView.setMinDate(calendar.getTime().getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        mCalendarView.setMaxDate(calendar.getTime().getTime());

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);

    }

    private void loadFechas() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s", Utils.URL_FECHAS_VALIDA, key,
                idLocal);
        StringRequest requestImage = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                mProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void loadHorarios() {
        String URL = Utils.URL_BECAS_HORARIO;
        StringRequest requestImage = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 2);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                finish();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private void procesarRespuesta(String response, int tipo) {
        try {

            if (tipo == 2) {
                mProgressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray horarioArray = jsonObject.getJSONArray("h");
                for (int i = 0; i < horarioArray.length(); i++) {
                    Horario horario = new Horario(i, 0, horarioArray.getString(i), "");
                    mList.add(horario);
                }
                horarioArray = jsonObject.getJSONArray("r");
                receptores = new ArrayList<>();
                for (int i = 0; i < horarioArray.length(); i++) {
                    String receptor = horarioArray.getString(i);
                    receptores.add(receptor);
                }
                if (!errorHorario) {
                    latDatos.setVisibility(View.VISIBLE);
                }
                count++;
                if (count == 2) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date(System.currentTimeMillis()));
                    getHorarios(calendar);
                }

            } else if (tipo == 1) {
                JSONObject jsonObject = new JSONObject(response);
                int estado = jsonObject.getInt("estado");
                switch (estado) {
                    case -1:
                        Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                        break;
                    case 1:
                        //Exito
                        loadInfo(jsonObject, 1);
                        break;
                    case 2:
                        Utils.showToast(getApplicationContext(), getString(R.string.becaConvocatoriaNo));
                        errorHorario = true;
                        latDatos.setVisibility(View.GONE);
                        break;
                    case 3:
                        Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                        break;
                    case 4:
                        Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                        break;
                    case 100:
                        Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                        break;
                }
            } else if (tipo == 3) {
                mProgressBarHorario.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(response);
                int estado = jsonObject.getInt("estado");
                switch (estado) {
                    case -1:
                        Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                        break;
                    case 1:
                        //Exito
                        loadInfo(jsonObject, 2);
                        break;
                    case 2:
                        //Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoNoHorario));

                        copiaHorarios = new ArrayList<>();

                        copiaHorarios.addAll(mList);
                        turnos = new HashMap<>();

                        reset();

                        adapter = new HorariosAdapter(copiaHorarios, getApplicationContext());
                        mRecyclerView.setAdapter(adapter);

                        mRecyclerView.setVisibility(View.VISIBLE);
                        mCardView.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                        break;
                    case 4:
                        Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                        break;
                    case 100:
                        Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
        }
    }

    private void loadInfo(JSONObject jsonObject, int tipo) {
        if (jsonObject.has("mensaje")) {

            if (tipo == 1) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("mensaje");

                    fechas = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        Vector3 vector3 = new Vector3(Integer.parseInt(object.getString("dia")),
                                Integer.parseInt(object.getString("mes")),
                                Integer.parseInt(object.getString("anio")));

                        fechas.add(vector3);
                    }

                    latDatos.setVisibility(View.VISIBLE);

                    count++;
                    if (count == 2) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(new Date(System.currentTimeMillis()));
                        getHorarios(calendar);
                    }

                } catch (JSONException e) {
                    errorHorario = true;
                    e.printStackTrace();
                }
            } else if (tipo == 2) {
                try {

                    horarios = new ArrayList<>();
                    turnos = new HashMap<>();

                    JSONArray jsonArray = jsonObject.getJSONArray("mensaje");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        Turno turno = Turno.mapper(object, Turno.LOW);

                        if (turnos.containsKey(turno.getFechaInicio())) {
                            turnos.get(turno.getFechaInicio()).add(turno);
                        } else {
                            ArrayList<Turno> list = new ArrayList<>();
                            list.add(turno);
                            turnos.put(turno.getFechaInicio(), list);
                        }

                        horarios.add(turno);

                    }
                    copiaHorarios = new ArrayList<>();

                    for (Horario horario : mList) {
                        boolean isReserved = false;
                        if (turnos.containsKey(horario.getHoraInicio())) {
                            int sizeR = receptores.size();
                            for (Turno t : turnos.get(horario.getHoraInicio())) {
                                for (String receptor : receptores) {
                                    Pattern pattern = Pattern.compile("[0-9]");
                                    Matcher matcher = pattern.matcher(receptor);
                                    if (matcher.find()) {
                                        String num = matcher.group();
                                        if (t.getReceptor() == Integer.parseInt(num)) {
                                            sizeR--;
                                        }
                                    }

                                }
                                if (sizeR <= 0) {
                                    isReserved = true;
                                }
                            }
                        }
                        if (!isReserved) copiaHorarios.add(horario);
                    }

                    adapter = new HorariosAdapter(copiaHorarios, getApplicationContext());
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mCardView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                update(position);
                posicionHorario = position;
            }
        });
        mCardView.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                if (!Utils.isDateHabilited(calendar) && isValidDate(calendar)) {

                    getHorarios(calendar);
                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoNoDia));
                    mProgressBarHorario.setVisibility(View.GONE);
                    mCardView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void update(int position) {
        if (copiaHorarios != null) {
            reset();
            copiaHorarios.get(position).setEstado(1);
            adapter.notifyDataSetChanged();
        }
    }

    private void reset() {
        for (Horario horario : copiaHorarios) {
            horario.setEstado(0);
        }
    }

    private void getHorarios(Calendar calendar) {
        mProgressBarHorario.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCardView.setVisibility(View.INVISIBLE);
        fecha[0] = calendar.get(Calendar.DAY_OF_MONTH);
        fecha[1] = calendar.get(Calendar.MONTH) + 1;
        fecha[2] = calendar.get(Calendar.YEAR);
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s&di=%s&me=%s&an=%s", Utils.URL_TURNO_HORARIO, key,
                idLocal, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        StringRequest requestImage = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response, 3);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                mProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestImage);
    }

    private boolean isValidDate(Calendar calendar) {
        if (fechas != null) {
            for (Vector3 vector3 : fechas) {
                if (calendar.get(Calendar.DAY_OF_MONTH) == vector3.x
                        && calendar.get(Calendar.MONTH) + 1 == vector3.y
                        && calendar.get(Calendar.YEAR) == vector3.z) {
                    return false;
                }
            }
            return true;

        }
        return false;
    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        mProgressBarHorario = findViewById(R.id.progresHorario);
        mCalendarView = findViewById(R.id.calendario);
        mCardView = findViewById(R.id.cardContinuar);
        mProgressBar = findViewById(R.id.progres);
        latDatos = findViewById(R.id.latDatos);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardContinuar:
                if (posicionHorario == -1 && fecha[0] != -1)
                    Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoSeleccionaHorario));
                else openReceptor();
                break;
        }
    }

    private void openReceptor() {
        Intent intent = new Intent(getApplicationContext(), SelectorReceptoresActivity.class);

        if (copiaHorarios != null && turnos != null && fecha[0] != 0 && mConvocatoria != null
        && receptores != null) {

            Horario horario = copiaHorarios.get(posicionHorario);
            ArrayList<Turno> lista = turnos.get(horario.getHoraInicio());
            if (lista == null) {
                lista = new ArrayList<>();
                lista.add(new Turno(0, horario.getHoraInicio()));
            }
            intent.putExtra(Utils.DATA_RESERVA, lista);
            intent.putExtra(Utils.DATA_TURNO, receptores);
            intent.putExtra(Utils.DATA_FECHA, fecha);
            intent.putExtra(Utils.DATA_CONVOCATORIA, mConvocatoria);
            startActivity(intent);
        }else{
            Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoErrorRecargar));
        }
    }
}
