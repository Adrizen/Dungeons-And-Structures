package dungeonsstructures;

import dungeonsstructures.Lista.Lista;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Jugador {

    private String nombre;
    private String tipo;
    private int dinero;
    private Lista items;
    private int salud; // La salud maxima es 100.
    private String categoria;
    private int vecesDerrotado;
    private int batallasGanadas;
    private Equipo equipo; // Una vez creado el jugador, se lo ingresa a una cola y ahí se asigna el equipo.

    public Jugador(String nombre, String tipo, String categoria, Lista items) {
        this.nombre = nombre;
        this.tipo = tipo.toUpperCase();
        this.dinero = 0;
        this.items = items;
        this.salud = 100;
        this.categoria = categoria.toUpperCase();
        this.vecesDerrotado = 0;
        this.batallasGanadas = 0;
        this.equipo = null;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public Lista getItems() {
        return items;
    }

    public int getSalud() {
        return salud;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getDinero() {
        return this.dinero;
    }

    public int getVecesDerrotado() {
        return this.vecesDerrotado;
    }

    public int getBatallasGanadas() {
        return this.batallasGanadas;
    }

    public void setSalud(int s) {
        this.salud = s;
    }

    public void setEquipo(Equipo e) {
        this.equipo = e;
    }

    public void setDinero(int d) {
        this.dinero = d;
    }

    public void setCategoria(String c) {
        this.categoria = c;
    }

    public void setTipo(String t) {
        this.tipo = t;
    }

    public void setVecesDerrotado(int v) {
        this.vecesDerrotado = v;
    }

    public void setBatallasGanadas(int b) {
        this.batallasGanadas = b;
    }
    
    // Compara las victorias de dos jugadores. Si quien invoca el método tiene más victorias devuelve 1, si tiene menos devuelve -1 y si son iguales devuelve 0.
    public int compararVictorias(Jugador j){
        int rta;
        if (this.batallasGanadas == j.getBatallasGanadas()){
            rta = 0;
        } else {
            if (this.batallasGanadas > j.getBatallasGanadas()){
                rta = 1;
            } else {
                rta = -1;
            }
        }
        return rta;
    }
    
    @Override
    public String toString() {
        String salida = "Nombre: " + nombre + " - Tipo: " + tipo + " - Categoria: " + categoria + " - Victorias: " + batallasGanadas + " - Equipo: ";
        if (equipo != null) {
            salida = salida + equipo.getNombre();
        } else {
            salida = salida + "No tiene";
        }
        return salida;
    }
}
