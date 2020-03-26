package mouserun.mouse;

import java.util.ArrayList;
import java.util.HashMap;
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
public class M20A04a extends Mouse {

    /**
     * Variable para almacenar la ultima celda visitada
     */
    private Grid lastGrid;
    Random generador;
    
    /**
     * Variable para guardar el anterior movimiento realizado
     */
    private int movAnterior;

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
    private final ArrayList<Integer> posiblesMovActuales; //ArrayList auxiliar para almacenar los posibles movimientos que se realizaran.

    /**
     * Pila para almacenar el camino recorrido.
     */
    private final Stack<Grid> pilaMovimientos;

    /**
     * Constructor (Puedes modificar el nombre a tu gusto).
     */
    public M20A04a() {
        super("Break;");
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        generador = new Random();
        posiblesCaminos = new HashMap<>();
        posiblesMovActuales = new ArrayList<>();
    }

    /**
     * @brief Método principal para el movimiento del raton. Incluye la gestión
     * de cuando un queso aparece o no.
     * @param currentGrid Celda actual
     * @param cheese Queso
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {

        return tomaDecision(currentGrid);
    }

    public int tomaDecision(Grid currentGrid) {
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
        posiblesCaminos.remove(new Pair(currentGrid.getX(), currentGrid.getY()));
        addHashMap(currentGrid);
        return movAnterior;
    }

    /**
     * @brief Vuelve a la posicion que indique la pilaMovimientos. Si se
     * encuentra en un Grid marcado como posible camino coge un nuevo camino no
     * visitado para explorar.
     * @param currentGrid Grid actual
     * @return Movimiento a tomar
     */
    public int volverAnterior(Grid currentGrid) {

        if (posiblesCaminos.containsKey(new Pair(currentGrid.getX(), currentGrid.getY()))) {
            stuck = false;
            posiblesCaminos.remove(new Pair(currentGrid.getX(), currentGrid.getY()));
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
        }

        stuck = false;
        return BOMB;
    }

    /**
     * @brief Añade el Grid actual al Hashmap y al ArrayList posiblesCaminos los
     * posibles caminos
     * @param currentGrid Grid en el que se encuentra el ratón actualmente
     */
    public void addHashMap(Grid currentGrid) {
        int numCaminos = 0;
        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX(), currentGrid.getY()))) {
            celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
        }

        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX(), currentGrid.getY() - 1)) && currentGrid.canGoDown()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX(), currentGrid.getY() + 1)) && currentGrid.canGoUp()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX() - 1, currentGrid.getY())) && currentGrid.canGoLeft()) {
            numCaminos++;
        }
        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX() + 1, currentGrid.getY())) && currentGrid.canGoRight()) {
            numCaminos++;
        }
        if (numCaminos > 1) {
            posiblesCaminos.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
        }
    }

    /**
     * @brief Método que se llama cuando aparece un nuevo queso
     */
    @Override
    public void newCheese() {

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
    public boolean testGrid(int direction, Grid currentGrid) {
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
    public boolean visitada(Grid casilla, int direccion) {
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
        Pair par = new Pair(x, y);
        return celdasVisitadas.containsKey(par);
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es mayor que la de la anterior
     */
    public boolean actualArriba(Grid actual, Grid anterior) {
        return actual.getY() > anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición Y de la actual es menor que la de la anterior
     */
    public boolean actualAbajo(Grid actual, Grid anterior) {
        return actual.getY() < anterior.getY();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es mayor que la de la anterior
     */
    public boolean actualDerecha(Grid actual, Grid anterior) {
        return actual.getX() > anterior.getX();
    }

    /**
     * @brief Método para calcular si una casilla está en una posición relativa
     * respecto a otra
     * @param actual Celda actual
     * @param anterior Celda anterior
     * @return True Si la posición X de la actual es menor que la de la anterior
     */
    public boolean actualIzquierda(Grid actual, Grid anterior) {
        return actual.getX() < anterior.getX();
    }

}
