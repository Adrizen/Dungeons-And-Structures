
package dungeonsstructures.ArbolAVL;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class NodoAVL {
    private Comparable clave;
    private Object objeto;
    private int altura;
    private NodoAVL izquierdo;
    private NodoAVL derecho;
    
    public NodoAVL(Comparable c, Object o, NodoAVL i, NodoAVL d){
        clave = c;
        objeto = o;
        izquierdo = i;
        derecho = d;
        altura = 0;
    }
    
    public Comparable getClave(){
        return clave;
    }
    
    public Object getObjeto(){
        return objeto;
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
        //System.out.println(elem + " Altura izquierdo: " +altIzq +", " + "Altura derecho: " +altDer);
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
