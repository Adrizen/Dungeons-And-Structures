package dungeonsstructures.Grafo;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class NodoVert {

    private Object elemento;
    private NodoVert sigVertice;
    private NodoAdy primerAdy;

    public NodoVert(Object elem, NodoVert sigV) {
        this.elemento = elem;
        this.sigVertice = sigV;
        primerAdy = null;
    }

    public NodoAdy getPrimerAdy() {
        return primerAdy;
    }

    public Object getElem() {
        return this.elemento;
    }

    public NodoVert getSigVertice() {
        return this.sigVertice;
    }

    public void setPrimerAdy(NodoAdy nuevoAdy) {
        this.primerAdy = nuevoAdy;
    }

    public void setSigVertice(NodoVert siguiente) {
        this.sigVertice = siguiente;
    }

}
