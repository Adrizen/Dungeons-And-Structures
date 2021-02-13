
package dungeonsstructures;


public class Item implements Comparable <Item>{
    private String codigo;
    private String nombre;
    private int precio;
    private int puntosAtaque;
    private int puntosDefensa;
    private int copias;
    
    
    public Item(String codigo, String nombre,int precio, int puntosAtaque,int puntosDefensa,int copias){
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.puntosAtaque = puntosAtaque;
        this.puntosDefensa = puntosDefensa;
        this.copias = copias;
    }
    
    @Override
    public int compareTo(Item i){
        return (this.codigo.compareTo(i.getCodigo()));
    }
    
    public String getCodigo(){
        return this.codigo;
    }
    
    public int getPrecio(){
        return precio;
    }
    
    public String toString(){
        return ("Código: " + codigo + " - Nombre: " + nombre + " - Precio: " + precio + " - Puntos Ataque: " + puntosAtaque + " - Punto Defensa: " + puntosDefensa + " - Copias: " + copias);
    }
    
    
    
    
    
    
    
    
}
