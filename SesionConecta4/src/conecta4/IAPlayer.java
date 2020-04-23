package conecta4;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author José María Serrano
 * @version 1.5 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase IAPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es la que tenemos que implementar y completar
 *
 */
public class IAPlayer extends Player {

    private Estado estadoActual = null;
    private byte nivelActual = 0;
    private int nivelBase = 0;
    private int nivelMaximo = 5;
    public byte[][] tableroActual;
    private byte[][] tableroAnterior;
    //public int conecta;
    public int generados = 0;

    private class Estado {

        private final ArrayList<Estado> hijos;

        private final Estado padre;
        private final byte movX;
        private final byte movY;
        private final byte nivel;
        private final boolean estFinal;

        private int valor;

        public Estado(Estado padre, byte movX, byte movY, byte nivel, boolean estFinal, int conecta) {
            if (!estFinal) {
                this.hijos = new ArrayList();
            } else {
                this.hijos = null;
            }
            this.padre = padre;
            this.movX = movX;//Fila
            this.movY = movY;//Columna
            this.nivel = nivel;
            this.estFinal = estFinal;

            if (nivel % 2 == 0) {
                valor = Integer.MIN_VALUE;
            } else {
                valor = Integer.MAX_VALUE;
            }
            //this.print();
            generados++;
            if (nivel < nivelMaximo) {
                genHijos(conecta);
            } else {
                valor = calculaValor(construyeTablero(), conecta);
                this.print();
            }
        }

        private void genHijos(int conecta) {
            if (!estFinal) {
                byte[][] tableroAux = construyeTablero();
                for (int i = 0; i < tableroActual[0].length; i++) {
                    boolean encontrado = false;
                    int j;
                    for (j = tableroActual.length - 1; j >= 0 && !encontrado; j--) {
                        if (tableroAux[j][i] == 0) {
                            encontrado = true;
                            j++;
                        }
                    }
                    if (encontrado) {
                        byte[][] tableroEstadoHijo = new byte[tableroActual.length][tableroActual[0].length];
                        for (int k = 0; k < tableroActual.length; k++) {
                            System.arraycopy(tableroAux[k], 0, tableroEstadoHijo[k], 0, tableroAux[0].length);
                        }
                        if (nivel % 2 == 0) {
                            tableroEstadoHijo[j][i] = -1;
                        } else {
                            tableroEstadoHijo[j][i] = 1;
                        }
                        boolean estadoFinalHijo = false;
                        if (nivel >= 5 && nivel < 14) {
                            estadoFinalHijo = comprobarVictoria(tableroEstadoHijo, j, i, conecta) != 0;
                        } else if (nivel == 14) {
                            estadoFinalHijo = true;
                        }
                        Estado nuevo = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo, conecta);
                        hijos.add(nuevo);
                        if (nivel % 2 == 0) {
                            if (nuevo.valor > this.valor) {
                                this.valor = nuevo.valor;
                            }
                        } else {
                            if (nuevo.valor < this.valor) {
                                this.valor = nuevo.valor;
                            }
                        }
                    }
                }
            }//else if(this.comprobarVictoria(construyeTablero(), movX, movY) != 0){
            //this.print();
            //}
        }

        private int mayorHijos() {
            int max = Integer.MIN_VALUE;
            for (Estado hijo : hijos) {
                if (hijo.valor > max) {
                    max = hijo.valor;
                }
            }
            return max;
        }

        private int menorHijos() {
            int min = Integer.MAX_VALUE;
            for (Estado hijo : hijos) {
                if (hijo.valor < min) {
                    min = hijo.valor;
                }
            }
            return min;
        }

        public void actHijos() {
            if ((nivel) % 2 == 0) {
                for (Estado hijo : hijos) {
                    if (!hijo.estFinal) {
                        hijo.actHijos();
                        hijo.valor = hijo.menorHijos();
                    }
                }
            } else {
                for (Estado hijo : hijos) {
                    if (!hijo.estFinal) {
                        hijo.actHijos();
                        hijo.valor = hijo.mayorHijos();
                    }
                }
            }
        }

        public int getMejorJugada() {
            int minimax = Byte.MIN_VALUE;
            for (Estado hijo : hijos) {
                if (hijo.valor > minimax) {
                    minimax = hijo.valor;
                    estadoActual = hijo;
                }
            }
            return estadoActual.movY;
        }

