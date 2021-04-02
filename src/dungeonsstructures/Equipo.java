
package dungeonsstructures;

import dungeonsstructures.Lista.Lista;


public class Equipo {
    private String nombre;
    private String categoria;
    private String localizacion;
    private Lista jugadores;
    
    public Equipo(String n, String c, String l, Lista j){
        this.nombre = n;
        this.categoria = c;
        this.localizacion = l;
        this.jugadores = j;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public String getCategoria(){
        return this.categoria;
    }
    
    public String getLocalizacion(){
        return this.localizacion;
    }
    
    public Lista getJugadores() {
        return jugadores;
    }
    
    public void setCategoria(String c){
        this.categoria = c;
    }

    public String toString(){
        return ("Categoría: " + categoria + ", Localización: " + localizacion + ", Jugadores:" + "\n" + " " + jugadores.toString());
    }
    
    
}
