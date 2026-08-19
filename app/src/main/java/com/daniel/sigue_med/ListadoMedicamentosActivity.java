package com.daniel.sigue_med;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.google.android.material.card.MaterialCardView;

public class ListadoMedicamentosActivity
        extends AppCompatActivity {


// =====================================================
// COMPONENTES
// =====================================================

    // Contenedor donde mostraremos las tarjetas
    // de los medicamentos.
    private LinearLayout contenedorMedicamentos;

    // Botón para volver al menú principal.
    private Button botonVolver;


// =====================================================
// ON CREATE
// =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // =================================================
        // CARGAR XML
        // =================================================

        setContentView(
                R.layout.activity_listado_medicamentos
        );


        // =================================================
        // CONECTAR COMPONENTES CON XML
        // =================================================

        contenedorMedicamentos =
                findViewById(
                        R.id.contenedorMedicamentos
                );

        botonVolver =
                findViewById(
                        R.id.botonVolver
                );


        // =================================================
        // BOTÓN VOLVER
        // =================================================

        botonVolver.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        finish();
                    }
                }
        );


        // =================================================
        // CARGAR MEDICAMENTOS
        // =================================================

        cargarMedicamentos();
    }


// =====================================================
// ON RESUME
// =====================================================

    @Override
    protected void onResume() {

        super.onResume();

        /*
         * Cuando volvemos de RegistroMedicamentoActivity,
         * volvemos a consultar SQLite.
         *
         * Esto permite que el listado muestre inmediatamente
         * los datos modificados.
         */

        cargarMedicamentos();
    }


