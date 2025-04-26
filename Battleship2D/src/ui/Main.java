package ui;

import java.util.Random;
import java.util.Scanner;
import model.*;

public class Main {
    private GameController controller;
    private Player player;
    private Player machine;
    private int standardPlayerWins;
    private int standardMachineWins;
    private int customPlayerWins;
    private int customMachineWins;
    private int customShipCount;
    private int boardWidth = 10;
    private int boardHeight = 10;

    // Se empieza el juego
    public static void main(String[] args) {
        new Main().startGame();
    }

    /**
     * Menu principal del juego
     * @pre Ninguna
     * @post Se analiza la opcion elegida: iniciar partida, mostrar estadisticas o salir
     */
    public void startGame() {
        Scanner sc = new Scanner(System.in);
        int option;
        do {
            System.out.println("\n=== Bienvenido a Battleship ===");
            System.out.println("1. Partida estandar");
            System.out.println("2. Partida personalizada");
            System.out.println("3. Consultar estadisticas");
            System.out.println("4. Salir");
            System.out.print("> ");
            option = sc.nextInt();
            switch (option) {
                case 1:
                    playStandardGame(sc);
                    break;
                case 2:
                    playCustomGame(sc);
                    break;
                case 3:
                    showStats();
                    break;
                case 4:
                    System.out.println("Gracias por jugar. Hasta la proxima!");
                    break;
                default:
                    System.out.println("Opcion invalida. Por favor elige 1-4.");
            }
        } while (option != 4);
        sc.close();
    }

    /**
     * Arranca una partida estandar con tablero (10x10)
     * @param sc scanner para entrada del usuario
     * @pre sc != null
     * @post Se juega una partida estandar y se actualizan los contadores de victorias
     */
    private void playStandardGame(Scanner sc) {
        boardWidth = 10;
        boardHeight = 10;
        player = new Player(askPlayerName(sc), boardWidth, boardHeight);
        machine = new Player("Maquina", boardWidth, boardHeight);
        controller = new GameController(player, machine);

        System.out.println("\n--- Partida estandar (10x10) ---");
        setupStandardShips(player, sc);
        setupStandardShipsMachine();

        System.out.println("Barcos colocados exitosamente");
        System.out.println("Tu tablero:");
        printGrid(player.getBoard().getGrid(), false);
        System.out.println("Tablero rival (barcos ocultos):");
        printGrid(machine.getBoard().getGrid(), true);
        System.out.println("\n¡Muy bien! ¡Ahora vamos a jugar!\n");

        playTurns();

        if (controller.getWinner().equals(player.getName())) {
            standardPlayerWins++;
            System.out.println("Gano el jugador en modo estandar.");
        } else {
            standardMachineWins++;
            System.out.println("Gano la maquina en modo estandar.");
        }
    }

    /**
     * Empieza una partida personalizada
     * @param sc scanner para entrada del usuario
     * @pre sc != null
     * @post Se juega una partida personalizada y se actualizan los contadores de victorias
     */
    private void playCustomGame(Scanner sc) {
        System.out.println("\n--- Partida personalizada ---");
        boardWidth = askIntInRange(sc, "Ingresa el ancho del tablero (1-20):", 1, 20);
        boardHeight = askIntInRange(sc, "Ingresa el alto del tablero (1-20):", 1, 20);
        customShipCount = askIntInRange(sc, "Ingresa cuantos barcos deseas (1-10):", 1, 10);

        player = new Player(askPlayerName(sc), boardWidth, boardHeight);
        machine = new Player("Maquina", boardWidth, boardHeight);
        controller = new GameController(player, machine);

        setupCustomShips(player, sc);
        setupCustomShipsMachine();

        System.out.println("Barcos colocados exitosamente");
        System.out.println("Tu tablero:");
        printGrid(player.getBoard().getGrid(), false);
        System.out.println("Tablero rival (barcos ocultos):");
        printGrid(machine.getBoard().getGrid(), true);
        System.out.println("\n¡Muy bien! ¡Ahora vamos a jugar!\n");

        playTurns();

        if (controller.getWinner().equals(player.getName())) {
            customPlayerWins++;
            System.out.println("Gano el jugador en modo personalizado.");
        } else {
            customMachineWins++;
            System.out.println("Gano la maquina en modo personalizado.");
        }
    }

