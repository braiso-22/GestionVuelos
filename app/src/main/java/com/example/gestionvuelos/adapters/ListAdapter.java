package com.example.gestionvuelos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestionvuelos.R;
import com.example.gestionvuelos.vo.Vuelo;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<Vuelo> vuelos;


    public ListAdapter(Context context, ArrayList<Vuelo> vuelos) {
        //super(context,R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.vuelos = vuelos;
    }

    @Override
    public int getCount() {
        return vuelos.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.dialog_search_flight, parent, false);
            viewHolder.type = (TextView) convertView.findViewById(R.id.textoType);
            viewHolder.from = (TextView) convertView.findViewById(R.id.textoFrom2);
            viewHolder.to = (TextView) convertView.findViewById(R.id.textoTo2);
            viewHolder.depart = (TextView) convertView.findViewById(R.id.textoDepart2);
            viewHolder.returno = (TextView) convertView.findViewById(R.id.textoReturn2);
            viewHolder.passengers = (TextView) convertView.findViewById(R.id.textoPassengers2);
            viewHolder.stops = (TextView) convertView.findViewById(R.id.textoStops2);

            result = convertView;
            convertView.setTag(viewHolder);

        viewHolder.type.setText(viewHolder.type.getText()+" "+vuelos.get(position).getTipo().toString());
        viewHolder.from.setText(viewHolder.from.getText()+" "+vuelos.get(position).getFrom());
        viewHolder.to.setText(viewHolder.to.getText()+" "+ vuelos.get(position).getTo());
        viewHolder.depart.setText(viewHolder.depart.getText()+" "+vuelos.get(position).getDepart().toString());
        try {
            viewHolder.returno.setText(viewHolder.returno.getText()+" "+vuelos.get(position).getReturno().toString());
        } catch (Exception e) {
            Log.i("log tag", "sin vuelta");
        }
        viewHolder.passengers.setText(viewHolder.passengers.getText()+" "+String.valueOf(vuelos.get(position).getPassengers()));
        viewHolder.stops.setText(viewHolder.stops.getText()+" "+vuelos.get(position).getParadas().toString());

        return result;
    }

    private static class ViewHolder {
        TextView type, from, to, depart, returno, passengers, stops;
    }

}