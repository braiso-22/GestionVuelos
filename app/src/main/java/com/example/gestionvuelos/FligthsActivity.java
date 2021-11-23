package com.example.gestionvuelos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestionvuelos.fragments.DatePickerFragment;
import com.example.gestionvuelos.fragments.ListaDialogFragment;
import com.example.gestionvuelos.fragments.ListenerDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;

public class FligthsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ListenerDialogFragment {



    enum ProviderType {
        BASIC,
        GOOGLE
    }
    TextView bienvenida;
    EditText from ;
    EditText to ;
    EditText numPasageros;
    Button botonMenos ;
    Button botonMas  ;
    ImageButton botonCalendar1 ;
    ImageButton botonCalendar2 ;
    boolean flagCalendar;
    boolean flagCiudad;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fligths);

        //Cargar usuario y provider

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String provider = bundle.getString("provider");

        //Guardar Datos

        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        prefs.putString("email", email);
        prefs.putString("provider", provider);
        prefs.apply();

        setup(email, provider);
    }

    private void setup(String email, String provider) {
        bienvenida = findViewById(R.id.textoBienvenida);
        from = findViewById(R.id.textoFrom);
        to = findViewById(R.id.textoTo);
        numPasageros = findViewById(R.id.texto_passengers);
        botonMenos = findViewById(R.id.boton_menos);
        botonMas = findViewById(R.id.boton_mas);
        botonCalendar1 = findViewById(R.id.botonCalendar1);
        botonCalendar2 = findViewById(R.id.botonCalendar2);
        bienvenida.setText(bienvenida.getText() + " " + email);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaDialogFragment lista = new ListaDialogFragment();
                lista.show(getSupportFragmentManager(),"ListDialog");
                flagCiudad=true;
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaDialogFragment lista = new ListaDialogFragment();
                lista.show(getSupportFragmentManager(),"ListDialog");
                flagCiudad=false;
            }
        });

        botonCalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "selector fecha");
                flagCalendar =true;
            }
        });
        botonCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "selector fecha");
                flagCalendar = false;
            }
        });

        botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numero = Integer.valueOf(numPasageros.getText().toString());
                if (numero >= 1) {
                    numero--;
                }
                if(numero==0){
                    numero=19;
                }
                numPasageros.setText(String.valueOf(numero));
            }
        });
        botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numero = Integer.valueOf(numPasageros.getText().toString());
                if (numero <= 18) {
                    numero++;
                }
                if(numero==19){
                    numero=1;
                }
                numPasageros.setText(String.valueOf(numero));
            }
        });
    }

    private void signOut() {
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        prefs.clear();
        prefs.apply();
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate f = LocalDate.of(year, month + 1, dayOfMonth);
        EditText fecha;

        if(flagCalendar){
             fecha =  findViewById(R.id.editTextDate);
        }else {
            fecha = findViewById(R.id.editTextDate2);
        }

        fecha.setText(f.toString());
    }

    @Override
    public void onListaDialogClick(String str) {
        if(flagCiudad){
            if(to.getText().toString().equals(str)){
                Toast.makeText(this, "No pueden coincidir origen y destino", Toast.LENGTH_LONG).show();
            }else{
                from.setText(str);
            }

        }else{
            if(from.getText().toString().equals(str)){
                Toast.makeText(this, "No pueden coincidir origen y destino", Toast.LENGTH_LONG).show();
            }else{
                to.setText(str);
            }
        }
    }


}