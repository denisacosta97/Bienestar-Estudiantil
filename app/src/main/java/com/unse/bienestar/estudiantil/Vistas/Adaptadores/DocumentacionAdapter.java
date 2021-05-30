package com.unse.bienestar.estudiantil.Vistas.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Interfaces.OnClickListenerAdapter;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Documentacion;
import com.unse.bienestar.estudiantil.Modelos.Familiar;
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
    private int tipo = 0;
    private ArrayList<Archivo> documentacions;

    public DocumentacionAdapter(Activity context, int tipo, ArrayList<Archivo> documentacions) {
        this.context = context;
        this.tipo = tipo;
        this.documentacions = documentacions;
    }

    public DocumentacionAdapter(ArrayList<Archivo> list, OnClickListenerAdapter agregar,
                                OnClickListenerAdapter borrar, Activity ctx) {
        context = ctx;
        this.agregar = agregar;
        this.borrar = borrar;
        arrayList = list;
    }


    @NonNull
    @Override
    public DocumentacionAdapter.DocumentacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (tipo != 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_documentacion, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gfamiliar, parent, false);

        return new DocumentacionAdapter.DocumentacionViewHolder(view, agregar, borrar, tipo);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentacionAdapter.DocumentacionViewHolder holder, int position) {

        if (tipo != 1) {
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
        } else {
            Archivo documentacion = documentacions.get(position);
            if (documentacion != null) {
                holder.txtNombre.setText(documentacion.getNombre());
                if (documentacion.getValidez() == 1) {
                    holder.txtDesc.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    holder.txtDesc.setText("Archivo cargado");
                } else {
                    holder.txtDesc.setTextColor(context.getResources().getColor(R.color.colorRed));
                    holder.txtDesc.setText("Sin archivo cargado");
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return tipo != 1 ? arrayList.size() : documentacions.size();
    }

    static class DocumentacionViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtFile, txtEstado;
        CardView cardAgregar, cardBorrar;
        OnClickListenerAdapter agregar;
        OnClickListenerAdapter borrar;

        TextView txtNombre, txtDesc;
        CardView mCardView;

        DocumentacionViewHolder(View itemView, final OnClickListenerAdapter agregar, final OnClickListenerAdapter borrar, int tipo) {
            super(itemView);

            if (tipo != 1) {
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
            } else {
                mCardView = itemView.findViewById(R.id.card);
                txtNombre = itemView.findViewById(R.id.txtNombre);
                txtDesc = itemView.findViewById(R.id.txtDescripcion);
            }


        }
    }

}
