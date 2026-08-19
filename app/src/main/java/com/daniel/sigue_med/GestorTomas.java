package com.daniel.sigue_med;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class GestorTomas {


    // =====================================================
    // GUARDAR UNA TOMA
    // =====================================================

    public static long guardarToma(
            Context context,
            Toma toma) {


        // Crear ayudante de la base de datos

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir base de datos para escritura

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // Preparar los datos

        ContentValues valores =
                new ContentValues();


        valores.put(
                "idMedicamento",
                toma.getIdMedicamento()
        );


        valores.put(
                "fechaHora",
                toma.getFechaHora()
        );


        valores.put(
                "estado",
                toma.getEstado()
        );


        // Insertar la toma

        long id =
                db.insert(
                        "tomas",
                        null,
                        valores
                );


        // Cerrar base de datos

        db.close();


        // Devolver ID generado

        return id;
    }


    // =====================================================
    // OBTENER TOMAS DE UN MEDICAMENTO
    // =====================================================

    public static ArrayList<Toma> obtenerTomasPorMedicamento(
            Context context,
            int idMedicamento) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para lectura

        SQLiteDatabase db =
                helper.getReadableDatabase();


        // Crear lista

        ArrayList<Toma> lista =
                new ArrayList<>();


        // Realizar SELECT

        Cursor cursor =
                db.rawQuery(

                        "SELECT * FROM tomas " +
                                "WHERE idMedicamento = ? " +
                                "ORDER BY fechaHora ASC",

                        new String[]{
                                String.valueOf(
                                        idMedicamento
                                )
                        }
                );


        // Recorrer resultados

        while (cursor.moveToNext()) {


            // Obtener ID

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "id"
                            )
                    );


            // Obtener fecha y hora

            String fechaHora =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "fechaHora"
                            )
                    );


            // Obtener estado

            String estado =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "estado"
                            )
                    );


            // Crear objeto Toma

            Toma toma =
                    new Toma(
                            idMedicamento,
                            fechaHora,
                            estado
                    );


            // Asignar ID

            toma.setId(id);


            // Añadir a la lista

            lista.add(toma);
        }


        // Cerrar cursor

        cursor.close();


        // Cerrar base de datos

        db.close();


        // Devolver lista

        return lista;
    }


    // =====================================================
    // ACTUALIZAR ESTADO DE UNA TOMA
    // =====================================================

    public static void actualizarEstado(
            Context context,
            int idToma,
            String nuevoEstado) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para escritura

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // Preparar datos

        ContentValues valores =
                new ContentValues();


        valores.put(
                "estado",
                nuevoEstado
        );


        // Actualizar la toma

        db.update(

                "tomas",

                valores,

                "id = ?",

                new String[]{
                        String.valueOf(
                                idToma
                        )
                }
        );


        // Cerrar base de datos

        db.close();
    }


    // =====================================================
    // ELIMINAR UNA TOMA
    // =====================================================

    public static void eliminarToma(
            Context context,
            int idToma) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para escritura

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // Eliminar toma

        db.delete(

                "tomas",

                "id = ?",

                new String[]{
                        String.valueOf(
                                idToma
                        )
                }
        );


        // Cerrar base de datos

        db.close();
    }


    // =====================================================
    // COMPROBAR SI UNA TOMA YA EXISTE
    // =====================================================

    public static boolean existeToma(
            Context context,
            int idMedicamento,
            String fechaHora) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para lectura

        SQLiteDatabase db =
                helper.getReadableDatabase();


        // Buscar toma

        Cursor cursor =
                db.rawQuery(

                        "SELECT id FROM tomas " +
                                "WHERE idMedicamento = ? " +
                                "AND fechaHora = ?",

                        new String[]{
                                String.valueOf(
                                        idMedicamento
                                ),
                                fechaHora
                        }
                );


        // Comprobar si existe

        boolean existe =
                cursor.moveToFirst();


        // Cerrar cursor

        cursor.close();


        // Cerrar base de datos

        db.close();


        // Devolver resultado

        return existe;
    }


    // =====================================================
    // ELIMINAR TODAS LAS TOMAS DE UN MEDICAMENTO
    // =====================================================

    public static void eliminarTomasPorMedicamento(
            Context context,
            int idMedicamento) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para escritura

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // Eliminar todas las tomas
        // pertenecientes al medicamento

        db.delete(

                "tomas",

                "idMedicamento = ?",

                new String[]{
                        String.valueOf(
                                idMedicamento
                        )
                }
        );


        // Cerrar base de datos

        db.close();
    }


    // =====================================================
    // ACTUALIZAR ESTADO POR MEDICAMENTO Y FECHA/HORA
    // =====================================================

    public static void actualizarEstadoPorMedicamentoYFechaHora(
            Context context,
            int idMedicamento,
            String fechaHora,
            String nuevoEstado) {


        // Crear ayudante

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);


        // Abrir para escritura

        SQLiteDatabase db =
                helper.getWritableDatabase();


        // Preparar los nuevos datos

        ContentValues valores =
                new ContentValues();


        valores.put(
                "estado",
                nuevoEstado
        );


        // Actualizar solamente la toma
        // que pertenece al medicamento
        // y coincide con la fecha y hora

        db.update(

                "tomas",

                valores,

                "idMedicamento = ? AND fechaHora = ?",

                new String[]{
                        String.valueOf(
                                idMedicamento
                        ),
                        fechaHora
                }
        );


        // Cerrar base de datos

        db.close();
    }
    // =====================================================
