package com.example.gestionvuelos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestionvuelos.util.Util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.gestionvuelos.FligthsActivity.ProviderType;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    private void setup() {
        String titulo = "Autenticacion";
        Button botonLogin = findViewById(R.id.botonLogin);
        Button botonRegister = findViewById(R.id.botonRegister);
        Button botonGoogle = findViewById(R.id.botonGoogle);
        EditText textoEmail = findViewById(R.id.textEmail);
        EditText textoPass = findViewById(R.id.textPassword);

        botonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textoEmail.getText().toString();
                String pass = textoPass.getText().toString();
                if (Util.isEmail(textoEmail) && Util.isPass(textoPass)) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Registro Correcto", Toast.LENGTH_SHORT).show();
                                cambiarActivity(email, ProviderType.BASIC);
                            } else {
                                showAlert("Se produjo un error en el registro de usuario");
                            }
                        }
                    });
                }else{

                    showAlert("Sintaxis incorrecta del email o contraseña");
                }
            }
        });
        botonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = textoEmail.getText().toString();
                String pass = textoPass.getText().toString();
                if (Util.isEmail(textoEmail) && Util.isPass(textoPass)) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Acceso Correcto", Toast.LENGTH_SHORT).show();
                                cambiarActivity(email, ProviderType.BASIC);
                            } else {
                                showAlert("Se produjo un error en inicio de sesión");
                            }
                        }
                    });

                }else{
                    showAlert("Sintaxis incorrecta del email o contraseña");
                }
            }
        });
        botonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void showAlert(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(mensaje);
        builder.setTitle("Error");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cambiarActivity(String email, @NonNull ProviderType provider) {
        Intent homeIntent = new Intent(this, FligthsActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
    }


}