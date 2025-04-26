package model;

public class Player {
    private String name;
    private Board board;
    private int wins;

    /**
     * Constructor para tablero personalizado
     *
     * @param name nombre del jugador
     * @param width ancho del tablero
     * @param height alto del tablero
     */
    public Player(String name, int width, int height) {
        this.name = name;
        this.board = new Board(width, height);
        this.wins = 0;
    }

    /**
     * Constructor para standar (10x10)
     *
     * @param name nombre del jugador
     */
    public Player(String name) {
        this(name, 10, 10);
    }

    /**
     * Devuelve el nombre del jugador
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve el tablero del jugador
     * @return tablero
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Aumenta de uno en uno el contador de partidas ganadas
     */
    public void increaseWins() {
        wins++;
    }

    /**
     * Devuelve la cantidad de partidas que fueron victoria
     * @return numero de victorias
     */
    public int getWins() {
        return wins;
    }
}
