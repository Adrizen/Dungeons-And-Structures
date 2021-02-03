
package dungeonsstructures.ArbolAVL;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class NodoAVL {
    private Comparable elem;
    private int altura;
    private NodoAVL izquierdo;
    private NodoAVL derecho;
    
    public NodoAVL(Comparable e, NodoAVL i, NodoAVL d){
        elem = e;
        izquierdo = i;
        derecho = d;
        altura = 0;
    }
    
    public Comparable getElem(){
        return elem;
    }
    
    public void setElem(Comparable e){
        this.elem = e;
    }
    
    public int getAltura(){
        return altura;
    }
    
    public void recalcularAltura() {
        int altIzq = -1, altDer = -1;
        if (this.izquierdo != null) {
            altIzq = this.izquierdo.altura;
        }
        if (this.derecho != null) {
            altDer = this.derecho.altura;
        }
        this.altura = (Math.max(altIzq, altDer) + 1);
    }

    public NodoAVL getIzquierdo(){
        return izquierdo;
    }
    
    public void setIzquierdo(NodoAVL i){
        izquierdo = i;
    }
    
    public NodoAVL getDerecho(){
        return derecho;
    }
    
    public void setDerecho(NodoAVL d){
        derecho = d;
    }
}
