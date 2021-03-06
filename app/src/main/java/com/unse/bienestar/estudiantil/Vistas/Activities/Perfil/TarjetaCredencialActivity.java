package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Credencial.CredencialView;
import com.unse.bienestar.estudiantil.Herramientas.Credencial.OnSwipeTouchListener;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Alumno;
import com.unse.bienestar.estudiantil.Modelos.CredencialBeca;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.Modelos.CredencialSocio;
import com.unse.bienestar.estudiantil.Modelos.CredencialTorneo;
import com.unse.bienestar.estudiantil.R;

import org.slf4j.helpers.Util;

import androidx.appcompat.app.AppCompatActivity;

public class TarjetaCredencialActivity extends AppCompatActivity {

    TextView mDeporte, mApellido, mNombre, mFacultad, mLegajo, mNombreEquipo,
            mAnio, txtRol, txtFecha, txtTitulo, txtSexo, txtDni, txtNombreEquipo, txtDeporte;
    ImageView mFoto;
    Button btnAceptar;
    View mView;
    LinearLayout mLayout;
    Alumno mAlumno;

    Object mCredencial;
    CredencialView mCredencialView;

    LinearLayout layDeporte;

    int tipo = 0;

    boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta_credencial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().getIntExtra(Utils.TIPO_CREDENCIAL, -1) != -1) {
            tipo = getIntent().getIntExtra(Utils.TIPO_CREDENCIAL, -1);
        }
        if (getIntent().getParcelableExtra(Utils.TIPO_CREDENCIAL_DATO) != null) {
            mCredencial = getIntent().getParcelableExtra(Utils.TIPO_CREDENCIAL_DATO);
        }
        if (getIntent().getParcelableExtra(Utils.ALUMNO_NAME) != null) {
            mAlumno = getIntent().getParcelableExtra(Utils.ALUMNO_NAME);
        }
        if (mCredencial != null) {
            loadViews();

            updateView();

            loadData();

            loadListener();
        } else {
            Utils.showToast(getApplicationContext(), getString(R.string.noData));
            finish();
        }


    }

    private void createImage() {
        Bitmap bitmap = getBitmapFromView(mLayout);
        bitmap = Utils.resize(bitmap, 600, 375);
        FileStorageManager.saveBitmap(getApplicationContext(), Utils.FOLDER_CREDENCIALES, getFileName(), bitmap, true);
        Utils.showToast(getApplicationContext(), getString(R.string.credencialGuardada));
    }

    private String getFileName() {
        String file = "";
        int id = new PreferenceManager(getApplicationContext()).getValueInt(Utils.MY_ID);
        switch (tipo) {
            case 1:
                return Utils.limpiarAcentos(String.format("B_%s_%s", " ", id)).toUpperCase().replaceAll(" - ",
                        " ").replaceAll(" ", "_") + ".png";
            case 2:
                return Utils.limpiarAcentos(String.format("D_%s_%s", ((CredencialDeporte) mCredencial).getNombre(), id))
                        .toUpperCase().replaceAll(" ", "_") + ".png";
            case 3:
                return Utils.limpiarAcentos(String.format("T_%s_%s", " ", id)).toUpperCase().replaceAll(" - ",
                        " ").replaceAll(" ", "_") + ".png";
            case 4:
                return Utils.limpiarAcentos(String.format("S_%s_%s", " ", id)).toUpperCase().replaceAll(" ",
                        "_") + ".png";
        }
        return "";
    }

    private Bitmap getBitmapFromView(View layout) {
        Bitmap returnBitmap = null;
        if (layout.getMeasuredHeight() < 0) {
            returnBitmap = Bitmap.createBitmap(layout.getLayoutParams().width, layout.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        } else {
            //layout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            returnBitmap = Bitmap.createBitmap(layout.getMeasuredWidth(), layout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(returnBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        if (layout.getMeasuredHeight() > 0)
            layout.layout(layout.getLeft(), layout.getTop(), layout.getRight(), layout.getBottom());
        else
            layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredWidth());
        layout.draw(canvas);
        return returnBitmap;
    }

    private void updateView() {
        switch (tipo) {
            case 1:
                //Beca
                mView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.credencial_beca, null);
                mView.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                );
                mLayout.addView(mView);
                txtTitulo = mView.findViewById(R.id.txtTitulo);
                mApellido = mView.findViewById(R.id.txtApellido);
                mNombre = mView.findViewById(R.id.txtNombre);
                mFacultad = mView.findViewById(R.id.txtFacultad);
                mLegajo = mView.findViewById(R.id.txtLegajo);
                txtFecha = mView.findViewById(R.id.txtFecha);
                mAnio = mView.findViewById(R.id.txtAnio);
                txtDni = mView.findViewById(R.id.txtDni);
                mFoto = mView.findViewById(R.id.imgUser);
                Utils.changeColor(mView.findViewById(R.id.topB).getBackground(), getApplicationContext(), R.color.colorYellow);
                Utils.changeColor(mView.findViewById(R.id.bottomB).getBackground(), getApplicationContext(), R.color.colorYellow);

                break;
            case 2:
                mView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.credencial_deporte, null);
                mView.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                );
                mLayout.addView(mView);
                mDeporte = mView.findViewById(R.id.txtDeporte);
                mApellido = mView.findViewById(R.id.txtApellido);
                mNombre = mView.findViewById(R.id.txtNombre);
                mFacultad = mView.findViewById(R.id.txtFacultad);
                mLegajo = mView.findViewById(R.id.txtLegajo);
                mNombreEquipo = mView.findViewById(R.id.txtNombreEquipo);
                mAnio = mView.findViewById(R.id.txtAnio);
                mFoto = mView.findViewById(R.id.imgUser);
                Utils.changeColor(mView.findViewById(R.id.topB).getBackground(), getApplicationContext(), R.color.colorPrimaryDark);
                Utils.changeColor(mView.findViewById(R.id.bottomB).getBackground(), getApplicationContext(), R.color.colorPrimaryDark);
                break;
            case 3:
                //Torneo
                mView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.credencial_torneo, null);
                mView.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                );
                mLayout.addView(mView);
                txtTitulo = mView.findViewById(R.id.txtTitulo);
                txtRol = mView.findViewById(R.id.txtRol);
                mApellido = mView.findViewById(R.id.txtApellido);
                mNombre = mView.findViewById(R.id.txtNombre);
                mAnio = mView.findViewById(R.id.txtAnio);
                mFoto = mView.findViewById(R.id.imgUser);
                txtNombreEquipo = mView.findViewById(R.id.txtNombreEquipo);
                txtDni = mView.findViewById(R.id.txtDni);
                Utils.changeColor(mView.findViewById(R.id.topB).getBackground(), getApplicationContext(), R.color.colorGreen);
                Utils.changeColor(mView.findViewById(R.id.bottomB).getBackground(), getApplicationContext(), R.color.colorGreen);
                break;
            case 4:
                mView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.credencial_socio, null);
                mView.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                );
                mLayout.addView(mView);
                txtTitulo = mView.findViewById(R.id.txtTitulo);
                txtFecha = mView.findViewById(R.id.txtFecha);
                txtRol = mView.findViewById(R.id.txtRol);
                mApellido = mView.findViewById(R.id.txtApellido);
                mNombre = mView.findViewById(R.id.txtNombre);
                mAnio = mView.findViewById(R.id.txtAnio);
                mFoto = mView.findViewById(R.id.imgUser);
                txtSexo = mView.findViewById(R.id.txtSexo);
                txtDni = mView.findViewById(R.id.txtDni);
                Utils.changeColor(mView.findViewById(R.id.topB).getBackground(), getApplicationContext(), R.color.colorFCEyT);
                Utils.changeColor(mView.findViewById(R.id.bottomB).getBackground(), getApplicationContext(), R.color.colorFCEyT);
                break;
        }
    }

    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImage();
            }
        });

        OnSwipeTouchListener.OnGestureListener gestureListener = new OnSwipeTouchListener.OnGestureListener() {
            @Override
            public void onSwipeRight() {
                //if (isBack)
                    //onMove();
            }

            @Override
            public void onSwipeLeft() {
               // if (!isBack)
                    //onMove();
            }

            @Override
            public void onSwipeTop() {

            }

            @Override
            public void onSwipeBottom() {

            }
        };
        OnSwipeTouchListener listener = new OnSwipeTouchListener(getApplicationContext(), gestureListener);
        mCredencialView.setOnTouchListener(listener);
    }

    private void onMove() {
        if (isBack) {
            mCredencialView.showFront();
            isBack = false;
        } else {
            mCredencialView.showBack();
            isBack = true;
        }
    }

    private void loadData() {
        int id = new PreferenceManager(getApplicationContext()).getValueInt(Utils.MY_ID);
        String URL = String.format("%s%s.jpg", Utils.URL_USUARIO_IMAGE_LOAD, id);
        Bitmap bitmap = FileStorageManager.getBitmap(getApplicationContext(), Utils.FOLDER,
                String.format(Utils.PROFILE_PIC, id), false);
        switch (tipo) {
            case 1:
                //Beca
                CredencialBeca credencialBeca = (CredencialBeca) mCredencial;
                txtTitulo.setText(String.format("AUTORIZACION #%s", credencialBeca.getId()));
                mApellido.setText(credencialBeca.getApellidoU());
                mNombre.setText(credencialBeca.getNombreU());
                mFacultad.setText(credencialBeca.getFacultad());
                mLegajo.setText(credencialBeca.getLegajo());
                txtFecha.setText(credencialBeca.getFecha());
                mAnio.setText(String.valueOf(credencialBeca.getAnio()));
                txtDni.setText(String.valueOf(credencialBeca.getIdUsuario()));
                Glide.with(mFoto.getContext()).load(URL).apply(new RequestOptions().error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.ic_user)).into(mFoto);

                break;
            case 2:
                CredencialDeporte credencialDeporte = (CredencialDeporte) mCredencial;
                mDeporte.setText(credencialDeporte.getNombre());
                mApellido.setText(credencialDeporte.getApellido());
                mNombre.setText(credencialDeporte.getNombreU());
                mNombreEquipo.setText("NO ASIGNADO");
                mAnio.setText(String.valueOf(credencialDeporte.getAnio()));
                mLegajo.setText(String.valueOf(mAlumno != null ? mAlumno.getLegajo() : "NO ASIGNADO"));
                mFacultad.setText(String.valueOf(mAlumno != null ? mAlumno.getFacultad() : "NO ASIGNADO"));
                Glide.with(mFoto.getContext()).load(bitmap != null ? bitmap : URL)
                        .apply(new RequestOptions().error(R.drawable.ic_user)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.ic_user)).into(mFoto);
                break;
            case 3:
                //Torneo
                CredencialTorneo credencialTorneo = (CredencialTorneo) mCredencial;
                txtTitulo.setText(credencialTorneo.getNombreDeporte());
                txtNombreEquipo.setText(credencialTorneo.getDescripcion());
                txtRol.setText(Utils.getTipoUser(credencialTorneo.getTipoUsuario()));
                mApellido.setText(credencialTorneo.getApellido());
                mNombre.setText(credencialTorneo.getNombreUsuario());
                mAnio.setText(String.valueOf(credencialTorneo.getAnio()));
                txtDni.setText(String.valueOf(credencialTorneo.getIdUsuario()));
                Glide.with(mFoto.getContext()).load(URL).apply(new RequestOptions().error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.ic_user)).into(mFoto);
                break;
            case 4:
                CredencialSocio credencialSocio = (CredencialSocio) mCredencial;
                txtTitulo.setText(String.format("%s #%s", "CREDENCIAL", credencialSocio.getId()));
                txtFecha.setText(credencialSocio.getFechaRegistro());
                txtRol.setText(Utils.getTipoUser(credencialSocio.getTipoUsuario()));
                mApellido.setText(credencialSocio.getApellido());
                mNombre.setText(credencialSocio.getNombre());
                mAnio.setText(String.valueOf(credencialSocio.getAnio()));
                txtDni.setText(String.valueOf(credencialSocio.getIdUsuario()));
                Glide.with(mFoto.getContext()).load(URL).apply(new RequestOptions().error(R.drawable.ic_user).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.ic_user)).into(mFoto);
                txtSexo.setText(credencialSocio.getSexo());
                break;
        }
    }

    private void loadViews() {
        View view1 = findViewById(R.id.front_card_container);
        View view2 = findViewById(R.id.containerCard);
        mLayout = (LinearLayout) view2;
        btnAceptar = findViewById(R.id.btnAceptar);
        mCredencialView = findViewById(R.id.credencial);
    }
}
