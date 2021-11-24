package com.example.gestionvuelos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.gestionvuelos.R;

import com.example.gestionvuelos.vo.Vuelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SearchFlightDialogFragment extends DialogFragment {
    Vuelo vuelo;
    View inflado;
    TextView type, from, to, depart, returni, passengers, maxStops;

    public SearchFlightDialogFragment(Vuelo v){
        super();
        this.vuelo=v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(infService);

        inflado = li.inflate(R.layout.dialog_search_flight, null);
        type = inflado.findViewById(R.id.textType);
        from = inflado.findViewById(R.id.textFrom2);
        to = inflado.findViewById(R.id.textTo2);
        depart = inflado.findViewById(R.id.textDepart2);
        returni = inflado.findViewById(R.id.textReturn2);
        passengers = inflado.findViewById(R.id.textPassengers2);
        maxStops = inflado.findViewById(R.id.textMaxStops2);
        llenarTexts();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle("Quieres guardar este vuelo?");
        builder.setView(inflado);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });

        return builder.create();


    }



    public void llenarTexts() {
        addText(type, vuelo.getTipo().toString());
        addText(from, vuelo.getFrom());
        addText(to, vuelo.getTo());
        addText(depart,vuelo.getDepart().toString());
        if(vuelo.getReturno()!=null){
            addText(returni,vuelo.getReturno().toString());
        }else{
            returni.setVisibility(View.INVISIBLE);
        }
        addText(passengers, String.valueOf(vuelo.getPassengers()));
        addText(maxStops,vuelo.getParadas().toString());

    }

    private void addText(TextView tv, String txt) {
        tv.setText(tv.getText().toString()+ txt);
    }


}
