package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Modelos.Horario;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HorariosAdapter extends RecyclerView.Adapter<HorariosAdapter.OpcionesViewHolder> {

    private ArrayList<Horario> arrayList;
    private Context context;

    public HorariosAdapter(ArrayList<Horario> list, Context ctx) {
        context = ctx;
        arrayList = list;
    }


    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getId();
    }

    @NonNull
    @Override
    public HorariosAdapter.OpcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opciones_horario, parent, false);


        return new HorariosAdapter.OpcionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorariosAdapter.OpcionesViewHolder holder, int position) {

        Horario s = arrayList.get(position);
        String hora = String.format("%s\n-\n%s", s.getHoraInicio(), s.getHoraFin());
        holder.txtTitulo.setText(hora);
        holder.txtTitulo.setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class OpcionesViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo;
        CardView mCardView;

        OpcionesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            mCardView = itemView.findViewById(R.id.card);


        }
    }

}
