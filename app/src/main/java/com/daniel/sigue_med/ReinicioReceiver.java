package com.daniel.sigue_med;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class ReinicioReceiver extends BroadcastReceiver {


    // =====================================================
    // ANDROID TERMINÓ DE ARRANCAR
    // =====================================================

    @Override
    public void onReceive(
            Context context,
            Intent intent) {


        // =================================================
        // COMPROBAR QUE REALMENTE ES UN REINICIO
        // =================================================

        if (Intent.ACTION_BOOT_COMPLETED.equals(
                intent.getAction())) {


            Log.d(
                    "REINICIO",
                    "El teléfono ha terminado de arrancar"
            );


            // =============================================
            // OBTENER TODOS LOS MEDICAMENTOS
            // =============================================

            ArrayList<Medicamento> medicamentos =
                    GestorMedicamentos
                            .obtenerMedicamentosBBDD(
                                    context
                            );


            // =============================================
            // RECORRER LOS MEDICAMENTOS
            // =============================================

            for (
                    Medicamento medicamento :
                    medicamentos
            ) {


                // =========================================
                // COMPROBAR SI LAS ALARMAS ESTÁN ACTIVAS
                // =========================================

                if (medicamento.isAlarmaActiva()) {


                    // =====================================
                    // VOLVER A PROGRAMAR LAS ALARMAS
                    // =====================================

                    GestorAlarmas.programarAlarmas(

                            context,

                            medicamento
                    );


                    Log.d(
                            "REINICIO",
                            "Alarmas programadas para: "
                                    + medicamento.getNombre()
                    );
                }
            }
        }
    }
}

