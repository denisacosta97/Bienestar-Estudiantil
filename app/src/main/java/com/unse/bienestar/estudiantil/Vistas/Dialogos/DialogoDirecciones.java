package com.unse.bienestar.estudiantil.Vistas.Dialogos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoDirecciones extends DialogFragment {

    View view;
    TextView txtTitulo;
    EditText edtDire;
    OnClickListenerAdapter mListener;
    Button btnAceptar;
    Context mContext;
    String titulo;

    public DialogoDirecciones(String msj, OnClickListenerAdapter listener, Context context) {
        titulo = msj;
        mListener = listener;
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_dire, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Esto es lo nuevoooooooo, evita los bordes cuadrados
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        return view;
    }

    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        edtDire = view.findViewById(R.id.edtDire);
        txtTitulo = view.findViewById(R.id.txtTitulo);
    }

    private void loadData() {
        txtTitulo.setText(titulo);
    }


    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtDire.getText().toString().equals("")) {
                    dismiss();
                    mListener.onClick(edtDire.getText().toString().toUpperCase());

                } else {
                    Utils.showToast(mContext, getString(R.string.camposVacios));
                }
            }
        });

    }

}
