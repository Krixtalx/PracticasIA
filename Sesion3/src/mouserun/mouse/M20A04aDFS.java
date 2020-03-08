package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import javafx.util.Pair;
import mouserun.game.Mouse;
import mouserun.game.Grid;
import mouserun.game.Cheese;

/**
 * Clase que contiene el esqueleto del raton base para las prácticas de
 * Inteligencia Artificial del curso 2019-20.
 *
 * @author Cristóbal José Carmona (ccarmona@ujaen.es) y Ángel Miguel García Vico
 * (agvico@ujaen.es)
 */
public class M20A04aDFS extends Mouse {

	/**
	 * Variable para almacenar la ultima celda visitada
	 */
	private Grid lastGrid;
	Random generador;

	/**
	 * Variable para guardar el anterior movimiento realizado
	 */
	private int movAnterior;
	private int corrupta = 0; //Borrar al terminar

	/**
	 * Boolean donde se almacena el estado del raton
	 */
	private boolean stuck = false;


	/**
	 * Tabla hash para almacenar las celdas visitadas por el raton:
	 * Clave:Coordenadas Valor: La celda
	 */
	private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
	private final HashMap<Pair<Integer, Integer>, Grid> posiblesCaminos;
	private final HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> adyacencias; //Almacena las adyacencias de las celdas visitadas
	private final HashSet<Pair<Integer, Integer>> visitadasDFS; //Almacena las posiciones visitadas en la ultima búsqueda
	private final ArrayList<Integer> posiblesMovActuales; //ArrayList auxiliar para almacenar los posibles movimientos que se realizaran.

	/**
	 * Pila para almacenar el camino recorrido.
	 */
	private final Stack<Grid> pilaMovimientos;

	private final Stack<Grid> pilaDFS; //Almacena los movimiento realizados en la búsqueda

	/**
	 * Constructor (Puedes modificar el nombre a tu gusto).
	 */
	public M20A04aDFS() {
		super("BreakDFS;");
		celdasVisitadas = new HashMap<>();
		pilaMovimientos = new Stack<>();
		generador = new Random();
		posiblesCaminos = new HashMap<>();
		posiblesMovActuales = new ArrayList<>();
		adyacencias = new HashMap<>();
		adyacencias.put(new Pair<>(0, 0), new ArrayList<>());
		pilaDFS = new Stack<>();
		visitadasDFS = new HashSet<>();
	}

	/**
	 * @brief Método principal para el movimiento del raton. Incluye la gestión
	 * de cuando un queso aparece o no.
	 * @param currentGrid Celda actual
	 * @param cheese Queso
	 */
	@Override
	public int move(final Grid currentGrid, final Cheese cheese) {

		Pair posicionQueso = new Pair(cheese.getX(), cheese.getY());

		if (celdasVisitadas.containsKey(posicionQueso)) {
			int corre = recorreDFS(currentGrid/*, new Grid(cheese.getX(), cheese.getY())*/);
			System.out.println("CORRIENDO A: " + corre);
			return corre;
		} else {
			System.out.println("EXPLORA");
			return tomaDecision(currentGrid);
		}
	}

	public int recorreDFS(Grid currentGrid) {
		visitadasDFS.add(new Pair(currentGrid.getX(), currentGrid.getY()));
		ArrayList<Pair<Integer, Integer>> lista = adyacencias.get(new Pair(currentGrid.getX(), currentGrid.getY()));
//		System.out.println("ADYACENTES: " + lista);
//		System.out.println("PILA: " + pilaDFS);
//		System.out.println("VISITADAS: " + visitadasDFS);
		Iterator<Pair<Integer, Integer>> it = lista.listIterator();
		Pair<Integer, Integer> evaluando = null;
		boolean sigue = true;
		while (it.hasNext() && sigue) {
			evaluando = it.next();
			if (celdasVisitadas.containsKey(evaluando) && !visitadasDFS.contains(evaluando)) {
				sigue = false;
			}
		}
		if (evaluando == null) {
			System.out.println("F");
			return BOMB;
		}
		if (!sigue) {
			System.out.println("=====BUENA=====");
			//pilaDFS.add(currentGrid);
			pilaMovimientos.add(currentGrid);
			return relativa(currentGrid, new Grid(evaluando.getKey(), evaluando.getValue()));
		} else {
			System.out.println("=====MALA=====");
			//int mov = relativa(currentGrid, pilaDFS.pop());
			int mov = relativa(currentGrid, pilaMovimientos.pop());
			return mov;
		}
	}

