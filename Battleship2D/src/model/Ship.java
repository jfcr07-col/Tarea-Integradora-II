package model;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private ShipConfig config;
    private List<Coordinate> positions;
    private boolean sunk;

    /**
     * Constructor del barco
     *
     * @param config atributos del barco (nombre y tamano)
     */
    public Ship(ShipConfig config) {
        this.config = config;
        this.positions = new ArrayList<>();
        this.sunk = false;
    }

    /**
     * Se añaden las coordenadas del barco segun su posicion y orientacion
     *
     * @param x posicion inicial en x
     * @param y posicion inicial en y
     * @param horizontal true si el barco es horizontal, false si es vertical
     */
    public void addCoordinates(int x, int y, boolean horizontal) {
        int size = config.getSize();

        for (int i = 0; i < size; i++) {
            int posX = horizontal ? x + i : x;
            int posY = horizontal ? y : y + i;
            positions.add(new Coordinate(posX, posY));
        }
    }

    
    // Verifica si todas las coordenadas del barco fueron impactadas y lo marca como hundido si es asi
    public void checkSunk() {
        for (Coordinate c : positions) {
            if (!c.isHit()) return;
        }
        sunk = true;
    }

    /**
     * Indica si el barco ya esta hundido
     *
     * @return true si esta hundido, false si no
     */
    public boolean isSunk() {
        return sunk;
    }

    /**
     * Devuelve las coordenadas del barco
     *
     * @return lista de coordenadas
     */
    public List<Coordinate> getPositions() {
        return positions;
    }
}