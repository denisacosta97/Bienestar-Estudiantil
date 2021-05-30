package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unse.bienestar.estudiantil.Modelos.Familiar;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

public class FamiliarAdapter extends RecyclerView.Adapter<FamiliarAdapter.TurnoViewHolder> {

    private ArrayList<Familiar> mList;
    private Context mContext;

    public FamiliarAdapter(ArrayList<Familiar> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public FamiliarAdapter.TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gfamiliar, viewGroup, false);
        return new FamiliarAdapter.TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamiliarAdapter.TurnoViewHolder holder, int i) {

        Familiar grupo = mList.get(i);
        holder.txtNombre.setText(grupo.getDescripcion());
        if (grupo.getCantidad() > 0) {
            holder.txtDesc.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        } else {
            holder.txtDesc.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        }
        String format = "";
        if (grupo.getCantidad() != 1)
            format = "%s archivos cargados";
        else
            format = "%s archivo cargado";
        holder.txtDesc.setText(String.format(format, grupo.getCantidad()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDesc;
        CardView mCardView;

        TurnoViewHolder(View view) {
            super(view);
            mCardView = view.findViewById(R.id.card);
            txtNombre = view.findViewById(R.id.txtNombre);
            txtDesc = view.findViewById(R.id.txtDescripcion);

        }
    }

}
