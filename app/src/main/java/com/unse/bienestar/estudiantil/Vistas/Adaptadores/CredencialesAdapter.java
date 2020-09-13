package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.CredencialDeporte;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CredencialesAdapter extends RecyclerView.Adapter<CredencialesAdapter.CredencialViewHolder> {

    private ArrayList<Object> mArchivos;
    private Context context;
    private boolean isInscripcion = false;
    private OnClickListenerAdapter mOnClickListenerAdapter;
    private int tipo;

    public static final int DEPORTE = 1;

    public OnClickListenerAdapter getOnClickListenerAdapter() {
        return mOnClickListenerAdapter;
    }

    public void setOnClickListenerAdapter(OnClickListenerAdapter onClickListenerAdapter) {
        mOnClickListenerAdapter = onClickListenerAdapter;
    }

    public CredencialesAdapter(ArrayList<Object> list, Context ctx, boolean isInscripcion, int tipo) {
        this.mArchivos = list;
        this.context = ctx;
        this.isInscripcion = isInscripcion;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public CredencialesAdapter.CredencialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (mArchivos.size() == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacio, parent, false);
        } else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credenciales, parent, false);

        return new CredencialesAdapter.CredencialViewHolder(view, mOnClickListenerAdapter);
    }

    public void setList(ArrayList<Object> credencials) {
        this.mArchivos = credencials;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final CredencialesAdapter.CredencialViewHolder holder, int position) {

        if (mArchivos.size() != 0) {
            switch (tipo) {
                case DEPORTE:
                    CredencialDeporte credencial = getItem(position, DEPORTE);
                    holder.mNumArchivo.setText(String.format("%s/%s", credencial.getIdInscripcion(), String.valueOf(
                            credencial.getAnio()
                    ).substring(2)));
                    holder.txtTitulo.setText(String.format("%s %s", credencial.getNombre(),
                            String.valueOf(credencial.getAnio()).substring(2)));
                    if (credencial.getValidez() == 0) {
                        Glide.with(holder.imgIcono.getContext()).load(R.drawable.ic_error).into(holder.imgIcono);
                    } else {
                        Glide.with(holder.imgIcono.getContext()).load(R.drawable.ic_chek).into(holder.imgIcono);
                    }
                    holder.txtEstado.setVisibility(View.GONE);
                    break;
            }

            if (!isInscripcion) {

            } else {

            }
        } else {
            holder.txtTitulo.setText("CREDENCIALES NO ASIGNADAS");
        }


    }

    private CredencialDeporte getItem(int position, int tipo) {
        switch (tipo) {
            case DEPORTE:
                return (CredencialDeporte) mArchivos.get(position);

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mArchivos.size() == 0 ? 1 : mArchivos.size();
    }

    static class CredencialViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, mNumArchivo, txtEstado;
        ImageView imgIcono;


        CredencialViewHolder(View itemView, final OnClickListenerAdapter listene) {
            super(itemView);

            mNumArchivo = itemView.findViewById(R.id.txtId);
            txtTitulo = itemView.findViewById(R.id.txtDescripcion);
            imgIcono = itemView.findViewById(R.id.imgEstado);
            txtEstado = itemView.findViewById(R.id.txtEstado);


        }
    }

}
