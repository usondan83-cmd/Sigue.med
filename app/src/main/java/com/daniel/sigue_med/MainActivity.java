package com.daniel.sigue_med;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


// COMPONENTES

    private Button botonEntrar;


// ON CREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // CREAR CANAL DE NOTIFICACIONES

        NotificacionToma.crearCanal(this);

        // =================================================
        // SOLICITAR PERMISO DE NOTIFICACIONES
        // =================================================

        /*
         * Android 13 (API 33) y posteriores necesitan
         * permiso para poder mostrar notificaciones.
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        100
                );
            }
        }

        // CONECTAR BOTÓN

        botonEntrar = findViewById(R.id.botonEntrar);

        // BOTÓN ENTRAR

        botonEntrar.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        MenuPrincipalActivity.class
                                );

                        startActivity(intent);
                    }
                }
        );

    }


}
