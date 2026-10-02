package com.daniel.sigue_med;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistroMedicamentoActivity
        extends AppCompatActivity {


// =====================================================
// COMPONENTES
// =====================================================

    private Spinner spinnerUnidad;

    private Spinner spinnerFrecuencia;

    private EditText editNombre;

    private EditText editDosis;

    private EditText editHoraInicio;

    private EditText editFechaInicio;

    private EditText editDiasTratamiento;

    private Button botonGuardar;


// =====================================================
// VENTANA DE GENERACIÓN DE TOMAS
// =====================================================

    /*
     * En vez de generar TODAS las tomas del tratamiento
     * completo (lo cual puede ser miles de filas para
     * tratamientos largos), solo generamos las tomas
     * de los próximos N días.
     *
     * MainActivity (u otra pantalla) deberá encargarse
     * de volver a generar tomas cuando esta ventana
     * se vaya agotando.
     */

    private static final int DIAS_VENTANA_TOMAS = 14;


// =====================================================
// ID DEL MEDICAMENTO QUE SE ESTÁ EDITANDO
// =====================================================

    /*
     * -1 significa que estamos registrando
     * un medicamento nuevo.
     *
     * Si contiene otro número significa que
     * estamos editando un medicamento existente.
     */

    private int idMedicamentoEditar = -1;


// =====================================================
// ON CREATE
// =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // =================================================
        // CARGAR DISEÑO XML
        // =================================================

        setContentView(
                R.layout.activity_registro_medicamento
        );


        // =================================================
        // CONECTAR COMPONENTES CON XML
        // =================================================

        editNombre =
                findViewById(
                        R.id.editNombre
                );

        editDosis =
                findViewById(
                        R.id.editDosis
                );

        spinnerUnidad =
                findViewById(
                        R.id.spinnerUnidad
                );

        spinnerFrecuencia =
                findViewById(
                        R.id.spinnerFrecuencia
                );

        editHoraInicio =
                findViewById(
                        R.id.editHoraInicio
                );

        editFechaInicio =
                findViewById(
                        R.id.editFechaInicio
                );

        editDiasTratamiento =
                findViewById(
                        R.id.editDiasTratamiento
                );

        botonGuardar =
                findViewById(
                        R.id.botonGuardar
                );
    }
}
