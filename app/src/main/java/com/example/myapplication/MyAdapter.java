package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Valuta> list;

    public MyAdapter(Context context, ArrayList<Valuta> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //itt értéket adunk
        Valuta valuta = list.get(position);
        holder.valutaName.setText(valuta.getName());
        holder.valutaValue.setText(valuta.getValue());
        holder.valutaDate.setText(valuta.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //textView ami a xml van azt itt rendereled hozzá
        TextView valutaName,valutaValue,valutaDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            valutaName=itemView.findViewById(R.id.textViewValutaName);
            valutaValue=itemView.findViewById(R.id.textViewValutaValue);
            valutaDate=itemView.findViewById(R.id.textViewValutaDate);
        }
    }
}
