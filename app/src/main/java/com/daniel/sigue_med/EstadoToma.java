package com.daniel.sigue_med;

public final class EstadoToma {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String TOMADA = "TOMADA";
    public static final String OMITIDA = "OMITIDA";

    // Evita que alguien cree instancias de esta clase,
    // ya que solo contiene constantes.
    private EstadoToma() {
    }
}