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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestionvuelos.fragments.DatePickerFragment;
import com.example.gestionvuelos.fragments.ListaDialogFragment;
import com.example.gestionvuelos.fragments.ListenerDialogFragment;
import com.example.gestionvuelos.fragments.SearchFlightDialogFragment;
import com.example.gestionvuelos.vo.FlightType;
import com.example.gestionvuelos.vo.Stops;
import com.example.gestionvuelos.vo.Vuelo;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;

public class FlightsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ListenerDialogFragment {


    enum ProviderType {
        BASIC,
        GOOGLE
    }

    TextView bienvenida;
    EditText from;
    EditText to;
    EditText depart;
    EditText returni;
    EditText numPasageros;
    RadioGroup stopsGroup;
    RadioButton roundTrip;
    RadioButton oneWay;
    Button botonMenos;
    Button botonMas;
    Button botonSearch;
    ImageButton botonCalendar1;
    ImageButton botonCalendar2;
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
        setContentView(R.layout.activity_flights);

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
        depart = findViewById(R.id.textoDepart);
        returni = findViewById(R.id.editTextDate2);
        stopsGroup = findViewById(R.id.radioGroup2);
        roundTrip = findViewById(R.id.radioRoundTrip);
        oneWay = findViewById(R.id.radioOneWay);
        botonMenos = findViewById(R.id.boton_menos);
        botonMas = findViewById(R.id.boton_mas);
        botonSearch = findViewById(R.id.butonSearch);
        botonCalendar1 = findViewById(R.id.botonCalendar1);
        botonCalendar2 = findViewById(R.id.botonCalendar2);
        bienvenida.setText(bienvenida.getText() + " " + email);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaDialogFragment lista = new ListaDialogFragment();
                lista.show(getSupportFragmentManager(), "ListDialog");
                flagCiudad = true;
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaDialogFragment lista = new ListaDialogFragment();
                lista.show(getSupportFragmentManager(), "ListDialog");
                flagCiudad = false;
            }
        });

        oneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableView(botonCalendar2);
                disableView(returni);
            }
        });
        roundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableView(botonCalendar2);
                enableView(returni);
            }
        });

        botonCalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "selector fecha");
                flagCalendar = true;
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
                if (numero == 0) {
                    numero = 19;
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
                if (numero == 19) {
                    numero = 1;
                }
                numPasageros.setText(String.valueOf(numero));
            }
        });
        botonSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                int radioButtonID = stopsGroup.getCheckedRadioButtonId();
                RadioButton radioButton = stopsGroup.findViewById(radioButtonID);
                int idx = stopsGroup.indexOfChild(radioButton);


                Stops[] stopsArray= {Stops.NONSTOP,Stops.ONESTOP, Stops.TWOORMORE};
                SearchFlightDialogFragment dialog;
                if ( seleccionable() && roundTrip.isChecked()) {//
                    dialog = new SearchFlightDialogFragment(new Vuelo(FlightType.ROUNDTRIP,from.getText().toString(), to.getText().toString(),depart.getText().toString(),returni.getText().toString(),numPasageros.getText().toString(),stopsArray[idx] ));
                    dialog.show(getSupportFragmentManager(), "PersonalizedDialog");
                }else if(seleccionable() && oneWay.isChecked()){
                    dialog = new SearchFlightDialogFragment(new Vuelo(FlightType.ROUNDTRIP,from.getText().toString(), to.getText().toString(),depart.getText().toString(),numPasageros.getText().toString(),stopsArray[idx] ));
                    dialog.show(getSupportFragmentManager(), "PersonalizedDialog");
                }else{
                    crearToast("No se han rellenado todos los campos");
                }
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
        EditText depart = findViewById(R.id.textoDepart);
        EditText returning = findViewById(R.id.editTextDate2);
        LocalDate f1 = null, f2 = null;

        try {
            f1 = LocalDate.parse(depart.getText().toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            f2 = LocalDate.parse(returning.getText().toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*
        Comprobaciones de fechas
        * */
        if (!f.isBefore(LocalDate.now())) {
            try {
                if (flagCalendar) {
                    if (f2 == null || f.isBefore(f2)) {
                        depart.setText(f.toString());
                    } else {
                        crearToast("La salida no puede ser después del regreso");
                    }

                } else {
                    if (f1 == null || !f.isBefore(f1)) {
                        returning.setText(f.toString());
                    } else {
                        crearToast("El regreso no puede ser antes de la salida");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else {
            crearToast("Las fechas no pueden ser anteriores al día de hoy");
        }

    }

    @Override
    public void onListaDialogClick(String str) {
        if (flagCiudad) {
            if (to.getText().toString().equals(str)) {
                crearToast("No pueden coincidir origen y destino");
            } else {
                from.setText(str);
            }

        } else {
            if (from.getText().toString().equals(str)) {
                crearToast("No pueden coincidir origen y destino");
            } else {
                to.setText(str);
            }
        }
    }

    public void disableView(View v) {
        v.setEnabled(false);
    }

    public void enableView(View v) {
        v.setEnabled(true);
    }

    public void crearToast(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    public boolean seleccionable() {
        boolean result;
        if (roundTrip.isSelected()) {
            result = isFiled(from) && isFiled(to) && isFiled(depart) && isFiled(returni);
        } else {
            result = isFiled(from) && isFiled(to) && isFiled(depart);
        }
        return result;
    }

    private boolean isFiled(EditText ed) {
        return !ed.getText().toString().equals("");
    }


}