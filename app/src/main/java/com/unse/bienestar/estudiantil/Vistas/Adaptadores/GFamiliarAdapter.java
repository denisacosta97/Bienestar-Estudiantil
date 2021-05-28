package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unse.bienestar.estudiantil.Interfaces.OnClickOptionListener;
import com.unse.bienestar.estudiantil.Modelos.GFamiliar;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

public class GFamiliarAdapter extends RecyclerView.Adapter<GFamiliarAdapter.TurnoViewHolder> {

    private ArrayList<GFamiliar> mList;
    private Context mContext;

    public GFamiliarAdapter(ArrayList<GFamiliar> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public GFamiliarAdapter.TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gfamiliar, viewGroup, false);
        return new GFamiliarAdapter.TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GFamiliarAdapter.TurnoViewHolder holder, int i) {

        GFamiliar grupo = mList.get(i);
        holder.txtNombre.setText(grupo.getNombre());
        /*holder.txtDesc.setText(grupo.getDescripcion());
        switch (grupo.getDescripcion()) {
            case "Incompleto":
                holder.txtDesc.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                break;
            case "Completo":
                holder.txtDesc.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                break;
        }*/

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