    /**
     * Se analizan los turnos de usuario y maquina hasta que termine la partida
     * @pre controller != null && player != null && machine != null
     * @post Se determina un ganador y el juego termina
     */
    private void playTurns() {
        Scanner sc = new Scanner(System.in);
        while (!controller.isGameOver()) {
            int x, y;
            do {
                System.out.print("Ingresa X (1-" + boardWidth + "|): ");
                x = sc.nextInt();
                System.out.print("Ingresa Y (1-" + boardHeight + "|): ");
                y = sc.nextInt();
                if (!isValidAttack(machine, x - 1, y - 1)) {
                    System.out.println("Coords invalidas o ya atacadas. Intenta otra vez.");
                }
            } while (!isValidAttack(machine, x - 1, y - 1));

            boolean hit = controller.executeUserTurn(x - 1, y - 1);
            System.out.println(hit
                ? "¡Tocado en (" + x + "," + y + ")!"
                : "¡Agua en (" + x + "," + y + ")!"
            );

            int[] m = controller.executeAITurn();
            System.out.println("Maquina ataco en (" + (m[0] + 1) + "," + (m[1] + 1) + ").");
            showBoards();
        }
    }

    /**
     * Muestra ambos tableros
     * @pre player != null && machine != null
     * @post Se imprimen los dos tableros en consola
     */
    private void showBoards() {
        System.out.println("\nTablero jugador:");
        printGrid(player.getBoard().getGrid(), false);
        System.out.println("Tablero maquina (barcos ocultos):");
        printGrid(machine.getBoard().getGrid(), true);
    }

    /**
     * Imprime el tablero como matriz
     * @param grid matriz de los estados
     * @param hideShips true para ocultar barcos intactos
     * @pre grid != null && grid.length > 0
     * @post Se imprime la matriz con codigos de estado
     */
    private void printGrid(int[][] grid, boolean hideShips) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int c = grid[y][x];
                if (hideShips && c == 4) c = 0;
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    /**
     * Ubica barcos para una partida estandar para el jugador
     * @param pl jugador
     * @param sc scanner para entrada
     * @pre pl != null && sc != null
     * @post Todos los barcos estandar colocados en el tablero del jugador
     */
    private void setupStandardShips(Player pl, Scanner sc) {
        Board b = pl.getBoard();
        System.out.println("Ubicando barcos estandar...");
        System.out.println("1) LANCHA (1 casilla)");
        placeShipInteractive(b, new Ship(ShipConfig.LANCHA), true, sc);
        System.out.println("2) BARCO_MEDICO (2 casillas) - vertical");
        placeShipInteractive(b, new Ship(ShipConfig.BARCO_MEDICO), false, sc);
        System.out.println("3) BARCO_PROVISIONES (3 casillas) - horizontal");
        placeShipInteractive(b, new Ship(ShipConfig.BARCO_PROVISIONES), true, sc);
        System.out.println("4) BARCO_MUNICION (3 casillas) - vertical");
        placeShipInteractive(b, new Ship(ShipConfig.BARCO_MUNICION), false, sc);
        System.out.println("5) BUQUE_DE_GUERRA (4 casillas) - horizontal");
        placeShipInteractive(b, new Ship(ShipConfig.BUQUE_DE_GUERRA), true, sc);
        System.out.println("6) PORTAAVIONES (5 casillas) - vertical");
        placeShipInteractive(b, new Ship(ShipConfig.PORTAAVIONES), false, sc);
    }

    /**
     * Ubica barcos de una partida estandar para la maquina
     * @post Todos los barcos estandar colocados aleatoriamente en el tablero de la maquina
     */
    private void setupStandardShipsMachine() {
        Board b = machine.getBoard();
        Random r = new Random();
        placeShipRandom(b, new Ship(ShipConfig.LANCHA), true, r);
        placeShipRandom(b, new Ship(ShipConfig.BARCO_MEDICO), false, r);
        placeShipRandom(b, new Ship(ShipConfig.BARCO_PROVISIONES), true, r);
        placeShipRandom(b, new Ship(ShipConfig.BARCO_MUNICION), false, r);
        placeShipRandom(b, new Ship(ShipConfig.BUQUE_DE_GUERRA), true, r);
        placeShipRandom(b, new Ship(ShipConfig.PORTAAVIONES), false, r);
    }

    /**
     * Ubica barcos de una partida personalizada para el jugador
     * @param pl jugador, no debe ser nulo
     * @param sc scanner, no debe ser nulo
     * @pre customShipCount > 0
     * @post customShipCount barcos colocados en el tablero del jugador
     */
    private void setupCustomShips(Player pl, Scanner sc) {
        Board b = pl.getBoard();
        System.out.println("Ubicando barcos personalizados...");
        for (int i = 1; i <= customShipCount; i++) {
            int len = askIntInRange(sc, "Ingresa longitud del barco " + i + " (1-5):", 1, 5);
            int ori = askIntInRange(sc, "Ingresa orientacion (1=hor,2=ver):", 1, 2);
            boolean hor = (ori == 1);
            placeShipInteractive(b, new Ship(mapLengthToConfig(len)), hor, sc);
        }
    }

