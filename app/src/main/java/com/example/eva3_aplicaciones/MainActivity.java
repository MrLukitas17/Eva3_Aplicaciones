package com.example.eva3_aplicaciones;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText etCorreo, etPassword;
    Button btnInicio_sesion, btnRegistrar;
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnInicio_sesion = findViewById(R.id.btnInicio_sesion);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        firebaseAuth = FirebaseAuth.getInstance();

        // Defino el admin de la app
        final String adminEmail = "admin@gmail.com";
        final String adminPassword = "123456";

        // configuracion del boton Inicio de sesion
        btnInicio_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etCorreo.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar su nombre de usuario y contraseña", Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (email.equals(adminEmail)) {
                                        Intent intentAdmin = new Intent(MainActivity.this, admin.class);
                                        startActivity(intentAdmin);
                                    } else {
                                        Intent intentUsuario = new Intent(MainActivity.this, paginaprincipal.class);
                                        intentUsuario.putExtra("Nombre", email);
                                        startActivity(intentUsuario);
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(MainActivity.this, registro.class);
                startActivity(intentRegistro);
            }
        });
    }
}
