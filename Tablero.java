public class Tablero {
    private int[][] tablero;

    public Tablero() {
        tablero = new int[10][10];
    }

    public void mostrarTablero(boolean ocultarBarcos) {
        System.out.println("   [A] [B] [C] [D] [E] [F] [G] [H] [I] [J]");
        for (int i = 0; i < 10; i++) {
            System.out.print("[" + (i + 1) + "]");
            for (int j = 0; j < 10; j++) {
                if (tablero[i][j] == 1 && ocultarBarcos) {
                    System.out.print(" [ ]");
                } else {
                    System.out.print(" [" + (tablero[i][j] == 1 ? "B" : tablero[i][j] == 2 ? "X" : tablero[i][j] == -1 ? "O" : " ") + "]");
                }
            }
            System.out.println();
        }
    }

    public boolean colocarBarco(int fila, int columna, int longitud, boolean horizontal) {
        if (horizontal) {
            if (columna + longitud > 10) return false;
            for (int i = 0; i < longitud; i++) {
                if (tablero[fila][columna + i] != 0) {
                    return false;
                }
            }
            for (int i = 0; i < longitud; i++) {
                tablero[fila][columna + i] = 1;
            }
        } else {
            if (fila + longitud > 10) {
                return false;
            }
            for (int i = 0; i < longitud; i++) {
                if (tablero[fila + i][columna] != 0) {
                    return false;
                }
            }
            for (int i = 0; i < longitud; i++) {
                tablero[fila + i][columna] = 1;
            }
        }
        return true;
    }

    public int realizarAtaque(int fila, int columna) {
        if (tablero[fila][columna] == 1) {
            tablero[fila][columna] = 2; // Impacto
            return 1;
        } else if (tablero[fila][columna] == 0) {
            tablero[fila][columna] = -1; // Agua
            return 0;
        } else {
            return -1; // Ya atacado antes
        }
    }

    public int obtenerValorCasilla(int fila, int columna) {
        return tablero[fila][columna];
    }
}
