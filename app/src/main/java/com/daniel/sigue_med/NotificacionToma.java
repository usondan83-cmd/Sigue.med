package com.daniel.sigue_med;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificacionToma {


    // =====================================================
    // IDENTIFICADOR DEL CANAL
    // =====================================================

    /*
     * Este identificador identifica el canal de Android.
     *
     * IMPORTANTE:
     * Una vez creado un canal, Android conserva su configuración.
     * Por eso, si posteriormente cambiamos el sonido o la
     * importancia, puede ser necesario eliminar/reinstalar
     * la aplicación para probar los cambios.
     */

    public static final String CANAL_ID =
            "canal_tomas_medicamentos";


    // =====================================================
    // CREAR CANAL DE NOTIFICACIONES
    // =====================================================

    public static void crearCanal(Context context) {


        // =================================================
        // COMPROBAR VERSIÓN DE ANDROID
        // =================================================

        /*
         * Los NotificationChannel existen desde Android 8
         * (API 26).
         */

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {


            // =============================================
            // OBTENER SONIDO DE NOTIFICACIÓN DEL TELÉFONO
            // =============================================

            /*
             * Utilizamos el sonido de notificación
             * predeterminado del teléfono.
             *
             * No necesitamos incluir un archivo de sonido
             * dentro de nuestra aplicación.
             */

            Uri sonido =
                    RingtoneManager.getDefaultUri(
                            RingtoneManager.TYPE_NOTIFICATION
                    );


            // =============================================
            // CONFIGURAR ATRIBUTOS DEL SONIDO
            // =============================================

            AudioAttributes atributosSonido =
                    new AudioAttributes.Builder()

                            .setUsage(
                                    AudioAttributes.USAGE_NOTIFICATION
                            )

                            .setContentType(
                                    AudioAttributes.CONTENT_TYPE_SONIFICATION
                            )

                            .build();


            // =============================================
            // CREAR CANAL
            // =============================================

            NotificationChannel canal =
                    new NotificationChannel(

                            CANAL_ID,

                            "Tomas de medicamentos",

                            NotificationManager.IMPORTANCE_HIGH
                    );


            // =============================================
            // DESCRIPCIÓN
            // =============================================

            canal.setDescription(
                    "Avisos para las tomas de medicamentos"
            );


            // =============================================
            // CONFIGURAR SONIDO
            // =============================================

            canal.setSound(
                    sonido,
                    atributosSonido
            );


            // =============================================
            // ACTIVAR VIBRACIÓN
            // =============================================

            canal.enableVibration(true);


            /*
             * Patrón de vibración:
             *
             * 0 ms  -> espera inicial
             * 500   -> vibra
             * 300   -> pausa
             * 500   -> vibra
             */

            canal.setVibrationPattern(
                    new long[]{
                            0,
                            500,
                            300,
                            500
                    }
            );


            // =============================================
            // OBTENER ADMINISTRADOR
            // =============================================

            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );


            // =============================================
            // CREAR CANAL
            // =============================================

            if (manager != null) {

                manager.createNotificationChannel(
                        canal
                );
            }
        }
    }


    // =====================================================
    // MOSTRAR NOTIFICACIÓN DE TOMA
    // =====================================================

    /*
     * La usan tanto ReceptorAlarma (primera notificación)
     * como RecordatorioComprobacionReceiver (si la toma
     * sigue pendiente a los 5 minutos).
     */

    public static void mostrarNotificacion(
            Context context,
            int idMedicamento,
            String nombreMedicamento,
            String fechaHora,
            int idAlarma) {


        // =================================================
        // ACCIÓN: TOMADA
        // =================================================

        Intent intentTomada =
                new Intent(context, AccionTomaReceiver.class);

        intentTomada.putExtra("idMedicamento", idMedicamento);
        intentTomada.putExtra("fechaHora", fechaHora);
        intentTomada.putExtra("idAlarma", idAlarma);
        intentTomada.putExtra("accion", "TOMADA");

        PendingIntent pendingTomada =
                PendingIntent.getBroadcast(
                        context,
                        idAlarma,
                        intentTomada,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        // =================================================
        // ACCIÓN: OMITIR
        // =================================================

        Intent intentOmitir =
                new Intent(context, AccionTomaReceiver.class);

        intentOmitir.putExtra("idMedicamento", idMedicamento);
        intentOmitir.putExtra("fechaHora", fechaHora);
        intentOmitir.putExtra("idAlarma", idAlarma);
        intentOmitir.putExtra("accion", "OMITIR");

        int requestCodeOmitir = idAlarma + 100000;

        PendingIntent pendingOmitir =
                PendingIntent.getBroadcast(
                        context,
                        requestCodeOmitir,
                        intentOmitir,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );


        // =================================================
        // CONSTRUIR NOTIFICACIÓN
        // =================================================

        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(context, CANAL_ID);

        notificacion
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💊 Hora de tomar el medicamento")
                .setContentText("Es hora de tomar " + nombreMedicamento)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificacion.addAction(
                android.R.drawable.ic_menu_save, "✓ Tomada", pendingTomada
        );

        notificacion.addAction(
                android.R.drawable.ic_menu_close_clear_cancel, "Omitir", pendingOmitir
        );


        // =================================================
        // MOSTRAR NOTIFICACIÓN
        // =================================================

        NotificationManager manager =
                (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

        if (manager != null) {
            manager.notify(idAlarma, notificacion.build());
        }
    }
}