package com.tpi.mslogistica.domain;

/**
 * Tipo de tramo según su posición en la ruta
 */
public enum TipoTramo {
    ORIGEN,      // Primer tramo: desde origen hasta primer depósito o destino
    INTERMEDIO,  // Tramo entre depósitos
    DESTINO      // Último tramo: desde último depósito hasta destino final
}
