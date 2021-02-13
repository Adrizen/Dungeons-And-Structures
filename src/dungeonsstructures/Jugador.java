package dungeonsstructures;

import dungeonsstructures.Lista.Lista;

public class Jugador implements Comparable <Jugador>{

    private String nombre;
    private String tipo;
    private int dinero; 
    private Lista items;
    private int salud; // La salud maxima es 100.
    private String categoria;
    private int vecesDerrotado;
    private int batallasGanadas;
    private Equipo equipo; // Una vez creado el jugador, se lo ingresa a una cola y ahí se asigna el equipo.

    public Jugador(String nombre, String tipo, String categoria) {
        this.nombre = nombre.toUpperCase();
        this.tipo = tipo.toUpperCase();
        this.dinero = 0;
        this.items = new Lista();
        this.salud = 100;
        this.categoria = categoria.toUpperCase();
        this.vecesDerrotado = 0;
        this.batallasGanadas = 0;
        this.equipo = null;
    }
    
    @Override
    public int compareTo(Jugador j){
        return (this.nombre.compareTo(j.getNombre()));
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void setEquipo(Equipo e){
        this.equipo = e;
    }
    
    public int getVecesDerrotado(){
        return this.vecesDerrotado;
    }
    
    public int getBatallasGanadas(){
        return this.batallasGanadas;
    }

    @Override
    public String toString(){
        return ("Nombre: " + nombre + " - Tipo: " + tipo + " - Categoria: " + categoria + " - Equipo: " + equipo);
    }
}
