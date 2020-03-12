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
 *         (agvico@ujaen.es)
 */
public class M20A04aDFS extends Mouse {

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ATRIBUTOS
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    Random generador;

    /**
     * Variable para guardar el anterior movimiento realizado
     */
    private int movAnterior;
    private int corrupta = 0; // Borrar al terminar

    /**
     * Boolean utilizado para determinar si el ratón se encuentra atrancado o no.
     */
    private boolean stuck = false;

    /**
     * Tabla hash para almacenar las celdas visitadas por el raton:
     * Clave:Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    // private final HashMap<Pair<Integer, Integer>, Grid> posiblesCaminos;
    private final ArrayList<Grid> posiblesCaminos; // Almacena las celdas que contienen
    // bifurcaciones no visitadas.
    private final HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> adyacencias; // Almacena las
    // adyacencias de las
    // celdas visitadas.
    private final HashSet<Pair<Integer, Integer>> visitadasDFS; // Almacena las posiciones visitadas en la ultima
    // búsqueda.
    private final ArrayList<Integer> posiblesMovActuales; // ArrayList auxiliar para almacenar los posibles movimientos
    // que se realizaran.

    /**
     * Pila para almacenar el camino recorrido.
     */
    private final Stack<Grid> pilaMovimientos;

    private final Stack<Grid> pilaDFS; // Almacena los movimiento realizados en la búsqueda
    private int contador = 0; // Contador para borrar la pila de movimientos
    private boolean revertir = false;

