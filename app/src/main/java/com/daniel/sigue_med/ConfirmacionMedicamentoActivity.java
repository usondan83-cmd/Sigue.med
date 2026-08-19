package com.daniel.sigue_med;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmacionMedicamentoActivity extends AppCompatActivity {

    // Texto donde se muestra el resumen
    private TextView textoResumen;

    // Botón para volver
    private Button botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmacion_medicamento);

        // Conectar componentes con XML
        textoResumen = findViewById(R.id.textoResumen);

        botonVolver = findViewById(R.id.botonVolver);

        // Listener del botón Volver
        botonVolver.setOnClickListener(view -> {

            Intent intent = new Intent(
                    ConfirmacionMedicamentoActivity.this,
                    MenuPrincipalActivity.class
            );

            startActivity(intent);
        });

        // Recuperar los datos enviados desde RegistroMedicamentoActivity
        String nombre = getIntent().getStringExtra("nombre");

        double dosis = getIntent().getDoubleExtra("dosis", 0);

        String unidad = getIntent().getStringExtra("unidad");

        String frecuencia = getIntent().getStringExtra("frecuencia");

        String horaInicio = getIntent().getStringExtra("horaInicio");

        int diasTratamiento =
                getIntent().getIntExtra("diasTratamiento", 0);

        // Crear el resumen
        String resumen =
                "Medicamento: " + nombre +
                        "\n\nDosis: " + dosis + " " + unidad +
                        "\n\nFrecuencia: " + frecuencia +
                        "\n\nPrimera toma: " + horaInicio +
                        "\n\nDuración: " + diasTratamiento + " días";

        // Mostrar el resumen
        textoResumen.setText(resumen);
    }
}