package dungeonsstructures.ArbolAVL;

import dungeonsstructures.Lista.*;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class ArbolAVL {

    private NodoAVL raiz;

    public ArbolAVL() {
        raiz = null;
    }

    public boolean insertar(Comparable elem) {
        boolean exito = false;
        if (this.raiz == null) {  // El árbol estaba vacio.
            this.raiz = new NodoAVL(elem, null, null);
            exito = true;
        } else {            // El árbol no estaba vacio.
            exito = insertarAux(this.raiz, null, elem, this.raiz.getAltura());
        }
        return exito;
    }

    // Método auxiliar que es utilizado para llamar recursivamente a los nodos correspondientes (izquierdo si es menor, derecha si es mayor).
    private boolean insertarAux(NodoAVL nodo, NodoAVL padreNodo, Comparable elem, int altura) {
        int balance;
        boolean exito = false;
        if (elem.compareTo(nodo.getElem()) < 0) {
            if (nodo.getIzquierdo() == null) {
                NodoAVL nuevo = new NodoAVL(elem, null, null);
                nodo.setIzquierdo(nuevo);
                exito = true;
            } else {
                exito = insertarAux(nodo.getIzquierdo(), nodo, elem, altura-1);
            }
        } else {
            if (elem.compareTo(nodo.getElem()) > 0) {
                if (nodo.getDerecho() == null) {
                    NodoAVL nuevo = new NodoAVL(elem, null, null);
                    nodo.setDerecho(nuevo);
                    exito = true;
                } else {
                    exito = insertarAux(nodo.getDerecho(), nodo, elem, altura-1);
                }
            }
        }
        if (exito) {    // Si se pudo insertar un nuevo nodo (exito = true) debo checkear la altura y el balance de cada nodo a la vuelta de la recursión (y rotar si es necesario).
            nodo.recalcularAltura();
            balance = calcularBalance(nodo);
            if (balance > 1) {  // 'nodo' está inclinado a la izquierda.
                int balanceHijo = calcularBalance(nodo.getIzquierdo());
                if (balanceHijo >= 0) { // El hijo de 'nodo' está inclinado para el mismo lado que su padre.
                    System.out.println("rotDer");
                    NodoAVL h = rotarDerecha(nodo);
                    cambiarRaiz(nodo,padreNodo,h,true);
                    nodo.recalcularAltura();    // Luego de rotar debo volver a calcular la altura del nodo.
                } else {    // El hijo de 'nodo' está inclinado para el lado contrario que su padre. Rotación doble Izq-Der.
                    System.out.println("rotIzq-Der");
                    NodoAVL h = rotarIzquierda(nodo.getIzquierdo());
                    nodo.setIzquierdo(h);
                    nodo.getIzquierdo().recalcularAltura();
                    NodoAVL h2 = rotarDerecha(nodo);
                    nodo.recalcularAltura();
                    //cambiarRaiz(nodo,h2);
                    padreNodo.setIzquierdo(h2);
                }
            } else {
                if (balance < -1) { // 'nodo' está inclinado a la derecha.
                    int balanceHijo = calcularBalance(nodo.getDerecho());
                    if (balanceHijo <= 0) { // El hijo de 'nodo' está inclinado para el mismo lado que su padre.
                        System.out.println("rotIzq");
                        NodoAVL h = rotarIzquierda(nodo);
                        cambiarRaiz(nodo,padreNodo,h,true);
                        nodo.recalcularAltura();
                    } else {    // El hijo de 'nodo' está inclinado para el lado contrario que su padre. Rotación doble Der-Izq.
                        System.out.println("rotDer-Izq");
                        NodoAVL h = rotarDerecha(nodo.getDerecho());
                        nodo.setDerecho(h);
                        nodo.getDerecho().recalcularAltura();
                        NodoAVL h2 = rotarIzquierda(nodo);
                        nodo.recalcularAltura();
                        //cambiarRaiz(nodo,h2);
                        padreNodo.setDerecho(h2);
                    }
                }
            }
        }
        return exito;
    }

    // Método auxiliar que, dado un nodo, calcula el balance utilizando la altura de sus hijos.
    private int calcularBalance(NodoAVL nodo) {
        int alturaIzq = -1, alturaDer = -1;
        if (nodo.getIzquierdo() != null) {
            alturaIzq = nodo.getIzquierdo().getAltura();
        }
        if (nodo.getDerecho() != null) {
            alturaDer = nodo.getDerecho().getAltura();
        }
        return (alturaIzq - alturaDer);
    }
    
    // Se rota a derecha cuando el árbol está caido a la izquierda.
    private NodoAVL rotarDerecha(NodoAVL nodo){
        NodoAVL h, temp;
        h = nodo.getIzquierdo();
        temp = h.getDerecho();
        h.setDerecho(nodo);
        nodo.setIzquierdo(temp);
        return h;   // Devuelvo el nodo que va a subir.
    }
    
    // Se rota a la izquierda cuando el árbol está caido a la derecha.
    private NodoAVL rotarIzquierda(NodoAVL nodo){
        NodoAVL h, temp;
        h = nodo.getDerecho();
        temp = h.getIzquierdo();
        h.setIzquierdo(nodo);
        nodo.setDerecho(temp);
        return h;
    }

    // Luego de una rotación (Izq o Der) puede que tenga que cambiar la raíz del árbol.
    private void cambiarRaiz(NodoAVL nodo, NodoAVL padreNodo, NodoAVL h, boolean simple) {
        if (nodo == this.raiz) { // El nodo a balancear (o sea, "bajar") es la raiz.
            this.raiz = h;
        } else {
            if (simple){    // Es inutil preguntar si es 'simple'?. No voy a llamar a cambiarRaiz a half-way una rotación doble. Pero quiza en una situación explota.
                if (padreNodo.getDerecho().getElem() == nodo.getElem()){
                    padreNodo.setDerecho(h);
                } else {
                    padreNodo.setIzquierdo(h);
                }
            }
        }
    }

    public boolean eliminar(Comparable elem) {

    }

    public boolean pertenece(Comparable elemento) {
        return auxPertenece(elemento, this.raiz);
    }

    private boolean auxPertenece(Comparable elemento, NodoAVL nodo) {
        boolean exito = false;
        if (nodo != null) {
            if (elemento.compareTo(nodo.getElem()) == 0) {
                exito = true;
            } else {
                if (elemento.compareTo(nodo.getElem()) < 0) {
                    exito = auxPertenece(elemento, nodo.getIzquierdo());
                }
                if (elemento.compareTo(nodo.getElem()) > 0) {
                    exito = auxPertenece(elemento, nodo.getDerecho());
                }
            }
        }
        return exito;
    }

    public Lista listar() {
        Lista lista = new Lista();
        auxListar(lista, this.raiz);
        return lista;
    }

    private void auxListar(Lista lista, NodoAVL nodo) {
        if (nodo != null) {
            auxListar(lista, nodo.getDerecho());
            lista.insertar(nodo.getElem(), 1);
            auxListar(lista, nodo.getIzquierdo());
        }
    }

    public Lista listarRango(Comparable min, Comparable max) {
        Lista lista = new Lista();
        if (min == max) {
            lista.insertar(min, 1);
        } else {
            auxListarRango(min, max, this.raiz, lista);
        }
        return lista;
    }

    private void auxListarRango(Comparable min, Comparable max, NodoAVL nodo, Lista lista) {
        if (nodo != null) {
            if (nodo.getElem().compareTo(min) >= 0 && nodo.getElem().compareTo(max) <= 0) {
                auxListarRango(min, max, nodo.getIzquierdo(), lista);
                lista.insertar(nodo.getElem(), lista.longitud() + 1);
                auxListarRango(min, max, nodo.getDerecho(), lista);
            } else {
                if (nodo.getIzquierdo() != null) {
                    if (nodo.getIzquierdo().getElem().compareTo(min) > 0) {
                        auxListarRango(min, max, nodo.getIzquierdo(), lista);
                    }
                }
                if (nodo.getDerecho() != null) {
                    if (nodo.getDerecho().getElem().compareTo(max) < 0) {
                        auxListarRango(min, max, nodo.getDerecho(), lista);
                    }
                }
            }
        }
    }

    public Comparable minimoElem() {
        if (this.raiz != null) {
            NodoAVL aux = this.raiz;
            NodoAVL aux2 = aux;
            while (aux != null) {
                aux2 = aux;
                aux = aux.getIzquierdo();
            }
            return aux2.getElem();
        }
        return null;
    }

    public Comparable maximoElem() {
        if (this.raiz != null) {
            NodoAVL aux = this.raiz;
            NodoAVL aux2 = aux;
            while (aux != null) {
                aux2 = aux;
                aux = aux.getDerecho();
            }
            return aux2.getElem();
        }
        return null;
    }

    public boolean esVacio() {
        return (this.raiz == null);
    }

    public void vaciar() {
        this.raiz = null;
    }

    public String toString() {
        return (auxToString(this.raiz));
    }

    private String auxToString(NodoAVL nodo) {
        String cadenita = "";
        if (nodo != null) {
            cadenita += nodo.getElem() + " ";
            if (nodo.getIzquierdo() != null) {
                cadenita += "HI: " + nodo.getIzquierdo().getElem() + " ";
            } else {
                cadenita += "HI: - ";
            }
            if (nodo.getDerecho() != null) {
                cadenita += "HD: " + nodo.getDerecho().getElem() + ".\n";
            } else {
                cadenita += "HD: - .\n";
            }
            cadenita += auxToString(nodo.getIzquierdo());
            cadenita += auxToString(nodo.getDerecho());
        }
        return cadenita;
    }

}
