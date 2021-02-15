
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
    
    public String toString(){
        return ("Categoría: " + categoria + ", Localización: " + localizacion + ", Jugadores:" + "\n" + " " + jugadores.toString());
    }
    
    
}
