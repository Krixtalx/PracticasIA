/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;
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

    private Estado estadoActual;
    private byte nivelActual = 0;
    public byte[][] tableroActual;
    public int conecta;
    public int generados = 0;

    private class Estado {

        private final ArrayList<Estado> hijos;
        private final Estado padre;
        private final byte movX;
        private final byte movY;
        private final byte nivel;
        private final boolean estFinal;

        public Estado(Estado padre, byte movX, byte movY, byte nivel, boolean estFinal) {
            this.hijos = new ArrayList<>();
            this.padre = padre;
            this.movX = movX;
            this.movY = movY;
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
                        if (nivel > 7 && nivel < 14) {
                            estadoFinalHijo = comprobarVictoria(tableroEstadoHijo, j, i) != 0;
                        } else if (nivel == 14) {
                            estadoFinalHijo = true;
                        }
                        hijos.add(new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo));
                    }
                }
            }
        }

        private byte[][] construyeTablero() {
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

        private void print() {
            byte[][] tableroEstado = construyeTablero();
            System.out.println("EstadoFinal: " + estFinal);
            System.out.println("Nivel " + nivel);
            for (byte[] tableroEstado1 : tableroEstado) {
                for (int j = 0; j < tableroEstado[0].length; j++) {
                    System.out.print(tableroEstado1[j] + "	");
                }
                System.out.println();
            }
            System.out.println();
        }

        public Estado getHijo(int pos) {
            return hijos.get(pos);
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
        int[][] aux = tablero.toArray();
        tableroActual = new byte[tablero.getFilas()][tablero.getColumnas()];
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                tableroActual[i][j] = (byte) aux[i][j];
            }
        }

        this.conecta = conecta;
        estadoActual = new Estado(null, (byte) 0, (byte) 0, (byte) nivelActual, false);
        System.out.println("Generados: " + generados);
        nivelActual++;
        // ...
        // Calcular la mejor columna posible donde hacer nuestra turnoJugada
        //Pintar Ficha (sustituir 'columna' por el valor adecuado)
        //Pintar Ficha
        int columna = getRandomColumn(tablero);

        return tablero.checkWin(tablero.setButton(columna, Conecta4.PLAYER2), columna, conecta);

    }
// turnoJugada

} // IAPlayer
