package model;

import java.util.Random;

public class GameController {
    private Player user;
    private Player ai;
    private Random generator;

    public GameController(Player user, Player ai) {
        this.user = user;
        this.ai = ai;
        this.generator = new Random();
    }

    // Para el ataque del usuario
    public boolean executeUserTurn(int x, int y) {
        return ai.getBoard().receiveAttack(x, y);
    }

    // Ataque de la maquina, (no puede repetir coordenadas)
    public int[] executeAITurn() {
        int[][] g = user.getBoard().getGrid();
        int height = g.length;
        int width  = g[0].length;
        int x, y;
        do {
            x = generator.nextInt(width);
            y = generator.nextInt(height);
        } while (g[y][x] == 1 || g[y][x] == 2 || g[y][x] == 3);
        user.getBoard().receiveAttack(x, y);
        return new int[]{ x, y };
    }

    // Se define si ya termino la partida
    public boolean isGameOver() {
        return allSunk(user) || allSunk(ai);
    }

    // Se establece el nombre del ganador
    public String getWinner() {
        return allSunk(user) ? ai.getName() : user.getName();
    }

    // Se define si todos los barcos del jugador fueron hundidos
    private boolean allSunk(Player p) {
        for (Ship s : p.getBoard().getShips()) {
            if (!s.isSunk()) return false;
        }
        return true;
    }
}