package model;

// Barcos standar
public enum ShipConfig {
    LANCHA(1), 
    BARCO_MEDICO(2), 
    BARCO_PROVISIONES(3), 
    BARCO_MUNICION(3), 
    BUQUE_DE_GUERRA(4), 
    PORTAAVIONES(5);

    private final int size;

    /**
     * Constructor del enum con el tamano del barco
     *
     * @param size cantidad de casillas del barco
     */
    ShipConfig(int size) {
        this.size = size;
    }

    /**
     * Devuelve el tamano del barco
     *
     * @return tamano del barco
     */
    public int getSize() {
        return size;
    }
}