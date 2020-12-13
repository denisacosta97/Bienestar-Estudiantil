package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.R;


import java.util.ArrayList;

import static android.view.View.GONE;

public class DocumentacionAdapter extends RecyclerView.Adapter<DocumentacionAdapter.DocumentacionViewHolder>  {

    private ArrayList<Documentacion> arrayList;
    private Context context;

    public DocumentacionAdapter(ArrayList<Documentacion> list, Context ctx) {
        context = ctx;
        arrayList = list;
    }


    @NonNull
    @Override
    public DocumentacionAdapter.DocumentacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_documentacion, parent, false);


        return new DocumentacionAdapter.DocumentacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentacionAdapter.DocumentacionViewHolder holder, int position) {

        Documentacion s = arrayList.get(position);
        holder.txtTitulo.setText(s.getNombre());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class DocumentacionViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo;

        DocumentacionViewHolder(View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);

        }
    }

}
