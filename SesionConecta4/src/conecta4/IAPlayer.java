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

    /**
     * Estado en que se encuentra la partida dentro del árbol
     */
    private Estado estadoActual = null;

    /**
     * Nivel actual del agente
     */
    private int nivelActual = 0;

    /**
     * Nivel máximo hasta el que se genera el árbol
     */
    private int nivelMaximo = 8;

    /**
     * Número de niveles adicionales que se generan al alcanzar el límite
     */
    private final int incremento = 4;

    /**
     * Matriz que representa el tablero en el turno del agente
     */
    public int[][] tableroActual;

    /**
     * Matriz que representa el tablero en el turno anterior del agente
     */
    private int[][] tableroAnterior;

    /**
     * Subclase para la gestión del árbol de estados posibles
     */
    private class Estado {

        /**
         * Colección de los estados hijo del nodo actual
         */
        private final ArrayList<Estado> hijos;

        /**
         * Referencia al estado padre
         */
        private final Estado padre;

        /**
         * Fila en la que debe colocarse la ficha para obtener el tablero
         */
        private final int movX;

        /**
         * Columna en la que debe colocarse la ficha para obtener el tablero
         */
        private final int movY;

        /**
         * Nivel del árbol en que se encuentra el nodo
         */
        private final int nivel;

        /**
         * Indica si el nodo es un estado terminal o no
         */
        private final boolean estFinal;

        /**
         * Valor del nodo para la toma de decisiones
         */
        private int valor;

        /**
         * Valor alfa auxiliar para la poda alfa-beta
         */
        private int alfa;

        /**
         * Valor beta auxiliar para la poda alfa-beta
         */
        private int beta;

        /**
         * Constructor parametrizado, genera todo el árbol hasta el nivel límite
         *
         * @param padre Estado padre del que se está creando
         * @param movX Fila en la que se coloca la ficha
         * @param movY Columna en la que se coloca la ficha
         * @param nivel Nivel del nodo en el árbol
         * @param estFinal Indica si el estado es terminal
         * @param conecta Número de fichas consecutivas para ganar la partida
         */
        public Estado(Estado padre, int movX, int movY, int nivel, boolean estFinal, int conecta) {
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
            //Expande los valores alfa-beta del padre
            if (padre != null) {
                this.alfa = padre.alfa;
                this.beta = padre.beta;
            } else {
                this.alfa = Integer.MIN_VALUE;
                this.beta = Integer.MAX_VALUE;
            }

            if (nivel % 2 == 0) {
                valor = Integer.MIN_VALUE;
            } else {
                valor = Integer.MAX_VALUE;
            }

            //Genera hijos hasta llegar al nivel límite, tras lo cual obtiene
            //el valor heurístico del tablero para propagar hacia los padres
            if (nivel < nivelMaximo) {
                genHijos(conecta);
            } else {
                valor = -1 * calculaValor(construyeTablero(), conecta);
            }
        }

        /**
         * Genera los hijos del nodo actual, podando las ramas que nunca serán
         * seleccionadas
         *
         * @param conecta Número de fichas consecutivas para la victoria
         */
        private void genHijos(int conecta) {
            //Genera para todos los nodos no terminales
            if (!estFinal) {
                int[][] tableroAux = construyeTablero();
                boolean podar = false;
                //Prueba en todas las casillas válidas mientras no sea necesario podar
                for (int i = 0; i < tableroActual[0].length; i++) {
                    boolean encontrado = false;
                    int j;
                    for (j = tableroActual.length - 1; j >= 0 && !encontrado; j--) {
                        if (tableroAux[j][i] == 0) {
                            encontrado = true;
                            j++;
                        }
                    }
                    //Si la columna para la jugada es válida, crea el hijo
                    if (encontrado) {
                        int[][] tableroEstadoHijo = new int[tableroActual.length][tableroActual[0].length];
                        for (int k = 0; k < tableroActual.length; k++) {
                            System.arraycopy(tableroAux[k], 0, tableroEstadoHijo[k], 0, tableroAux[0].length);
                        }
                        if (nivel % 2 == 0) {
                            tableroEstadoHijo[j][i] = -1;
                        } else {
                            tableroEstadoHijo[j][i] = 1;
                        }
                        //Comprueba si es estado terminal (si hay ganador o no)
                        boolean estadoFinalHijo = false;
                        if (nivel >= conecta) {
                            estadoFinalHijo = comprobarVictoria(tableroEstadoHijo, j, i, conecta) != 0;
                        }

                        Estado nuevo = new Estado(this, j, i, (nivel + 1), estadoFinalHijo, conecta);

                        //Realizamos la poda Alfa-Beta
                        if (nivel % 2 == 0) {
                            if (nuevo.valor >= this.alfa) {
                                this.valor = nuevo.valor;
                                this.alfa = nuevo.valor;

                                hijos.add(nuevo);
                            }
                        } else {
                            if (nuevo.valor <= this.valor) {
                                this.valor = nuevo.valor;
                                this.beta = nuevo.valor;
                            }
                            hijos.add(nuevo);
                        }

                    }
                }
            }
        }

        /**
         * Obtiene la mejor jugada que puede hacer la IA en la situación actual.
         *
         * @return pos donde debe echar la ficha.
         */
        public int getMejorJugada() {
            int minimax = Integer.MIN_VALUE;
            for (Estado hijo : hijos) {
                if (hijo.valor >= minimax) {
                    minimax = hijo.valor;
                    estadoActual = hijo;
                }
            }
            return estadoActual.movY;
        }

        /**
         * Construye el tablero en base a tableroActual y el nivel en el que nos
         * encontramos
         *
         * @return tablero determinado por el estado que llama al método
         */
        public final int[][] construyeTablero() {
            int[][] tablero = new int[tableroActual.length][tableroActual[0].length];
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

        /**
         * Muestra por pantalla el estado que lo llama
         */
        public void print() {
            int[][] tableroEstado = construyeTablero();
            System.out.println("EstadoFinal: " + estFinal);
            System.out.println("Nivel " + nivel);
            System.out.println("Valor: " + valor);
            for (int[] tableroEstado1 : tableroEstado) {
                for (int j = 0; j < tableroEstado[0].length; j++) {
                    System.out.print(tableroEstado1[j] + "	");
                }
                System.out.println();
            }
            System.out.println();
        }

        /**
         * Muestra por pantalla los estados hijo del estado que llama al método
         */
        public void printHijos() {
            System.out.println("Hijos");
            for (Estado hijo : hijos) {
                hijo.print();
            }
        }

        /**
         * Devuelve el hijo correspondiente a echar la ficha en la columna col
         *
         * @param col: Columna donde se echa la ficha
         * @return Estado hijo correspondiente
         */
        public Estado getHijo(int col) {
            if(hijos.isEmpty())
                genHijos(4);
            for (Estado hijo : hijos) {
                if (hijo.movY == col) {
                    return hijo;
                }
            }
            System.out.println("Algo salio mal (getHijo)");
            return null;
        }

        /*
		 * @return ArrayList de los hijos del estado actual
         */
        public ArrayList<Estado> getListaHijos() {
            return hijos;
        }

        /**
         * Función que comprueba si habrá una victoria al introducir una ficha
         * en la pos x, y del tablero estTablero.
         *
         * @param estTablero
         * @param x
         * @param y
         * @param conecta Num de fichas que debe de haber concatenadas para que
         * haya una victoria
         * @return Jugador ganador en el caso de que lo haya. 0 en caso
         * contrario
         */
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
    }

    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int turnoJugada(Grid tablero, int conecta) {
        System.out.println("Nivel Actual: " + nivelActual);
        if (nivelActual == 18) {
            System.out.println("aqui");
        }
        generaNiveles(conecta, tablero.getFilas() * tablero.getColumnas() - 1);
        
        actTableroActual(tablero);
        int posHijo;
        
        if (estadoActual != null) {
            posHijo = jugadaP1();
            
            estadoActual = estadoActual.getHijo(posHijo);
            generaNiveles(conecta, tablero.getFilas() * tablero.getColumnas() - 1);
        } else {
            estadoActual = new Estado(null, 0, 0, nivelActual, false, conecta);
        }

        // Calcular la mejor columna posible donde hacer nuestra turnoJugada
        int columna = estadoActual.getMejorJugada();
        actTableroAnterior(estadoActual.construyeTablero());
        nivelActual++;

        return tablero.checkWin(tablero.setButton(columna, Conecta4.PLAYER2), columna, conecta);

    }

    /**
     * Genera nuevo niveles si es necesario
     *
     * @param conecta Número de fichas consecutivas para ganar
     * @param limite Último nivel total del árbol
     */
    public void generaNiveles(int conecta, int limite) {
        if (estadoActual != null) {
            if (estadoActual.nivel == nivelMaximo) {
                nivelMaximo += incremento;
                if (nivelMaximo > limite) {
                    nivelMaximo = limite;
                }
                estadoActual.genHijos(conecta);
            }
        }
    }

    /**
     * Actualiza el tablero tableroActual
     *
     * @param tablero Tablero de juego a copiar
     */
    public void actTableroActual(Grid tablero) {
        int[][] aux = tablero.toArray();
        tableroActual = new int[tablero.getFilas()][tablero.getColumnas()];
        for (int i = 0; i < tablero.getFilas(); i++) {
            System.arraycopy(aux[i], 0, tableroActual[i], 0, tablero.getColumnas());
        }
    }

    /**
     * Actualiza el tablero tableroAnterior
     *
     * @param tablero Matriz del tablero a copiar
     */
    public void actTableroAnterior(int[][] tablero) {
        tableroAnterior = new int[tablero.length][tablero[0].length];
        for (int i = 0; i < tablero.length; i++) {
            System.arraycopy(tablero[i], 0, tableroAnterior[i], 0, tablero[i].length);
        }
    }

    /**
     * Función usada para determinar que movimiento ha realizado el humano y
     * poder asi ir al estado hijo correcto.
     *
     * @return columna en la que se ha introducido la ficha.
     */
    public int jugadaP1() {
        for (int i = 0; i < tableroActual.length; i++) {
            for (int j = 0; j < tableroActual[i].length; j++) {
                if (tableroAnterior[i][j] != tableroActual[i][j]) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * Muestra una rama aleatoria del estado actual
     */
    public void mostrarRama() {
        ArrayList<Estado> hijos = estadoActual.getListaHijos();
        Random generador = new Random();
        while (hijos != null && hijos.size() > 0) {
            int elegido = generador.nextInt(hijos.size());
            hijos.get(elegido).print();
            hijos = hijos.get(elegido).getListaHijos();
        }
    }

    /**
     * Función encargada de calcular el valor heurístico que tendrá el estado al
     * que le pertenezca el tablero que se pasa como parametro
     *
     * @param tablero sobre el que se calculará el valor heurístico.
     * @param conecta
     * @return valor heurístico correspondiente al tablero.
     */
    public int calculaValor(int[][] tablero, int conecta) {
        int sumaTotal = 0;
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int col = 0; col < tablero[fila].length; col++) {
                //Vertical
                if (fila + conecta - 1 < tablero.length) {
                    int inicial = 0;
                    int repeticiones = 0;
                    int mayorRepes = 0;
                    boolean valido = true;
                    for (int posicion = 0; posicion < conecta && valido; posicion++) {
                        if (inicial == 0 && tablero[fila + posicion][col] != 0) {
                            inicial = tablero[fila + posicion][col];
                            repeticiones = 0;
                            repeticiones++;
                        } else if (tablero[fila + posicion][col] == inicial) {
                            repeticiones++;
                        } else if (tablero[fila + posicion][col] == inicial * -1) {
                            valido = false;
                        }
                    }
                    int sum;
                    if (valido) {
                        if (repeticiones > mayorRepes) {
                            mayorRepes = repeticiones;
                        }
                        sum = (int) Math.pow(10, mayorRepes) * inicial;
                        sumaTotal += sum;
                    }
                }

                //Horizontal
                if (col + conecta - 1 < tablero[fila].length) {
                    int inicial = 0;
                    int repeticiones = 0;
                    int mayorRepes = 0;
                    boolean valido = true;
                    for (int posicion = 0; posicion < conecta && valido; posicion++) {
                        if (inicial == 0 && tablero[fila][col + posicion] != 0) {
                            inicial = tablero[fila][col + posicion];
                            repeticiones = 0;
                            repeticiones++;
                        } else if (tablero[fila][col + posicion] == inicial) {
                            repeticiones++;
                        } else if (tablero[fila][col + posicion] == inicial * -1) {
                            valido = false;
                        }
                    }
                    int sum;
                    if (valido) {
                        if (repeticiones > mayorRepes) {
                            mayorRepes = repeticiones;
                        }
                        sum = (int) Math.pow(10, mayorRepes) * inicial;
                        sumaTotal += sum;
                    }
                }

                //Diagonal \
                if (fila + conecta - 1 < tablero.length && col + conecta - 1 < tablero[fila].length) {
                    int inicial = 0;
                    int repeticiones = 0;
                    int mayorRepes = 0;
                    boolean valido = true;
                    for (int posicion = 0; posicion < conecta && valido; posicion++) {
                        if (inicial == 0 && tablero[fila + posicion][col + posicion] != 0) {
                            inicial = tablero[fila + posicion][col + posicion];
                            repeticiones = 0;
                            repeticiones++;
                        } else if (tablero[fila + posicion][col + posicion] == inicial) {
                            repeticiones++;
                        } else if (tablero[fila + posicion][col + posicion] == inicial * -1) {
                            valido = false;
                        }
                    }
                    int sum;
                    if (valido) {
                        if (repeticiones > mayorRepes) {
                            mayorRepes = repeticiones;
                        }
                        sum = (int) Math.pow(10, mayorRepes) * inicial;
                        sumaTotal += sum;
                    }
                }

                //Diagonal /
                if (fila + conecta - 1 < tablero.length && col >= conecta - 1) {
                    int inicial = 0;
                    int repeticiones = 0;
                    int mayorRepes = 0;
                    boolean valido = true;
                    for (int posicion = 0; posicion < conecta && valido; posicion++) {
                        if (inicial == 0 && tablero[fila + posicion][col - posicion] != 0) {
                            inicial = tablero[fila + posicion][col - posicion];
                            repeticiones = 0;
                            repeticiones++;
                        } else if (tablero[fila + posicion][col - posicion] == inicial) {
                            repeticiones++;
                        } else if (tablero[fila + posicion][col - posicion] == inicial * -1) {
                            valido = false;
                        }
                    }
                    int sum;
                    if (valido) {
                        if (repeticiones > mayorRepes) {
                            mayorRepes = repeticiones;
                        }
                        sum = (int) Math.pow(10, mayorRepes) * inicial;
                        sumaTotal += sum;
                    }
                }
            }
        }
        return sumaTotal;
    }
} // IAPlayer
