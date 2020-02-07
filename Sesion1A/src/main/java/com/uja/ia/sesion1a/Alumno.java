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
public class Alumno {
	protected final String nombre;
	protected final String dni;
	protected final String correoE;

	public Alumno() {
		nombre = "";
		dni = "";
		correoE = "";
	}	
	
	public Alumno(String nombre, String dni, String correoE) {
		this.nombre = nombre;
		this.dni = dni;
		this.correoE = correoE;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDni() {
		return dni;
	}

	public String getCorreoE() {
		return correoE;
	}

	@Override
	public String toString() {
		return "Alumno{" + "nombre=" + nombre + ", dni=" + dni + ", correoE=" + correoE + '}';
	}
	
	
}