// OBTENER TODAS LAS TOMAS PENDIENTES
// =====================================================

    public static ArrayList<Toma> obtenerTomasPendientes(
            Context context) {


        // =================================================
        // ABRIR BASE DE DATOS
        // =================================================

        BaseDeDatosHelper helper =
                new BaseDeDatosHelper(context);

        SQLiteDatabase db =
                helper.getReadableDatabase();


        // =================================================
        // CREAR LISTA DE RESULTADOS
        // =================================================

        ArrayList<Toma> lista =
                new ArrayList<>();


        // =================================================
        // CREAR CURSOR
        // =================================================

        Cursor cursor = null;


        try {


            // =================================================
            // CONSULTA SQL
            // =================================================

            /*
             * Buscamos solamente las tomas cuyo estado
             * sea PENDIENTE.
             *
             * ORDER BY fechaHora ASC
             *
             * significa que las ordenamos de la más antigua
             * a la más reciente.
             */

            cursor = db.rawQuery(

                    "SELECT * FROM tomas " +
                            "WHERE estado = ? " +
                            "ORDER BY fechaHora ASC",

                    new String[]{
                            "PENDIENTE"
                    }
            );


            // =================================================
            // RECORRER RESULTADOS
            // =================================================

            while (cursor.moveToNext()) {


                // =============================================
                // OBTENER ID
                // =============================================

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        );


                // =============================================
                // OBTENER ID MEDICAMENTO
                // =============================================

                int idMedicamento =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "idMedicamento"
                                )
                        );


                // =============================================
                // OBTENER FECHA Y HORA
                // =============================================

                String fechaHora =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "fechaHora"
                                )
                        );


                // =============================================
                // OBTENER ESTADO
                // =============================================

                String estado =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "estado"
                                )
                        );


                // =============================================
                // CREAR OBJETO TOMA
                // =============================================

                Toma toma =
                        new Toma(

                                idMedicamento,

                                fechaHora,

                                estado
                        );


                // =============================================
                // ASIGNAR ID
                // =============================================

                toma.setId(id);


                // =============================================
                // AÑADIR A LA LISTA
                // =============================================

                lista.add(toma);
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



        // DEVOLVER RESULTADOS


        return lista;
    }
}

