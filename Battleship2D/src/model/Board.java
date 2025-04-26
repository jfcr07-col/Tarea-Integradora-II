package model;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private int[][] grid;
    private List<Ship> ships;
    private int width;
    private int height;

    /**
     * Constructor del tablero con dimensiones personalizadas
     *
     * @param width  ancho del tablero
     * @param height alto del tablero
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
        this.ships = new ArrayList<>();
    }

    /**
     * Constructor por defecto (10x10)
     */
    public Board() {
        this(10, 10);
    }

    /**
     * Devuelve la lista de barcos colocados en el tablero
     *
     * @return lista de barcos
     */
    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Agrega un barco al tablero si hay espacio disponible
     *
     * @param ship barco a agregar
     * @param startX coordenada inicial en X
     * @param startY coordenada inicial en Y
     * @param isHorizontal true si el barco está en posición horizontal
     * @return true si el barco fue agregado, false si no
     */
    public boolean addShip(Ship ship, int startX, int startY, boolean isHorizontal) {
        // Se limpia para evitar la acumulacion de datos en este caso serian los propios intentos
        ship.getPositions().clear();

        // Genero la lista de coordenadas
        ship.addCoordinates(startX, startY, isHorizontal);

        // Valido limites y solapamientos
        for (Coordinate c : ship.getPositions()) {
            int x = c.getX(), y = c.getY();
            if (x < 0 || x >= width || y < 0 || y >= height || grid[y][x] != 0) {
                return false;
            }
        }

        // Marco las casillas como ocupadas (4 = barco intacto)
        for (Coordinate c : ship.getPositions()) {
            grid[c.getY()][c.getX()] = 4;
        }

        ships.add(ship);
        return true;
    }

    /**
     * Procesa un ataque en la coordenada que se dio
     *
     * @param x coordenada X del ataque
     * @param y coordenada Y del ataque
     * @return true si el ataque fue un impacto, false si fue agua
     */
    public boolean receiveAttack(int x, int y) {
        for (Ship ship : ships) {
            for (Coordinate coord : ship.getPositions()) {
                if (coord.getX() == x && coord.getY() == y) {
                    coord.markHit();
                    ship.checkSunk();
                    grid[y][x] = ship.isSunk() ? 3 : 2; // 3 = hundido, 2 = tocado
                    return true;
                }
            }
        }
        // Marca agua atacada (1 = agua marcada)
        grid[y][x] = 1;
        return false;
    }

    /**
     * Devuelve la matriz del tablero
     *
     * @return equivale al estado
     */
    public int[][] getGrid() {
        return grid;
    }
}
