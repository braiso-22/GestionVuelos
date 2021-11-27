package com.example.gestionvuelos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gestionvuelos.adapters.ListAdapter;
import com.example.gestionvuelos.vo.FlightType;
import com.example.gestionvuelos.vo.Stops;
import com.example.gestionvuelos.vo.Vuelo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class Historial extends AppCompatActivity {

    FirebaseFirestore db;
    ListView lista;
    ListAdapter lAdapter;
    long numVuelo;
    ArrayList<Vuelo> vuelos;
    String email;
    Button back;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        vuelos = new ArrayList<>();
        back=findViewById(R.id.botonBack);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        lista = findViewById(R.id.listaVuelos);
        db = FirebaseFirestore.getInstance();

        db.collection("vuelos").document(email).collection("numVuelos").document("num").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    numVuelo = documentSnapshot.getLong("num");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    numVuelo = 0;
                }
                for (int i = 1; i <= numVuelo; i++)
                    db.collection("vuelos").document(email).collection("vuelos").document("vuelo" + i).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                Map<String, Object> map = documentSnapshot.getData();
                                if (map != null) {
                                    if (map.size() == 7) {
                                        vuelos.add(crearVueloRound(documentSnapshot));
                                    } else if (map.size() == 6) {
                                        vuelos.add(crearVueloOneWay(documentSnapshot));
                                    }

                                    lAdapter = new ListAdapter(Historial.this, vuelos);
                                    lista.setAdapter(lAdapter);
                                }
                            }
                        }
                    });
            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Vuelo crearVueloRound(DocumentSnapshot dS) {
        String type = dS.getString("type"), from = dS.getString("from"), to = dS.getString("to"), stops = dS.getString("stops"), depart = dS.getString("depart"), returno = dS.getString("return"), passengers = dS.getString("passengers");
        return new Vuelo(FlightType.valueOf(type), from, to, depart, returno, passengers, Stops.valueOf(stops));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Vuelo crearVueloOneWay(DocumentSnapshot dS) {
        String type = dS.getString("type"), from = dS.getString("from"), to = dS.getString("to"), stops = dS.getString("stops"), depart = dS.getString("depart"), passengers = dS.getString("passengers");
        return new Vuelo(FlightType.valueOf(type), from, to, depart, passengers, Stops.valueOf(stops));
    }

    private void deleteAll() {
        for (int i = 1; i <= numVuelo; i++) {
            db.collection("vuelos").document(email).collection("vuelos").document("vuelo" + i).delete();

        }
        Historial.this.finish();
    }
}