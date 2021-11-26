package com.example.gestionvuelos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestionvuelos.util.Util;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.gestionvuelos.FlightsActivity.ProviderType;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    GoogleSignInClient googleClient;
    private int GOOGLE_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        setup();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        try {
            cambiarActivity(account.getEmail(), ProviderType.GOOGLE);
            Toast.makeText(LoginActivity.this, "Acceso Correcto", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    private void setup() {

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
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                } else {

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

                } else {
                    showAlert("Sintaxis incorrecta del email o contraseña");
                }
            }
        });


        botonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.key))
                        .requestEmail()
                        .build();

                googleClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                Log.i("codigo", String.valueOf(gso.isIdTokenRequested()));
                //signIn();
                Toast.makeText(LoginActivity.this,"OPCION DESHABILITADA",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signIn() {
        startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
    }

    private void showAlert(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(mensaje);
        builder.setTitle("Error");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cambiarActivity(String email, @NonNull ProviderType provider) {
        Intent homeIntent = new Intent(this, FlightsActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG3","signWithCreadentiaSuccess");
                            FirebaseUser user = mAuth.getCurrentUser();
                            cambiarActivity(user.getEmail(), ProviderType.GOOGLE);


                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag22", "signInWithCredential:failure", task.getException());
                        }
                    }
                });



            }

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            System.out.println(e.getMessage());
            Log.i("TAG2", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}
