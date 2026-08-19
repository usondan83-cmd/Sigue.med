package com.daniel.sigue_med;

public class Toma {


    // =====================================================
    // DATOS DE LA TOMA
    // =====================================================

    private int id;

    private int idMedicamento;

    private String fechaHora;

    private String estado;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Toma(
            int idMedicamento,
            String fechaHora,
            String estado) {

        this.idMedicamento =
                idMedicamento;

        this.fechaHora =
                fechaHora;

        this.estado =
                estado;
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
    // ID DEL MEDICAMENTO
    // =====================================================

    public int getIdMedicamento() {

        return idMedicamento;
    }


    public void setIdMedicamento(
            int idMedicamento) {

        this.idMedicamento =
                idMedicamento;
    }


    // =====================================================
    // FECHA Y HORA
    // =====================================================

    public String getFechaHora() {

        return fechaHora;
    }


    public void setFechaHora(
            String fechaHora) {

        this.fechaHora =
                fechaHora;
    }


    // =====================================================
    // ESTADO
    // =====================================================

    public String getEstado() {

        return estado;
    }


    public void setEstado(
            String estado) {

        this.estado =
                estado;
    }
}