package com.daniel.sigue_med;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceptorAlarma extends BroadcastReceiver {


    // =====================================================
    // CUANDO ANDROID RECIBE LA ALARMA
    // =====================================================

    @Override
    public void onReceive(
            Context context,
            Intent intent) {


        // =================================================
        // OBTENER ID DEL MEDICAMENTO
        // =================================================

        int idMedicamento =
                intent.getIntExtra(
                        "idMedicamento",
                        -1
                );


        // =================================================
        // OBTENER NOMBRE DEL MEDICAMENTO
        // =================================================

        String nombreMedicamento =
                intent.getStringExtra(
                        "nombreMedicamento"
                );


        // =================================================
        // OBTENER FECHA Y HORA
        // =================================================

        String fechaHora =
                intent.getStringExtra(
                        "fechaHora"
                );


        // =================================================
        // OBTENER ID DE LA ALARMA
        // =================================================

        int idAlarma =
                intent.getIntExtra(
                        "idAlarma",
                        idMedicamento
                );


        // =================================================
        // COMPROBAR NOMBRE
        // =================================================

        if (nombreMedicamento == null ||
                nombreMedicamento.isEmpty()) {

            nombreMedicamento =
                    "Medicamento";
        }


        // =================================================
        // COMPROBAR FECHA Y HORA
        // =================================================

        if (fechaHora == null ||
                fechaHora.isEmpty()) {

            return;
        }


        // =====================================================
        // MOSTRAR NOTIFICACIÓN
        // =====================================================

        NotificacionToma.mostrarNotificacion(
                context,
                idMedicamento,
                nombreMedicamento,
                fechaHora,
                idAlarma
        );


        // =====================================================
        // PROGRAMAR PRIMERA COMPROBACIÓN (A LOS 5 MINUTOS)
        // =====================================================

        RecordatorioComprobacionReceiver.programarComprobacion(
                context,
                idMedicamento,
                nombreMedicamento,
                fechaHora,
                idAlarma,
                1
        );
    }
}