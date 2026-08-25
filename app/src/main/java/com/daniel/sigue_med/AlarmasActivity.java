package com.daniel.sigue_med;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.google.android.material.card.MaterialCardView;

public class AlarmasActivity extends AppCompatActivity {

    // COMPONENTES

    private LinearLayout contenedorAlarmas;

    private Button botonVolver;

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarmas);

        // =================================================
        // CONECTAR COMPONENTES CON XML
        // =================================================

        contenedorAlarmas = findViewById(R.id.contenedorAlarmas);

        botonVolver = findViewById(R.id.botonVolver);


        // BOTÓN VOLVER

        botonVolver.setOnClickListener(view -> finish());

        // CARGAR MEDICAMENTOS

        cargarAlarmas();
    }

    // ON RESUME

    @Override
    protected void onResume() {

        super.onResume();

        cargarAlarmas();
    }


    // =====================================================
    // CARGAR ALARMAS
    // =====================================================

    private void cargarAlarmas() {

        // LIMPIAR CONTENEDOR

        contenedorAlarmas.removeAllViews();

        // OBTENER MEDICAMENTOS

        ArrayList<Medicamento> medicamentos =
                GestorMedicamentos
                        .obtenerMedicamentosBBDD(this);

        // COMPROBAR SI HAY MEDICAMENTOS

        if (medicamentos.isEmpty()) {

            TextView texto =  new TextView(this);

            texto.setText(
                    "No hay medicamentos registrados."
            );

            texto.setTextSize(18);

            texto.setPadding(
                    30,
                    30,
                    30,
                    30
            );

            contenedorAlarmas.addView(texto);

            return;
        }

        // RECORRER MEDICAMENTOS

        for (Medicamento medicamento : medicamentos) {

            crearTarjetaAlarma(medicamento);
        }
    }

    // CREAR TARJETA DE ALARMA

    private void crearTarjetaAlarma(Medicamento medicamento) {

        // CREAR TARJETA

        MaterialCardView tarjeta = new MaterialCardView(this);

        tarjeta.setRadius(20);

        tarjeta.setCardElevation(6);

        // =================================================
        // CONTENIDO
        // =================================================

        LinearLayout contenido =  new LinearLayout(this);


        contenido.setOrientation(LinearLayout.VERTICAL);


        contenido.setPadding(
                30,
                30,
                30,
                30
        );


        // =================================================
        // NOMBRE
        // =================================================

        TextView nombre =
                new TextView(this);


        nombre.setText(
                "💊 "
                        + medicamento.getNombre()
        );


        nombre.setTextSize(22);

        nombre.setTextAlignment(
                View.TEXT_ALIGNMENT_CENTER
        );


        // =================================================
        // INFORMACIÓN
        // =================================================

        TextView informacion =
                new TextView(this);


        String textoInformacion =

                "Dosis: "
                        + medicamento.getDosis()
                        + " "
                        + medicamento.getUnidad()

                        + "\nFrecuencia: "
                        + medicamento.getFrecuencia()

                        + "\nPrimera toma: "
                        + medicamento.getHoraInicio();


        informacion.setText(
                textoInformacion
        );


        informacion.setTextSize(17);


        informacion.setPadding(
                0,
                20,
                0,
                20
        );


        // =================================================
        // ESTADO DE LA ALARMA
        // =================================================

        TextView estado =
                new TextView(this);


        actualizarTextoEstado(
                estado,
                medicamento
        );


        estado.setTextSize(18);

        estado.setTextAlignment(
                View.TEXT_ALIGNMENT_CENTER
        );


        // =================================================
        // BOTÓN
        // =================================================

        Button botonAlarma =
                new Button(this);


        actualizarTextoBoton(
                botonAlarma,
                medicamento
        );


        // =================================================
        // EVENTO DEL BOTÓN
        // =================================================

        botonAlarma.setOnClickListener(
                view -> {


                    // =====================================
                    // ACTIVAR ALARMA
                    // =====================================

                    if (!medicamento.isAlarmaActiva()) {


                        // Programar alarmas

                        boolean alarmasProgramadas =
                                GestorAlarmas.programarAlarmas(
                                        AlarmasActivity.this,
                                        medicamento
                                );


                        // Si no se pudo programar (falta permiso),
                        // no hacemos nada más. El usuario ya fue
                        // redirigido a Ajustes por
                        // solicitarPermisoAlarmasExactas().

                        if (!alarmasProgramadas) {

                            return;
                        }


                        // Cambiar estado

                        medicamento.setAlarmaActiva(
                                true
                        );


                        // Guardar en SQLite

                        GestorMedicamentos
                                .actualizarMedicamento(
                                        AlarmasActivity.this,
                                        medicamento
                                );


                        // Actualizar interfaz

                        estado.setText(
                                "🔔 Alarma activada"
                        );


                        botonAlarma.setText(
                                "Desactivar alarma"
                        );


                    }
                }
        );


        // =================================================
        // AÑADIR ELEMENTOS
        // =================================================

        contenido.addView(
                nombre
        );

        contenido.addView(
                informacion
        );

        contenido.addView(
                estado
        );

        contenido.addView(
                botonAlarma
        );


        // =================================================
        // AÑADIR CONTENIDO A TARJETA
        // =================================================

        tarjeta.addView(
                contenido
        );


        // =================================================
        // MÁRGENES DE LA TARJETA
        // =================================================

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


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

        contenedorAlarmas.addView(
                tarjeta
        );
    }


    // =====================================================
    // ACTUALIZAR TEXTO DEL ESTADO
    // =====================================================

    private void actualizarTextoEstado(
            TextView estado,
            Medicamento medicamento) {


        if (medicamento.isAlarmaActiva()) {

            estado.setText(
                    "🔔 Alarma activada"
            );

        } else {

            estado.setText(
                    "🔕 Alarma desactivada"
            );
        }
    }


    // =====================================================
    // ACTUALIZAR TEXTO DEL BOTÓN
    // =====================================================

    private void actualizarTextoBoton(
            Button boton,
            Medicamento medicamento) {


        if (medicamento.isAlarmaActiva()) {

            boton.setText(
                    "Desactivar alarma"
            );

        } else {

            boton.setText(
                    "Activar alarma"
            );
        }
    }
}