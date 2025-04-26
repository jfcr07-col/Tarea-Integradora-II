package model;

public class Coordinate {
    private int x, y;
    private boolean hit;

    /**
     * Constructor de la coordenada
     *
     * @param x posicion horizontal
     * @param y posicion vertical
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.hit = false;
    }

    /**
     * Marca esta coordenada como impactada
     */
    public void markHit() {
        hit = true;
    }

    /**
     * Revisa si ya fue impactada
     *
     * @return true si fue impactada, false si no
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Devuelve el valor de x
     *
     * @return posicion x
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve el valor de y
     *
     * @return posicion y
     */
    public int getY() {
        return y;
    }
}