    // COSA DE DFS2.0
    private boolean evaluado = false;
    private boolean hayDFS = false;
    private final Stack<Integer> caminoDFS;
    private Grid objetivo;

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS PRINCIPALES
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Constructor (Puedes modificar el nombre a tu gusto).
     */
    public M20A04aDFS() {
        super("BreakDFS;");
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        generador = new Random();
        posiblesCaminos = new ArrayList<>();
        posiblesMovActuales = new ArrayList<>();
        adyacencias = new HashMap<>();
        adyacencias.put(new Pair<>(0, 0), new ArrayList<>());
        pilaDFS = new Stack<>();
        visitadasDFS = new HashSet<>();
        caminoDFS = new Stack<>();
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método principal para el movimiento del raton. Incluye la gestión de
     *        cuando un queso aparece o no.
     * @param currentGrid Celda actual
     * @param cheese      Queso
     */
    // TODO: si no sabe donde esta el queso, seguir explorando
    // si puede seguir explorando el camino, sigue
    // si no, dfs a la ultima bifurcacion
    // TODO: si sabe donde esta, hacer DFS
    @Override
    public int move(final Grid currentGrid, final Cheese cheese) {

        Pair<Integer, Integer> queso = new Pair<>(cheese.getX(), cheese.getY());
        System.out.println("BUENA1");
        if (objetivo != null) {
            // System.out.println("YA CASI NO HAY DFS");
            if (mismaPosicion(currentGrid, objetivo)) {
                hayDFS = false;
                stuck = false;
                objetivo = null;
            }
        }

        System.out.println("BUENA2");
        if (hayDFS && !caminoDFS.empty()) {
            System.out.println("CAMINO: " + caminoDFS);
            System.out.println("YENDO A: " + caminoDFS.peek());
            return caminoDFS.pop();
        } else if (!evaluado && celdasVisitadas.containsKey(queso)) {
            System.out.println("BUENA3");
            evaluado = true;
            Grid destino = new Grid(cheese.getX(), cheese.getY());
            addHashMap(currentGrid);
            recorreDFS(destino, currentGrid);
            return caminoDFS.pop();
        } else {
            System.out.println("BUENA4");
            return tomaDecision(currentGrid);
        }
    }

    // Esto sera la version buena donde poner lo que funcione, la otra de prueba
    public int move2(final Grid currentGrid, final Cheese cheese) {
        return UP;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Traza una ruta desde la posición actual hasta la posición destino
     *
     * @param posicion Posicion en la que se encuentra el ratón
     * @param destino  Posicion a la que quiere ir el ratón
     */
    public void recorreDFS(Grid posicion, Grid destino) {
        Grid actual = new Grid(posicion.getX(), posicion.getY());
        // Itera mientras no llegue al destino
        while (!mismaPosicion(actual, destino)) {

            // Añade a las visitadas la actual
            Pair<Integer, Integer> evaluando = new Pair<>(actual.getX(), actual.getY());
            visitadasDFS.add(evaluando);

            // Obtiene las adyacencias de la actual
            ArrayList<Pair<Integer, Integer>> lista = adyacencias.get(evaluando);
            Iterator<Pair<Integer, Integer>> it = lista.listIterator();
            boolean sigue = true;
            // Busca una adyacencia válida
            while (it.hasNext() && sigue) {
                evaluando = it.next();
                if (celdasVisitadas.containsKey(evaluando) && !visitadasDFS.contains(evaluando)) {
                    sigue = false;
                }
            }

            // Si la hay, sigue el DFS
            // si no, vuelve atrás
            if (sigue) {
                int mov = caminoDFS.pop();
                actual = getCelda(actual, mov);
            } else {
                Grid temp = new Grid(evaluando.getKey(), evaluando.getValue());
                caminoDFS.add(relativa(temp, actual));
                actual = temp;
            }
        }
        System.out.println("DFS TERMINADO");
        // System.out.println("CAMINO: " + caminoDFS);
        hayDFS = true;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método encargado de la toma de decisión en el movimiento del ratón.
     * @param currentGrid
     * @return Movimiento decidido
     */
    public int tomaDecision(final Grid currentGrid) {
        posiblesMovActuales.clear();
        if (!stuck) {
            // Intenga seguir el camino en la misma direccion
            switch (movAnterior) {
                case UP:
                    if (currentGrid.canGoUp() && !currentGrid.canGoRight() && !currentGrid.canGoLeft()
                            && !visitada(currentGrid, UP)) {
                        pilaMovimientos.push(currentGrid);
                        addHashMap(currentGrid);
                        return UP;
                    }
                    break;
                case DOWN:
                    if (currentGrid.canGoDown() && !currentGrid.canGoRight() && !currentGrid.canGoLeft()
                            && !visitada(currentGrid, DOWN)) {
                        pilaMovimientos.push(currentGrid);
                        addHashMap(currentGrid);
                        return DOWN;
                    }
                    break;

                case LEFT:
                    if (currentGrid.canGoLeft() && !currentGrid.canGoDown() && !currentGrid.canGoUp()
                            && !visitada(currentGrid, LEFT)) {
                        pilaMovimientos.push(currentGrid);
                        addHashMap(currentGrid);
                        return LEFT;
                    }
                    break;

                case RIGHT:
                    if (currentGrid.canGoRight() && !currentGrid.canGoDown() && !currentGrid.canGoUp()
                            && !visitada(currentGrid, RIGHT)) {
                        pilaMovimientos.push(currentGrid);
                        addHashMap(currentGrid);
                        return RIGHT;
                    }
                    break;
            }

            // Obtiene las celdas a las que puede acceder desde la posición actual
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

        // Si está vacía, vuelve atrás
        // si no, avanza en una dirección aleatoria
        if (posiblesMovActuales.isEmpty()) {
            stuck = true;
            // System.out.println("COSA MALA DE VOLVER ATRAS");
            addHashMap(currentGrid);
            movAnterior = volverAnterior(currentGrid);
            // System.out.println("Camino: " + caminoDFS);
        } else {
            movAnterior = posiblesMovActuales.get(generador.nextInt(posiblesMovActuales.size()));
            pilaMovimientos.push(currentGrid);
            addHashMap(currentGrid);
        }

        return movAnterior;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Vuelve a la posicion que indique la pilaMovimientos. Si se encuentra
     *        en un Grid marcado como posible camino coge un nuevo camino no
     *        visitado para explorar.
     * @param currentGrid Grid actual
     * @return Movimiento a tomar
     */
    public int volverAnterior(final Grid currentGrid) {
        System.out.println("BUENA5");
        objetivo = posiblesCaminos.remove(posiblesCaminos.size() - 1);
        // System.out.println("Objetivo: " + objetivo.getX() + "." + objetivo.getY());
        recorreDFS(objetivo, currentGrid);
        return BOMB;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Añade el Grid actual al Hashmap y al ArrayList posiblesCaminos los
     *        posibles caminos
     * @param currentGrid Grid en el que se encuentra el ratón actualmente
     */
    public void addHashMap(final Grid currentGrid) {
        int numCaminos = 0;
        // Añade la celda actual al mapa
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()))) {
            celdasVisitadas.put(new Pair<>(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
        }

        // Actualiza adyacencias de la celda actual y sus vecinas
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1))
                && currentGrid.canGoDown()) {
            numCaminos++;

            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1));

            adyacencias.put(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1), new ArrayList<>());
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY()));
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1))
                && currentGrid.canGoUp()) {
            numCaminos++;

            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1));

            adyacencias.put(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1), new ArrayList<>());
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY()));
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()))
                && currentGrid.canGoLeft()) {
            numCaminos++;

            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()));

            adyacencias.put(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()), new ArrayList<>());
            adyacencias.get(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY()));
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()))
                && currentGrid.canGoRight()) {
            numCaminos++;

            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()));

            adyacencias.put(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()), new ArrayList<>());
            adyacencias.get(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()))
                    .add(new Pair<>(currentGrid.getX(), currentGrid.getY()));
        }

        // Si puede ir por varios sitios, se guarda para volver más tarde
        if (numCaminos > 1) {
            posiblesCaminos.add(currentGrid);
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método que se llama cuando aparece un nuevo queso
     */
    @Override
    public void newCheese() {
        pilaDFS.clear();
        visitadasDFS.clear();
        caminoDFS.clear();
        revertir = true;
        hayDFS = false;
        evaluado = false;
        stuck = false;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método que se llama cuando el raton pisa una bomba
     */
    @Override
    public void respawned() {
        pilaDFS.clear();
        visitadasDFS.clear();
        caminoDFS.clear();
        pilaMovimientos.clear();
        stuck = true;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
        System.out.println("====================ADYACENCIAS=====================");
        System.out.println(adyacencias);
        System.out.println("-------------------------------------------------------------------\n");
    }

    /**
     * @brief Método encargado de devolver el movimiento necesario para ir desde la
     *        celda actual a la celda destino.
     * @param actual  Celda en la que se encuentra el ratón
     * @param destino Celda a la que quiere ir el ratón
     * @return movimiento necesario.
     */
    private int relativa(Grid actual, Grid destino) {
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

    /**
     * @brief Devuelve si la celda a es igual a la celda b
     * @param a
     * @param b
     * @return resultado
     */
    private boolean mismaPosicion(Grid a, Grid b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }

    /**
     * @brief Calcula la distancia entre la celda a y la celda b
     * @param a
     * @param b
     * @return destancia entre las 2 celdas
     */
    public int distancia(Grid a, Grid b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * @brief Método que devuelve si de una casilla dada, está contenida en el mapa
     *        de celdasVisitadas
     * @param casilla   Casilla que se pasa para saber si ha sido visitada
     * @param direccion Dirección de la casilla visitada
     * @return True Si la casilla vecina que indica la dirección había sido visitada
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
        final Pair<Integer, Integer> par = new Pair<>(x, y);
        return celdasVisitadas.containsKey(par);
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     *        respecto a otra
     * @param actual   Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es mayor que la de la anterior
     */
    public boolean actualArriba(final Grid actual, final Grid anterior) {
        return actual.getY() > anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     *        respecto a otra
     * @param actual   Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es menor que la de la anterior
     */
    public boolean actualAbajo(final Grid actual, final Grid anterior) {
        return actual.getY() < anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     *        respecto a otra
     * @param actual   Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es mayor que la de la anterior
     */
    public boolean actualDerecha(final Grid actual, final Grid anterior) {
        return actual.getX() > anterior.getX();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     *        respecto a otra
     * @param actual   Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es menor que la de la anterior
     */
    public boolean actualIzquierda(final Grid actual, final Grid anterior) {
        return actual.getX() < anterior.getX();
    }

    public Grid getArriba(Grid actual) {
        return new Grid(actual.getX(), actual.getY() + 1);
    }

    public Grid getAbajo(Grid actual) {
        return new Grid(actual.getX(), actual.getY() - 1);
    }

    public Grid getDerecha(Grid actual) {
        return new Grid(actual.getX() + 1, actual.getY());
    }

    public Grid getIzquierda(Grid actual) {
        return new Grid(actual.getX() - 1, actual.getY());
    }

    public Pair<Integer, Integer> posicionActual(Grid celda) {
        return new Pair<>(celda.getX(), celda.getY());
    }

    /**
     * Devuelve el movimiento contrario
     *
     * @param movimiento
     * @return movimientoContrario a movimiento
     */
    public int contrario(int movimiento) {
        switch (movimiento) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                return BOMB;
        }
    }

    public Grid getCelda(Grid actual, int movimiento) {
        switch (movimiento) {
            case UP:
                return new Grid(actual.getX(), actual.getY() + 1);
            case DOWN:
                return new Grid(actual.getX(), actual.getY() - 1);
            case LEFT:
                return new Grid(actual.getX() - 1, actual.getY());
            default:
                return new Grid(actual.getX() + 1, actual.getY());
        }
    }
}
