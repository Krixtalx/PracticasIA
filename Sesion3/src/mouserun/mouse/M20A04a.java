/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    boolean stuck = false;

    /**
     * Tabla hash para almacenar las celdas visitadas por el raton: Clave:
     * Coordenadas Valor: La celda
     */
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    private final ArrayList<Grid> posiblesCaminos;

    /**
     * Pila para almacenar el camino recorrido.
     */
    private final Stack<Grid> pilaMovimientos;

    /**
     * Constructor (Puedes modificar el nombre a tu gusto).
     */
    public M20A04a() {
        super("MXXA04");
        celdasVisitadas = new HashMap<>();
        pilaMovimientos = new Stack<>();
        generador = new Random();
        posiblesCaminos = new ArrayList<>();
    }

    /**
     * @brief Método principal para el movimiento del raton. Incluye la gestión
     * de cuando un queso aparece o no.
     * @param currentGrid Celda actual
     * @param cheese Queso
     */
    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        if (currentGrid.canGoUp() && testGrid(Mouse.UP, currentGrid) && !stuck) {
            lastGrid = currentGrid;
            pilaMovimientos.push(lastGrid);
            addHashMap(currentGrid);
            return Mouse.UP;
        } else if (currentGrid.canGoRight() && testGrid(Mouse.RIGHT, currentGrid) && !stuck) {
            lastGrid = currentGrid;
            pilaMovimientos.push(lastGrid);
            addHashMap(currentGrid);
            return Mouse.RIGHT;
        } else if (currentGrid.canGoLeft() && testGrid(Mouse.LEFT, currentGrid) && !stuck) {
            lastGrid = currentGrid;
            pilaMovimientos.push(lastGrid);
            addHashMap(currentGrid);
            return Mouse.LEFT;
        } else if (currentGrid.canGoDown() && testGrid(Mouse.DOWN, currentGrid) && !stuck) {
            lastGrid = currentGrid;
            pilaMovimientos.push(lastGrid);
            addHashMap(currentGrid);
            return Mouse.DOWN;
        } else if (stuck) {
            if (posiblesCaminos.contains(currentGrid)) {
                stuck = false;
            } else {
                int aux = volverAnterior(currentGrid);
                if (aux != -1) {
                    return aux;
                } else {
                    stuck = false;
                }
            }
        }
        if (!posiblesCaminos.contains(currentGrid)) {
            stuck = true;
        }
        return -1;
    }
    
    public int volverAnterior(Grid currentGrid) {
        if (!pilaMovimientos.empty()) {
            if (actualAbajo(currentGrid, pilaMovimientos.lastElement())) {
                pilaMovimientos.pop();
                return Mouse.UP;
            }
            if (actualArriba(currentGrid, pilaMovimientos.lastElement())) {
                pilaMovimientos.pop();
                return Mouse.DOWN;
            }
            if (actualDerecha(currentGrid, pilaMovimientos.lastElement())) {
                pilaMovimientos.pop();
                return Mouse.LEFT;
            }
            if (actualIzquierda(currentGrid, pilaMovimientos.lastElement())) {
                pilaMovimientos.pop();
                return Mouse.RIGHT;
            }
        }
        return -1;
    }
    
    public void addHashMap(Grid currentGrid) {
        celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
        if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX(), currentGrid.getY() - 1)) && currentGrid.canGoDown()) {
            posiblesCaminos.add(currentGrid);
            
        } else if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX(), currentGrid.getY() + 1)) && currentGrid.canGoUp()) {
            posiblesCaminos.add(currentGrid);
            
        } else if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX() - 1, currentGrid.getY())) && currentGrid.canGoLeft()) {
            posiblesCaminos.add(currentGrid);
            
        } else if (!celdasVisitadas.containsKey(new Pair(currentGrid.getX() + 1, currentGrid.getY())) && currentGrid.canGoRight()) {
            posiblesCaminos.add(currentGrid);
            
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
            case Mouse.UP:
                y += 1;
                break;
            
            case Mouse.DOWN:
                y -= 1;
                break;
            
            case Mouse.LEFT:
                x -= 1;
                break;
            
            case Mouse.RIGHT:
                x += 1;
                break;
        }
        
        return !(lastGrid.getX() == x && lastGrid.getY() == y);
        
    }

    /**
     * @brief Método que devuelve si de una casilla dada, está contenida en el
     * mapa de celdasVisitadas
     * @param casilla Casilla que se pasa para saber si ha sido visitada
     * @return True Si esa casilla ya la había visitado
     */
    public boolean visitada(Grid casilla) {
        Pair par = new Pair(casilla.getX(), casilla.getY());
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
