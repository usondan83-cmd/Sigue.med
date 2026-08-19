package com.daniel.sigue_med;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatosHelper extends SQLiteOpenHelper {


    // =====================================================
    // DATOS DE LA BASE DE DATOS
    // =====================================================

    private static final String NOMBRE_BBDD =
            "sigue_med.db";


    /*
     * Versión actual de la base de datos.
     *
     * Hemos pasado de la versión 5 a la versión 6
     * porque necesitamos asegurarnos de que la columna
     * alarmaActiva exista.
     */

    private static final int VERSION_BBDD = 6;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BaseDeDatosHelper(Context context) {

        super(
                context,
                NOMBRE_BBDD,
                null,
                VERSION_BBDD
        );
    }


    // =====================================================
    // CREAR BASE DE DATOS
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {


        android.util.Log.d(
                "BBDD",
                "========== EJECUTANDO onCreate =========="
        );


        // =================================================
        // TABLA MEDICAMENTOS
        // =================================================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS medicamentos (" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "nombre TEXT NOT NULL, " +

                        "dosis REAL NOT NULL, " +

                        "unidad TEXT NOT NULL, " +

                        "frecuencia TEXT NOT NULL, " +

                        "horaInicio TEXT NOT NULL, " +

                        "fechaInicio TEXT NOT NULL, " +

                        "diasTratamiento INTEGER NOT NULL, " +

                        /*
                         * 0 = alarma desactivada
                         * 1 = alarma activada
                         */

                        "alarmaActiva INTEGER NOT NULL DEFAULT 0" +

                        ")"
        );


        // =================================================
        // TABLA TOMAS
        // =================================================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS tomas (" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "idMedicamento INTEGER NOT NULL, " +

                        "fechaHora TEXT NOT NULL, " +

                        "estado TEXT NOT NULL, " +

                        "FOREIGN KEY (idMedicamento) " +
                        "REFERENCES medicamentos(id)" +

                        ")"
        );
    }


    // =====================================================
    // ACTUALIZAR BASE DE DATOS
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {


        android.util.Log.d(
                "BBDD",
                "========== EJECUTANDO onUpgrade: "
                        + oldVersion
                        + " -> "
                        + newVersion
                        + " =========="
        );


        // =================================================
        // VERSIÓN 1 → VERSIÓN 2
        // =================================================

        if (oldVersion < 2) {


            /*
             * Añadimos fechaInicio.
             */

            db.execSQL(
                    "ALTER TABLE medicamentos " +
                            "ADD COLUMN fechaInicio TEXT"
            );
        }


        // =================================================
        // VERSIÓN 2 → VERSIÓN 3
        // =================================================

        if (oldVersion < 3) {


            /*
             * Crear tabla de tomas.
             */

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS tomas (" +

                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            "idMedicamento INTEGER NOT NULL, " +

                            "fechaHora TEXT NOT NULL, " +

                            "estado TEXT NOT NULL, " +

                            "FOREIGN KEY (idMedicamento) " +
                            "REFERENCES medicamentos(id)" +

                            ")"
            );
        }


        // =================================================
        // VERSIÓN 3 → VERSIÓN 4
        // =================================================

        if (oldVersion < 4) {


            /*
             * Comprobar que la tabla medicamentos existe.
             */

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS medicamentos (" +

                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            "nombre TEXT NOT NULL, " +

                            "dosis REAL NOT NULL, " +

                            "unidad TEXT NOT NULL, " +

                            "frecuencia TEXT NOT NULL, " +

                            "horaInicio TEXT NOT NULL, " +

                            "fechaInicio TEXT, " +

                            "diasTratamiento INTEGER NOT NULL" +

                            ")"
            );


            /*
             * Comprobar que la tabla tomas existe.
             */

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS tomas (" +

                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            "idMedicamento INTEGER NOT NULL, " +

                            "fechaHora TEXT NOT NULL, " +

                            "estado TEXT NOT NULL, " +

                            "FOREIGN KEY (idMedicamento) " +
                            "REFERENCES medicamentos(id)" +

                            ")"
            );
        }


        // =================================================
        // VERSIÓN 4 → VERSIÓN 5
        // =================================================

        if (oldVersion < 5) {


            /*
             * Añadimos alarmaActiva.
             *
             * 0 = alarma desactivada
             * 1 = alarma activada
             */

            if (!existeColumna(
                    db,
                    "medicamentos",
                    "alarmaActiva"
            )) {

                db.execSQL(
                        "ALTER TABLE medicamentos " +
                                "ADD COLUMN alarmaActiva " +
                                "INTEGER NOT NULL DEFAULT 0"
                );
            }
        }


        // =================================================
        // VERSIÓN 5 → VERSIÓN 6
        // =================================================

        if (oldVersion < 6) {


            /*
             * Tenemos instalaciones que pueden tener
             * una base de datos de versión 5 creada
             * mediante el antiguo onCreate(), donde
             * alarmaActiva NO existía.
             *
             * Si la columna no existe, la añadimos.
             */

            if (!existeColumna(
                    db,
                    "medicamentos",
                    "alarmaActiva"
            )) {

                db.execSQL(
                        "ALTER TABLE medicamentos " +
                                "ADD COLUMN alarmaActiva " +
                                "INTEGER NOT NULL DEFAULT 0"
                );
            }
        }
    }


    // =====================================================
    // COMPROBAR SI EXISTE UNA COLUMNA
    // =====================================================

    private boolean existeColumna(
            SQLiteDatabase db,
            String nombreTabla,
            String nombreColumna) {


        Cursor cursor = null;


        try {


            cursor = db.rawQuery(

                    "PRAGMA table_info("
                            + nombreTabla
                            + ")",

                    null
            );


            /*
             * PRAGMA table_info devuelve información
             * sobre todas las columnas de la tabla.
             */

            int indiceNombre =
                    cursor.getColumnIndex("name");


            while (cursor.moveToNext()) {


                String columna =
                        cursor.getString(
                                indiceNombre
                        );


                if (nombreColumna.equals(
                        columna
                )) {

                    return true;
                }
            }


        } finally {


            if (cursor != null) {

                cursor.close();
            }
        }


        return false;
    }
}