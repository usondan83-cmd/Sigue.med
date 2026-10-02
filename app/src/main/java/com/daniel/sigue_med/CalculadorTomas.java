package com.daniel.sigue_med;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalculadorTomas {


    // =====================================================
    // OBTENER HORAS DE LA FRECUENCIA
    // =====================================================

    public static int obtenerHorasFrecuencia(
            String frecuencia) {


        if (frecuencia.equals("Cada 4 horas")) {

            return 4;

        } else if (frecuencia.equals("Cada 6 horas")) {

            return 6;

        } else if (frecuencia.equals("Cada 8 horas")) {

            return 8;

        } else if (frecuencia.equals("Cada 12 horas")) {

            return 12;

        } else if (frecuencia.equals("Cada 24 horas")) {

            return 24;
        }


        // Frecuencia desconocida

        return 0;
    }


    // =====================================================
    // CALCULAR TODAS LAS TOMAS
    // =====================================================

    public static ArrayList<LocalDateTime> calcularTomas(
            String fechaInicio,
            String horaInicio,
            String frecuencia,
            int diasTratamiento) {


        // Crear lista donde guardaremos las tomas

        ArrayList<LocalDateTime> tomas =
                new ArrayList<>();


        // =================================================
        // OBTENER INTERVALO DE HORAS
        // =================================================

        int horasFrecuencia =
                obtenerHorasFrecuencia(
                        frecuencia
                );


        // =================================================
        // COMPROBAR FRECUENCIA
        // =================================================

        if (horasFrecuencia == 0) {

            Log.e(
                    "TOMAS",
                    "Frecuencia desconocida: "
                            + frecuencia
            );

            return tomas;
        }


        // =================================================
        // COMPROBAR DURACIÓN
        // =================================================

        if (diasTratamiento <= 0) {

            Log.e(
                    "TOMAS",
                    "Días de tratamiento inválidos: "
                            + diasTratamiento
            );

            return tomas;
        }


        try {


            // =================================================
            // FORMATO DE FECHA
            // =================================================

            /*
             * La fecha se guarda desde
             * RegistroMedicamentoActivity
             * con este formato:
             *
             * yyyy-MM-dd
             *
             * Ejemplo:
             *
             * 2026-08-13
             */

            DateTimeFormatter formatoFecha =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd"
                    );


            // =================================================
            // FORMATO DE HORA
            // =================================================

            /*
             * La hora se guarda con:
             *
             * HH:mm
             *
             * Ejemplo:
             *
             * 08:30
             */

            DateTimeFormatter formatoHora =
                    DateTimeFormatter.ofPattern(
                            "HH:mm"
                    );


            // =================================================
            // CONVERTIR FECHA
            // =================================================

            LocalDate fecha =
                    LocalDate.parse(
                            fechaInicio,
                            formatoFecha
                    );


            // =================================================
            // CONVERTIR HORA
            // =================================================

            LocalTime hora =
                    LocalTime.parse(
                            horaInicio,
                            formatoHora
                    );


            // =================================================
            // CREAR FECHA Y HORA INICIAL
            // =================================================

            LocalDateTime fechaHora =
                    LocalDateTime.of(
                            fecha,
                            hora
                    );


            // =================================================
            // CALCULAR NÚMERO DE TOMAS
            // =================================================

            int numeroTomas =
                    (24 / horasFrecuencia)
                            * diasTratamiento;


            // =================================================
            // GENERAR LAS TOMAS
            // =================================================

            for (
                    int i = 0;
                    i < numeroTomas;
                    i++
            ) {


                // Añadir la toma actual

                tomas.add(
                        fechaHora
                );


                // Calcular siguiente toma

                fechaHora =
                        fechaHora.plusHours(
                                horasFrecuencia
                        );
            }


        } catch (Exception e) {


            // =================================================
            // ERROR AL CALCULAR LAS TOMAS
            // =================================================

            Log.e(
                    "TOMAS",
                    "Error al calcular las tomas",
                    e
            );


            tomas.clear();
        }


        // =================================================
        // DEVOLVER RESULTADO
        // =================================================

        return tomas;
    }


    // =====================================================
    // CALCULAR TOMAS DENTRO DE UNA VENTANA LIMITADA
    // =====================================================

    /*
     * Versión con límite superior.
     *
     * En vez de devolver TODAS las tomas del tratamiento
     * completo (lo cual puede ser miles de filas para
     * tratamientos largos, provocando bloqueos al guardar
     * en SQLite y al programar alarmas), esta versión
     * corta la lista en cuanto se alcanza "limiteSuperior".
     *
     * Útil para:
     *
     * - RegistroMedicamentoActivity.crearTomas()
     * - GestorAlarmas.programarAlarmas()
     *
     * Ejemplo de uso:
     *
     * LocalDateTime limite = LocalDateTime.now().plusDays(14);
     *
     * CalculadorTomas.calcularTomas(
     *         fechaInicio, horaInicio, frecuencia,
     *         diasTratamiento, limite
     * );
     */

    public static ArrayList<LocalDateTime> calcularTomas(
            String fechaInicio,
            String horaInicio,
            String frecuencia,
            int diasTratamiento,
            LocalDateTime limiteSuperior) {


        // =================================================
        // CALCULAR TODAS LAS TOMAS (MÉTODO EXISTENTE)
        // =================================================

        ArrayList<LocalDateTime> todas =
                calcularTomas(
                        fechaInicio,
                        horaInicio,
                        frecuencia,
                        diasTratamiento
                );


        // =================================================
        // CREAR LISTA LIMITADA
        // =================================================

        ArrayList<LocalDateTime> limitadas =
                new ArrayList<>();


        // =================================================
        // RECORRER Y CORTAR AL LLEGAR AL LÍMITE
        // =================================================

        for (LocalDateTime toma : todas) {

            if (toma.isAfter(limiteSuperior)) {

                break;
            }

            limitadas.add(toma);
        }


        // =================================================
        // DEVOLVER RESULTADO
        // =================================================

        return limitadas;
    }
}