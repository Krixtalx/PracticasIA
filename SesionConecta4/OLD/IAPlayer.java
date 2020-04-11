/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;

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

    private class Estado {
        //TODO: si es la raiz, se guarda el tablero, si no, solo el movimiento

        private int[][] tablero;
        private int movimiento;
        private ArrayList<Integer> hijos;
        private int valor;

        //Copia modificada del método Grid.checkWin
        public int comprobarVictoria(int x, int y, int conecta) {
            /*
			*	x fila
			*	y columna
             */
            int filas = tablero.length;
            int columnas = tablero[0].length;

            //Comprobar vertical
            int ganar1 = 0;
            int ganar2 = 0;
            int ganador = 0;
            boolean salir = false;
            for (int i = 0; (i < filas) && !salir; i++) {
                if (tablero[i][y] != Conecta4.VACIO) {
                    if (tablero[i][y] == Conecta4.PLAYER1) {
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
                        if (tablero[i][y] == Conecta4.PLAYER2) {
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
                if (tablero[x][j] != Conecta4.VACIO) {
                    if (tablero[x][j] == Conecta4.PLAYER1) {
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
                        if (tablero[x][j] == Conecta4.PLAYER2) {
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
                if (tablero[a][b] != Conecta4.VACIO) {
                    if (tablero[a][b] == Conecta4.PLAYER1) {
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
                        if (tablero[a][b] == Conecta4.PLAYER2) {
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
                if (tablero[a][b] != Conecta4.VACIO) {
                    if (tablero[a][b] == Conecta4.PLAYER1) {
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
                        if (tablero[a][b] == Conecta4.PLAYER2) {
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

    //private Estado raiz = null;
    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int turnoJugada(Grid tablero, int conecta) {

        // ...
        // Calcular la mejor columna posible donde hacer nuestra turnoJugada
        //Pintar Ficha (sustituir 'columna' por el valor adecuado)
        //Pintar Ficha
        System.out.println("=====INICIO=====");
        tablero.print();
        System.out.println("=====ARBOL=====");
        maxNivel = tablero.getFilas() * tablero.getColumnas();
        totalGeneradas = 0;
        mostrarHijos(tablero.toArray(), 0, Conecta4.PLAYER2, conecta);
        System.out.println("generadas=" + totalGeneradas);
        int columna = getRandomColumn(tablero);

        return tablero.checkWin(tablero.setButton(columna, Conecta4.PLAYER2), columna, conecta);

    } // turnoJugada

    private int maxNivel;
    private int totalGeneradas;

    public void mostrarHijos(int[][] tablero, int nivel, int jugador, int conecta) {
        //System.out.println("-----NIVEL " + nivel + "-----");
        for (int i = 0; i < tablero[0].length; i++) {
//			if(nivel < 4){
//				System.out.println(totalGeneradas);
//			}
            if (tablero[0][i] == 0) {
                int[][] estTablero = new int[tablero.length][tablero[0].length];
                for (int f = 0; f < estTablero.length; f++) {
                    System.arraycopy(tablero[f], 0, estTablero[f], 0, estTablero[f].length);
                }

                int fila = estTablero.length - 1;
                while (0 <= fila && estTablero[fila][i] != 0) {
                    fila--;
                }

                if (fila >= 0) {
                    estTablero[fila][i] = jugador;
                }

                if (comprobarVictoria(estTablero, fila, i, conecta) == 0 /*&& nivel < maxNivel*/) {
                    mostrarHijos(estTablero, nivel + 1, -1 * jugador, conecta);
                } else {
                    maxNivel = nivel;
                    /*System.out.println("Nivel " + nivel + ", hijo " + i);
					for (int f = 0; f < estTablero.length; f++) {
						for (int c = 0; c < estTablero[f].length; c++) {
							System.out.printf(estTablero[f][c] + "	");
						}
						System.out.printf("\n");
					}
					System.out.printf("\n");
                     */
                    //Scanner entrada = new Scanner(System.in);
                    //entrada.nextLine();
                }
                //System.out.printf("%10d\n", ++totalGeneradas);
                totalGeneradas++;
            }

        }
        //System.out.println("-----FIN NIVEL " + nivel + "-----");
    }

    public int comprobarVictoria(int[][] estTablero, int x, int y, int conecta) {
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

} // IAPlayer
