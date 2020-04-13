/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;
import java.util.Scanner;
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
			//this.hijos = new ArrayList<>(4);
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
						valor = (byte) comprobarVictoria(tableroEstadoHijo, j, i);
						if (nivel >= 6 && nivel < 14) {
							estadoFinalHijo = valor != 0;
						} else if (nivel == 14) {
							estadoFinalHijo = true;
						}
						//hijos.add(new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo));
						if (hijo1 == null) {
							hijo1 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
							hijo1.valor = (byte) (this.valor * -1);
						} else if (hijo2 == null) {
							hijo2 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
							hijo2.valor = (byte) (this.valor * -1);
						} else if (hijo3 == null) {
							hijo3 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
							hijo3.valor = (byte) (this.valor * -1);
						} else if (hijo4 == null) {
							hijo4 = new Estado(this, (byte) j, (byte) i, (byte) (nivel + 1), estadoFinalHijo);
							hijo4.valor = (byte) (this.valor * -1);
						}
					}
				}

				//TODO: el 4 -> a variable global
				//Nivel par = max, nivel impar = min
				byte minimax;
				if (nivel % 2 == 0) {
					minimax = Byte.MIN_VALUE;
					if(hijo1 != null){
						if(hijo1.valor > minimax){
							minimax = hijo1.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor > minimax){
							minimax = hijo2.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor > minimax){
							minimax = hijo2.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor > minimax){
							minimax = hijo2.valor;
						}
					}
				} else {
					minimax = Byte.MAX_VALUE;
					if(hijo1 != null){
						if(hijo1.valor < minimax){
							minimax = hijo1.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor < minimax){
							minimax = hijo2.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor < minimax){
							minimax = hijo2.valor;
						}
					}
					if(hijo2 != null){
						if(hijo2.valor < minimax){
							minimax = hijo2.valor;
						}
					}
				}
				
				this.valor = minimax;
			} else {
				this.valor = (byte) (-1 * comprobarVictoria(construyeTablero(), movX, movY));
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

		public Estado getHijo(int col) {
			/*for (Estado hijo : hijos) {
				if(hijo.movY == col){
					return hijo;
				}
			}*/
			if (hijo1.movY == col) {
				return hijo1;
			} else if (hijo2.movY == col) {
				return hijo2;
			} else if (hijo3.movY == col) {
				return hijo3;
			} else if (hijo4.movY == col) {
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
		int posHijo = -1;
		if (estadoActual != null) {
			System.out.println("ASI EMPIEZA LA COSA:");
			estadoActual.print();
			posHijo = jugadaP1(tablero);
		}

		this.conecta = conecta;
		if (estadoActual == null) {
			estadoActual = new Estado(null, (byte) 0, (byte) 0, (byte) nivelActual, false);
		} else {

			if (posHijo == -1) {
				System.out.println("ESTAMOS EN LA MIERDA BRO");
				return -1;
			}
			estadoActual = estadoActual.getHijo(posHijo);
		}
		System.out.println("----ESTADO ACTUAL----");
		estadoActual.print();
		//new Scanner(System.in).nextLine();
		System.out.println("Generados: " + generados);
		// ...
		// Calcular la mejor columna posible donde hacer nuestra turnoJugada
		//Pintar Ficha (sustituir 'columna' por el valor adecuado)
		//Pintar Ficha
		int columna = getRandomColumn(tablero);
		estadoActual = estadoActual.getHijo(columna);
		System.out.println("---Raiz---");
		estadoActual.print();
		actTableroAnterior(estadoActual.construyeTablero());
		/*System.out.println("=============ANTERIOR=============");
		for (int i = 0; i < tableroAnterior.length; i++) {
			for (int j = 0; j < tableroAnterior[i].length; j++) {
				System.out.printf("%d	", tableroAnterior[i][j]);
			}
			System.out.println("");
		}*/
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

		System.out.println(":::COMPARANDO:::");
		for (int i = 0; i < tableroAnterior.length; i++) {
			for (int j = 0; j < tableroAnterior[i].length; j++) {
				System.out.printf("%d	", tableroAnterior[i][j]);
			}
			System.out.println("");
		}
		System.out.println("Y");
		for (int i = 0; i < tableroActual.length; i++) {
			for (int j = 0; j < tableroActual[i].length; j++) {
				System.out.printf("%d	", tableroActual[i][j]);
			}
			System.out.println("");
		}

		for (int i = 0; i < tableroAnterior.length; i++) {
			for (int j = 0; j < tableroAnterior[i].length; j++) {
				if (tableroAnterior[i][j] != tableroActual[i][j]) {
					System.out.println("COLUMNA DIFERENTE: " + j);
					return j;
				}
			}
		}
		return -1;
	}
// turnoJugada

} // IAPlayer
