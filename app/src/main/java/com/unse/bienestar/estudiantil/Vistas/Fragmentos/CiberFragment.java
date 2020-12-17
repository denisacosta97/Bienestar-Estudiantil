package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Inicio.MainActivity;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoGeneral;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.PERMISSION_ALL;
import static com.unse.bienestar.estudiantil.Herramientas.Utils.REQUEST_GROUP_PERMISSIONS_LOCATION;

public class CiberFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardScanner;
    Activity mActivity;
    FragmentManager mFragmentManagerM;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ciber, container, false);

        loadViews();

        loadData();

        return view;
    }

    private void loadData() {
        cardScanner.setOnClickListener(this);
    }

    private void loadViews() {
        cardScanner = view.findViewById(R.id.cardScanner);
    }

    public void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(mActivity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
        ((MainActivity) mActivity).qrCiber = true;
    }

    public void setFragmentManagerM(FragmentManager fragmentManagerM) {
        mFragmentManagerM = fragmentManagerM;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        PreferenceManager manager = new PreferenceManager(getContext());
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        switch (v.getId()) {
            case R.id.cardScanner:
                if (isLogin)
                    if (Utils.isPermissionGranted(getContext(), Manifest.permission.CAMERA)) {
                        //Utils.showToast(getContext(), getString(R.string.noDisponible));
                        scanQR();
                    } else {
                        checkPermission();
                    }
                else Utils.showToast(getContext(), getString(R.string.primeroRegistrar));
                break;
        }
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            scanQR();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent();
            intent.setAction(ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_GROUP_PERMISSIONS_LOCATION);
            Utils.showToast(getContext(), "Por favor, autoriza el permiso de cámara");

        } else {
            DialogoGeneral.Builder builder = new DialogoGeneral.Builder(getContext())
                    .setTitulo(getString(R.string.permisoNecesario))
                    .setIcono(R.drawable.ic_advertencia)
                    .setDescripcion(getString(R.string.permisoCamara))
                    .setTipo(DialogoGeneral.TIPO_ACEPTAR_CANCELAR)
                    .setListener(new YesNoDialogListener() {
                        @Override
                        public void yes() {
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSION_ALL);
                        }

                        @Override
                        public void no() {
                        }
                    });
            DialogoGeneral dialogoGeneral = builder.build();
            dialogoGeneral.show(mFragmentManagerM, "dialogo_permiso");
        }
    }
}