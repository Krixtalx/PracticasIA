package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
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
public class M20A04aGRE extends Mouse {

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                   ATRIBUTOS
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    Random generador;

    /**
     * Variable para guardar el anterior movimiento realizado
     */
    private int movAnterior;

    /**
     * Tabla hash para almacenar las celdas visitadas por el raton:
     * Clave:Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    private final ArrayList<Grid> posiblesCaminos;                                                //Almacena las celdas que contienen bifurcaciones no visitadas.
    private final HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> adyacencias; //Almacena las adyacencias de las celdas visitadas.
    private final ArrayList<Integer> posiblesMovActuales;                                         //ArrayList auxiliar para almacenar los posibles movimientos que se realizaran.

    //DFS
    private final LinkedList<Integer> caminoDFS;
    private final LinkedList<Integer> caminoDFSVuelta;
    private boolean nuevoQueso = false;
    private boolean quesoVisitado = false;
    private boolean hayDFS = false;
    private boolean volviendo = false;

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                   MÉTODOS PRINCIPALES
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Constructor (Puedes modificar el nombre a tu gusto).
     */
    public M20A04aGRE() {
        super("BreakDFS;");
        celdasVisitadas = new HashMap<>();
        generador = new Random();
        posiblesCaminos = new ArrayList<>();
        posiblesMovActuales = new ArrayList<>();
        adyacencias = new HashMap<>();
        adyacencias.put(new Pair<>(0, 0), new ArrayList<>());
        caminoDFS = new LinkedList();
        caminoDFSVuelta = new LinkedList();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método principal para el movimiento del raton. Incluye la gestión
     * de cuando un queso aparece o no.
     * @param currentGrid Celda actual
     * @param cheese Queso
     */
    @Override
    public int move(final Grid currentGrid, final Cheese cheese) {

        if (nuevoQueso) {
            Pair<Integer, Integer> posicionQueso = new Pair<>(cheese.getX(), cheese.getY());
            quesoVisitado = celdasVisitadas.containsKey(posicionQueso);
            nuevoQueso = false;
        }

        debug();

        if (quesoVisitado) {
            if (!hayDFS) {
                if (!celdasVisitadas.containsKey(getPosicion(currentGrid))) {
                    addHashMap(currentGrid);
                }
                recorreDFS(currentGrid, new Grid(cheese.getX(), cheese.getY()), caminoDFS);
                if (!caminoDFS.isEmpty()) {
                    return caminoDFS.pollFirst();
                }

            } else {
                return caminoDFS.pollFirst();
            }
        } else {
            if (volviendo) {
                if (hayDFS) {
                    if (!caminoDFSVuelta.isEmpty()) {
                        return caminoDFSVuelta.pollFirst();
                    } else {
                        volviendo = false;
                        hayDFS = false;
                        return tomaDecision(currentGrid);
                    }
                }
            } else {
                return tomaDecision(currentGrid);
            }
        }
        return BOMB;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Devuelve la ruta a seguir desde la posicion hasta el destino
     *
     * @param posicion
     * @param destino
     * @param camino
     */
    public void recorreDFS(Grid posicion, Grid destino, LinkedList<Integer> camino) {
        Grid actual = new Grid(posicion.getX(), posicion.getY());
        HashSet<Pair<Integer, Integer>> visitadasDFS = new HashSet<>();
        while (!mismaPosicion(actual, destino)) {

            Pair<Integer, Integer> evaluando = getPosicion(actual);
            visitadasDFS.add(evaluando);
            ArrayList<Pair<Integer, Integer>> lista = adyacencias.get(evaluando);
            Iterator<Pair<Integer, Integer>> it = lista.listIterator();
            boolean sigue = true;
            while (it.hasNext() && sigue) {
                evaluando = it.next();
                if (celdasVisitadas.containsKey(evaluando) && !visitadasDFS.contains(evaluando)) {
                    sigue = false;
                }
            }
            if (sigue) {
                int mov = camino.pollLast();
                mov = contrario(mov);
                actual = getCelda(actual, mov);
            } else {
                Grid temp = new Grid(evaluando.getKey(), evaluando.getValue());
                camino.add(relativa(actual, temp));
                actual = temp;
            }
        }
        hayDFS = true;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método encargado de la toma de decisión en el movimiento del
     * ratón.
     * @param currentGrid
     * @return Movimiento decidido
     */
    public int tomaDecision(final Grid currentGrid) {
        posiblesMovActuales.clear();
        posiblesCaminos.remove(currentGrid);
        switch (movAnterior) {
            case UP:
                if (currentGrid.canGoUp() && !currentGrid.canGoRight() && !currentGrid.canGoLeft() && !visitada(currentGrid, UP)) {
                    addHashMap(currentGrid);
                    return UP;
                }
                break;
            case DOWN:
                if (currentGrid.canGoDown() && !currentGrid.canGoRight() && !currentGrid.canGoLeft() && !visitada(currentGrid, DOWN)) {
                    addHashMap(currentGrid);
                    return DOWN;
                }
                break;

            case LEFT:
                if (currentGrid.canGoLeft() && !currentGrid.canGoDown() && !currentGrid.canGoUp() && !visitada(currentGrid, LEFT)) {
                    addHashMap(currentGrid);
                    return LEFT;
                }
                break;

            case RIGHT:
                if (currentGrid.canGoRight() && !currentGrid.canGoDown() && !currentGrid.canGoUp() && !visitada(currentGrid, RIGHT)) {
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

        addHashMap(currentGrid);

        if (!posiblesMovActuales.isEmpty()) {
            movAnterior = posiblesMovActuales.get(generador.nextInt(posiblesMovActuales.size()));
        } else {
            volviendo = true;
            limpiezaPendientes();
            if (!posiblesCaminos.isEmpty()) {
                recorreDFS(currentGrid, masCercana(currentGrid), caminoDFSVuelta);
                if (!caminoDFSVuelta.isEmpty()) {
                    movAnterior = caminoDFSVuelta.pollFirst();
                } else {
                    movAnterior = BOMB;
                }
            } else {
                movAnterior = BOMB;
            }
        }

        return movAnterior;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Añade el Grid actual al Hashmap y al ArrayList posiblesCaminos los
     * posibles caminos
     * @param currentGrid Grid en el que se encuentra el ratón actualmente
     */
    public void addHashMap(final Grid currentGrid) {
        int numCaminos = 0;
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY()))) {
            celdasVisitadas.put(new Pair<>(currentGrid.getX(), currentGrid.getY()), currentGrid);
            adyacencias.put(new Pair<>(currentGrid.getX(), currentGrid.getY()), new ArrayList<>());
            incExploredGrids();
        }

        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1)) && currentGrid.canGoDown()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1)) && currentGrid.canGoUp()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() - 1, currentGrid.getY())) && currentGrid.canGoLeft()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(currentGrid.getX() + 1, currentGrid.getY())) && currentGrid.canGoRight()) {
            numCaminos++;
        }
        if (numCaminos > 0) {
            posiblesCaminos.add(currentGrid);
        }
        actualizaAdy(currentGrid);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Actualiza las adyascencias del grid actual
     * @param currentGrid Grid del que se actualizarán las adyascencias
     */
    public void actualizaAdy(Grid currentGrid) {

        if (currentGrid.canGoDown()) {
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX(), currentGrid.getY() - 1));
        }
        if (currentGrid.canGoUp()) {
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX(), currentGrid.getY() + 1));
        }
        if (currentGrid.canGoLeft()) {
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX() - 1, currentGrid.getY()));
        }
        if (currentGrid.canGoRight()) {
            adyacencias.get(new Pair<>(currentGrid.getX(), currentGrid.getY())).add(new Pair<>(currentGrid.getX() + 1, currentGrid.getY()));
        }
    }

    private void limpiezaPendientes() {
        for (int i = 0; i < posiblesCaminos.size(); i++) {
            if (i < posiblesCaminos.size() && actualizaPendientes(posiblesCaminos.get(i))) {
                posiblesCaminos.remove(posiblesCaminos.get(i));
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Determina si la celda que se pasa como parámetro tiene caminos
     * pendientes o no
     * @param celda Celda
     * @return True si esta completa, False si aún le quedan caminos pendientes
     */
    public boolean actualizaPendientes(Grid celda) {
        int numCaminos = 0;
        if (!celdasVisitadas.containsKey(new Pair<>(celda.getX(), celda.getY() - 1)) && celda.canGoDown()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(celda.getX(), celda.getY() + 1)) && celda.canGoUp()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(celda.getX() - 1, celda.getY())) && celda.canGoLeft()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair<>(celda.getX() + 1, celda.getY())) && celda.canGoRight()) {
            numCaminos++;
        }
        return numCaminos == 0;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método que se llama cuando aparece un nuevo queso
     */
    @Override
    public void newCheese() {
        caminoDFS.clear();
        caminoDFSVuelta.clear();
        nuevoQueso = true;
        quesoVisitado = false;
        hayDFS = false;
        volviendo = false;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método que se llama cuando el raton pisa una bomba
     */
    @Override
    public void respawned() {
        caminoDFS.clear();
        caminoDFSVuelta.clear();
        nuevoQueso = true;
        quesoVisitado = false;
        hayDFS = false;
        volviendo = true;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                    MÉTODOS AUXILIARES
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Función de Debug. Borrar al final.
     */
    private void debug() {
        System.out.println("\n-------------------------------------------------------------------");
        System.out.println(posiblesMovActuales.size() + " posibles movimientos");
        System.out.println(celdasVisitadas.size() + " celdas visitadas");
        System.out.println(posiblesCaminos.size() + " posibles caminos");
        System.out.println(adyacencias.size() + " adyascencias guardadas");
        System.out.println("nuevoQueso: " + nuevoQueso + " - quesoVisitado: " + quesoVisitado + " - hayDFS: " + hayDFS + " - volviendo: " + volviendo);
        System.out.println("POSIBLES: ");
        for (int i = 0; i < posiblesCaminos.size(); i++) {
            System.out.printf("%s, ", getPosicion(posiblesCaminos.get(i)));
        }
        System.out.println("\n");
        System.out.println("-------------------------------------------------------------------\n");
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Método encargado de devolver el movimiento necesario para ir desde
     * la celda actual a la celda destino.
     * @param actual Celda en la que se encuentra el ratón
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Devuelve si la celda a es igual a la celda b
     * @param a
     * @param b
     * @return resultado
     */
    private boolean mismaPosicion(Grid a, Grid b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Devuelve el grid mas cercano a actual
     *
     * @param actual
     * @return Grid
     */
    private Grid masCercana(Grid actual) {
        Grid aux = null;
        int distancia = 9999;
        for (Grid posibleCamino : posiblesCaminos) {
            if (distancia(actual, posibleCamino) < distancia) {
                aux = posibleCamino;
                distancia = distancia(actual, posibleCamino);
            }
        }
        return aux;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * @brief Calcula la distancia entre la celda a y la celda b
     * @param a Grid
     * @param b Grid
     * @return distancia entre las 2 celdas
     */
    public int distancia(Grid a, Grid b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
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
        final Pair<Integer, Integer> par = new Pair<>(x, y);
        return celdasVisitadas.containsKey(par);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Grid getArriba(Grid actual) {
        return new Grid(actual.getX(), actual.getY() + 1);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Grid getAbajo(Grid actual) {
        return new Grid(actual.getX(), actual.getY() - 1);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Grid getDerecha(Grid actual) {
        return new Grid(actual.getX() + 1, actual.getY());
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Grid getIzquierda(Grid actual) {
        return new Grid(actual.getX() - 1, actual.getY());
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Devuelve un pair con la posicion del Grid que se pasa como parametro
     *
     * @param celda Grid
     * @return Pair con la posicion X e Y
     */
    public Pair<Integer, Integer> posicionActual(Grid celda) {
        return new Pair(celda.getX(), celda.getY());
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Devuelve el movimiento contrario al movimiento que se pasa como parámetro
     *
     * @param movimiento
     * @return movimiento contrario
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Devuelve un pair con la posicion del Grid que se pasa como parametro
     *
     * @param celda Grid
     * @return Pair con la posicion X e Y
     */
    public Pair<Integer, Integer> getPosicion(Grid celda) {
        return new Pair(celda.getX(), celda.getY());
    }
}
