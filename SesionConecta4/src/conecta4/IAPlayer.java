/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;
import java.util.List;

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

		private final List<Estado> hijos;
		private final Estado padre;
		private final int[][] estTablero;
		private int filaJugada;
		private int colJugada;

		public Estado(Estado padre, int[][] tablero) {
			this.hijos = new ArrayList();
			this.padre = padre;
			int[][] temp = tablero;
			estTablero = new int[temp.length][temp[0].length];
			for (int i = 0; i < temp.length; i++) {
				System.arraycopy(temp[i], 0, estTablero[i], 0, temp[i].length);
			}
			filaJugada = 0;
			colJugada = 0;
		}

		public void mostrarTablero(int nivel) {
			System.out.println("Nivel " + nivel);
			for (int i = 0; i < estTablero.length; i++) {
				for (int j = 0; j < estTablero[i].length; j++) {
					System.out.printf(estTablero[i][j] + " ");
				}
				System.out.printf("\n");
			}
			System.out.printf("\n");

			for (int i = 0; i < hijos.size(); i++) {
				hijos.get(i).mostrarTablero(nivel + 1);
			}
		}

		public void addFicha(int col, int jugador) {
			int fila = estTablero.length - 1;
			while (0 <= fila && estTablero[fila][col] != 0) {
				fila--;
			}

			if (fila >= 0) {
				estTablero[fila][col] = jugador;
				filaJugada = fila;
				colJugada = col;
			}
		}

		private void generaHijos(int jugador, int conecta, int nivel) {
			if (comprobarVictoria(filaJugada, colJugada, conecta) == 0 && nivel > 0) {
				for (int i = 0; i < estTablero[0].length; i++) {
					Estado nuevo = new Estado(this, estTablero);
					nuevo.addFicha(i, jugador);
					int jugHijo;
					hijos.add(nuevo);
					if (jugador == Conecta4.PLAYER1) {
						jugHijo = Conecta4.PLAYER2;
					} else {
						jugHijo = Conecta4.PLAYER1;
					}
					nuevo.generaHijos(jugHijo, conecta, nivel - 1);
				}
			}
		}

		//Copia modificada del método Grid.checkWin
		public int comprobarVictoria(int x, int y, int conecta) {
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

	private Estado raiz = null;

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
		int columna = getRandomColumn(tablero);
		if (raiz == null) {
			raiz = new Estado(null, tablero.toArray());
			raiz.generaHijos(Conecta4.PLAYER2, conecta, 3);
		}
		System.out.println("================COSA DE VER================");
		raiz.mostrarTablero(0);
		return tablero.checkWin(tablero.setButton(columna, Conecta4.PLAYER2), columna, conecta);

	} // turnoJugada

} // IAPlayer
