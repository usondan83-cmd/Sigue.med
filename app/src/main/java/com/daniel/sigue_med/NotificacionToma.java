package com.daniel.sigue_med;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

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
}