    /**
     * Ubica los barcos de una partida personalizada de la maquina
     * @post customShipCount barcos colocados aleatoriamente en el tablero de la maquina
     */
    private void setupCustomShipsMachine() {
        Board b = machine.getBoard();
        Random r = new Random();
        for (int i = 0; i < customShipCount; i++) {
            int len = r.nextInt(5) + 1;
            boolean hor = r.nextBoolean();
            placeShipRandom(b, new Ship(mapLengthToConfig(len)), hor, r);
        }
    }

    /**
     * Coloca un barco 
     * @param b tablero
     * @param ship barco a colocar
     * @param horizontal orientacion del barco
     * @param sc scanner
     * @pre b != null && ship != null && sc != null
     * @post El barco se coloca en una posicion valida o se repite intentos
     */
    private void placeShipInteractive(Board b, Ship ship, boolean horizontal, Scanner sc) {
        boolean ok;
        do {
            int x = askIntInRange(sc, "Ingresa X (1-" + boardWidth + "):", 1, boardWidth) - 1;
            int y = askIntInRange(sc, "Ingresa Y (1-" + boardHeight + "):", 1, boardHeight) - 1;
            ok = b.addShip(ship, x, y, horizontal);
            if (!ok) {
                System.out.println("Ubicacion invalida o solapamiento. Intenta otra vez.");
            } else {
                printGrid(b.getGrid(), false);
            }
        } while (!ok);
    }

    /**
     * Coloca un barco de forma aleatoria
     * @param b tablero
     * @param ship barco
     * @param horizontal orientacion
     * @param r generador de aleatorios
     * @pre b != null && ship != null && r != null
     * @post El barco se coloca en posicion valida aleatoria
     */
    private void placeShipRandom(Board b, Ship ship, boolean horizontal, Random r) {
        boolean ok;
        do {
            int x = r.nextInt(boardWidth);
            int y = r.nextInt(boardHeight);
            ok = b.addShip(ship, x, y, horizontal);
        } while (!ok);
    }

    /**
     * Valida si el ataque se puede realizar
     * @param target jugador objetivo,
     * @param x coordenada X (0-Z)
     * @param y coordenada Y (0-Z)
     * @pre target != null
     * @post true si la casilla esta en rango y no atacada antes
     */
    private boolean isValidAttack(Player target, int x, int y) {
        int[][] g = target.getBoard().getGrid();
        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) return false;
        return g[y][x] == 0 || g[y][x] == 4;
    }

    /**
     * Lee un numero entero en un rango especifico
     * @param sc scanner
     * @param msg mensaje a mostrar al usuario
     * @param min valor minimo
     * @param max valor maximo
     * @pre sc != null && msg != null && min <= max
     * @post valor que se devuelve de forma [min,max]
     */
    private int askIntInRange(Scanner sc, String msg, int min, int max) {
        int val;
        do {
            System.out.print(msg + " ");
            val = sc.nextInt();
        } while (val < min || val > max);
        return val;
    }

    /**
     * Lee la longitud de barco a su configuracion
     * @param len longitud del barco
     * @pre len >= 1
     * @post Retorna el ShipConfig correspondiente o PORTAAVIONES por defecto
     */
    private ShipConfig mapLengthToConfig(int len) {
        switch (len) {
            case 1: return ShipConfig.LANCHA;
            case 2: return ShipConfig.BARCO_MEDICO;
            case 3: return ShipConfig.BARCO_PROVISIONES;
            case 4: return ShipConfig.BUQUE_DE_GUERRA;
            default: return ShipConfig.PORTAAVIONES;
        }
    }

    /**
     * Muestra las estadisticas de las partidas
     * @pre Ninguna
     * @post Se imprimen las victorias acumuladas por modo y jugador
     */
    private void showStats() {
        System.out.println("\nEstadisticas:");
        System.out.println("Estandar - Jugador: " + standardPlayerWins
         + ", Maquina: " + standardMachineWins);
        System.out.println("Personalizado - Jugador: " + customPlayerWins
         + ", Maquina: " + customMachineWins);
    }

    /**
     * Solicita el nombre del jugador
     * @param sc scanner
     * @pre sc != null
     * @post Retorna un String no vacio con el nombre ingresado
     */
    private String askPlayerName(Scanner sc) {
        System.out.print("Ingresa tu nombre: ");
        return sc.next();
    }
}
