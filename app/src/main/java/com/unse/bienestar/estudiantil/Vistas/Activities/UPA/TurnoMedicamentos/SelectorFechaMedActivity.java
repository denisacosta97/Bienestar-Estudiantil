package com.unse.bienestar.estudiantil.Vistas.Activities.UPA.TurnoMedicamentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.matrix.Vector3;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.RecyclerListener.ItemClickSupport;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
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

public class SelectorFechaMedActivity extends AppCompatActivity implements View.OnClickListener {

    CalendarView mCalendarView;
    RecyclerView mRecyclerView;
    TextView txtFecha;
    CardView cardContinuar, cardSeleccionar;
    HorariosAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Horario> copiaHorarios;
    ProgressBar mProgressBarHorario, mProgressBar;
    LinearLayout latDatos;
    HashMap<String, ArrayList<Turno>> turnos;
    HashMap<String, ArrayList<Horario>> horariosPorServicio;
    ArrayList<Vector3> fechas;
    ArrayList<Turno> horarios;

    boolean errorHorario = false;
    int count, posicionHorario = -1;
    int[] fecha = new int[3];

    CalendarView.OnDateChangeListener calendarListener;

    public static SelectorFechaMedActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_fecha_med);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*if (getIntent().getParcelableExtra(Utils.SERVICIO) != null) {
            mServicio = getIntent().getParcelableExtra(Utils.SERVICIO);
        }*/

        instance = this;

        loadViews();

        loadData();

        loadListener();
    }

    private void loadData() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable progress = mProgressBar.getIndeterminateDrawable();
            progress.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mProgressBar.setIndeterminateDrawable(progress);

            Drawable progress2 = mProgressBarHorario.getIndeterminateDrawable();
            progress2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mProgressBarHorario.setIndeterminateDrawable(progress2);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            cardSeleccionar.setVisibility(View.VISIBLE);
            mCalendarView.setVisibility(View.GONE);
        }else{
            cardSeleccionar.setVisibility(View.GONE);
            mCalendarView.setVisibility(View.VISIBLE);
        }

        latDatos.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarHorario.setVisibility(View.GONE);
        cardContinuar.setVisibility(View.INVISIBLE);

        loadHorarios();

        loadFechas();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        mCalendarView.setMaxDate(calendar.getTime().getTime());
        mCalendarView.setMinDate(new Date(System.currentTimeMillis()).getTime());


        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);

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
        cardSeleccionar.setOnClickListener(this);
        cardContinuar.setOnClickListener(this);
        calendarListener = new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                fecha[0] = calendar.get(Calendar.DAY_OF_MONTH);
                fecha[1] = calendar.get(Calendar.MONTH) + 1;
                fecha[2] = calendar.get(Calendar.YEAR);
                if (!Utils.isDateHabilited(calendar) && !isValidDate(calendar)) {
                    getHorarios(calendar);
                } else {
                    checkDay(calendar);
                }
            }
        };
        mCalendarView.setOnDateChangeListener(calendarListener);
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

    private boolean isValidDate(Calendar calendar) {
        if (fechas != null) {
            for (Vector3 vector3 : fechas) {
                if (calendar.get(Calendar.DAY_OF_MONTH) == vector3.x
                        && calendar.get(Calendar.MONTH) + 1 == vector3.y
                        && calendar.get(Calendar.YEAR) == vector3.z) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void loadHorarios() {
        String URL = Utils.URL_HORARIOS_MEDICAM;
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

    private void procesarRespuesta(String response, int tipo) {
        try {

            if (tipo == 2) {
                mProgressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(response);
                int id = 1;
                JSONObject datos = jsonObject.getJSONObject(String.valueOf(id));
                JSONArray dias = datos.getJSONArray("d");
                horariosPorServicio = new HashMap<>();
                for (int i = 0; i < dias.length(); i++) {
                    JSONObject object = dias.getJSONObject(i);
                    String dia = object.getString("n");
                    JSONArray rangos = object.getJSONArray("h");
                    for (int j = 0; j < rangos.length(); j++) {
                        Horario horario = new Horario(i, 0, rangos.getString(j), "");
                        if (horariosPorServicio.containsKey(dia)) {
                            horariosPorServicio.get(dia).add(horario);
                        } else {
                            ArrayList<Horario> list = new ArrayList<>();
                            list.add(horario);
                            horariosPorServicio.put(dia, list);
                        }
                    }
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
                        Utils.showToast(getApplicationContext(), getString(R.string.noData));
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
                        String dia = getNameDay();
                        if (horariosPorServicio.get(dia) != null) {
                            copiaHorarios.addAll(horariosPorServicio.get(dia));
                            turnos = new HashMap<>();
                            reset();
                            adapter = new HorariosAdapter(copiaHorarios, getApplicationContext());
                            mRecyclerView.setAdapter(adapter);



                            mRecyclerView.setVisibility(View.VISIBLE);
                            cardContinuar.setVisibility(View.VISIBLE);

                            checkDay(null);
                        }

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

    private void checkDay(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, fecha[0]);
            calendar.set(Calendar.MONTH, fecha[1] - 1);
            calendar.set(Calendar.YEAR, fecha[2]);
        }
       /* Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, fecha[0]);
        calendar.set(Calendar.MONTH, fecha[1] - 1);
        calendar.set(Calendar.YEAR, fecha[2]);*/
        if (Utils.isDateHabilited(calendar) || isValidDate(calendar)) {
            Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoNoDia));
            mProgressBarHorario.setVisibility(View.GONE);
            cardContinuar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.GONE);
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

                        Turno turno = Turno.mapper(object, Turno.LOW_2);

                        horarios.add(turno);

                    }
                    copiaHorarios = new ArrayList<>();

                    String dia = getNameDay();

                    if (horariosPorServicio.get(dia) != null) {
                        for (Horario horario : horariosPorServicio.get(dia)) {
                            boolean isReserved = false;

                            for (Turno t : horarios) {
                                if (t.getFechaInicio().equals(horario.getHoraInicio())) {
                                    isReserved = true;
                                }
                            }
                            if (!isReserved)
                                copiaHorarios.add(horario);
                        }
                    }


                    adapter = new HorariosAdapter(copiaHorarios, getApplicationContext());
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    cardContinuar.setVisibility(View.VISIBLE);

                    checkDay(null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private String getNameDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, fecha[0]);
        calendar.set(Calendar.MONTH, fecha[1] - 1);
        calendar.set(Calendar.YEAR, fecha[2]);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return "L";
            case Calendar.TUESDAY:
                return "M";
            case Calendar.WEDNESDAY:
                return "Y";
            case Calendar.THURSDAY:
                return "J";
            case Calendar.FRIDAY:
                return "V";
            default:
                return "0";
        }

    }

    private void getHorarios(Calendar calendar) {
        mProgressBarHorario.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        cardContinuar.setVisibility(View.INVISIBLE);
        fecha[0] = calendar.get(Calendar.DAY_OF_MONTH);
        fecha[1] = calendar.get(Calendar.MONTH) + 1;
        fecha[2] = calendar.get(Calendar.YEAR);
        txtFecha.setText(String.format("SELECCIONAR FECHA\n\n%02d/%02d/%s", fecha[0], fecha[1], fecha[2]));
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int idLocal = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s?key=%s&idU=%s&di=%s&me=%s&an=%s", Utils.URL_TUNO_HORARIO_MEDICAM, key,
                idLocal, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
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

    private void loadViews() {
        txtFecha = findViewById(R.id.txtFecha);
        cardSeleccionar = findViewById(R.id.cardFecha);
        mRecyclerView = findViewById(R.id.recycler);
        mProgressBarHorario = findViewById(R.id.progresHorario);
        mCalendarView = findViewById(R.id.calendario);
        cardContinuar = findViewById(R.id.cardContinuar);
        latDatos = findViewById(R.id.latDatos);
        mProgressBar = findViewById(R.id.progres);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardFecha:
                openDialog();
                break;
            case R.id.cardContinuar:
                if (posicionHorario == -1)
                    Utils.showToast(getApplicationContext(), getString(R.string.becaTurnoSeleccionaHorario));
                else openFinal();
                break;
        }
    }

    private void openDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                fecha[0] = calendar.get(Calendar.DAY_OF_MONTH);
                fecha[1] = calendar.get(Calendar.MONTH) + 1;
                fecha[2] = calendar.get(Calendar.YEAR);
                if (!Utils.isDateHabilited(calendar) && !isValidDate(calendar)) {
                    txtFecha.setText(String.format("SELECCIONAR FECHA\n\n%02d/%02d/%s", dayOfMonth, monthOfYear + 1, year));
                    getHorarios(calendar);
                } else {
                    checkDay(calendar);
                }

            }

        };

        new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void openFinal() {
        Intent intent = new Intent(getApplicationContext(), MedicamentosActivity.class);
        intent.putExtra(Utils.DATA_FECHA, fecha);
        Horario horario = copiaHorarios.get(posicionHorario);
        //Convocatoria convocatoria = new Convocatoria(, 0, mServicio.getName(), null, null);
        intent.putExtra(Utils.DATA_RESERVA, horario.getHoraInicio());
        //intent.putExtra(Utils.DATA_CONVOCATORIA, convocatoria);
        startActivity(intent);
    }

}