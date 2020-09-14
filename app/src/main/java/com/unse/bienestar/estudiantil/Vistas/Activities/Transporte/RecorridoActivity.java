package com.unse.bienestar.estudiantil.Vistas.Activities.Transporte;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Modelos.Paradas;
import com.unse.bienestar.estudiantil.Modelos.Punto;
import com.unse.bienestar.estudiantil.Modelos.Recorrido;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RecorridoActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap map;
    private ArrayList<LatLng> points;
    private ArrayList<Marker> listMarker;
    private ArrayList<Paradas> paradas;
    private ArrayList<Punto> mPuntos;
    private Recorrido mRecorridos;
    private PolylineOptions polylineOptions;
    private CardView buttonIda;
    private SupportMapFragment mapFragment;
    DialogoProcesamiento dialog;
    ImageView imgIcono;

    private int id, colorPolyline;
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.RECORRIDO) != null) {
            mRecorridos = getIntent().getParcelableExtra(Utils.RECORRIDO);
        }

        loadViews();

        loadData();

        loadListener();

        loadInfo();

        setToolbar();
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) findViewById(R.id.txtTitulo)).setText(mRecorridos.getDescripcion());
        Utils.changeColorDrawable(imgIcono, getApplicationContext(), R.color.colorPrimary);
    }

    private void loadData() {
        points = new ArrayList<LatLng>();
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }


    private void loadListener() {
        buttonIda.setOnClickListener(this);
        imgIcono.setOnClickListener(this);
    }


    private void loadViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        imgIcono = findViewById(R.id.imgFlecha);
        buttonIda = findViewById(R.id.btnida);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 20) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night_mode));
            colorPolyline = 1;
        } else if (timeOfDay < 8) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night_mode));
            colorPolyline = 1;
        } else
            colorPolyline = 0;

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Utils.isPermissionGranted(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    && Utils.isPermissionGranted(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION))
                map.setMyLocationEnabled(true);
        }
        String provider = locationManager != null ? locationManager.getBestProvider(criteria, true) : null;
        LatLng sde = null;
        if (provider != null) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                sde = new LatLng(location.getLatitude(), location.getLongitude());

            } else {
                sde = new LatLng(-27.791156, -64.250532);
            }
        } else {
            sde = new LatLng(-27.791156, -64.250532);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sde, 15));
    }

    private void loadInfo() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        String URL = String.format("%s", Utils.URL_RECORRIDOS_DATOS);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuesta(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();

            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuesta(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            loadInfo(jsonObject, mRecorridos.getIdRecorrido());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void loadInfo(JSONObject jsonObject, int idRecorrido) {
        try {
            JSONObject recorridos = jsonObject.getJSONObject("recorridos");
            JSONObject par = jsonObject.getJSONObject("paradas");
            JSONArray puntos = null, puntitos = null;
            switch (idRecorrido) {
                case 1:
                    JSONObject up = recorridos.getJSONObject("1");
                    puntos = up.getJSONArray("p");

                    JSONObject prdaUP = par.getJSONObject("1");
                    puntitos = prdaUP.getJSONArray("p");
                    break;
                case 2:
                    JSONObject upp = recorridos.getJSONObject("2");
                    puntos = upp.getJSONArray("p");

                    JSONObject prdaUPP = par.getJSONObject("2");
                    puntitos = prdaUPP.getJSONArray("p");
                    break;
                case 3:
                    JSONObject u_z = recorridos.getJSONObject("3");
                    puntos = u_z.getJSONArray("p");

                    JSONObject prdaUZ = par.getJSONObject("3");
                    puntitos = prdaUZ.getJSONArray("p");
                    break;
            }

            mPuntos = new ArrayList<>();
            paradas = new ArrayList<>();

            for (int i = 0; i < puntos.length(); i++) {

                JSONArray o = puntos.getJSONArray(i);
                double lat = o.getDouble(1);
                double lon = o.getDouble(0);

                Punto pnto = new Punto();
                pnto.setIdRecorrido(idRecorrido);
                pnto.setId(i);
                pnto.setFechaRecepcion(String.valueOf(0));
                pnto.setLatitud(lat);
                pnto.setLongitud(lon);
                mPuntos.add(pnto);
            }

            for (int i = 0; i < puntitos.length(); i++) {

                JSONObject o = puntitos.getJSONObject(i);

                JSONArray latlng = o.getJSONArray("p");
                double lat = latlng.getDouble(1);
                double lon = latlng.getDouble(0);

                Paradas p = new Paradas();
                p.setIdRecorrido(idRecorrido);
                p.setIdParada(i);
                p.setLat(lat);
                p.setLon(lon);
                p.setTipo(0);
                p.setDescripcion(o.getString("d"));

                paradas.add(p);
            }

            loadPuntos();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadPoints(int color) {

        polylineOptions = new PolylineOptions();
        if (color == 1)
            polylineOptions.color(Color.WHITE);
        else
            polylineOptions.color(Color.BLACK);

        if (mPuntos != null && mPuntos.size() > 0) {
            convertToLatLong(mPuntos);
            polylineOptions.width(10);
            polylineOptions.addAll(points);
            map.addPolyline(polylineOptions);
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.recorridoError));
        }

        //Inicio
        /*int height = 150;
        int width = 150;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_transporte);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        //Fin
        BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_transporte);
        Bitmap b2 = bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);

        if (points.size() != 0) {
            map.addMarker(new MarkerOptions()
                    .position(points.get(0))
                    .draggable(false)
                    .title("Origen")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            map.addMarker(new MarkerOptions()
                    .position(points.get(points.size() - 1))
                    .draggable(false)
                    .title("Destino")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2)));
        }*/

    }

    private void convertToLatLong(ArrayList<Punto> puntos) {
        LatLng pnto = null;
        for (int i = 0; i < puntos.size(); i++) {
            pnto = new LatLng(puntos.get(i).getLatitud(), puntos.get(i).getLongitud());
            points.add(pnto);
        }
    }

    private void loadParadas() {
        listMarker = new ArrayList<>();

        int height = 140;
        int width = 140;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_parada);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if (paradas.size() != 0) {
            for (Paradas p : paradas) {
                LatLng latLng = new LatLng(p.getLat(), p.getLon());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(p.getDescripcion())
                        .snippet(p.getDescripcion()).
                                icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                Marker marker = map.addMarker(markerOptions);
                marker.setTag(p.getIdParada());
                listMarker.add(marker);

                /*map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.custom_window_recorridos, null);

                        TextView calle = v.findViewById(R.id.txtCalle);
                        String ca = marker.getTitle();
                        calle.setText(ca);
                        TextView calle2 = v.findViewById(R.id.txtEntre);
                        String ca2 = marker.getSnippet();
                        calle2.setText(ca2);
                        TextView ver = v.findViewById(R.id.txtVerif);
                        ver.setText(verif);

                        return v;
                    }
                });*/

            }
        } else {

            Utils.showCustomToast(RecorridoActivity.this, getApplicationContext(),
                    "Error al cargar paradas", R.drawable.ic_error);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnida:
                loadPuntos();
                break;
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }

    }

    private void loadPuntos() {
        map.clear();
        loadPoints(colorPolyline);
        loadParadas();
    }
}
