package com.daniel.sigue_med;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MenuPrincipalActivity
        extends AppCompatActivity {


    // =====================================================
    // FORMATO DE FECHA DE LAS TOMAS
    // =====================================================

    /*
     * IMPORTANTE:
     *
     * Este es el mismo formato utilizado en
     * RegistroMedicamentoActivity cuando se crean
     * las tomas.
     *
     * Ejemplo:
     *
     * 17/08/2026 16:00
     */

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            );


    // =====================================================
    // TARJETAS DEL MENÚ
    // =====================================================

    private MaterialCardView cardRegistrar;

    private MaterialCardView cardListado;

    private MaterialCardView cardAlarmas;


    // =====================================================
    // TARJETA PRÓXIMA TOMA
    // =====================================================

    private MaterialCardView cardProximaToma;

    private TextView textoProximoMedicamento;

    private TextView textoProximaToma;

    private Button botonMarcarToma;


    // =====================================================
    // TOMA MOSTRADA ACTUALMENTE
    // =====================================================

    /*
     * Guardamos la toma que actualmente aparece
     * en la tarjeta.
     */

    private Toma proximaToma;


    // =====================================================
    // HANDLER
    // =====================================================

    /*
     * El Handler permite comprobar periódicamente
     * si ya ha llegado la hora de la próxima toma.
     */

    private final Handler handler =
            new Handler(
                    Looper.getMainLooper()
            );


    // =====================================================
    // COMPROBADOR DE HORA
    // =====================================================

    private final Runnable comprobadorHora =
            new Runnable() {

                @Override
                public void run() {


                    // =================================================
                    // ACTUALIZAR BOTÓN
                    // =================================================

                    actualizarEstadoBoton();


                    // =================================================
                    // COMPROBAR DE NUEVO EN 1 SEGUNDO
                    // =================================================

                    handler.postDelayed(
                            this,
                            1000
                    );
                }
            };


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // =================================================
        // CARGAR XML
        // =================================================

        setContentView(
                R.layout.activity_menu_principal
        );


        // =================================================
        // CONECTAR TARJETAS
        // =================================================

        cardRegistrar =
                findViewById(
                        R.id.cardRegistrar
                );


        cardListado =
                findViewById(
                        R.id.cardListado
                );


        cardAlarmas =
                findViewById(
                        R.id.cardAlarmas
                );


        // =================================================
        // CONECTAR TARJETA PRÓXIMA TOMA
        // =================================================

        cardProximaToma =
                findViewById(
                        R.id.cardProximaToma
                );


        textoProximoMedicamento =
                findViewById(
                        R.id.textoProximoMedicamento
                );


        textoProximaToma =
                findViewById(
                        R.id.textoProximaToma
                );


        botonMarcarToma =
                findViewById(
                        R.id.botonMarcarToma
                );


        // =================================================
        // TARJETA REGISTRAR MEDICAMENTO
        // =================================================

        cardRegistrar.setOnClickListener(
                view -> {

                    Intent intent =
                            new Intent(
                                    MenuPrincipalActivity.this,
                                    RegistroMedicamentoActivity.class
                            );


                    startActivity(intent);
                }
        );


        // =================================================
        // TARJETA MIS MEDICAMENTOS
        // =================================================

        cardListado.setOnClickListener(
                view -> {

                    Intent intent =
                            new Intent(
                                    MenuPrincipalActivity.this,
                                    ListadoMedicamentosActivity.class
                            );


                    startActivity(intent);
                }
        );


        // =================================================
        // TARJETA ALARMAS
        // =================================================

        cardAlarmas.setOnClickListener(
                view -> {

                    Intent intent =
                            new Intent(
                                    MenuPrincipalActivity.this,
                                    AlarmasActivity.class
                            );


                    startActivity(intent);
                }
        );


        // =================================================
        // BOTÓN TOMADA
        // =================================================

        botonMarcarToma.setOnClickListener(
                view -> {


                    // =================================================
                    // COMPROBAR QUE EXISTE UNA TOMA
                    // =================================================

                    if (proximaToma == null) {

                        return;
                    }


                    // =================================================
                    // COMPROBAR HORA
                    // =================================================

                    try {


                        LocalDateTime ahora =
                                LocalDateTime.now();


                        LocalDateTime fechaToma =
                                LocalDateTime.parse(

                                        proximaToma.getFechaHora(),

                                        FORMATO_FECHA_HORA
                                );


                        /*
                         * Segunda protección.
                         *
                         * Aunque el botón esté deshabilitado,
                         * volvemos a comprobar que la hora haya
                         * llegado.
                         */

                        if (
                                ahora.isBefore(fechaToma)
                        ) {

                            return;
                        }


                    } catch (Exception e) {


                        e.printStackTrace();

                        return;
                    }


                    // =================================================
                    // MARCAR COMO TOMADA
                    // =================================================

                    GestorTomas.actualizarEstado(

                            MenuPrincipalActivity.this,

                            proximaToma.getId(),

                            "TOMADA"
                    );


                    // =================================================
                    // BUSCAR SIGUIENTE TOMA
                    // =================================================

                    cargarProximaToma();
                }
        );


        // =================================================
        // CARGAR PRÓXIMA TOMA
        // =================================================

        cargarProximaToma();
    }


    // =====================================================
    // ON RESUME
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();


        // =================================================
        // ACTUALIZAR PRÓXIMA TOMA
        // =================================================

        cargarProximaToma();


        // =================================================
        // INICIAR COMPROBADOR
        // =================================================

        iniciarComprobadorHora();
    }


    // =====================================================
    // ON PAUSE
    // =====================================================

    @Override
    protected void onPause() {

        super.onPause();


        // =================================================
        // DETENER HANDLER
        // =================================================

        detenerComprobadorHora();
    }


    // =====================================================
    // INICIAR COMPROBADOR
    // =====================================================

    private void iniciarComprobadorHora() {


        /*
         * Eliminamos cualquier ejecución anterior
         * para evitar tener varios Handler funcionando.
         */

        handler.removeCallbacks(
                comprobadorHora
        );


        // Ejecutar inmediatamente

        handler.post(
                comprobadorHora
        );
    }


    // =====================================================
    // DETENER COMPROBADOR
    // =====================================================

    private void detenerComprobadorHora() {

        handler.removeCallbacks(
                comprobadorHora
        );
    }


    // =====================================================
    // BUSCAR PRÓXIMA TOMA
    // =====================================================

    private void cargarProximaToma() {


        // =================================================
        // OBTENER TOMAS PENDIENTES
        // =================================================

        ArrayList<Toma> tomasPendientes =
                GestorTomas.obtenerTomasPendientes(
                        this
                );


        // =================================================
        // COMPROBAR SI NO HAY TOMAS
        // =================================================

        if (tomasPendientes.isEmpty()) {


            mostrarSinTomas();

            return;
        }


        // =================================================
        // MOMENTO ACTUAL
        // =================================================

        LocalDateTime ahora =
                LocalDateTime.now();


        // =================================================
        // VARIABLES PARA LA TOMA MÁS CERCANA
        // =================================================

        Toma tomaMasCercana = null;

        LocalDateTime fechaTomaMasCercana = null;


        // =================================================
        // RECORRER TODAS LAS TOMAS
        // =================================================

        for (
                Toma toma :
                tomasPendientes
        ) {


            try {


                // =================================================
                // CONVERTIR FECHA/HORA
                // =================================================

                LocalDateTime fechaToma =
                        LocalDateTime.parse(

                                toma.getFechaHora(),

                                FORMATO_FECHA_HORA
                        );


                // =================================================
                // IGNORAR TOMAS YA PASADAS
                // =================================================

                /*
                 * ESTA ES LA PARTE IMPORTANTE.
                 *
                 * Si la toma ocurrió antes de "ahora",
                 * no la utilizamos como Próxima toma.
                 *
                 * Por ejemplo:
                 *
                 * Ahora       -> 15:30
                 *
                 * 10:00       -> ignorar
                 * 14:00       -> ignorar
                 * 16:00       -> candidata
                 * 20:00       -> candidata
                 *
                 * La de 16:00 será la elegida.
                 */

                if (
                        fechaToma.isBefore(ahora)
                ) {

                    continue;
                }


                // =================================================
                // COMPROBAR SI ES LA MÁS CERCANA
                // =================================================

                if (

                        fechaTomaMasCercana == null

                                ||

                                fechaToma.isBefore(
                                        fechaTomaMasCercana
                                )

                ) {


                    fechaTomaMasCercana =
                            fechaToma;


                    tomaMasCercana =
                            toma;
                }


            } catch (Exception e) {


                /*
                 * Si una toma tiene una fecha incorrecta,
                 * simplemente la ignoramos.
                 */

                e.printStackTrace();
            }
        }


        // =================================================
        // COMPROBAR SI ENCONTRAMOS UNA TOMA
        // =================================================

        if (tomaMasCercana == null) {


            /*
             * Puede ocurrir que todas las tomas pendientes
             * sean antiguas.
             *
             * En ese caso no mostramos ninguna como
             * "Próxima toma".
             */

            mostrarSinTomas();

            return;
        }


        // =================================================
        // GUARDAR TOMA SELECCIONADA
        // =================================================

        proximaToma =
                tomaMasCercana;


        // =================================================
        // OBTENER MEDICAMENTO
        // =================================================

        Medicamento medicamento =
                GestorMedicamentos.obtenerMedicamentoPorId(

                        this,

                        proximaToma.getIdMedicamento()
                );


        // =================================================
        // COMPROBAR MEDICAMENTO
        // =================================================

        if (medicamento == null) {


            textoProximoMedicamento.setText(
                    "Medicamento no encontrado"
            );


            textoProximaToma.setText(
                    ""
            );


            botonMarcarToma.setVisibility(
                    View.GONE
            );


            return;
        }


        // =================================================
        // MOSTRAR MEDICAMENTO
        // =================================================

        textoProximoMedicamento.setText(

                "💊 "
                        + medicamento.getNombre()

                        + "\n"

                        + medicamento.getDosis()
                        + " "
                        + medicamento.getUnidad()
        );


        // =================================================
        // MOSTRAR FECHA/HORA
        // =================================================

        textoProximaToma.setText(

                "📅 "
                        + formatearFecha(
                        proximaToma.getFechaHora()
                )
        );


        // =================================================
        // MOSTRAR BOTÓN
        // =================================================

        botonMarcarToma.setVisibility(
                View.VISIBLE
        );


        // =================================================
        // ACTUALIZAR ESTADO DEL BOTÓN
        // =================================================

        actualizarEstadoBoton();
    }


    // =====================================================
    // MOSTRAR QUE NO HAY TOMAS
    // =====================================================

    private void mostrarSinTomas() {


        // =================================================
        // BORRAR TOMA ACTUAL
        // =================================================

        proximaToma = null;


        // =================================================
        // MOSTRAR MENSAJE
        // =================================================

        textoProximoMedicamento.setText(
                "No hay tomas pendientes"
        );


        textoProximaToma.setText(
                ""
        );


        // =================================================
        // OCULTAR BOTÓN
        // =================================================

        botonMarcarToma.setVisibility(
                View.GONE
        );


        botonMarcarToma.setEnabled(
                false
        );
    }


    // =====================================================
    // ACTUALIZAR ESTADO DEL BOTÓN
    // =====================================================

    private void actualizarEstadoBoton() {


        // =================================================
        // COMPROBAR TOMA
        // =================================================

        if (proximaToma == null) {


            botonMarcarToma.setEnabled(
                    false
            );


            return;
        }


        try {


            // =================================================
            // MOMENTO ACTUAL
            // =================================================

            LocalDateTime ahora =
                    LocalDateTime.now();


            // =================================================
            // FECHA DE LA TOMA
            // =================================================

            LocalDateTime fechaToma =
                    LocalDateTime.parse(

                            proximaToma.getFechaHora(),

                            FORMATO_FECHA_HORA
                    );


            // =================================================
            // TODAVÍA NO HA LLEGADO
            // =================================================

            if (
                    ahora.isBefore(fechaToma)
            ) {


                botonMarcarToma.setEnabled(
                        false
                );


                botonMarcarToma.setText(

                        "⏳ Esperar hasta las "
                                + fechaToma.format(

                                DateTimeFormatter.ofPattern(
                                        "HH:mm"
                                )
                        )
                );


            } else {


                // =================================================
                // YA HA LLEGADO LA HORA
                // =================================================

                botonMarcarToma.setEnabled(
                        true
                );


                botonMarcarToma.setText(
                        "✓ TOMADA"
                );
            }


        } catch (Exception e) {


            // =================================================
            // POR SEGURIDAD
            // =================================================

            botonMarcarToma.setEnabled(
                    false
            );


            botonMarcarToma.setText(
                    "Hora no disponible"
            );


            e.printStackTrace();
        }
    }


    // =====================================================
    // FORMATEAR FECHA
    // =====================================================

    private String formatearFecha(
            String fechaHora) {


        try {


            // =================================================
            // CONVERTIR FECHA
            // =================================================

            LocalDateTime fecha =
                    LocalDateTime.parse(

                            fechaHora,

                            FORMATO_FECHA_HORA
                    );


            // =================================================
            // AHORA
            // =================================================

            LocalDateTime ahora =
                    LocalDateTime.now();


            // =================================================
            // HOY
            // =================================================

            if (
                    fecha.toLocalDate()
                            .equals(
                                    ahora.toLocalDate()
                            )
            ) {


                return "Hoy a las "
                        + fecha.format(

                        DateTimeFormatter.ofPattern(
                                "HH:mm"
                        )
                );
            }


            // =================================================
            // MAÑANA
            // =================================================

            if (
                    fecha.toLocalDate()
                            .equals(

                                    ahora.toLocalDate()
                                            .plusDays(1)

                            )
            ) {


                return "Mañana a las "
                        + fecha.format(

                        DateTimeFormatter.ofPattern(
                                "HH:mm"
                        )
                );
            }


            // =================================================
            // OTRA FECHA
            // =================================================

            return fecha.format(

                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy 'a las' HH:mm"
                    )
            );


        } catch (Exception e) {


            // =================================================
            // SI HAY ERROR
            // =================================================

            return fechaHora;
        }
    }
}