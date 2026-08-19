package com.daniel.sigue_med;

public class Medicamento {

    // =====================================================
    // DATOS DEL MEDICAMENTO
    // =====================================================

    private int id;

    private String nombre;

    private double dosis;

    private String unidad;

    private String frecuencia;

    private String horaInicio;

    private String fechaInicio;

    private int diasTratamiento;

    private boolean alarmaActiva;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Medicamento(
            String nombre,
            double dosis,
            String unidad,
            String frecuencia,
            String horaInicio,
            String fechaInicio,
            int diasTratamiento) {

        this.nombre = nombre;

        this.dosis = dosis;

        this.unidad = unidad;

        this.frecuencia = frecuencia;

        this.horaInicio = horaInicio;

        this.fechaInicio = fechaInicio;

        this.diasTratamiento = diasTratamiento;
    }

    // =====================================================
    // ID
    // =====================================================

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    // =====================================================
    // NOMBRE
    // =====================================================

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    // =====================================================
    // DOSIS
    // =====================================================

    public double getDosis() {

        return dosis;
    }


    public void setDosis(double dosis) {

        this.dosis = dosis;
    }

    // =====================================================
    // UNIDAD
    // =====================================================

    public String getUnidad() {

        return unidad;
    }


    public void setUnidad(String unidad) {

        this.unidad = unidad;
    }

    // =====================================================
    // FRECUENCIA
    // =====================================================

    public String getFrecuencia() {

        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {

        this.frecuencia = frecuencia;
    }


    // =====================================================
    // HORA DE INICIO
    // =====================================================

    public String getHoraInicio() {

        return horaInicio;
    }


    public void setHoraInicio(String horaInicio) {

        this.horaInicio = horaInicio;
    }


    // =====================================================
    // FECHA DE INICIO
    // =====================================================

    public String getFechaInicio() {

        return fechaInicio;
    }


    public void setFechaInicio(String fechaInicio) {

        this.fechaInicio = fechaInicio;
    }


    // =====================================================
    // DÍAS DE TRATAMIENTO
    // =====================================================

    public int getDiasTratamiento() {

        return diasTratamiento;
    }

    public void setDiasTratamiento(
            int diasTratamiento) {
        this.diasTratamiento = diasTratamiento;
    }
// =====================================================
// ALARMA ACTIVA
// =====================================================

    public boolean isAlarmaActiva() {

        return alarmaActiva;
    }
    public void setAlarmaActiva(boolean alarmaActiva) {

        this.alarmaActiva = alarmaActiva;
    }
}