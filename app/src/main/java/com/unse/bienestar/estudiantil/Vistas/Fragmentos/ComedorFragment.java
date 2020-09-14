package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unse.bienestar.estudiantil.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ComedorFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comedor, container, false);

        loadViews();

        loadListener();

        return view;
    }

    private void loadListener() {
        cardView.setOnClickListener(this);
    }

    private void loadViews() {
        cardView = view.findViewById(R.id.card);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.card){
            final String appPackageName = "com.unse.bienestar.comedordos";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

    }

}
