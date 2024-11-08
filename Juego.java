import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Juego {
    private ArrayList<Barco> barcosP1;
    private ArrayList<Barco> barcosP2;
    private Tablero tableroP1;
    private Tablero tableroP2;
    private Random random;


    private int[] ultimaCoordenadaImpacto = null;
    private ArrayList<int[]> listaObjetivos = new ArrayList<>();

    public Juego() {
        barcosP1 = new ArrayList<>();
        barcosP2 = new ArrayList<>();
        tableroP1 = new Tablero();
        tableroP2 = new Tablero();
        random = new Random();

        generarBarcos(barcosP1);
        generarBarcos(barcosP2);


        colocarBarcosComputadora();
    }

    private void generarBarcos(ArrayList<Barco> barcos) {
        barcos.add(new Barco("Carrier", 5));
        barcos.add(new Barco("Battleship", 4));
        barcos.add(new Barco("Cruiser", 3));
        barcos.add(new Barco("Submarine", 3));
        barcos.add(new Barco("Destroyer", 2));
    }

    private void colocarBarcosComputadora() {
        for (Barco barco : barcosP2) {
            boolean colocado = false;
            while (!colocado) {
                int fila = random.nextInt(10);
                int columna = random.nextInt(10);
                boolean horizontal = random.nextBoolean();
                colocado = tableroP2.colocarBarco(fila, columna, barco.getLongitud(), horizontal);
            }
        }
    }

    public void colocarBarcosJugador() {
        Scanner scanner = new Scanner(System.in);
        for (Barco barco : barcosP1) {
            boolean colocado = false;
            while (!colocado) {
                System.out.println("Colocando el barco: " + barco.getNombre() + " de longitud " + barco.getLongitud());
                System.out.print("Fila (1-10): ");
                int fila = scanner.nextInt() - 1;
                System.out.print("Columna (A-J): ");
                char columnaChar = scanner.next().toUpperCase().charAt(0);
                int columna = columnaChar - 'A';
                System.out.print("Horizontal (true/false): ");
                boolean horizontal = scanner.nextBoolean();
                colocado = tableroP1.colocarBarco(fila, columna, barco.getLongitud(), horizontal);

                if (!colocado) {
                    System.out.println("Posición no válida. Intenta de nuevo.");
                }
            }
            tableroP1.mostrarTablero(false);
        }
    }

    public void jugar() {
        Scanner scanner = new Scanner(System.in);
        boolean juegoActivo = true;

        // Colocar barcos del jugador
        colocarBarcosJugador();

        // Ciclo del juego
        while (juegoActivo) {
            // Turno del jugador
            System.out.println("Tu tablero:");
            tableroP1.mostrarTablero(false);
            System.out.println("Tablero del oponente:");
            tableroP2.mostrarTablero(true);

            System.out.println("Ingresa las coordenadas para atacar.");
            System.out.print("Fila (1-10): ");
            int fila = scanner.nextInt() - 1;
            System.out.print("Columna (A-J): ");
            char columnaChar = scanner.next().toUpperCase().charAt(0);
            int columna = columnaChar - 'A';

            int resultadoAtaque = tableroP2.realizarAtaque(fila, columna);
            if (resultadoAtaque == 1) {
                System.out.println("¡Impacto!");
            } else if (resultadoAtaque == 0) {
                System.out.println("Agua.");
            } else {
                System.out.println("Ya atacaste esta casilla antes.");
            }

            // Comprobar si el jugador ganó
            if (verificarVictoria(tableroP2)) {
                System.out.println("¡Felicidades, has ganado!");
                juegoActivo = false;
                continue;
            }

            // Turno de la computadora
            System.out.println("Turno del oponente...");
            turnoComputadora();

            // Comprobar si la computadora ganó
            if (verificarVictoria(tableroP1)) {
                System.out.println("La computadora ha ganado.");
                juegoActivo = false;
            }
        }
    }

    private void turnoComputadora() {
        boolean ataqueRealizado = false;

        // Si hay coordenadas en la lista de objetivos, intenta en esas posiciones
        while (!ataqueRealizado && !listaObjetivos.isEmpty()) {
            int[] objetivo = listaObjetivos.remove(0);
            int resultado = tableroP1.realizarAtaque(objetivo[0], objetivo[1]);

            if (resultado == 1) { // Impacto
                System.out.println("La computadora hizo un impacto en (" + (objetivo[0] + 1) + ", " + (char) ('A' + objetivo[1]) + ")");
                generarObjetivosAdyacentes(objetivo[0], objetivo[1]);
                ataqueRealizado = true;
            } else if (resultado == 0) { // Agua
                System.out.println("La computadora atacó en (" + (objetivo[0] + 1) + ", " + (char) ('A' + objetivo[1]) + ") y fue agua.");
                ataqueRealizado = true;
            }
        }

        // Si no hay objetivos pendientes o no hizo impacto, ataca aleatoriamente
        while (!ataqueRealizado) {
            int filaAtaque = random.nextInt(10);
            int columnaAtaque = random.nextInt(10);
            int resultadoAtaqueComputadora = tableroP1.realizarAtaque(filaAtaque, columnaAtaque);

            if (resultadoAtaqueComputadora == 1) { // Impacto
                System.out.println("La computadora hizo un impacto en (" + (filaAtaque + 1) + ", " + (char) ('A' + columnaAtaque) + ")");
                generarObjetivosAdyacentes(filaAtaque, columnaAtaque);
                ataqueRealizado = true;
            } else if (resultadoAtaqueComputadora == 0) { // Agua
                System.out.println("La computadora atacó en (" + (filaAtaque + 1) + ", " + (char) ('A' + columnaAtaque) + ") y fue agua.");
                ataqueRealizado = true;
            }
        }
    }

    private void generarObjetivosAdyacentes(int fila, int columna) {
        // Agregar casillas adyacentes (arriba, abajo, izquierda, derecha) a la lista de objetivos si son válidas
        if (fila > 0) listaObjetivos.add(new int[]{fila - 1, columna});
        if (fila < 9) listaObjetivos.add(new int[]{fila + 1, columna});
        if (columna > 0) listaObjetivos.add(new int[]{fila, columna - 1});
        if (columna < 9) listaObjetivos.add(new int[]{fila, columna + 1});
    }

    private boolean verificarVictoria(Tablero tablero) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (tablero.obtenerValorCasilla(i, j) == 1) {
                    return false; // Hay partes de barcos sin impactar
                }
            }
        }
        return true; // Todos los barcos han sido impactados
    }
}