        public byte[][] construyeTablero() {
            byte[][] tablero = new byte[tableroActual.length][tableroActual[0].length];
            for (int k = 0; k < tableroActual.length; k++) {
                System.arraycopy(tableroActual[k], 0, tablero[k], 0, tableroActual[0].length);
            }
            Stack<Estado> pilaMov = new Stack<>();
            Estado estadoActual = this;
            for (int i = nivel; i > nivelActual; i--) {
                pilaMov.push(estadoActual);
                estadoActual = estadoActual.padre;
            }
            int nivelAux = nivelActual;
            while (!pilaMov.isEmpty()) {
                Estado aux = pilaMov.pop();
                if (nivelAux % 2 == 0) {
                    tablero[aux.movX][aux.movY] = -1;
                } else {
                    tablero[aux.movX][aux.movY] = 1;
                }
                nivelAux++;
            }
            return tablero;
        }

        public void print() {
            byte[][] tableroEstado = construyeTablero();
            System.out.println("EstadoFinal: " + estFinal);
            System.out.println("Nivel " + nivel);
            System.out.println("Valor: " + valor);
            for (byte[] tableroEstado1 : tableroEstado) {
                for (int j = 0; j < tableroEstado[0].length; j++) {
                    System.out.print(tableroEstado1[j] + "	");
                }
                System.out.println();
            }
            System.out.println();
        }

        public void printHijos() {
            System.out.println("Hijos");
            for (Estado hijo : hijos) {
                hijo.print();
            }
        }

        public Estado getHijo(int col) {
            for (Estado hijo : hijos) {
                if (hijo.movY == col) {
                    return hijo;
                }
            }
            System.out.println("Algo salio mal (getHijo)");
            return null;
        }

        public ArrayList<Estado> getListaHijos() {
            return hijos;
        }

