package com.example.asistenciauda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


    private ArrayList<usuario> itemList = new ArrayList<>();

    public ItemAdapter(ArrayList<usuario> itemList) {

        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listaalumnos, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.Name.setText(itemList.get(position).getNombre());
        holder.Apellido.setText(itemList.get(position).getApellidoPaterno());
        holder.Ncontrol.setText(itemList.get(position).getNoControl());

        holder.usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),registro.class);
                intent.putExtra("id",itemList.get(position).getID());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private Button usuarios;
        private  Button eliminar;

        public TextView Name;
        public TextView Apellido, Ncontrol ;
        public ImageView imageView;

        @SuppressLint("WrongViewCast")
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            Apellido = itemView.findViewById(R.id.Apellido);
            Ncontrol = itemView.findViewById(R.id.Ncontrol);
            imageView = itemView.findViewById(R.id.icon);
            usuarios = itemView.findViewById(R.id.editar);


        }
    }


}
