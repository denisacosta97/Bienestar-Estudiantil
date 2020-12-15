package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentacionAdapter extends RecyclerView.Adapter<DocumentacionAdapter.DocumentacionViewHolder> {

    private ArrayList<Archivo> arrayList;
    private Activity context;
    private OnClickListenerAdapter agregar;
    private OnClickListenerAdapter borrar;

    public DocumentacionAdapter(ArrayList<Archivo> list, OnClickListenerAdapter agregar, OnClickListenerAdapter borrar, Activity ctx) {
        context = ctx;
        this.agregar = agregar;
        this.borrar = borrar;
        arrayList = list;
    }


    @NonNull
    @Override
    public DocumentacionAdapter.DocumentacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_documentacion, parent, false);

        return new DocumentacionAdapter.DocumentacionViewHolder(view, agregar, borrar);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentacionAdapter.DocumentacionViewHolder holder, int position) {

        Archivo archivo = arrayList.get(position);

        holder.txtTitulo.setText(archivo.getNombre());
        if (archivo.getFile() != null) {
            holder.txtFile.setVisibility(View.VISIBLE);
            holder.txtFile.setText(archivo.getFileName(context));
        } else {
            holder.txtFile.setVisibility(View.GONE);
            holder.txtFile.setText("");
        }
        switch (archivo.getValidez()) {
            case 0:
                holder.txtEstado.setText("PENDIENTE DE CARGA");
                holder.txtEstado.setTextColor(context.getApplicationContext().getResources().getColor(R.color.colorOrange));
                break;
            case 1:
                holder.txtEstado.setText("CARGADO");
                holder.txtEstado.setTextColor(context.getApplicationContext().getResources().getColor(R.color.colorGreen));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class DocumentacionViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtFile, txtEstado;
        CardView cardAgregar, cardBorrar;
        OnClickListenerAdapter agregar;
        OnClickListenerAdapter borrar;

        DocumentacionViewHolder(View itemView, final OnClickListenerAdapter agregar, final OnClickListenerAdapter borrar) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtFile = itemView.findViewById(R.id.txtDescripcion);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            cardAgregar = itemView.findViewById(R.id.btnAgregar);
            cardBorrar = itemView.findViewById(R.id.btnBorrar);
            this.agregar = agregar;
            this.borrar = borrar;
            if (agregar != null) {
                cardAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agregar.onClick(getAdapterPosition());
                    }
                });
            }
            if (borrar != null) {
                cardBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        borrar.onClick(getAdapterPosition());
                    }
                });
            }

        }
    }

}
