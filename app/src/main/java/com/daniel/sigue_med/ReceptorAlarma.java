package com.daniel.sigue_med;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

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
        // ACCIÓN: TOMADA
        // =====================================================

        Intent intentTomada =
                new Intent(
                        context,
                        AccionTomaReceiver.class
                );


        intentTomada.putExtra(
                "idMedicamento",
                idMedicamento
        );


        intentTomada.putExtra(
                "fechaHora",
                fechaHora
        );


        intentTomada.putExtra(
                "idAlarma",
                idAlarma
        );


        /*
         * Indicamos qué acción queremos realizar.
         *
         * Esto permitirá que AccionTomaReceiver sepa
         * si el usuario ha pulsado "Tomada" u "Omitir".
         */

        intentTomada.putExtra(
                "accion",
                "TOMADA"
        );


        // =================================================
        // PENDING INTENT TOMADA
        // =================================================

        PendingIntent pendingTomada =
                PendingIntent.getBroadcast(

                        context,

                        idAlarma,

                        intentTomada,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        // =====================================================
        // ACCIÓN: OMITIR
        // =====================================================

        Intent intentOmitir =
                new Intent(
                        context,
                        AccionTomaReceiver.class
                );


        intentOmitir.putExtra(
                "idMedicamento",
                idMedicamento
        );


        intentOmitir.putExtra(
                "fechaHora",
                fechaHora
        );


        intentOmitir.putExtra(
                "idAlarma",
                idAlarma
        );


        /*
         * Esta vez indicamos que la acción es OMITIR.
         */

        intentOmitir.putExtra(
                "accion",
                "OMITIR"
        );


        // =================================================
        // PENDING INTENT OMITIR
        // =================================================

        /*
         * MUY IMPORTANTE:
         *
         * Utilizamos un requestCode diferente al de
         * "Tomada".
         *
         * De esta manera Android no confundirá ambos
         * PendingIntent.
         */

        int requestCodeOmitir =
                idAlarma + 100000;


        PendingIntent pendingOmitir =
                PendingIntent.getBroadcast(

                        context,

                        requestCodeOmitir,

                        intentOmitir,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        // =====================================================
        // CREAR NOTIFICACIÓN
        // =====================================================

        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(
                        context,
                        NotificacionToma.CANAL_ID
                );


        notificacion
                .setSmallIcon(
                        android.R.drawable.ic_dialog_info
                )

                .setContentTitle(
                        "💊 Hora de tomar el medicamento"
                )

                .setContentText(
                        "Es hora de tomar "
                                + nombreMedicamento
                )

                .setPriority(
                        NotificationCompat.PRIORITY_HIGH
                )

                .setAutoCancel(true);


        // =====================================================
        // BOTÓN TOMADA
        // =====================================================

        notificacion.addAction(

                android.R.drawable.ic_menu_save,

                "✓ Tomada",

                pendingTomada
        );


        // =====================================================
        // BOTÓN OMITIR
        // =====================================================

        notificacion.addAction(

                android.R.drawable.ic_menu_close_clear_cancel,

                "Omitir",

                pendingOmitir
        );


        // =====================================================
        // OBTENER ADMINISTRADOR
        // =====================================================

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );


        // =====================================================
        // MOSTRAR NOTIFICACIÓN
        // =====================================================

        manager.notify(

                idAlarma,

                notificacion.build()
        );
    }
}