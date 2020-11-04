package com.unse.bienestar.estudiantil.Vistas.Activities.Gestion.GestionNoticias;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Noticia;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class NoticiaLectorActivity extends AppCompatActivity implements View.OnClickListener {

    Noticia mNoticia;
    TextView etiqueta, titulo, cuerpo, fecha, hora;
    ImageView imgFlecha, imgFoto;
    LinearLayout linlayShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getParcelableExtra(Utils.NOTICIA) != null) {
            mNoticia = getIntent().getParcelableExtra(Utils.NOTICIA);
        }

        if (mNoticia != null) {

            setToolbar();

            loadViews();

            loadData();

            loadListener();

            Utils.changeColorDrawable(imgFlecha, getApplicationContext(), R.color.colorPrimary);

        } else {
            Utils.showToast(getApplicationContext(), "Error al abrir la noticia, vuelta a intentar");
            finish();
        }

    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ((TextView) findViewById(R.id.txtTitulo)).setText("");
    }

    private void loadListener() {
        imgFlecha.setOnClickListener(this);
        linlayShare.setOnClickListener(this);
    }

    private void loadData() {
        etiqueta.setText(mNoticia.getNombreArea());
        titulo.setText(mNoticia.getTitulo());
        cuerpo.setText(Utils.getText(mNoticia.getDescripcion()));
        fecha.setText(Utils.getBirthday(Utils.getFechaDateWithHour(mNoticia.getFechaRegistro())));
        hora.setText(Utils.getTime(mNoticia.getFechaRegistro()));
        String URL = String.format(Utils.URL_IMAGE_NOTICIA, mNoticia.getImagen());
        Glide.with(imgFoto.getContext())
                .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(URL)
                .into(imgFoto);

    }

    private void loadViews() {
        etiqueta = findViewById(R.id.txtEtiqueta);
        titulo = findViewById(R.id.txtTitulo2);
        cuerpo = findViewById(R.id.txtCuerpo);
        fecha = findViewById(R.id.txtFecha);
        hora = findViewById(R.id.txtHoraPublicado);
        imgFlecha = findViewById(R.id.imgFlecha);
        imgFoto = findViewById(R.id.imgPortada);
        linlayShare = findViewById(R.id.linlayShare);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.linlayShare:
                if (imgFoto != null)
                    checkPermission();
                else
                    Toast.makeText(this, "No es posible compartir esta noticia.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.onShare(mNoticia, NoticiaLectorActivity.this, imgFoto);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Utils.onShare(mNoticia, NoticiaLectorActivity.this, imgFoto);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(NoticiaLectorActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_GROUP_PERMISSIONS_LOCATION);
            Utils.showToast(getApplicationContext(), "Por favor, autoriza el permiso de almacenamiento");

        } else {
            DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getApplicationContext())
                    .setTitulo(getString(R.string.permisoNecesario))
                    .setIcono(R.drawable.ic_advertencia)
                    .setDescripcion(getString(R.string.permisosFoto))
                    .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                    .setListener(new YesNoDialogListener() {
                        @Override
                        public void yes() {
                            ActivityCompat.requestPermissions(NoticiaLectorActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_ALL);
                        }

                        @Override
                        public void no() {
                        }
                    });
            DialogoGeneral dialogoGeneral = builder.build();
            dialogoGeneral.show(getSupportFragmentManager(), "dialogo_permiso");
        }
    }
}
