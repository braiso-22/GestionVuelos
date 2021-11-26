package com.example.gestionvuelos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import com.example.gestionvuelos.R;

import com.example.gestionvuelos.vo.Vuelo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SearchFlightDialogFragment extends DialogFragment {
    Vuelo vuelo;
    View inflado;
    String email;
    FirebaseFirestore db;
    TextView type, from, to, depart, returni, passengers, maxStops;
    long numVuelo;
    public SearchFlightDialogFragment(Vuelo v, String email, FirebaseFirestore db) {
        super();
        this.vuelo = v;
        this.email = email;
        this.db = db;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(infService);

        inflado = li.inflate(R.layout.dialog_search_flight, null);
        type = inflado.findViewById(R.id.textoType);
        from = inflado.findViewById(R.id.textoFrom2);
        to = inflado.findViewById(R.id.textoTo2);
        depart = inflado.findViewById(R.id.textoDepart2);
        returni = inflado.findViewById(R.id.textoReturn2);
        passengers = inflado.findViewById(R.id.textoPassengers2);
        maxStops = inflado.findViewById(R.id.textoStops2);
        llenarTexts();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle("Quieres guardar este vuelo?");
        builder.setView(inflado);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.collection("vuelos").document(email).collection("numVuelos").document("num").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try{
                            numVuelo = documentSnapshot.getLong("num");
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                            numVuelo=0;
                        }

                        Log.i("mensaje1", String.valueOf(numVuelo));
                        Map<String, Object> vueloNumMap = new HashMap<>();
                        numVuelo=numVuelo+1l;
                        vueloNumMap.put("num",numVuelo);
                        db.collection("vuelos").document(email).collection("numVuelos").document("num").set(vueloNumMap);

                        Map<String, Object> vueloMap = new HashMap<>();
                        vueloMap.put("type", vuelo.getTipo().toString());
                        vueloMap.put("from", vuelo.getFrom());
                        vueloMap.put("to", vuelo.getTo());
                        vueloMap.put("depart",vuelo.getDepart().toString());
                        vueloMap.put("return",vuelo.getReturno().toString());
                        vueloMap.put("passengers",String.valueOf(vuelo.getPassengers()));
                        vueloMap.put("stops",vuelo.getParadas().toString());

                        String vueloNum = "vuelo"+numVuelo;
                        Log.i("menjsaje2", String.valueOf(numVuelo));
                        db.collection("vuelos").document(email)
                                .collection("vuelos")
                                .document(vueloNum).set(vueloMap);
                    }
                });




            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                crearToast("No se ha guardado el vuelo");
            }

        });

        return builder.create();


    }


    public void llenarTexts() {
        addText(type, vuelo.getTipo().toString());
        addText(from, vuelo.getFrom());
        addText(to, vuelo.getTo());
        addText(depart, vuelo.getDepart().toString());
        if (vuelo.getReturno() != null) {
            addText(returni, vuelo.getReturno().toString());
        } else {
            returni.setVisibility(View.INVISIBLE);
        }
        addText(passengers, String.valueOf(vuelo.getPassengers()));
        addText(maxStops, vuelo.getParadas().toString());

    }

    private void addText(TextView tv, String txt) {
        tv.setText(tv.getText().toString() + txt);
    }

    public void crearToast(String texto) {
        Toast.makeText(getActivity(), texto, Toast.LENGTH_SHORT).show();
    }

}
