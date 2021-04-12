package dungeonsstructures.ColaPrioridad;

import dungeonsstructures.Nodo;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Cola {

    private Nodo frente;
    private Nodo fin;

    public Cola() {
        this.frente = null;
        this.fin = null;
    }

    // Pone un objeto en la cola.
    public boolean poner(Object elem) {
        Nodo nuevoNodo;
        nuevoNodo = new Nodo(elem, null);
        if (this.fin == null) {
            this.fin = nuevoNodo;
            this.frente = nuevoNodo;
        } else {
            this.fin.setEnlace(nuevoNodo);
            this.fin = this.fin.getEnlace();
        }
        return true;    // Siempre se pueden colocar elementos.
    }

    // Saca un objeto de la cola.
    public boolean sacar() {
        boolean exito = true;
        if (this.frente == null) {
            exito = false;  // La Cola está vacia y no se puede quitar nada.
        } else {
            this.frente = this.frente.getEnlace(); // Se quita el primer elemento cambiando la referencia de 'frente'.
            if (this.frente == null) {             // Si el frente es nulo no hay más elementos.
                this.fin = null;
            }
        }
        return exito;
    }

    // Devuelve el primer elemento de la Cola.
    public Object obtenerFrente() {
        Object elem;
        if (this.frente == null) {  // Si el frente es nulo, entonces no hay elementos en la Cola.
            elem = null;
        } else {
            elem = this.frente.getElemento();
        }
        return elem;
    }

    // Si la cola está vacia devuelve true, si no devuelve false.
    public boolean esVacia() {
        boolean esVacia;
        esVacia = (this.frente == null);
        return esVacia;
    }

    // Vacia la cola.
    public void vaciar() {
        this.frente = null;
        this.fin = null;
    }

    // Devuelve una copia de la cola.
    public Cola clone() {
        Cola clonCola = new Cola();
        Nodo aux1 = this.frente;
        if (this.frente != null) {      // Solo clono si existen elementos en la Cola original.
            clonCola.frente = new Nodo(aux1.getElemento(), null);
            aux1 = aux1.getEnlace();
            Nodo aux2 = clonCola.frente;
            while (aux1 != null) {      // Mientras el puntero de la Cola original sea distinto a null, hay elementos.
                aux2.setEnlace(new Nodo(aux1.getElemento(), null));
                aux2 = aux2.getEnlace(); //El puntero que apunta a la Cola clon avanza.
                aux1 = aux1.getEnlace(); //El puntero que apunta a la Cola original avanza.
            }
            clonCola.fin = aux2;
        }
        return clonCola;
    }

    public String toString() {
        String cadenita = "";
        Nodo aux1 = this.frente;
        if (this.frente == null) {
            cadenita = "Cola vacia";
        } else {
            while (aux1 != null) {
                cadenita = cadenita + aux1.getElemento() + ", ";
                aux1 = aux1.getEnlace();
            }
        }
        return cadenita;
    }

}
