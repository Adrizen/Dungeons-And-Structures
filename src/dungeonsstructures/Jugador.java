package dungeonsstructures;

import dungeonsstructures.Lista.Lista;

public class Jugador {

    private String nombre;
    private String tipo;
    private int dinero; 
    private Lista items;
    private int salud; // La salud maxima es 100.
    private String categoria;
    private int vecesDerrotado;
    private int batallasGanadas;
    private Equipo equipo; // No se puede crear un jugador si no tiene un equipo.

    public Jugador(String nombre, String tipo, String categoria, Equipo equipo) {
        this.nombre = nombre.toUpperCase();
        this.tipo = tipo.toUpperCase();
        this.dinero = 0;
        this.items = new Lista();
        this.salud = 100;
        this.categoria = categoria.toUpperCase();
        this.vecesDerrotado = 0;
        this.batallasGanadas = 0;
        this.equipo = equipo;
    }

    @Override
    public String toString(){
        return ("Nombre: " + nombre + " - Tipo: " + tipo + " - Categoria: " + categoria + " - Equipo: " + equipo);
    }
}