	public int relativa(Grid actual, Grid destino) {
		if (actual.getX() < destino.getX()) {
			return RIGHT;
		} else if (actual.getX() > destino.getX()) {
			return LEFT;
		} else if (actual.getY() < destino.getY()) {
			return UP;
		} else {
			return DOWN;
		}
	}

	public boolean mismaPosicion(Grid a, Grid b) {
		return a.getX() == b.getX() && a.getY() == b.getY();
	}

	public int tomaDecision(final Grid currentGrid) {
		posiblesMovActuales.clear();
		if (!stuck) {
			switch (movAnterior) {
				case UP:
					if (currentGrid.canGoUp() && !currentGrid.canGoRight() && !currentGrid.canGoLeft() && !visitada(currentGrid, UP)) {
						pilaMovimientos.push(currentGrid);
						addHashMap(currentGrid);
						return UP;
					}
					break;
				case DOWN:
					if (currentGrid.canGoDown() && !currentGrid.canGoRight() && !currentGrid.canGoLeft() && !visitada(currentGrid, DOWN)) {
						pilaMovimientos.push(currentGrid);
						addHashMap(currentGrid);
						return DOWN;
					}
					break;

				case LEFT:
					if (currentGrid.canGoLeft() && !currentGrid.canGoDown() && !currentGrid.canGoUp() && !visitada(currentGrid, LEFT)) {
						pilaMovimientos.push(currentGrid);
						addHashMap(currentGrid);
						return LEFT;
					}
					break;

				case RIGHT:
					if (currentGrid.canGoRight() && !currentGrid.canGoDown() && !currentGrid.canGoUp() && !visitada(currentGrid, RIGHT)) {
						pilaMovimientos.push(currentGrid);
						addHashMap(currentGrid);
						return RIGHT;
					}
					break;
			}

			if (currentGrid.canGoUp() && !visitada(currentGrid, UP)) {
				posiblesMovActuales.add(UP);
			}
			if (currentGrid.canGoDown() && !visitada(currentGrid, DOWN)) {
				posiblesMovActuales.add(DOWN);
			}
			if (currentGrid.canGoLeft() && !visitada(currentGrid, LEFT)) {
				posiblesMovActuales.add(LEFT);
			}
			if (currentGrid.canGoRight() && !visitada(currentGrid, RIGHT)) {
				posiblesMovActuales.add(RIGHT);
			}
		}

		if (posiblesMovActuales.isEmpty()) {
			stuck = true;
			movAnterior = volverAnterior(currentGrid);
		} else {
			movAnterior = posiblesMovActuales.get(generador.nextInt(posiblesMovActuales.size()));
			pilaMovimientos.push(currentGrid);
		}
		//debug();
		posiblesCaminos.remove(new Pair<>(currentGrid.getX(), currentGrid.getY()));
		addHashMap(currentGrid);
//		System.out.println("====================ADYACENCIAS=====================");
//		System.out.println(adyacencias);
		return movAnterior;
	}

