package com.daniel.sigue_med;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GestorAlarmas {


    // =====================================================
    // VENTANA DE PROGRAMACIÓN DE ALARMAS
    // =====================================================

    /*
     * No programamos alarmas para el tratamiento completo
     * (podrían ser miles para tratamientos largos, superando
     * el límite de alarmas exactas que impone Android por
     * aplicación).
     *
     * Solo programamos las de los próximos N días.
     *
     * Debe coincidir con DIAS_VENTANA_TOMAS de
     * RegistroMedicamentoActivity, para que las alarmas
     * programadas correspondan exactamente con las tomas
     * ya insertadas en SQLite.
     */

    private static final int DIAS_VENTANA_ALARMAS = 14;


    // =====================================================
    // COMPROBAR SI PODEMOS PROGRAMAR ALARMAS EXACTAS
    // =====================================================

    public static boolean puedeProgramarAlarmasExactas(
            Context context) {


        // Obtener AlarmManager

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE
                );


        // Android anterior a Android 12

        if (Build.VERSION.SDK_INT <
                Build.VERSION_CODES.S) {

            return true;
        }


        // Android 12 o superior

        return alarmManager.canScheduleExactAlarms();
    }


    // =====================================================
    // SOLICITAR PERMISO PARA ALARMAS EXACTAS
    // =====================================================

    public static void solicitarPermisoAlarmasExactas(
            Context context) {


        // Las alarmas exactas requieren este permiso
        // desde Android 12.

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S) {


            Intent intent =
                    new Intent(
                            android.provider.Settings
                                    .ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    );


            // Indicar que queremos configurar
            // nuestra propia aplicación.

            intent.setData(
                    android.net.Uri.parse(
                            "package:"
                                    + context.getPackageName()
                    )
            );


            // Abrir configuración de Android

            context.startActivity(intent);
        }
    }


    // =====================================================
    // PROGRAMAR TODAS LAS ALARMAS DE UN MEDICAMENTO
    // =====================================================

    /*
     * MODIFICADO (paso 3 del plan):
     *
     * Antes se calculaban TODAS las tomas del tratamiento
     * completo y se intentaba programar una alarma exacta
     * por cada una (podían ser miles), superando el límite
     * que Android impone por aplicación.
     *
     * Ahora solo calculamos y programamos las tomas dentro
     * de una ventana de DIAS_VENTANA_ALARMAS días.
     */

    public static Boolean programarAlarmas(
            Context context,
            Medicamento medicamento) {


        // =================================================
        // COMPROBAR PERMISO
        // =================================================

        if (!puedeProgramarAlarmasExactas(context)) {

            // No tenemos permiso.

            solicitarPermisoAlarmasExactas(context);

            return false;
        }


        // =================================================
        // CALCULAR LÍMITE SUPERIOR DE LA VENTANA
        // =================================================

        LocalDateTime limiteSuperior =
                LocalDateTime.now()
                        .plusDays(
                                DIAS_VENTANA_ALARMAS
                        );


        // =================================================
        // OBTENER LAS TOMAS CALCULADAS DENTRO DE LA VENTANA
        // =================================================

        ArrayList<LocalDateTime> tomas =
                CalculadorTomas.calcularTomas(

                        medicamento.getFechaInicio(),

                        medicamento.getHoraInicio(),

                        medicamento.getFrecuencia(),

                        medicamento.getDiasTratamiento(),

                        limiteSuperior
                );


        // =================================================
        // OBTENER FECHA Y HORA ACTUALES
        // =================================================

        LocalDateTime ahora =
                LocalDateTime.now();


        // =================================================
        // OBTENER ALARM MANAGER
        // =================================================

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE
                );


        // =================================================
        // FORMATO DE FECHA Y HORA
        // =================================================

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );


        // =================================================
        // RECORRER LAS TOMAS
        // =================================================

        for (int i = 0; i < tomas.size(); i++) {


            // Obtener toma

            LocalDateTime toma =
                    tomas.get(i);


            // =================================================
            // IGNORAR TOMAS QUE YA HAN PASADO
            // =================================================

            if (!toma.isAfter(ahora)) {

                continue;
            }


            // =================================================
            // CONVERTIR LOCALDATETIME A MILISEGUNDOS
            // =================================================

            long tiempoAlarma =
                    toma.atZone(
                                    ZoneId.systemDefault()
                            )
                            .toInstant()
                            .toEpochMilli();


            // =================================================
            // CREAR ID ÚNICO DE LA ALARMA
            // =================================================

            int idAlarma =
                    medicamento.getId() * 1000 + i;


            // =================================================
            // CREAR INTENT
            // =================================================

            Intent intent =
                    new Intent(
                            context,
                            ReceptorAlarma.class
                    );


            // =================================================
            // ENVIAR DATOS PARA LA ALARMA
            // =================================================

            intent.putExtra(
                    "idMedicamento",
                    medicamento.getId()
            );

            intent.putExtra(
                    "nombreMedicamento",
                    medicamento.getNombre()
            );

            String fechaHora =
                    toma.format(formato);


            intent.putExtra(
                    "fechaHora",
                    fechaHora
            );

            intent.putExtra(
                    "idAlarma",
                    idAlarma
            );

            intent.putExtra(
                    "dosis",
                    medicamento.getDosis()
            );

            intent.putExtra(
                    "unidad",
                    medicamento.getUnidad()
            );


            // =================================================
            // CREAR PENDING INTENT
            // =================================================

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(

                            context,

                            idAlarma,

                            intent,

                            PendingIntent.FLAG_UPDATE_CURRENT
                                    | PendingIntent.FLAG_IMMUTABLE
                    );


            // =================================================
            // PROGRAMAR ALARMA
            // =================================================

            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M) {


                alarmManager.setExactAndAllowWhileIdle(

                        AlarmManager.RTC_WAKEUP,

                        tiempoAlarma,

                        pendingIntent
                );

            } else {


                alarmManager.setExact(

                        AlarmManager.RTC_WAKEUP,

                        tiempoAlarma,

                        pendingIntent
                );
            }
        }
        return true;
    }


    // =====================================================
    // CANCELAR TODAS LAS ALARMAS DE UN MEDICAMENTO
    // =====================================================

    /*
     * NOTA IMPORTANTE SOBRE ESTE MÉTODO:
     *
     * Sigue usando calcularTomas() SIN límite (todas las
     * tomas del tratamiento completo). Esto es intencional
     * por ahora: cancelarAlarmas() necesita generar los
     * MISMOS índices "i" que se usaron al programar, para
     * reconstruir los mismos idAlarma (medicamento.getId() * 1000 + i)
     * y poder cancelar el PendingIntent correcto con
     * FLAG_NO_CREATE.
     *
     * Como solo cancela PendingIntents que ya existen
     * (FLAG_NO_CREATE no crea nada nuevo), recorrer de más
     * no inserta nada en SQLite ni programa nada: simplemente
     * no encuentra pendingIntent para los índices que nunca
     * se llegaron a programar, y el "if (pendingIntent != null)"
     * los ignora. No causa el mismo problema de memoria que
     * programarAlarmas() tenía, pero sí es un recorrido más
     * largo de lo necesario para tratamientos muy extensos.
     *
     * Si en el futuro se quiere optimizar también esto,
     * habría que guardar en SQLite qué índices "i" se llegaron
     * a programar realmente, en vez de recalcularlos.
     */

    public static void cancelarAlarmas(
            Context context,
            Medicamento medicamento) {


        // =================================================
        // OBTENER ALARM MANAGER
        // =================================================

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE
                );


        // =================================================
        // OBTENER LAS TOMAS CALCULADAS
        // =================================================

        ArrayList<LocalDateTime> tomas =
                CalculadorTomas.calcularTomas(

                        medicamento.getFechaInicio(),

                        medicamento.getHoraInicio(),

                        medicamento.getFrecuencia(),

                        medicamento.getDiasTratamiento()
                );


        // =================================================
        // RECORRER LAS TOMAS
        // =================================================

        for (int i = 0; i < tomas.size(); i++) {


            // =================================================
            // CREAR ID DE LA MISMA ALARMA
            // =================================================

            int idAlarma =
                    medicamento.getId() * 1000 + i;


            // =================================================
            // CREAR INTENT
            // =================================================

            Intent intent =
                    new Intent(
                            context,
                            ReceptorAlarma.class
                    );


            // =================================================
            // ENVIAR ID DEL MEDICAMENTO
            // =================================================

            intent.putExtra(
                    "idMedicamento",
                    medicamento.getId()
            );


            // =================================================
            // ENVIAR NOMBRE
            // =================================================

            intent.putExtra(
                    "nombreMedicamento",
                    medicamento.getNombre()
            );


            // =================================================
            // OBTENER FECHA Y HORA
            // =================================================

            String fechaHora =
                    tomas.get(i).format(
                            DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy HH:mm"
                            )
                    );


            intent.putExtra(
                    "fechaHora",
                    fechaHora
            );


            // =================================================
            // ENVIAR ID DE ALARMA
            // =================================================

            intent.putExtra(
                    "idAlarma",
                    idAlarma
            );


            // =================================================
            // OBTENER PENDING INTENT EXISTENTE
            // =================================================

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(

                            context,

                            idAlarma,

                            intent,

                            PendingIntent.FLAG_NO_CREATE
                                    | PendingIntent.FLAG_IMMUTABLE
                    );


            // =================================================
            // CANCELAR ALARMA
            // =================================================

            if (pendingIntent != null) {


                alarmManager.cancel(
                        pendingIntent
                );


                // Eliminar también el PendingIntent

                pendingIntent.cancel();
            }
        }
    }
}