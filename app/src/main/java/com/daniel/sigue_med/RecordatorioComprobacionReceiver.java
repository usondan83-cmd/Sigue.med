package com.daniel.sigue_med;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class RecordatorioComprobacionReceiver extends BroadcastReceiver {

    private static final int MINUTOS_ESPERA = 5;

    // 1 = comprobación a los 5 min tras la notificación original
    // 2 = comprobación a los 10 min; si sigue pendiente, se omite

    private static final int MAX_RECORDATORIOS = 2;

    @Override
    public void onReceive(Context context, Intent intent) {

        int idMedicamento = intent.getIntExtra("idMedicamento", -1);
        String nombreMedicamento = intent.getStringExtra("nombreMedicamento");
        String fechaHora = intent.getStringExtra("fechaHora");
        int idAlarma = intent.getIntExtra("idAlarma", -1);
        int numeroRecordatorio = intent.getIntExtra("numeroRecordatorio", 1);

        if (idMedicamento == -1 || fechaHora == null || fechaHora.isEmpty()) {
            return;
        }

        String estadoActual =
                GestorTomas.obtenerEstadoPorMedicamentoYFechaHora(
                        context, idMedicamento, fechaHora
                );

        // Si el usuario ya respondió, o la toma ya no existe, no hacemos nada.

        if (estadoActual == null || !EstadoToma.PENDIENTE.equals(estadoActual)) {
            return;
        }

        if (numeroRecordatorio < MAX_RECORDATORIOS) {

            // Sigue pendiente: volver a notificar y reprogramar

            NotificacionToma.mostrarNotificacion(
                    context, idMedicamento, nombreMedicamento, fechaHora, idAlarma
            );

            programarComprobacion(
                    context, idMedicamento, nombreMedicamento,
                    fechaHora, idAlarma, numeroRecordatorio + 1
            );

        } else {

            // Se acabaron los recordatorios: marcar como OMITIDA

            GestorTomas.actualizarEstadoPorMedicamentoYFechaHora(
                    context, idMedicamento, fechaHora, EstadoToma.OMITIDA
            );

            NotificationManager manager =
                    (NotificationManager) context.getSystemService(
                            Context.NOTIFICATION_SERVICE
                    );

            if (manager != null) {
                manager.cancel(idAlarma);
            }
        }
    }

    public static void programarComprobacion(
            Context context,
            int idMedicamento,
            String nombreMedicamento,
            String fechaHora,
            int idAlarma,
            int numeroRecordatorio) {

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RecordatorioComprobacionReceiver.class);

        intent.putExtra("idMedicamento", idMedicamento);
        intent.putExtra("nombreMedicamento", nombreMedicamento);
        intent.putExtra("fechaHora", fechaHora);
        intent.putExtra("idAlarma", idAlarma);
        intent.putExtra("numeroRecordatorio", numeroRecordatorio);

        int requestCode = idAlarma + 300000;

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        long tiempoComprobacion =
                LocalDateTime.now()
                        .plusMinutes(MINUTOS_ESPERA)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, tiempoComprobacion, pendingIntent
            );

        } else {

            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, tiempoComprobacion, pendingIntent
            );
        }
    }
}