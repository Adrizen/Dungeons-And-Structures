package dungeonsstructures.Grafo;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class NodoAdy {
    private NodoVert vertice;
    private NodoAdy sigAdyacente;
    private int etiqueta;
    
    public NodoAdy(NodoVert vert, NodoAdy adyac, int etiq){
        this.sigAdyacente = adyac;
        this.vertice = vert;
        this.etiqueta = etiq;
    }
    
    public NodoVert getVertice(){
        return vertice;
    }
    
    public NodoAdy getSigAdyacente(){
        return sigAdyacente;
    }
    
    public int getEtiqueta(){
        return etiqueta;
    }
    
    public void setSiguienteAdy(NodoAdy siguiente){
        this.sigAdyacente = siguiente;
    }
    
}
