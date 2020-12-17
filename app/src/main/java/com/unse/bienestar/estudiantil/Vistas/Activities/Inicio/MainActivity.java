package com.unse.bienestar.estudiantil.Vistas.Activities.Inicio;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.unse.bienestar.estudiantil.Databases.RolViewModel;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Herramientas.VolleySingleton;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Rol;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Gestion.GestionSistemaActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Perfil.PerfilActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.BecasFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.CiberFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.ComedorFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.DeportesFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.InicioFragmento;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.PoliFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.ResidenciaFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.TransporteFragment;
import com.unse.bienestar.estudiantil.Vistas.Fragmentos.UPAFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static android.view.Gravity.LEFT;
import static android.view.Gravity.START;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    PreferenceManager manager;
    DialogoProcesamiento dialog;
    UsuarioViewModel mUsuarioViewModel;
    RolViewModel mRolViewModel;
    Toolbar mToolbar;
    View headerView;
    Fragment mFragment;
    int idUser = 0;
    ImageView imgPerfil, imgBienestar;
    TextView txtNombre;
    HashMap<String, Integer> ids;
    Double lat, lon;
    public Boolean isReady = false, qrCiber = false, isReadyCiber = false;
    String pat = "", idR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        setToolbar();

        loadData();

        loadListener();

        checkUser();

        comprobarNavigationView();

    }

    private void loadListener() {
    }


    private void loadData() {
        mFragment = new Fragment();
        ids = new HashMap<>();
        ids.put(getString(R.string.itemPerfil), R.id.item_perfil);
        ids.put(getString(R.string.itemSistema), R.id.item_sistema);
        ids.put(getString(R.string.itemNosotros), R.id.item_about);
        ids.put(getString(R.string.itemCondiciones), R.id.item_terminos);
        mRolViewModel = new RolViewModel(getApplicationContext());
        manager = new PreferenceManager(getApplicationContext());
        mUsuarioViewModel = new UsuarioViewModel(getApplicationContext());
    }

    private void loadViews() {
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        headerView = navigationView.inflateHeaderView(R.layout.cabecera_drawer);
        imgPerfil = headerView.findViewById(R.id.imgUserPerfil);
        imgBienestar = headerView.findViewById(R.id.logoBienestar);
        txtNombre = headerView.findViewById(R.id.txtNombreUser);
    }

    private void comprobarNavigationView() {
        if (navigationView != null) {
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        navigationView.setCheckedItem(R.id.item_inicio);
    }

    private void loadProfilePicture() {
        Bitmap bitmap = FileStorageManager.getBitmap(getApplicationContext(), Utils.FOLDER, String.format(Utils.PROFILE_PIC, idUser),
                false);
        if (bitmap != null) {
            Glide.with(imgPerfil.getContext()).load(bitmap).into(imgPerfil);
        } else {
            String URL = String.format("%s%s.jpg", Utils.URL_USUARIO_IMAGE_LOAD, idUser);
            Glide.with(imgPerfil.getContext()).load(URL)
                    .apply(new RequestOptions().error(R.drawable.ic_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_user))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            FileStorageManager.saveBitmap(getApplicationContext(), Utils.FOLDER,
                                    String.format(Utils.PROFILE_PIC,
                                            idUser),
                                    ((BitmapDrawable) resource).getBitmap(), false);
                            return false;
                        }
                    }).into(imgPerfil);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadProfilePicture();
        checkUser();
    }

    private void checkUser() {
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        if (isLogin) {
            idUser = manager.getValueInt(Utils.MY_ID);
            Usuario usuario = mUsuarioViewModel.getById(idUser);
            imgBienestar.setVisibility(View.GONE);
            imgPerfil.setVisibility(View.VISIBLE);
            txtNombre.setVisibility(View.VISIBLE);
            loadProfilePicture();
            txtNombre.setText(String.format("%s %s", usuario.getNombre(), usuario.getApellido()));
        } else {
            imgPerfil.setVisibility(View.GONE);
            imgBienestar.setVisibility(View.VISIBLE);
            txtNombre.setVisibility(View.VISIBLE);
            imgBienestar.setImageResource(R.drawable.ic_logo_bienestar_01);
            txtNombre.setText(getText(R.string.app_name_shor).toString().toUpperCase());
        }
        updateMenu(isLogin);

    }

    private void updateMenu(boolean isLogin) {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.item_sistema);
        Rol rol = mRolViewModel.getByPermission(10);
        if (rol != null && rol.getDescripcion().equals("")) {
            item.setVisible(false);
        }
        item = menu.findItem(R.id.item_perfil);
        if (!isLogin) {
            item.setVisible(false);
        }
    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean isOption = false;
        switch (itemDrawer.getItemId()) {
            case R.id.item_becas:
                fragmentoGenerico = new BecasFragment();
                break;
            case R.id.item_inicio:
                fragmentoGenerico = new InicioFragmento(getSupportFragmentManager());
                break;
            case R.id.item_poli:
                fragmentoGenerico = new PoliFragment(MainActivity.this);
                break;
            case R.id.item_upa:
                fragmentoGenerico = new UPAFragment();
                break;
            case R.id.item_ciber:
                fragmentoGenerico = new CiberFragment();
                ((CiberFragment) fragmentoGenerico).setActivity(MainActivity.this);
                break;
            case R.id.item_deporte:
                fragmentoGenerico = new DeportesFragment(getSupportFragmentManager());
                break;
            case R.id.item_transporte:
                fragmentoGenerico = new TransporteFragment(getSupportFragmentManager());
                ((TransporteFragment) fragmentoGenerico).setActivity(MainActivity.this);
                break;
            case R.id.item_comedor:

                fragmentoGenerico = new ComedorFragment();
                break;
            case R.id.item_resi:
                fragmentoGenerico = new ResidenciaFragment();
                break;
            case R.id.item_perfil:
                isOption = true;
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                break;
            case R.id.item_sistema:
                isOption = true;
                startActivity(new Intent(this, GestionSistemaActivity.class));
                break;
            case R.id.item_about:
                isOption = true;
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.item_terminos:
                isOption = true;
                startActivity(new Intent(this, TermsActivity.class));
                break;
        }


        /*if (!(fragmentoGenerico instanceof InicioFragmento)) {
            boolean isLogin = manager.getValue(Utils.IS_LOGIN);
            if (!isLogin) {
                fragmentoGenerico = new AccesoDenegadoFragment();
            }
        }*/

        if (mFragment != null && fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }
        if (!isOption)
            mFragment = fragmentoGenerico;

        if (!ids.containsKey(itemDrawer.getTitle()))
            ((TextView) findViewById(R.id.txtTitulo)).setText(itemDrawer.getTitle());

    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.GONE);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Bienestar Estudiantíl");
            mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentIntegrator = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentIntegrator != null) {
            if (intentIntegrator.getContents() == null) {
                Utils.showToast(getApplicationContext(), getString(R.string.qrCancelado));

            } else {
                String contenido = intentIntegrator.getContents();
                if (qrCiber)
                    decordeQRCiber(contenido);
                else
                    decodeQR(contenido);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void decordeQRCiber(String contenido) {
        if (contenido.equals("CYB-MAQ"))
            isReadyCiber = true;
        else Utils.showToast(getApplicationContext(), getString(R.string.qrInvalido));
    }

    private void registrarUso() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final String key = manager.getValueString(Utils.TOKEN);
        final int id = manager.getValueInt(Utils.MY_ID);
        String URL = Utils.URL_REGISTRAR_INGRESO;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaCiber(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showToast(getApplicationContext(), getString(R.string.servidorOff));
                dialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("key", key);
                param.put("idU", String.valueOf(id));
                param.put("iu", String.valueOf(id));
                return param;
            }
        };
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void decodeQR(String contenido) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]+");
        Matcher matcher = pattern.matcher(contenido);
        if (matcher.find()) {
            pat = matcher.group();
        }

        pattern = Pattern.compile("-[0-9]+");
        matcher = pattern.matcher(contenido);
        if (matcher.find()) {
            idR = matcher.group();
            idR = idR.substring(1);
        }
        getLatLong();
        isReady = true;
    }

    private void getLatLong() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager)
                getApplicationContext().getSystemService(LOCATION_SERVICE);
        String provider = locationManager != null ?
                locationManager.getBestProvider(criteria, true) : null;
        if (provider != null) {
            Location location = locationManager.getLastKnownLocation(provider);
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReady) {
            isReady = false;
            changeEstado(pat, idR);
        }
        if (isReadyCiber) {
            isReadyCiber = false;
            registrarUso();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mFragment instanceof CiberFragment) ((CiberFragment) mFragment).scanQR();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void changeEstado(String pat, String idR) {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        String key = manager.getValueString(Utils.TOKEN);
        int id = manager.getValueInt(Utils.MY_ID);
        Date date = new Date(System.currentTimeMillis());
        String fecha = Utils.getFechaName(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int anio = calendar.get(Calendar.YEAR);
        String URL = String.format("%s?idU=%s&key=%s&iu=%s&pat=%s&fl=%s&la=%s&lo=%s&ir=%s&d=%s&m=%s&a=%s",
                Utils.URL_PASAJERO_REGISTRAR_SERVICIO, id, key, id, pat, fecha, lat, lon, idR, dia, mes, anio);
        StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                procesarRespuestaCiber(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showCustomToast(MainActivity.this, getApplicationContext(),
                        getString(R.string.servidorOff), R.drawable.ic_error);
                dialog.dismiss();

            }
        });
        //Abro dialogo para congelar pantalla
        dialog = new DialogoProcesamiento();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog_process");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void procesarRespuestaCiber(String response) {
        try {
            dialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            int estado = jsonObject.getInt("estado");
            switch (estado) {
                case -1:
                    Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));
                    break;
                case 1:
                    //Exito
                    showDialogCiber(true);
                    break;
                case 2:
                case 5:
                    showDialogCiber(false);
                    break;
                case 3:
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInvalido));
                    break;
                case 4:
                    Utils.showToast(getApplicationContext(), getString(R.string.camposInvalidos));
                    break;
                case 100:
                    //No autorizado
                    Utils.showToast(getApplicationContext(), getString(R.string.tokenInexistente));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToast(getApplicationContext(), getString(R.string.errorInternoAdmin));

        }
    }

    private void showDialogCiber(boolean b) {
        DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                .setTitulo(getString(b ? R.string.qrEscaneado : R.string.advertencia))
                .setDescripcion(getString(b ? R.string.ciberUsoMaquina : R.string.ciberUsoMaquinaError))
                .setListener(new YesNoDialogListener() {
                    @Override
                    public void yes() {

                    }

                    @Override
                    public void no() {

                    }
                })
                .setTipo(DialogoGeneral.TIPO_ACEPTAR)
                .setIcono(b ? R.drawable.ic_chek : R.drawable.ic_advertencia);
        DialogoGeneral dialogoGeneral = builder.build();
        dialogoGeneral.show(getSupportFragmentManager(), "dialogo");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(LEFT);
        else if (!(mFragment instanceof InicioFragmento)) {
            seleccionarItem(navigationView.getMenu().getItem(0));
            navigationView.setCheckedItem(R.id.item_inicio);
        } else
            super.onBackPressed();
    }

}