// =====================================================
// CARGAR MEDICAMENTOS DESDE SQLITE
// =====================================================

    private void cargarMedicamentos() {


        // =================================================
        // LIMPIAR EL CONTENEDOR
        // =================================================

        /*
         * Eliminamos las tarjetas anteriores.
         *
         * Esto es importante porque onResume() puede llamar
         * nuevamente a este método.
         */

        contenedorMedicamentos.removeAllViews();


        // =================================================
        // OBTENER MEDICAMENTOS
        // =================================================

        ArrayList<Medicamento> medicamentos =
                GestorMedicamentos
                        .obtenerMedicamentosBBDD(this);


        // =================================================
        // COMPROBAR SI NO HAY MEDICAMENTOS
        // =================================================

        if (medicamentos.isEmpty()) {

            TextView textoSinMedicamentos =
                    new TextView(this);


            textoSinMedicamentos.setText(
                    "No hay medicamentos registrados."
            );


            textoSinMedicamentos.setTextSize(
                    18
            );


            textoSinMedicamentos.setPadding(
                    30,
                    30,
                    30,
                    30
            );


            contenedorMedicamentos.addView(
                    textoSinMedicamentos
            );


            return;
        }


        // =================================================
        // RECORRER LOS MEDICAMENTOS
        // =================================================

        for (
                Medicamento medicamento :
                medicamentos
        ) {


            // =================================================
            // GUARDAR ID DEL MEDICAMENTO
            // =================================================

            /*
             * Guardamos el ID porque lo necesitaremos
             * para abrir las pantallas de Tomas y Editar.
             */

            int idMedicamento =
                    medicamento.getId();


            // =================================================
            // CREAR TARJETA
            // =================================================

            MaterialCardView tarjeta =
                    new MaterialCardView(this);


            tarjeta.setRadius(
                    20
            );


            tarjeta.setCardElevation(
                    6
            );


            // =================================================
            // TEXTO DEL MEDICAMENTO
            // =================================================

            TextView textoMedicamento =
                    new TextView(this);


            String texto =

                    "💊 "
                            + medicamento.getNombre()

                            + "\n\nDosis: "
                            + medicamento.getDosis()
                            + " "
                            + medicamento.getUnidad()

                            + "\nFrecuencia: "
                            + medicamento.getFrecuencia()

                            + "\nFecha de inicio: "
                            + obtenerFecha(
                            medicamento.getFechaInicio()
                    )

                            + "\nPrimera toma: "
                            + medicamento.getHoraInicio()

                            + "\nDuración: "
                            + medicamento.getDiasTratamiento()
                            + " días";


            textoMedicamento.setText(
                    texto
            );


            // Tamaño de letra

            textoMedicamento.setTextSize(
                    18
            );


            // Espacio interior

            textoMedicamento.setPadding(
                    30,
                    30,
                    30,
                    30
            );


            // =================================================
            // BOTÓN TOMAS
            // =================================================

            Button botonTomas =
                    new Button(this);


            botonTomas.setText(
                    "Tomas"
            );


            botonTomas.setOnClickListener(
                    view -> {


                        // =================================================
                        // CREAR INTENT
                        // =================================================

                        Intent intent =
                                new Intent(

                                        ListadoMedicamentosActivity.this,

                                        TomasActivity.class
                                );


                        // =================================================
                        // ENVIAR ID
                        // =================================================

                        /*
                         * TomasActivity necesita saber qué medicamento
                         * estamos consultando.
                         */

                        intent.putExtra(
                                "idMedicamento",
                                idMedicamento
                        );


                        // =================================================
                        // ABRIR TOMAS
                        // =================================================

                        startActivity(
                                intent
                        );
                    }
            );


            // =================================================
            // BOTÓN EDITAR
            // =================================================

            Button botonEditar =
                    new Button(this);


            botonEditar.setText(
                    "Editar"
            );


            botonEditar.setOnClickListener(
                    view -> {


                        // =================================================
                        // CREAR INTENT
                        // =================================================

                        Intent intent =
                                new Intent(

                                        ListadoMedicamentosActivity.this,

                                        RegistroMedicamentoActivity.class
                                );


                        // =================================================
                        // ENVIAR ID DEL MEDICAMENTO
                        // =================================================

                        /*
                         * ESTA ES LA PARTE IMPORTANTE PARA EDITAR.
                         *
                         * RegistroMedicamentoActivity busca exactamente
                         * el parámetro "idMedicamento".
                         *
                         * Gracias a este ID sabrá qué medicamento
                         * debe cargar en el formulario.
                         */

                        intent.putExtra(
                                "idMedicamento",
                                idMedicamento
                        );


                        // =================================================
                        // ABRIR FORMULARIO
                        // =================================================

                        startActivity(
                                intent
                        );
                    }
            );


            // =================================================
            // BOTÓN ELIMINAR
            // =================================================

            Button botonEliminar =
                    new Button(this);


            botonEliminar.setText(
                    "Eliminar"
            );


            botonEliminar.setOnClickListener(
                    view -> {


                        // =================================================
                        // PEDIR CONFIRMACIÓN
                        // =================================================

                        /*
                         * Antes de eliminar mostramos un diálogo.
                         *
                         * Esto evita que el usuario pueda borrar
                         * accidentalmente un medicamento.
                         */

                        new AlertDialog.Builder(
                                ListadoMedicamentosActivity.this
                        )

                                .setTitle(
                                        "Eliminar medicamento"
                                )

                                .setMessage(
                                        "¿Seguro que quieres eliminar "
                                                + medicamento.getNombre()
                                                + "?"
                                )

                                .setNegativeButton(
                                        "Cancelar",
                                        null
                                )

                                .setPositiveButton(
                                        "Eliminar",
                                        (dialog, which) -> {

                                            eliminarMedicamento(
                                                    idMedicamento
                                            );
                                        }
                                )

                                .show();
                    }
            );


            // =================================================
            // CONTENEDOR DE BOTONES
            // =================================================

            LinearLayout botones =
                    new LinearLayout(this);


            botones.setOrientation(
                    LinearLayout.HORIZONTAL
            );


            // Añadir botón Tomas

            botones.addView(
                    botonTomas
            );


            // Añadir botón Editar

            botones.addView(
                    botonEditar
            );


            // Añadir botón Eliminar

            botones.addView(
                    botonEliminar
            );


            // =================================================
            // CONTENEDOR INTERNO DE LA TARJETA
            // =================================================

            LinearLayout contenido =
                    new LinearLayout(this);


            contenido.setOrientation(
                    LinearLayout.VERTICAL
            );


            // Añadir información

            contenido.addView(
                    textoMedicamento
            );


            // Añadir botones

            contenido.addView(
                    botones
            );


            // =================================================
            // AÑADIR CONTENIDO A LA TARJETA
            // =================================================

            tarjeta.addView(
                    contenido
            );


            // =================================================
            // PARÁMETROS DE LA TARJETA
            // =================================================

            LinearLayout.LayoutParams parametros =
                    new LinearLayout.LayoutParams(

                            LinearLayout.LayoutParams.MATCH_PARENT,

                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            // Margen inferior

            parametros.setMargins(
                    0,
                    0,
                    0,
                    20
            );


            tarjeta.setLayoutParams(
                    parametros
            );


            // =================================================
            // AÑADIR TARJETA AL CONTENEDOR
            // =================================================

            contenedorMedicamentos.addView(
                    tarjeta
            );
        }
    }


// =====================================================
// ELIMINAR MEDICAMENTO
// =====================================================

    private void eliminarMedicamento(
            int idMedicamento) {


        // =================================================
        // OBTENER MEDICAMENTO
        // =================================================

        Medicamento medicamentoActual =
                GestorMedicamentos.obtenerMedicamentoPorId(

                        ListadoMedicamentosActivity.this,

                        idMedicamento
                );


        // =================================================
        // CANCELAR ALARMAS
        // =================================================

        /*
         * Antes de eliminar el medicamento cancelamos
         * las alarmas que pudiera tener programadas.
         */

        if (medicamentoActual != null) {

            GestorAlarmas.cancelarAlarmas(

                    ListadoMedicamentosActivity.this,

                    medicamentoActual
            );
        }


        // =================================================
        // ELIMINAR TOMAS
        // =================================================

        /*
         * Eliminamos también las tomas asociadas al medicamento.
         *
         * De esta forma no quedan registros huérfanos
         * en la tabla "tomas".
         */

        GestorTomas.eliminarTomasPorMedicamento(

                ListadoMedicamentosActivity.this,

                idMedicamento
        );


        // =================================================
        // ELIMINAR MEDICAMENTO
        // =================================================

        GestorMedicamentos.eliminarMedicamento(

                ListadoMedicamentosActivity.this,

                idMedicamento
        );


        // =================================================
        // MENSAJE
        // =================================================

        Toast.makeText(

                ListadoMedicamentosActivity.this,

                "Medicamento eliminado",

                Toast.LENGTH_SHORT

        ).show();


        // =================================================
        // ACTUALIZAR LISTADO
        // =================================================

        cargarMedicamentos();
    }


// =====================================================
// OBTENER FECHA
// =====================================================

    private String obtenerFecha(
            String fecha) {


        // =================================================
        // COMPROBAR FECHA VACÍA
        // =================================================

        /*
         * Los medicamentos antiguos pueden no tener
         * fecha de inicio.
         */

        if (
                fecha == null ||
                        fecha.isEmpty()
        ) {

            return "No indicada";
        }


        // =================================================
        // DEVOLVER FECHA
        // =================================================

        return fecha;
    }
}