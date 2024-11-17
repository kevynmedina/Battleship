import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.exit;


public class JuegoGUI extends JFrame {
    private ArrayList<Barco> barcosP1;
    private ArrayList<Barco> barcosP2;
    private Tablero tableroP1;
    private Tablero tableroP2;
    private JPanel panelTableroP1;
    private JPanel panelTableroP2;
    private Random random;
    private ArrayList<int[]> listaObjetivos = new ArrayList<>();
    private JButton[][] botonesJugador;

    public JuegoGUI() {

        barcosP1 = new ArrayList<>();
        barcosP2 = new ArrayList<>();
        tableroP1 = new Tablero();
        tableroP2 = new Tablero();
        random = new Random();

        generarBarcos(barcosP1);
        generarBarcos(barcosP2);
        colocarBarcosComputadora();
        colocarBarcosJugador();

        setTitle("Batalla Naval");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 400);
        setLayout(new BorderLayout());

        // Crear los paneles para los tableros
        panelTableroP1 = new JPanel(new GridLayout(10, 10));
        panelTableroP2 = new JPanel(new GridLayout(10, 10));
        inicializarTablero(panelTableroP1, tableroP1, true);
        inicializarTablero(panelTableroP2, tableroP2, false);

        // Crear los títulos de las columnas
        String letras = "   A   B   C   D   E   F   G   H   I   J   ";
        JLabel textoTableroP1 = new JLabel(letras, JLabel.CENTER);
        JLabel textoTableroP2 = new JLabel(letras, JLabel.CENTER);
        textoTableroP1.setFont(new Font("Arial", Font.BOLD, 14));
        textoTableroP2.setFont(new Font("Arial", Font.BOLD, 14));

        // Crear los números de las filas (1, 2, 3, ..., 10) en un JPanel con GridLayout
        JPanel panelNumerosP1 = new JPanel(new GridLayout(10, 1)); // 10 filas, 1 columna
        JPanel panelNumerosP2 = new JPanel(new GridLayout(10, 1)); // 10 filas, 1 columna

        // Agregar los números del 1 al 10
        for (int i = 1; i <= 10; i++) {
            JLabel numero = new JLabel(String.valueOf(i), JLabel.CENTER);
            JLabel numero2 = new JLabel(String.valueOf(i), JLabel.CENTER);
            numero.setFont(new Font("Arial", Font.BOLD, 14));
            numero2.setFont(new Font("Arial", Font.BOLD, 14));
            panelNumerosP1.add(numero);
            panelNumerosP2.add(numero2);
        }


        JPanel panelSuperiorP1 = new JPanel(new BorderLayout());
        panelSuperiorP1.add(textoTableroP1, BorderLayout.CENTER);
        panelSuperiorP1.add(panelTableroP1, BorderLayout.CENTER);

        JPanel panelSuperiorP2 = new JPanel(new BorderLayout());
        panelSuperiorP2.add(textoTableroP2, BorderLayout.CENTER);
        panelSuperiorP2.add(panelTableroP2, BorderLayout.CENTER);

        // Crear paneles contenedores para los números y tableros
        JPanel panelIzquierdaP1 = new JPanel(new BorderLayout());
        panelIzquierdaP1.add(panelNumerosP1, BorderLayout.WEST); // Coloca los números verticales en la izquierda
        panelIzquierdaP1.add(panelSuperiorP1, BorderLayout.CENTER);

        JPanel panelIzquierdaP2 = new JPanel(new BorderLayout());
        panelIzquierdaP2.add(panelNumerosP2, BorderLayout.WEST); // Coloca los números verticales en la izquierda
        panelIzquierdaP2.add(panelSuperiorP2, BorderLayout.CENTER);

        // Añadir los paneles con los tableros y números a la ventana
        add(panelIzquierdaP1, BorderLayout.WEST);
        add(panelIzquierdaP2, BorderLayout.EAST);

        pintarBarcosEnTablero();

