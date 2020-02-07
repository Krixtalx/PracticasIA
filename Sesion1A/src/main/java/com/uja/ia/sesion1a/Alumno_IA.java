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

import java.util.Scanner;

/**
 *
 * @author Niskp
 */
public class Alumno_IA extends Alumno {

	private final int grupoPracticas;
	private final int notaPracticas;

	public Alumno_IA() {
		super();
		grupoPracticas = 0;
		notaPracticas = 0;
	}

	public Alumno_IA(String nombre, String dni, String correoE, int grupo, int nota) {
		super(nombre, dni, correoE);
		grupoPracticas = grupo;
		notaPracticas = nota;
	}

	public int getGrupoPracticas() {
		return grupoPracticas;
	}

	public int getNotaPracticas() {
		return notaPracticas;
	}

	public float calcularNota(int numNotas) {
		Scanner sc = new Scanner(System.in);
		float sumaNotas = 0;
		for (int i = 0; i < numNotas; i++) {
			System.out.println("Nota " + (i + 1));
			String nota = sc.nextLine();
			if (nota.contains(",")) {
				nota = nota.replace(',', '.');
			}
			sumaNotas += Float.parseFloat(nota);
		}
		return sumaNotas / numNotas;
	}
}