        public int comprobarVictoria(byte[][] estTablero, int x, int y, int conecta) {
            /*
			*	x fila
			*	y columna
             */
            int filas = estTablero.length;
            int columnas = estTablero[0].length;

            //Comprobar vertical
            int ganar1 = 0;
            int ganar2 = 0;
            int ganador = 0;
            boolean salir = false;
            for (int i = 0; (i < filas) && !salir; i++) {
                if (estTablero[i][y] != Conecta4.VACIO) {
                    if (estTablero[i][y] == Conecta4.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = Conecta4.PLAYER1;
                        salir = true;
                    }
                    if (!salir) {
                        if (estTablero[i][y] == Conecta4.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = Conecta4.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
            // Comprobar horizontal
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < columnas) && !salir; j++) {
                if (estTablero[x][j] != Conecta4.VACIO) {
                    if (estTablero[x][j] == Conecta4.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = Conecta4.PLAYER1;
                        salir = true;
                    }
                    if (ganador != Conecta4.PLAYER1) {
                        if (estTablero[x][j] == Conecta4.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = Conecta4.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
            // Comprobar oblicuo. De izquierda a derecha
            ganar1 = 0;
            ganar2 = 0;
            int a = x;
            int b = y;
            while (b > 0 && a > 0) {
                a--;
                b--;
            }
            while (b < columnas && a < filas && !salir) {
                if (estTablero[a][b] != Conecta4.VACIO) {
                    if (estTablero[a][b] == Conecta4.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = Conecta4.PLAYER1;
                        salir = true;
                    }
                    if (ganador != Conecta4.PLAYER1) {
                        if (estTablero[a][b] == Conecta4.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = Conecta4.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
                a++;
                b++;
            }
            // Comprobar oblicuo de derecha a izquierda 
            ganar1 = 0;
            ganar2 = 0;
            a = x;
            b = y;
            //buscar posición de la esquina
            while (b < columnas - 1 && a > 0) {
                a--;
                b++;
            }
            while (b > -1 && a < filas && !salir) {
                if (estTablero[a][b] != Conecta4.VACIO) {
                    if (estTablero[a][b] == Conecta4.PLAYER1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = Conecta4.PLAYER1;
                        salir = true;
                    }
                    if (ganador != Conecta4.PLAYER1) {
                        if (estTablero[a][b] == Conecta4.PLAYER2) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = Conecta4.PLAYER2;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
                a++;
                b--;
            }

            return ganador;
        }
    }

    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int turnoJugada(Grid tablero, int conecta) {

        byte tab[][] = {
            {0, 0, 0, -1},
            {1, 0, 1, 1},
            {-1, 0, -1, -1},
            {-1, -1, -1, 1}};
        System.out.println("valor: " + calculaValor(tab, conecta));
        System.exit(0);

        if (estadoActual != null) {

            if (estadoActual.nivel == nivelMaximo) {
                estadoActual.print();
                nivelMaximo += 3;
                estadoActual.genHijos(conecta);
                estadoActual.print();
                for (Estado hijo : estadoActual.getListaHijos()) {
                    hijo.print();
                }
                nivelMaximo--;
            }
        }

        actTableroActual(tablero);
        int posHijo;

        if (estadoActual != null) {
            estadoActual.print();
            for (Estado hijo : estadoActual.getListaHijos()) {
                hijo.print();
            }
            posHijo = jugadaP1(tablero);
            estadoActual = estadoActual.getHijo(posHijo);
        } else {
            estadoActual = new Estado(null, (byte) 0, (byte) 0, (byte) nivelActual, false, conecta);
        }

        //System.out.println("----ESTADO ACTUAL----");
        //estadoActual.print();
        //estadoActual.printHijos();
        System.out.println("----RAMA ALEATORIA----");
        mostrarRama();
        System.out.println("----FIN RAMA ALEATORIA----");
        /*new Scanner(System.in).nextLine();
         */
        // Calcular la mejor columna posible donde hacer nuestra turnoJugada
        int columna = estadoActual.getMejorJugada();
        actTableroAnterior(estadoActual.construyeTablero());

        System.out.println("ESTADO FINAL LLAMADA:");
        estadoActual.print();
        nivelActual++;

        return tablero.checkWin(tablero.setButton(columna, Conecta4.PLAYER2), columna, conecta);

    }

    public void actTableroActual(Grid tablero) {
        int[][] aux = tablero.toArray();
        tableroActual = new byte[tablero.getFilas()][tablero.getColumnas()];
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                tableroActual[i][j] = (byte) aux[i][j];
            }
        }
    }

    public void actTableroAnterior(byte[][] tablero) {
        tableroAnterior = new byte[tablero.length][tablero[0].length];
        for (int i = 0; i < tablero.length; i++) {
            System.arraycopy(tablero[i], 0, tableroAnterior[i], 0, tablero.length);
        }
    }

    public int jugadaP1(Grid tablero) {
        for (int i = 0; i < tableroAnterior.length; i++) {
            for (int j = 0; j < tableroAnterior[i].length; j++) {
                if (tableroAnterior[i][j] != tableroActual[i][j]) {
                    return j;
                }
            }
        }
        return -1;
    }

    public void mostrarRama() {
        ArrayList<Estado> hijos = estadoActual.getListaHijos();
        Random generador = new Random();
        while (hijos != null && hijos.size() > 0) {
            int elegido = generador.nextInt(hijos.size());
            hijos.get(elegido).print();
            hijos = hijos.get(elegido).getListaHijos();
        }
    }

    public int calculaValor(byte[][] tablero, int conecta) {
        int sumaTotal = 0;

        //Cálculo vertical
        for (int c = 0; c < tablero[0].length; c++) {
            int f = tablero.length - 1;
            byte inicial = tablero[f][c];
            int suma = inicial;
            while (f >= 0 && tablero[f][c] == inicial && tablero[f][c] != 0) {
                suma *= 10;
                f--;
            }
            if (f >= 0) {
                if (tablero[f][c] == inicial * -1) {
                    suma = 0;
                }
            }
            //sumaTotal += suma;
        }

        //Cálculo horizontal
        for (int f = 0; f < tablero.length; f++) {
            byte inicial = 0;
            int sumaFinal = 0;
            int repetidos = 1;
            int cerosRepetidos = 0;

            for (int c = 0; c < tablero[f].length; c++) {
                if (inicial == 0) {
                    inicial = tablero[f][c];
                    repetidos = 1;
                    if (tablero[f][c] == 0) {
                        cerosRepetidos++;
                    }
                } else if (inicial == tablero[f][c]) {
                    repetidos++;
                    if (repetidos == conecta) {
                        //System.out.printf("Inicial %d, fila %d, columna %d, repetidos %d\n", inicial, f, c, repetidos);
                        sumaFinal += inicial * (Math.pow(10, repetidos));
                        c = tablero[f].length;
                    }
                } else if (inicial == -1 * tablero[f][c]) {
                    repetidos = 0;
                    inicial = 0;
                    cerosRepetidos = 0;
                } else if (tablero[f][c] == 0 && (tablero[f].length - c + repetidos) >= conecta) {
                    int necesarios = conecta - repetidos;
                    boolean esPosible = true;
                    for (int k = c + 1; k < c + necesarios; k++) {
                        if (tablero[f][k] == inicial * -1) {
                            esPosible = false;
                        } else if (tablero[f][k] == inicial) {
                            repetidos++;
                        }
                    }
                    if (esPosible) {
                        //System.out.printf("Inicial %d, fila %d, columna %d\n", inicial, f, c);
                        sumaFinal += inicial * (Math.pow(10, repetidos));
                        inicial = 0;
                        c = tablero[f].length;
                    }
                }
            }
            if (repetidos + cerosRepetidos >= conecta) {
                sumaFinal += inicial * (Math.pow(10, repetidos));
            }
            sumaTotal += sumaFinal;
        }
//
//        boolean[][] evaluadosID = new boolean[tablero.length][tablero[0].length];
//        boolean[][] evaluadosDI = new boolean[tablero.length][tablero[0].length];
//        for (int i = 0; i < evaluadosID.length; i++) {
//            for (int j = 0; j < evaluadosID[i].length; j++) {
//                evaluadosID[i][j] = false;
//                evaluadosDI[i][j] = false;
//            }
//        }
//
//        for (int f = 0; f < tablero.length; f++) {
//            for (int c = 0; c < tablero[f].length; c++) {
//                if (!evaluadosID[f][c]) {
//                    //Izquierda a derecha hacia abajo
//                    if (f + conecta <= tablero.length && c + conecta <= tablero[f].length) {
//                        int inicial = tablero[f][c];
//                        int suma = inicial * 10;
//                        evaluadosID[f][c] = true;
//                        int contador = 1;
//                        while (f + contador < tablero.length && c + contador < tablero[f].length) {
//                            if (!(tablero[f + contador][c + contador] == 0 && inicial == 0)) {
//                                if (inicial == 0) {
//                                    inicial = tablero[f + contador][c + contador];
//                                    suma = inicial;
//                                }
//                                if (tablero[f + contador][c + contador] == inicial && !evaluadosID[f + contador][c + contador]) {
//                                    suma *= 10;
//                                } else if (tablero[f + contador][c + contador] == 0) {
//                                    sumaTotal += suma;
//                                    inicial = tablero[f + contador][c + contador];
//                                    suma = inicial;
//                                } else {
//                                    inicial = 0;
//                                    suma = inicial;
//                                }
//                            }
//                            evaluadosID[f + contador][c + contador] = true;
//                            contador++;
//                        }
//                        //sumaTotal += suma;
//                    }
//                }
//
//                if (!evaluadosDI[f][c]) {
//                    //Derecha a izquierda hacia abajo
//                    if (f + conecta <= tablero.length && c + 1 >= conecta) {
//                        int inicial = tablero[f][c];
//                        int suma = inicial * 10;
//                        evaluadosDI[f][c] = true;
//                        int contador = 1;
//                        while (f + contador < tablero.length && c - contador >= 0) {
//                            if (!(tablero[f + contador][c - contador] == 0 && inicial == 0)) {
//                                if (inicial == 0) {
//                                    inicial = tablero[f + contador][c - contador];
//                                    suma = inicial;
//                                }
//                                if (tablero[f + contador][c - contador] == inicial && !evaluadosDI[f + contador][c - contador]) {
//                                    suma *= 10;
//                                } else if (tablero[f + contador][c - contador] == 0) {
//                                    sumaTotal += suma;
//                                    inicial = tablero[f + contador][c - contador];
//                                    suma = inicial;
//                                } else {
//                                    inicial = 0;
//                                    suma = inicial;
//                                }
//                            }
//                            evaluadosDI[f + contador][c - contador] = true;
//                            contador++;
//                        }
//                        //sumaTotal += suma;
//                    }
//                }
//            }
//        }

        return sumaTotal;
    }
} // IAPlayer
