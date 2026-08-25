package com.daniel.sigue_med;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AccionTomaReceiver
        extends BroadcastReceiver {


    // =====================================================
    // CUANDO EL USUARIO PULSA "TOMADA"
    // =====================================================

    @Override
    public void onReceive(
            Context context,
            Intent intent) {



        // OBTENER ID DEL MEDICAMENTO

        int idMedicamento = intent.getIntExtra(
                        "idMedicamento",
                        -1
                );

        // OBTENER FECHA Y HORA

        String fechaHora = intent.getStringExtra(
                        "fechaHora"
                );

        // OBTENER ACCIÓN (TOMADA U OMITIR)

        String accion = intent.getStringExtra(
                "accion"
        );

        // COMPROBAR ID DEL MEDICAMENTO

        if (idMedicamento == -1) {

            return;
        }

        // COMPROBAR FECHA Y HORA

        if (fechaHora == null || fechaHora.isEmpty()) {

            return;
        }

        // DETERMINAR NUEVO ESTADO SEGÚN LA ACCIÓN

        String nuevoEstado =
                "OMITIR".equals(accion)
                        ? "OMITIDA"
                        : "TOMADA";

        // ACTUALIZAR ESTADO DE LA TOMA

        GestorTomas.actualizarEstadoPorMedicamentoYFechaHora(
                context,
                idMedicamento,
                fechaHora,
                nuevoEstado
        );

// MENSAJE

        String mensaje =
                "OMITIR".equals(accion)
                        ? "Toma marcada como omitida"
                        : "Toma marcada como realizada";

        Toast.makeText(
                context,
                mensaje,
                Toast.LENGTH_SHORT
        ).show();
    }
}
