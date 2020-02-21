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

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcfp0003
 */
public class Main {

    private static final int NUM_NOTAS = 4;

    public static Alumno nuevoAlumno() {
        Scanner sc = new Scanner(System.in);
        String nombre, dni, correoE;
        System.out.println("Nombre: ");
        nombre = sc.nextLine();
        System.out.println("DNI: ");
        dni = sc.nextLine();
        System.out.println("CorreoE: ");
        correoE = sc.nextLine();
        return new Alumno(nombre, dni, correoE);
    }

    public static void verAlumno(Alumno al) {
        System.out.println(al);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (BufferedReader lector = new BufferedReader(new FileReader("datos.txt")); FileWriter archivoPares = new FileWriter("pares.txt")) {
            String linea = lector.readLine();
            while (linea != null) {
                if (linea.length() > 0) {
                    //System.out.println("Elemento");
                    if ((Integer.parseInt(linea.split(",")[1].replace(" ", "")) % 2) == 0) {
                        archivoPares.write(linea);
                        archivoPares.write('\n');
                    }
                    linea = lector.readLine();
                }
            }
            lector.close();
            archivoPares.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-2);
        }
    }

}
