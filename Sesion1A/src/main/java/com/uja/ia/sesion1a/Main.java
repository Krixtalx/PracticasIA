/*
 * Copyright (C) 2020 Niskp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.uja.ia.sesion1a;



/**
 *
 * @author Niskp
 */

import java.io.*;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Alumno_IA test = new Alumno_IA(0, 0, "77690720D", "Jose Antonio", "jaca0011@red.ujaen.es");
        // test.calcularNota();
        BufferedReader archivoEntrada;
        FileWriter fichero=null;
        PrintWriter archivoSalida;
        try {
            fichero = new FileWriter("pares.txt", true);
            archivoEntrada = new BufferedReader(new FileReader("datos.txt"));
            archivoSalida = new PrintWriter(fichero);
            String linea;
            linea = archivoEntrada.readLine();
            while (linea != null) {
                String[] campos = linea.split(",");
                if (Character.getNumericValue(campos[1].charAt(3)) % 2 == 0) {
                    archivoSalida.println(linea);
                    System.out.println(linea);
                }
                linea = archivoEntrada.readLine();
            }

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException ex) {
                   ex.printStackTrace();
            }
        }
    }

}

