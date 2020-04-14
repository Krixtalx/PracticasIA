package conecta4;

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
    public byte[][] tableroActual;
    private byte[][] tableroAnterior;
    public int conecta;
    public int generados = 0;

    private class Estado {

        //private final ArrayList<Estado> hijos;
        private Estado hijo1;
        private Estado hijo2;
        private Estado hijo3;
        private Estado hijo4;

        private final Estado padre;
        private final byte movX;
        private final byte movY;
        private final byte nivel;
        private final boolean estFinal;

        private byte valor;

        public Estado(Estado padre, byte movX, byte movY, byte nivel, boolean estFinal) {
            this.hijo1 = null;
            this.hijo2 = null;
            this.hijo3 = null;
            this.hijo4 = null;
            this.padre = padre;
            this.movX = movX;//Fila
            this.movY = movY;//Columna
            this.nivel = nivel;
            this.estFinal = estFinal;

            generados++;
            genHijos();
        }

        private void genHijos() {
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
                        byte valorHijo = (byte) comprobarVictoria(tableroEstadoHijo, j, i);
                        if (nivel >= 5 && nivel < 14) {
                            estadoFinalHijo = valorHijo != 0;
                        } else if (nivel == 14) {
                            estadoFinalHijo = true;
                        }
                        if (hijo1 == null) {
                            hijo1 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
                            hijo1.valor = (byte) (valorHijo * -1);
                        } else if (hijo2 == null) {
                            hijo2 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
                            hijo2.valor = (byte) (valorHijo * -1);
                        } else if (hijo3 == null) {
                            hijo3 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
                            hijo3.valor = (byte) (valorHijo * -1);
                        } else if (hijo4 == null) {
                            hijo4 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
                            hijo4.valor = (byte) (valorHijo * -1);
                        }
                    }
                }
            }
        }

        public void actHijos() {
            if ((nivel) % 2 == 0) {
                if (hijo1 != null && !hijo1.estFinal) {
                    hijo1.actHijos();
                    hijo1.valor = Byte.MAX_VALUE;
                    if (hijo1.hijo1 != null) {
                        if (hijo1.hijo1.valor < hijo1.valor) {
                            hijo1.valor = hijo1.hijo1.valor;
                        }
                    }
                    if (hijo1.hijo2 != null) {
                        if (hijo1.hijo2.valor < hijo1.valor) {
                            hijo1.valor = hijo1.hijo2.valor;
                        }
                    }
                    if (hijo1.hijo3 != null) {
                        if (hijo1.hijo3.valor < hijo1.valor) {
                            hijo1.valor = hijo1.hijo3.valor;
                        }
                    }
                    if (hijo1.hijo4 != null) {
                        if (hijo1.hijo4.valor < hijo1.valor) {
                            hijo1.valor = hijo1.hijo4.valor;
                        }
                    }
                }
                if (hijo2 != null && !hijo2.estFinal) {
                    hijo2.actHijos();
                    hijo2.valor = Byte.MAX_VALUE;
                    if (hijo2.hijo1 != null) {
                        if (hijo2.hijo1.valor < hijo2.valor) {
                            hijo2.valor = hijo2.hijo1.valor;
                        }
                    }
                    if (hijo2.hijo2 != null) {
                        if (hijo2.hijo2.valor < hijo2.valor) {
                            hijo2.valor = hijo2.hijo2.valor;
                        }
                    }
                    if (hijo2.hijo3 != null) {
                        if (hijo2.hijo3.valor < hijo2.valor) {
                            hijo2.valor = hijo2.hijo3.valor;
                        }
                    }
                    if (hijo2.hijo4 != null) {
                        if (hijo2.hijo4.valor < hijo2.valor) {
                            hijo2.valor = hijo2.hijo4.valor;
                        }
                    }
                }
                if (hijo3 != null && !hijo3.estFinal) {
                    hijo3.actHijos();
                    hijo3.valor = Byte.MAX_VALUE;
                    if (hijo3.hijo1 != null) {
                        if (hijo3.hijo1.valor < hijo3.valor) {
                            hijo3.valor = hijo3.hijo1.valor;
                        }
                    }
                    if (hijo3.hijo2 != null) {
                        if (hijo3.hijo2.valor < hijo3.valor) {
                            hijo3.valor = hijo3.hijo2.valor;
                        }
                    }
                    if (hijo3.hijo3 != null) {
                        if (hijo3.hijo3.valor < hijo3.valor) {
                            hijo3.valor = hijo3.hijo3.valor;
                        }
                    }
                    if (hijo3.hijo4 != null) {
                        if (hijo3.hijo4.valor < hijo3.valor) {
                            hijo3.valor = hijo3.hijo4.valor;
                        }
                    }
                }
                if (hijo4 != null && !hijo4.estFinal) {
                    hijo4.actHijos();
                    hijo4.valor = Byte.MAX_VALUE;
                    if (hijo4.hijo1 != null) {
                        if (hijo4.hijo1.valor < hijo4.valor) {
                            hijo4.valor = hijo4.hijo1.valor;
                        }
                    }
                    if (hijo4.hijo2 != null) {
                        if (hijo4.hijo2.valor < hijo4.valor) {
                            hijo4.valor = hijo4.hijo2.valor;
                        }
                    }
                    if (hijo4.hijo3 != null) {
                        if (hijo4.hijo3.valor < hijo4.valor) {
                            hijo4.valor = hijo4.hijo3.valor;
                        }
                    }
                    if (hijo4.hijo4 != null) {
                        if (hijo4.hijo4.valor < hijo4.valor) {
                            hijo4.valor = hijo4.hijo4.valor;
                        }
                    }
                }
            } else {
                if (hijo1 != null && !hijo1.estFinal) {
                    hijo1.actHijos();
                    hijo1.valor = Byte.MIN_VALUE;
                    if (hijo1.hijo1 != null) {
                        if (hijo1.hijo1.valor > hijo1.valor) {
                            hijo1.valor = hijo1.hijo1.valor;
                        }
                    }
                    if (hijo1.hijo2 != null) {
                        if (hijo1.hijo2.valor > hijo1.valor) {
                            hijo1.valor = hijo1.hijo2.valor;
                        }
                    }
                    if (hijo1.hijo3 != null) {
                        if (hijo1.hijo3.valor > hijo1.valor) {
                            hijo1.valor = hijo1.hijo3.valor;
                        }
                    }
                    if (hijo1.hijo4 != null) {
                        if (hijo1.hijo4.valor > hijo1.valor) {
                            hijo1.valor = hijo1.hijo4.valor;
                        }
                    }
                }
                if (hijo2 != null && !hijo2.estFinal) {
                    hijo2.actHijos();
                    hijo2.valor = Byte.MIN_VALUE;
                    if (hijo2.hijo1 != null) {
                        if (hijo2.hijo1.valor > hijo2.valor) {
                            hijo2.valor = hijo2.hijo1.valor;
                        }
                    }
                    if (hijo2.hijo2 != null) {
                        if (hijo2.hijo2.valor > hijo2.valor) {
                            hijo2.valor = hijo2.hijo2.valor;
                        }
                    }
                    if (hijo2.hijo3 != null) {
                        if (hijo2.hijo3.valor > hijo2.valor) {
                            hijo2.valor = hijo2.hijo3.valor;
                        }
                    }
                    if (hijo2.hijo4 != null) {
                        if (hijo2.hijo4.valor > hijo2.valor) {
                            hijo2.valor = hijo2.hijo4.valor;
                        }
                    }
                }
                if (hijo3 != null && !hijo3.estFinal) {
                    hijo3.actHijos();
                    hijo3.valor = Byte.MIN_VALUE;
                    if (hijo3.hijo1 != null) {
                        if (hijo3.hijo1.valor > hijo3.valor) {
                            hijo3.valor = hijo3.hijo1.valor;
                        }
                    }
                    if (hijo3.hijo2 != null) {
                        if (hijo3.hijo2.valor > hijo3.valor) {
                            hijo3.valor = hijo3.hijo2.valor;
                        }
                    }
                    if (hijo3.hijo3 != null) {
                        if (hijo3.hijo3.valor > hijo3.valor) {
                            hijo3.valor = hijo3.hijo3.valor;
                        }
                    }
                    if (hijo3.hijo4 != null) {
                        if (hijo3.hijo4.valor > hijo3.valor) {
                            hijo3.valor = hijo3.hijo4.valor;
                        }
                    }
                }
                if (hijo4 != null && !hijo4.estFinal) {
                    hijo4.actHijos();
                    hijo4.valor = Byte.MIN_VALUE;
                    if (hijo4.hijo1 != null) {
                        if (hijo4.hijo1.valor > hijo4.valor) {
                            hijo4.valor = hijo4.hijo1.valor;
                        }
                    }
                    if (hijo4.hijo2 != null) {
                        if (hijo4.hijo2.valor > hijo4.valor) {
                            hijo4.valor = hijo4.hijo2.valor;
                        }
                    }
                    if (hijo4.hijo3 != null) {
                        if (hijo4.hijo3.valor > hijo4.valor) {
                            hijo4.valor = hijo4.hijo3.valor;
                        }
                    }
                    if (hijo4.hijo4 != null) {
                        if (hijo4.hijo4.valor > hijo4.valor) {
                            hijo4.valor = hijo4.hijo4.valor;
                        }
                    }
                }
            }

        }

        public int getMejorJugada() {
            byte minimax = Byte.MIN_VALUE;
            if (hijo1 != null) {
                if (hijo1.valor > minimax) {
                    minimax = hijo1.valor;
                    estadoActual = hijo1;
                }
            }
            if (hijo2 != null) {
                if (hijo2.valor > minimax) {
                    minimax = hijo2.valor;
                    estadoActual = hijo2;
                }
            }
            if (hijo3 != null) {
                if (hijo3.valor > minimax) {
                    minimax = hijo3.valor;
                    estadoActual = hijo3;
                }
            }
            if (hijo4 != null) {
                if (hijo4.valor > minimax) {
                    estadoActual = hijo4;
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
            if (hijo1 != null) {
                hijo1.print();
            }
            if (hijo2 != null) {
                hijo2.print();
            }
            if (hijo3 != null) {
                hijo3.print();
            }
            if (hijo4 != null) {
                hijo4.print();
            }
        }

        public Estado getHijo(int col) {
            if (hijo1 != null && hijo1.movY == col) {
                return hijo1;
            } else if (hijo2 != null && hijo2.movY == col) {
                return hijo2;
            } else if (hijo3 != null && hijo3.movY == col) {
                return hijo3;
            } else if (hijo4 != null && hijo4.movY == col) {
                return hijo4;
            }
            System.out.println("ESTAMOS EN LA MIERDA (getHijo)");
            return null;
        }

        public int comprobarVictoria(byte[][] estTablero, int x, int y) {
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
        actTableroActual(tablero);
        int posHijo;
        this.conecta = conecta;

        if (estadoActual != null) {
            posHijo = jugadaP1(tablero);
            estadoActual = estadoActual.getHijo(posHijo);
        } else {
            estadoActual = new Estado(null, (byte) 0, (byte) 0, (byte) nivelActual, false);
            estadoActual.actHijos();
        }

        System.out.println("----ESTADO ACTUAL----");
        estadoActual.print();
        estadoActual.printHijos();

        // Calcular la mejor columna posible donde hacer nuestra turnoJugada
        int columna = estadoActual.getMejorJugada();
        actTableroAnterior(estadoActual.construyeTablero());
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

//        System.out.println(":::COMPARANDO:::");
//        for (int i = 0; i < tableroAnterior.length; i++) {
//            for (int j = 0; j < tableroAnterior[i].length; j++) {
//                System.out.printf("%d	", tableroAnterior[i][j]);
//            }
//            System.out.println("");
//        }
//        System.out.println("Y");
//        for (int i = 0; i < tableroActual.length; i++) {
//            for (int j = 0; j < tableroActual[i].length; j++) {
//                System.out.printf("%d	", tableroActual[i][j]);
//            }
//            System.out.println("");
//        }
        for (int i = 0; i < tableroAnterior.length; i++) {
            for (int j = 0; j < tableroAnterior[i].length; j++) {
                if (tableroAnterior[i][j] != tableroActual[i][j]) {
//                    System.out.println("COLUMNA DIFERENTE: " + j);
                    return j;
                }
            }
        }
        return -1;
    }
// turnoJugada

} // IAPlayer
