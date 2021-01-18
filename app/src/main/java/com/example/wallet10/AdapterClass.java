package com.example.wallet10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    ArrayList<String> list;

    public AdapterClass(ArrayList<String> list){
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.details.setText(list.get(position));
        holder.TransN.setText("Transaction ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TransN,details;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TransN = itemView.findViewById(R.id.transactionnumber);
            details = itemView.findViewById(R.id.date);

        }
    }

}