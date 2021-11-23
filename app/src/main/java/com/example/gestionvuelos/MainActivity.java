package com.example.gestionvuelos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.gestionvuelos.FligthsActivity.ProviderType;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //envia un evento de inicio de app a firebase
        analytics();

        //inicia el login o Flights dependiendo de las shared preferences
        sesion();

        //cierra esta activity
        finish();
    }

    private void sesion() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String provider = prefs.getString("provider", null);

        if (email != null && provider != null) {
            cambioActivity(email, ProviderType.valueOf(provider),FligthsActivity.class);
        }else{
            cambioActivity("", ProviderType.valueOf("BASIC"),LoginActivity.class);
        }
    }

    private void analytics(){
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message","Integración completa");
        analytics.logEvent("InitScreen",bundle);
    }
    private void cambioActivity(String email, @NonNull ProviderType provider, Class c) {
        Intent homeIntent = new Intent(this, c);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
    }
}