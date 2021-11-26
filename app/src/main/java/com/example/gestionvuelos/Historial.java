package com.example.gestionvuelos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gestionvuelos.adapters.ListAdapter;
import com.example.gestionvuelos.vo.Vuelo;

public class Historial extends AppCompatActivity {
    ListView lista;
    ListAdapter lAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        lista= findViewById(R.id.listaVuelos);
        Vuelo[] letras ;

        lAdapter = new ListAdapter(this, letras);
        lista.setAdapter(lAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Historial.this, letras[i] + "" + numeros[i], Toast.LENGTH_SHORT).show();
            }
        });
    }
}