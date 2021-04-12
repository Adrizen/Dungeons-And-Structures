package dungeonsstructures;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Nodo {

    private Object elemento;
    private Nodo enlace;

    //Constructor.
    public Nodo(Object ele, Nodo enl) {
        this.elemento = ele;
        this.enlace = enl;
    }

    //Observador.
    public Object getElemento() {
        return this.elemento;
    }

    public Nodo getEnlace() {
        return this.enlace;
    }

    //Modificador.
    public void setEnlace(Nodo n) {
        this.enlace = n;
    }

    public void setElemento(Object o) {
        this.elemento = o;
    }
}
