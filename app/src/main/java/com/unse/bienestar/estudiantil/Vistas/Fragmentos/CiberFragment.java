package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Inicio.MainActivity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class CiberFragment extends Fragment implements View.OnClickListener  {

    View view;
    CardView cardScanner;
    Activity mActivity;

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
        ((MainActivity)mActivity).qrCiber = true;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardScanner){
            scanQR();
        }
    }
}