        setVisible(true);
    }



    private void inicializarTablero(JPanel panel, Tablero tablero, boolean esJugador) {

        if (esJugador) {
            botonesJugador = new JButton[10][10];
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(30, 30));
                panel.add(boton);

                if (esJugador) {
                    botonesJugador[i][j] = boton;
                }

                int fila = i;
                int columna = j;
                boton.addActionListener(e -> manejarClic(fila, columna, tablero, boton));
            }
        }
    }


    private void manejarClic(int fila, int columna, Tablero tablero, JButton boton) {
        if (tablero == tableroP2) {
            int resultadoAtaque = tableroP2.realizarAtaque(fila, columna);
            if (resultadoAtaque == 1) {
                System.out.println("¡Impacto!");
                boton.setBackground(Color.RED);
            } else if (resultadoAtaque == 0) {
                System.out.println("Agua.");
                boton.setBackground(Color.BLUE);
            } else {
                System.out.println("Ya atacaste esta casilla antes.");
            }
        }


        // Comprobar si el jugador ganó
        if (verificarVictoria(tableroP2)) {
            System.out.println("¡Felicidades, has ganado!");
            JOptionPane.showMessageDialog(null, "¡Felicidades, has ganado!");
            exit(1);
        }
        // Turno de la computadora
        System.out.println("Turno del oponente...");
        turnoComputadora();

        // Comprobar si la computadora ganó
        if (verificarVictoria(tableroP1)) {
            System.out.println("La computadora ha ganado.");
            JOptionPane.showMessageDialog(null, "La computadora ha ganado.");
            exit(1);
        }
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

    private void colocarBarcosJugador() {
        for (Barco barco : barcosP1) {
            boolean colocado = false;
            while (!colocado) {
                int fila = random.nextInt(10);
                int columna = random.nextInt(10);
                boolean horizontal = random.nextBoolean();
                colocado = tableroP1.colocarBarco(fila, columna, barco.getLongitud(), horizontal);

            }
        }
    }
    private boolean verificarVictoria(Tablero tablero) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (tablero.obtenerValorCasilla(i, j) == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void turnoComputadora() {
        boolean ataqueRealizado = false;

        // Si hay coordenadas en la lista de objetivos, intenta en esas posiciones
        while (!ataqueRealizado && !listaObjetivos.isEmpty()) {
            int resultado;
            int[] objetivo;
            do{
                objetivo = listaObjetivos.removeFirst();
                resultado = tableroP1.realizarAtaque(objetivo[0], objetivo[1]);
                System.out.println(resultado);
            }while(resultado==-1);

            if (resultado == 1) { // Impacto
                System.out.println("La computadora hizo un impacto en (" + (objetivo[0] + 1) + ", " + (char) ('A' + objetivo[1]) + ")");
                generarObjetivosAdyacentes(objetivo[0], objetivo[1]);
                botonesJugador[objetivo[0]][objetivo[1]].setBackground(Color.RED);
                ataqueRealizado = true;
            } else if (resultado == 0) { // Agua
                System.out.println("La computadora atacó en (" + (objetivo[0] + 1) + ", " + (char) ('A' + objetivo[1]) + ") y fue agua.");
                botonesJugador[objetivo[0]][objetivo[1]].setBackground(Color.BLUE);
                ataqueRealizado = true;
            }else{
                System.out.println("ya se ha atacado en esa casilla");
            }
        }

        // Si no hay objetivos pendientes o no hizo impacto, ataca aleatoriamente
        while (!ataqueRealizado) {
            int resultadoAtaqueComputadora;
            int filaAtaque ;
            int columnaAtaque ;
           do{
            filaAtaque = random.nextInt(10);
            columnaAtaque = random.nextInt(10);
             resultadoAtaqueComputadora = tableroP1.realizarAtaque(filaAtaque, columnaAtaque);
            System.out.println(resultadoAtaqueComputadora);
            }while(resultadoAtaqueComputadora==-1);

            if (resultadoAtaqueComputadora == 1) { // Impacto
                System.out.println("La computadora hizo un impacto en (" + (filaAtaque + 1) + ", " + (char) ('A' + columnaAtaque) + ")");
                generarObjetivosAdyacentes(filaAtaque, columnaAtaque);
                botonesJugador[filaAtaque][columnaAtaque].setBackground(Color.RED);
                ataqueRealizado = true;
            } else if (resultadoAtaqueComputadora == 0) { // Agua
                System.out.println("La computadora atacó en (" + (filaAtaque + 1) + ", " + (char) ('A' + columnaAtaque) + ") y fue agua.");
                botonesJugador[filaAtaque][columnaAtaque].setBackground(Color.BLUE);
                ataqueRealizado = true;

            }
        }
    }

    private void generarObjetivosAdyacentes(int fila, int columna) {
        if (fila > 0) listaObjetivos.add(new int[]{fila - 1, columna});
        if (fila < 9) listaObjetivos.add(new int[]{fila + 1, columna});
        if (columna > 0) listaObjetivos.add(new int[]{fila, columna - 1});
        if (columna < 9) listaObjetivos.add(new int[]{fila, columna + 1});
    }

    private void pintarBarcosEnTablero() {
        for (int fila = 0; fila < 10; fila++) {
            for (int columna = 0; columna < 10; columna++) {
                if (tableroP1.getCasilla(fila, columna) == 1) {
                    JButton boton = (JButton) panelTableroP1.getComponent(fila * 10 + columna);
                    boton.setBackground(Color.GREEN);
                }
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoGUI::new);
    }
}
