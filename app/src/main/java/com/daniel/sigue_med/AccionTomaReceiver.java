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

        // COMPROBAR ID DEL MEDICAMENTO

        if (idMedicamento == -1) {

            return;
        }

        // COMPROBAR FECHA Y HORA

        if (fechaHora == null || fechaHora.isEmpty()) {

            return;
        }

        // ACTUALIZAR ESTADO DE LA TOMA

        GestorTomas.actualizarEstadoPorMedicamentoYFechaHora(

                        context,

                        idMedicamento,

                        fechaHora,

                        "TOMADA"
                );

        // MENSAJE

        Toast.makeText(

                context,

                "Toma marcada como realizada",

                Toast.LENGTH_SHORT

        ).show();
    }
}
