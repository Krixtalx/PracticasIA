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

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Niskp
 */
public class Alumno_IA extends Alumno {

    private int grupoPracticas;
    private float notaPracticas;

    public Alumno_IA() {
        super();
    }

    public Alumno_IA(int grupoPracticas, float notaPracticas) {
        super();
        this.grupoPracticas = grupoPracticas;
        this.notaPracticas = notaPracticas;
    }

    public Alumno_IA(int grupoPracticas, float notaPracticas, String dni, String nombre, String email) {
        super(dni, nombre, email);
        this.grupoPracticas = grupoPracticas;
        this.notaPracticas = notaPracticas;
    }

    public void calcularNota() {
        int aux = 0, aux2=0;
        Scanner reader = new Scanner(System.in);
        for (int i = 0; i < 4; i++) {
            System.out.print("Introduzca la nota " + i + ": ");
            aux2=reader.nextInt();
            aux += aux2;
        }
        notaPracticas = aux / 4;
        System.out.println("\nNota de Prácticas del alumno " + nombre + ": " + notaPracticas);

    }

}
