package com.daniel.sigue_med;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class GestorMedicamentos {


    // =====================================================
    // OBTENER TODOS LOS MEDICAMENTOS DESDE SQLITE
    // =====================================================

    public static ArrayList<Medicamento> obtenerMedicamentosBBDD(
            Context context) {


        // =================================================
        // CREAR AYUDANTE DE BASE DE DATOS
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // =================================================
        // ABRIR BASE DE DATOS PARA LECTURA
        // =================================================

        SQLiteDatabase db =
                helper.getReadableDatabase();


        // =================================================
        // CREAR LISTA
        // =================================================

        ArrayList<Medicamento> lista =
                new ArrayList<>();


        Cursor cursor = null;


        try {


            // =================================================
            // CONSULTA SQL
            // =================================================

            cursor =
                    db.rawQuery(
                            "SELECT * FROM medicamentos",
                            null
                    );


            // =================================================
            // RECORRER RESULTADOS
            // =================================================

            while (cursor.moveToNext()) {


                // =============================================
                // ID
                // =============================================

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        );


                // =============================================
                // NOMBRE
                // =============================================

                String nombre =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "nombre"
                                )
                        );


                // =============================================
                // DOSIS
                // =============================================

                double dosis =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "dosis"
                                )
                        );


                // =============================================
                // UNIDAD
                // =============================================

                String unidad =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unidad"
                                )
                        );


                // =============================================
                // FRECUENCIA
                // =============================================

                String frecuencia =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "frecuencia"
                                )
                        );


                // =============================================
                // HORA DE INICIO
                // =============================================

                String horaInicio =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "horaInicio"
                                )
                        );


                // =============================================
                // FECHA DE INICIO
                // =============================================

                String fechaInicio =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "fechaInicio"
                                )
                        );


                // =============================================
                // DÍAS DE TRATAMIENTO
                // =============================================

                int diasTratamiento =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "diasTratamiento"
                                )
                        );


                // =============================================
                // ESTADO DE LA ALARMA
                // =============================================

                int alarmaActiva =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "alarmaActiva"
                                )
                        );


                // =============================================
                // CREAR OBJETO MEDICAMENTO
                // =============================================

                Medicamento medicamento =
                        new Medicamento(

                                nombre,

                                dosis,

                                unidad,

                                frecuencia,

                                horaInicio,

                                fechaInicio,

                                diasTratamiento
                        );


                // =============================================
                // ASIGNAR ESTADO DE ALARMA
                // =============================================

                /*
                 * SQLite guarda:
                 *
                 * 1 = true
                 * 0 = false
                 */

                medicamento.setAlarmaActiva(
                        alarmaActiva == 1
                );


                // =============================================
                // ASIGNAR ID
                // =============================================

                medicamento.setId(id);


                // =============================================
                // AÑADIR A LA LISTA
                // =============================================

                lista.add(medicamento);
            }


        } finally {


            // =================================================
            // CERRAR CURSOR
            // =================================================

            if (cursor != null) {

                cursor.close();
            }


            // =================================================
            // CERRAR BASE DE DATOS
            // =================================================

            db.close();
        }


        // =================================================
        // DEVOLVER LISTA
        // =================================================

        return lista;
    }


    // =====================================================
    // OBTENER UN MEDICAMENTO POR ID
    // =====================================================

    public static Medicamento obtenerMedicamentoPorId(
            Context context,
            int id) {


        // =================================================
        // CREAR AYUDANTE
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // =================================================
        // ABRIR BASE DE DATOS
        // =================================================

        SQLiteDatabase db =
                helper.getReadableDatabase();


        Cursor cursor = null;


        Medicamento medicamento = null;


        try {


            // =================================================
            // CONSULTA
            // =================================================

            cursor =
                    db.rawQuery(

                            "SELECT * FROM medicamentos " +
                                    "WHERE id = ?",

                            new String[]{
                                    String.valueOf(id)
                            }
                    );


            // =================================================
            // COMPROBAR RESULTADO
            // =================================================

            if (cursor.moveToFirst()) {


                // =============================================
                // NOMBRE
                // =============================================

                String nombre =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "nombre"
                                )
                        );


                // =============================================
                // DOSIS
                // =============================================

                double dosis =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "dosis"
                                )
                        );


                // =============================================
                // UNIDAD
                // =============================================

                String unidad =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unidad"
                                )
                        );


                // =============================================
                // FRECUENCIA
                // =============================================

                String frecuencia =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "frecuencia"
                                )
                        );


                // =============================================
                // HORA DE INICIO
                // =============================================

                String horaInicio =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "horaInicio"
                                )
                        );


                // =============================================
                // FECHA DE INICIO
                // =============================================

                String fechaInicio =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "fechaInicio"
                                )
                        );


                // =============================================
                // DÍAS DE TRATAMIENTO
                // =============================================

                int diasTratamiento =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "diasTratamiento"
                                )
                        );


                // =============================================
                // ESTADO DE LA ALARMA
                // =============================================

                int alarmaActiva =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "alarmaActiva"
                                )
                        );


                // =============================================
                // CREAR MEDICAMENTO
                // =============================================

                medicamento =
                        new Medicamento(

                                nombre,

                                dosis,

                                unidad,

                                frecuencia,

                                horaInicio,

                                fechaInicio,

                                diasTratamiento
                        );


                // =============================================
                // ASIGNAR ALARMA
                // =============================================

                medicamento.setAlarmaActiva(
                        alarmaActiva == 1
                );


                // =============================================
                // ASIGNAR ID
                // =============================================

                medicamento.setId(id);
            }


        } finally {


            // =================================================
            // CERRAR CURSOR
            // =================================================

            if (cursor != null) {

                cursor.close();
            }


            // =================================================
            // CERRAR BASE DE DATOS
            // =================================================

            db.close();
        }


        // =================================================
        // DEVOLVER MEDICAMENTO
        // =================================================

        return medicamento;
    }


    // =====================================================
    // GUARDAR MEDICAMENTO EN SQLITE
    // =====================================================

    public static long guardarMedicamento(
            Context context,
            Medicamento medicamento) {


        // =================================================
        // CREAR AYUDANTE
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // =================================================
        // ABRIR PARA ESCRITURA
        // =================================================

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // =================================================
        // PREPARAR DATOS
        // =================================================

        ContentValues valores =
                new ContentValues();


        valores.put(
                "nombre",
                medicamento.getNombre()
        );


        valores.put(
                "dosis",
                medicamento.getDosis()
        );


        valores.put(
                "unidad",
                medicamento.getUnidad()
        );


        valores.put(
                "frecuencia",
                medicamento.getFrecuencia()
        );


        valores.put(
                "horaInicio",
                medicamento.getHoraInicio()
        );


        valores.put(
                "fechaInicio",
                medicamento.getFechaInicio()
        );


        valores.put(
                "diasTratamiento",
                medicamento.getDiasTratamiento()
        );


        // =================================================
        // ESTADO DE LA ALARMA
        // =================================================

        valores.put(
                "alarmaActiva",
                medicamento.isAlarmaActiva()
                        ? 1
                        : 0
        );


        // =================================================
        // INSERTAR
        // =================================================

        long id = -1;


        try {


            id =
                    db.insertOrThrow(

                            "medicamentos",

                            null,

                            valores
                    );


            Log.d(
                    "BBDD",
                    "Medicamento guardado. ID = " + id
            );


        } catch (Exception e) {


            Log.e(
                    "BBDD",
                    "ERROR AL GUARDAR MEDICAMENTO",
                    e
            );


        } finally {


            // =================================================
            // CERRAR BASE DE DATOS
            // =================================================

            db.close();
        }


        // =================================================
        // DEVOLVER ID
        // =================================================

        return id;
    }


    // =====================================================
    // ACTUALIZAR MEDICAMENTO
    // =====================================================

    /*
     * IMPORTANTE:
     *
     * Este método antes devolvía void.
     *
     * Ahora devuelve int.
     *
     * El valor indica cuántos registros fueron modificados:
     *
     * 1 = medicamento actualizado correctamente.
     *
     * 0 = no se encontró ningún medicamento con ese ID.
     */

    public static int actualizarMedicamento(
            Context context,
            Medicamento medicamento) {


        // =================================================
        // CREAR AYUDANTE
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // =================================================
        // ABRIR PARA ESCRITURA
        // =================================================

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // =================================================
        // PREPARAR NUEVOS DATOS
        // =================================================

        ContentValues valores =
                new ContentValues();


        valores.put(
                "nombre",
                medicamento.getNombre()
        );


        valores.put(
                "dosis",
                medicamento.getDosis()
        );


        valores.put(
                "unidad",
                medicamento.getUnidad()
        );


        valores.put(
                "frecuencia",
                medicamento.getFrecuencia()
        );


        valores.put(
                "horaInicio",
                medicamento.getHoraInicio()
        );


        valores.put(
                "fechaInicio",
                medicamento.getFechaInicio()
        );


        valores.put(
                "diasTratamiento",
                medicamento.getDiasTratamiento()
        );


        // =================================================
        // CONSERVAR ESTADO DE LA ALARMA
        // =================================================

        /*
         * true  -> SQLite recibe 1
         *
         * false -> SQLite recibe 0
         */

        valores.put(
                "alarmaActiva",
                medicamento.isAlarmaActiva()
                        ? 1
                        : 0
        );


        // =================================================
        // ACTUALIZAR REGISTRO
        // =================================================

        int filasActualizadas =
                db.update(

                        "medicamentos",

                        valores,

                        "id = ?",

                        new String[]{
                                String.valueOf(
                                        medicamento.getId()
                                )
                        }
                );


        // =================================================
        // CERRAR BASE DE DATOS
        // =================================================

        db.close();


        // =================================================
        // DEVOLVER RESULTADO
        // =================================================

        return filasActualizadas;
    }


    // =====================================================
    // ELIMINAR MEDICAMENTO
    // =====================================================

    public static void eliminarMedicamento(
            Context context,
            int id) {


        // =================================================
        // CREAR AYUDANTE
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // =================================================
        // ABRIR PARA ESCRITURA
        // =================================================

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // =================================================
        // ELIMINAR TOMAS ASOCIADAS
        // =================================================

        /*
         * Antes de eliminar el medicamento eliminamos
         * todas las tomas relacionadas con él.
         */

        db.delete(

                "tomas",

                "idMedicamento = ?",

                new String[]{
                        String.valueOf(id)
                }
        );


        // =================================================
        // ELIMINAR MEDICAMENTO
        // =================================================

        db.delete(

                "medicamentos",

                "id = ?",

                new String[]{
                        String.valueOf(id)
                }
        );


        // =================================================
        // CERRAR BASE DE DATOS
        // =================================================

        db.close();
    }
}