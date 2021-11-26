package com.example.gestionvuelos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestionvuelos.R;
import com.example.gestionvuelos.vo.Vuelo;

public class ListAdapter extends BaseAdapter {
    Context context;
    private final Vuelo[] vuelos;


    public ListAdapter(Context context, Vuelo[] vuelos) {
        //super(context,R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.vuelos = vuelos;
    }

    @Override
    public int getCount() {
        return vuelos.length;
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
        if (convertView == null) {
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
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.type.setText(vuelos[position].getTipo().toString());
        viewHolder.from.setText(vuelos[position].getFrom());
        viewHolder.to.setText(vuelos[position].getTo());
        viewHolder.depart.setText(vuelos[position].getDepart().toString());
        viewHolder.returno.setText(vuelos[position].getReturno().toString());
        viewHolder.passengers.setText(String.valueOf(vuelos[position].getPassengers()));
        viewHolder.stops.setText(vuelos[position].getParadas().toString());

        return result;
    }

    private static class ViewHolder {
        TextView type, from, to, depart, returno, passengers, stops;
    }

}