package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unse.bienestar.estudiantil.Modelos.ServiciosUPA;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

public class ServiciosUPAAdapter extends RecyclerView.Adapter<ServiciosUPAAdapter.EventosViewHolder> {
    private ArrayList<ServiciosUPA> serviciosUPA;
    private Context context;
    View view;

    public ServiciosUPAAdapter(ArrayList<ServiciosUPA> list, Context ctx) {
        this.serviciosUPA = list;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ServiciosUPAAdapter.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicio_upa, parent, false);

        return new ServiciosUPAAdapter.EventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciosUPAAdapter.EventosViewHolder holder, int position) {
        ServiciosUPA upa = serviciosUPA.get(position);

        holder.mIcon.setImageResource(upa.getIcon());
        holder.mName.setText(upa.getName());
        String cat = getCategoria(upa.getCateg());
        holder.mCateg.setText(cat);
    }

    private String getCategoria(int categ) {
        if(categ == 0)
            return "Estudiante";
        return "Comunidad Universitaria";
    }


    @Override
    public long getItemId(int position) {
        return serviciosUPA.get(position).getIdServicio();
    }

    @Override
    public int getItemCount() {
        return serviciosUPA.size();
    }

    static class EventosViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mName, mCateg;

        EventosViewHolder(View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.icon);
            mName = itemView.findViewById(R.id.txtName);
            mCateg = itemView.findViewById(R.id.txtCateg);
        }
    }
}
