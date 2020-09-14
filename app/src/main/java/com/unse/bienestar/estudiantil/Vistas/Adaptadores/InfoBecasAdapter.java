package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.FontChangeUtil;
import com.unse.bienestar.estudiantil.Modelos.InfoBecas;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InfoBecasAdapter extends RecyclerView.Adapter<InfoBecasAdapter.EventosViewHolder>{

    private ArrayList<InfoBecas> mInfoBecas;
    private Context context;
    View view;

    public InfoBecasAdapter(ArrayList<InfoBecas> list, Context ctx) {
        this.mInfoBecas = list;
        this.context = ctx;
    }

    @NonNull
    @Override
    public InfoBecasAdapter.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_becas, parent, false);

        return new InfoBecasAdapter.EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoBecasAdapter.EventosViewHolder holder, int position) {
        InfoBecas torneo = mInfoBecas.get(position);

        holder.mIcon.setImageResource(torneo.getIcon());
        holder.mNameBeca.setText(torneo.getNameBeca());
    }


    @Override
    public long getItemId(int position) {
        return mInfoBecas.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mInfoBecas.size();
    }

    static class EventosViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mNameBeca;

        EventosViewHolder(View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.imgvIconB);
            mNameBeca = itemView.findViewById(R.id.txtNameBeca);
        }
    }

}
