package com.daniel.sigue_med;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TomasActivity extends AppCompatActivity {


    // =====================================================
    // COMPONENTES
    // =====================================================

    // Contenedor donde mostraremos todas las tomas
    private LinearLayout contenedorTomas;

    // Botón para volver
    private Button botonVolver;

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Cargar el diseño XML
        setContentView(
                R.layout.activity_tomas
        );


        // =================================================
        // CONECTAR COMPONENTES CON XML
        // =================================================

        contenedorTomas =
                findViewById(
                        R.id.contenedorTomas
                );

        botonVolver =
                findViewById(
                        R.id.botonVolver
                );


        // =================================================
        // BOTÓN VOLVER
        // =================================================

        botonVolver.setOnClickListener(view -> {

            finish();

        });


        // =================================================
        // CARGAR TOMAS
        // =================================================

        cargarTomas();
    }


    // =====================================================
    // ON RESUME
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        /*
         * Volvemos a cargar las tomas cuando regresamos
         * a esta pantalla.
         *
         * Esto permite que si hemos marcado una toma
         * desde otro lugar de la aplicación, la pantalla
         * se actualice.
         */

        cargarTomas();
    }


    // =====================================================
    // CARGAR TOMAS
    // =====================================================

    private void cargarTomas() {


        // =================================================
        // LIMPIAR CONTENEDOR
        // =================================================

        /*
         * Eliminamos las tarjetas anteriores.
         *
         * Esto evita que las tomas aparezcan duplicadas
         * cada vez que llamamos a cargarTomas().
         */

        contenedorTomas.removeAllViews();


        // =================================================
        // OBTENER ID DEL MEDICAMENTO
        // =================================================

        int idMedicamento =
                getIntent().getIntExtra(
                        "idMedicamento",
                        -1
                );


        // =================================================
        // COMPROBAR ID
        // =================================================

        if (idMedicamento == -1) {

            return;
        }


        // =================================================
        // OBTENER MEDICAMENTO
        // =================================================

        Medicamento medicamento =
                GestorMedicamentos
                        .obtenerMedicamentoPorId(
                                this,
                                idMedicamento
                        );


        // =================================================
        // COMPROBAR MEDICAMENTO
        // =================================================

        if (medicamento == null) {

            return;
        }


        // =================================================
        // CALCULAR TOMAS
        // =================================================

        ArrayList<LocalDateTime> tomasCalculadas =
                CalculadorTomas.calcularTomas(

                        medicamento.getFechaInicio(),

                        medicamento.getHoraInicio(),

                        medicamento.getFrecuencia(),

                        medicamento.getDiasTratamiento()
                );


        // =================================================
        // GUARDAR TOMAS EN SQLITE
        // =================================================

        /*
         * Calculamos las tomas del medicamento.
         *
         * Antes de guardar cada toma comprobamos si ya
         * existe.
         *
         * De esta manera no duplicamos tomas cada vez
         * que entramos en esta pantalla.
         */

        for (
                LocalDateTime tomaCalculada :
                tomasCalculadas
        ) {


            // -------------------------------------------------
            // CONVERTIR FECHA Y HORA A TEXTO
            // -------------------------------------------------

            String fechaHora = tomaCalculada.format(FORMATO_FECHA_HORA);

            // -------------------------------------------------
            // COMPROBAR SI YA EXISTE
            // -------------------------------------------------

            boolean existe =
                    GestorTomas.existeToma(

                            this,

                            idMedicamento,

                            fechaHora
                    );


            // -------------------------------------------------
            // CREAR TOMA SI NO EXISTE
            // -------------------------------------------------

            if (!existe) {


                Toma toma =
                        new Toma(

                                idMedicamento,

                                fechaHora,

                                EstadoToma.PENDIENTE
                        );


                GestorTomas.guardarToma(

                        this,

                        toma
                );
            }
        }


        // =================================================
        // OBTENER TOMAS DESDE SQLITE
        // =================================================

        ArrayList<Toma> tomas =
                GestorTomas
                        .obtenerTomasPorMedicamento(

                                this,

                                idMedicamento
                        );


        // =================================================
        // COMPROBAR SI HAY TOMAS
        // =================================================

        if (tomas.isEmpty()) {


            TextView textoSinTomas =
                    new TextView(this);


            textoSinTomas.setText(
                    "No se han podido calcular las tomas."
            );


            textoSinTomas.setTextSize(
                    18
            );


            textoSinTomas.setPadding(
                    30,
                    30,
                    30,
                    30
            );


            contenedorTomas.addView(
                    textoSinTomas
            );


            return;
        }


        // =================================================
        // MOSTRAR LAS TOMAS
        // =================================================

        for (Toma toma : tomas) {


            // =================================================
            // CONTENEDOR DE LA TOMA
            // =================================================

            LinearLayout contenedorToma =
                    new LinearLayout(this);


            contenedorToma.setOrientation(
                    LinearLayout.VERTICAL
            );


            contenedorToma.setPadding(
                    30,
                    30,
                    30,
                    30
            );


            // =================================================
            // TEXTO DE LA TOMA
            // =================================================

            TextView textoToma =
                    new TextView(this);


            String texto =

                    "💊 Toma"

                            + "\n"

                            + toma.getFechaHora()

                            + "\n\nEstado: "

                            + obtenerTextoEstado(
                            toma.getEstado()
                    );


            textoToma.setText(
                    texto
            );


            textoToma.setTextSize(
                    18
            );


            // Añadir texto al contenedor

            contenedorToma.addView(
                    textoToma
            );


            // =================================================
            // COMPROBAR ESTADO
            // =================================================

            /*
             * Solamente mostraremos los botones si la toma
             * todavía está pendiente.
             *
             * Si ya está TOMADA u OMITIDA, no queremos que
             * el usuario pueda cambiar accidentalmente
             * el estado.
             */

            if (
                    EstadoToma.PENDIENTE.equals(
                            toma.getEstado()
                    )
            ) {


                // =================================================
                // CONTENEDOR DE BOTONES
                // =================================================

                LinearLayout botones =
                        new LinearLayout(this);


                botones.setOrientation(
                        LinearLayout.HORIZONTAL
                );


                // =================================================
                // BOTÓN TOMADA
                // =================================================

                Button botonTomada =
                        new Button(this);


                botonTomada.setText(
                        EstadoToma.TOMADA
                );


                botonTomada.setOnClickListener(
                        view -> {


                            // -----------------------------------------
                            // ACTUALIZAR ESTADO EN SQLITE
                            // -----------------------------------------

                            GestorTomas.actualizarEstado(

                                    TomasActivity.this,

                                    toma.getId(),

                                    EstadoToma.TOMADA
                            );


                            // -----------------------------------------
                            // RECARGAR PANTALLA
                            // -----------------------------------------

                            cargarTomas();
                        }
                );


                // =================================================
                // BOTÓN OMITIDA
                // =================================================

                Button botonOmitida =
                        new Button(this);


                botonOmitida.setText(
                        EstadoToma.OMITIDA
                );


                botonOmitida.setOnClickListener(
                        view -> {


                            // -----------------------------------------
                            // ACTUALIZAR ESTADO EN SQLITE
                            // -----------------------------------------

                            GestorTomas.actualizarEstado(

                                    TomasActivity.this,

                                    toma.getId(),

                                    EstadoToma.OMITIDA
                            );


                            // -----------------------------------------
                            // RECARGAR PANTALLA
                            // -----------------------------------------

                            cargarTomas();
                        }
                );


                // =================================================
                // AÑADIR BOTONES
                // =================================================

                botones.addView(
                        botonTomada
                );


                botones.addView(
                        botonOmitida
                );


                // =================================================
                // AÑADIR BOTONES AL CONTENEDOR
                // =================================================

                contenedorToma.addView(
                        botones
                );


            } else {


                // =================================================
                // TOMA YA FINALIZADA
                // =================================================

                /*
                 * Si la toma ya está TOMADA u OMITIDA,
                 * mostramos un mensaje informativo en lugar
                 * de los botones.
                 */

                TextView mensajeEstado =
                        new TextView(this);


                mensajeEstado.setText(
                        obtenerMensajeEstado(
                                toma.getEstado()
                        )
                );


                mensajeEstado.setTextSize(
                        17
                );


                mensajeEstado.setPadding(
                        0,
                        15,
                        0,
                        0
                );


                contenedorToma.addView(
                        mensajeEstado
                );
            }


            // =================================================
            // AÑADIR TOMA A LA PANTALLA
            // =================================================

            contenedorTomas.addView(
                    contenedorToma
            );
        }
    }


    // =====================================================
    // CONVERTIR ESTADO A TEXTO AMIGABLE
    // =====================================================

    private String obtenerTextoEstado(
            String estado) {


        if (
                EstadoToma.TOMADA.equals(
                        estado
                )
        ) {

            return EstadoToma.TOMADA;
        }


        if (
                EstadoToma.OMITIDA.equals(
                        estado
                )
        ) {

            return EstadoToma.OMITIDA;
        }


        return EstadoToma.PENDIENTE;
    }


    // =====================================================
    // OBTENER MENSAJE DEL ESTADO
    // =====================================================

    private String obtenerMensajeEstado(
            String estado) {


        if (
                EstadoToma.TOMADA.equals(
                        estado
                )
        ) {

            return "✅ Esta toma ya ha sido realizada.";
        }


        if (
                EstadoToma.OMITIDA.equals(
                        estado
                )
        ) {

            return "❌ Esta toma ha sido omitida.";
        }


        return "⏳ Esta toma está pendiente.";
    }
}