	/**
	 * @brief Vuelve a la posicion que indique la pilaMovimientos. Si se
	 * encuentra en un Grid marcado como posible camino coge un nuevo camino no
	 * visitado para explorar.
	 * @param currentGrid Grid actual
	 * @return Movimiento a tomar
	 */
	public int volverAnterior(final Grid currentGrid) {

		if (posiblesCaminos.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()))) {
			stuck = false;
			posiblesCaminos.remove(new Pair<>(currentGrid.getX(), currentGrid.getY()));
			pilaMovimientos.push(currentGrid);
			if (currentGrid.canGoUp() && !visitada(currentGrid, UP)) {
				return UP;
			}
			if (currentGrid.canGoDown() && !visitada(currentGrid, DOWN)) {
				return DOWN;
			}
			if (currentGrid.canGoLeft() && !visitada(currentGrid, LEFT)) {
				return LEFT;
			}
			if (currentGrid.canGoRight() && !visitada(currentGrid, RIGHT)) {
				return RIGHT;
			}
		}
		while (!pilaMovimientos.isEmpty()) {
			if (actualAbajo(currentGrid, pilaMovimientos.lastElement())) {
				pilaMovimientos.pop();
				return UP;
			}
			if (actualArriba(currentGrid, pilaMovimientos.lastElement())) {
				pilaMovimientos.pop();
				return DOWN;
			}
			if (actualDerecha(currentGrid, pilaMovimientos.lastElement())) {
				pilaMovimientos.pop();
				return LEFT;
			}
			if (actualIzquierda(currentGrid, pilaMovimientos.lastElement())) {
				pilaMovimientos.pop();
				return RIGHT;
			}
			pilaMovimientos.pop();
			corrupta++;
		}

		stuck = false;
		return BOMB;
	}

	/**
	 * @brief Añade el Grid actual al Hashmap y al ArrayList posiblesCaminos los
	 * posibles caminos
	 * @param currentGrid Grid en el que se encuentra el ratón actualmente
	 */
	public void addHashMap(final Grid currentGrid) {
		int numCaminos = 0;
		if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()))) {
			celdasVisitadas.put(new Pair<>(currentGrid.getX(), currentGrid.getY()), currentGrid);

			incExploredGrids();
		}

		if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1)) && currentGrid.canGoDown()) {
			numCaminos++;

			adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1));

			adyacencias.put(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY() - 1), new ArrayList<>());
			adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1)).add(new Pair<>(currentGrid.getX(), currentGrid.getY()));
		}
		if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1)) && currentGrid.canGoUp()) {
			numCaminos++;

			adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1));

			adyacencias.put(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1), new ArrayList<>());
			adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1)).add(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY()));
		}
		if (!celdasVisitadas.containsKey(new Pair<Integer, Integer>(currentGrid.getX() - 1, currentGrid.getY())) && currentGrid.canGoLeft()) {
			numCaminos++;

			adyacencias.get(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY())).add(new Pair<Integer, Integer>(currentGrid.getX() - 1, currentGrid.getY()));

			adyacencias.put(new Pair<Integer, Integer>(currentGrid.getX() - 1, currentGrid.getY()), new ArrayList<>());
			adyacencias.get(new Pair<Integer, Integer>(currentGrid.getX() - 1, currentGrid.getY())).add(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY()));
		}
		if (!celdasVisitadas.containsKey(new Pair<Integer, Integer>(currentGrid.getX() + 1, currentGrid.getY())) && currentGrid.canGoRight()) {
			numCaminos++;

			adyacencias.get(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY())).add(new Pair<Integer, Integer>(currentGrid.getX() + 1, currentGrid.getY()));

			adyacencias.put(new Pair<Integer, Integer>(currentGrid.getX() + 1, currentGrid.getY()), new ArrayList<>());
			adyacencias.get(new Pair<Integer, Integer>(currentGrid.getX() + 1, currentGrid.getY())).add(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY()));
		}
		if (numCaminos > 1) {
			posiblesCaminos.put(new Pair<Integer, Integer>(currentGrid.getX(), currentGrid.getY()), currentGrid);
		}
	}

	/**
	 * @brief Método que se llama cuando aparece un nuevo queso
	 */
	@Override
	public void newCheese() {
		pilaDFS.clear();
		visitadasDFS.clear();
	}

	/**
	 * @brief Método que se llama cuando el raton pisa una bomba
	 */
	@Override
	public void respawned() {

	}

	/**
	 * @brief Método para evaluar que no nos movamos a la misma celda anterior
	 * @param direction Direccion del raton
	 * @param currentGrid Celda actual
	 * @return True Si las casillas X e Y anterior son distintas a las actuales
	 */
	public boolean testGrid(final int direction, final Grid currentGrid) {
		if (lastGrid == null) {
			return true;
		}

		int x = currentGrid.getX();
		int y = currentGrid.getY();

		switch (direction) {
			case UP:
				y += 1;
				break;

			case DOWN:
				y -= 1;
				break;

			case LEFT:
				x -= 1;
				break;

			case RIGHT:
				x += 1;
				break;
		}

		return !(lastGrid.getX() == x && lastGrid.getY() == y);

	}

	/**
	 *
	 * @brief Método que devuelve si de una casilla dada, está contenida en el
	 * mapa de celdasVisitadas
	 * @param casilla Casilla que se pasa para saber si ha sido visitada
	 * @param direccion Dirección de la casilla visitada
	 * @return True Si la casilla vecina que indica la dirección había sido
	 * visitada
	 */
	public boolean visitada(final Grid casilla, final int direccion) {
		int x = casilla.getX();
		int y = casilla.getY();

		switch (direccion) {
			case UP:
				y += 1;
				break;

			case DOWN:
				y -= 1;
				break;

			case LEFT:
				x -= 1;
				break;

			case RIGHT:
				x += 1;
				break;
		}
		final Pair<Integer, Integer> par = new Pair<Integer, Integer>(x, y);
		return celdasVisitadas.containsKey(par);
	}

	/**
	 * Función de Debug. Borrar al final.
	 */
	private void debug() {
		System.out.println("\n-------------------------------------------------------------------");
		System.out.println(posiblesMovActuales.size() + " posibles movimientos");
		System.out.println(celdasVisitadas.size() + " celdas visitadas");
		System.out.println(posiblesCaminos.size() + " posibles caminos");
		System.out.println(pilaMovimientos.size() + " movimientos guardados");
		System.out.println(corrupta + " veces pila corrupta");
		System.out.println("-------------------------------------------------------------------\n");
	}

	/**
	 * @brief Método para calcular si una casilla está en una posición relativa
	 * respecto a otra
	 * @param actual Celda actual
	 * @param anterior Celda anterior
	 * @return True Si la posición Y de la actual es mayor que la de la anterior
	 */
	public boolean actualArriba(final Grid actual, final Grid anterior) {
		return actual.getY() > anterior.getY();
	}

	/**
	 * @brief Método para calcular si una casilla está en una posición relativa
	 * respecto a otra
	 * @param actual Celda actual
	 * @param anterior Celda anterior
	 * @return True Si la posición Y de la actual es menor que la de la anterior
	 */
	public boolean actualAbajo(final Grid actual, final Grid anterior) {
		return actual.getY() < anterior.getY();
	}

	/**
	 * @brief Método para calcular si una casilla está en una posición relativa
	 * respecto a otra
	 * @param actual Celda actual
	 * @param anterior Celda anterior
	 * @return True Si la posición X de la actual es mayor que la de la anterior
	 */
	public boolean actualDerecha(final Grid actual, final Grid anterior) {
		return actual.getX() > anterior.getX();
	}

	/**
	 * @brief Método para calcular si una casilla está en una posición relativa
	 * respecto a otra
	 * @param actual Celda actual
	 * @param anterior Celda anterior
	 * @return True Si la posición X de la actual es menor que la de la anterior
	 */
	public boolean actualIzquierda(final Grid actual, final Grid anterior) {
		return actual.getX() < anterior.getX();
	}

}
