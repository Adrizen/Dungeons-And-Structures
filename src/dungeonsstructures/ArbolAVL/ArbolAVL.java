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
    private boolean insertarAux(NodoAVL nodo, NodoAVL nodoPadre, Comparable elem, int altura) {
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
            balancear(nodo, nodoPadre);
        }
        return exito;
    }

    private void balancear (NodoAVL nodo, NodoAVL nodoPadre) {
        int balance = calcularBalance(nodo);
        if (balance > 1) {  // 'nodo' está inclinado a la izquierda.
            int balanceHijo = calcularBalance(nodo.getIzquierdo());
            if (balanceHijo >= 0) { // El hijo de 'nodo' está inclinado para el mismo lado que su padre. (izquierda)
                System.out.println("rotDer");
                NodoAVL h = rotarDerecha(nodo);
                cambiarRaiz(nodo, nodoPadre, h);
                nodo.recalcularAltura();    // Luego de rotar debo volver a calcular la altura del nodo.
            } else {    // El hijo de 'nodo' está inclinado para el lado contrario que su padre. Rotación doble Izq-Der.
                System.out.println("rotIzq-Der");
                NodoAVL h = rotarIzquierda(nodo.getIzquierdo());
                nodo.setIzquierdo(h);
                NodoAVL h2 = rotarDerecha(nodo);
                nodo.recalcularAltura();
                cambiarRaiz(nodo, nodoPadre, h2);
                h.recalcularAltura();
            }
        } else {
            if (balance < -1) { // 'nodo' está inclinado a la derecha.
                int balanceHijo = calcularBalance(nodo.getDerecho());
                if (balanceHijo <= 0) { // El hijo de 'nodo' está inclinado para el mismo lado que su padre. (derecha)
                    System.out.println("rotIzq");
                    NodoAVL h = rotarIzquierda(nodo);
                    cambiarRaiz(nodo, nodoPadre, h);
                    nodo.recalcularAltura();
                } else {    // El hijo de 'nodo' está inclinado para el lado contrario que su padre. Rotación doble Der-Izq.
                    System.out.println("rotDer-Izq");
                    NodoAVL h = rotarDerecha(nodo.getDerecho());
                    nodo.setDerecho(h);
                    NodoAVL h2 = rotarIzquierda(nodo);
                    nodo.recalcularAltura();
                    cambiarRaiz(nodo, nodoPadre, h2);
                    h.recalcularAltura();
                }
            }
        }
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
        System.out.println("BALANCE DE " + nodo.getElem() + " ES " + (alturaIzq-alturaDer));
        return (alturaIzq - alturaDer);
    }
    
    // Se rota a derecha cuando el árbol está caido a la izquierda.
    private NodoAVL rotarDerecha(NodoAVL nodo){
        NodoAVL h, temp;
        h = nodo.getIzquierdo();
        temp = h.getDerecho();
        h.setDerecho(nodo);
        nodo.setIzquierdo(temp);
        nodo.recalcularAltura();
        return h;   // Devuelvo el nodo que va a subir.
    }
    
    // Se rota a la izquierda cuando el árbol está caido a la derecha.
    private NodoAVL rotarIzquierda(NodoAVL nodo){
        NodoAVL h, temp;
        h = nodo.getDerecho();
        temp = h.getIzquierdo();
        h.setIzquierdo(nodo);
        nodo.setDerecho(temp);
        nodo.recalcularAltura();
        return h;
    }

    // Luego de una rotación (Izq o Der) puede que tenga que cambiar la raíz del árbol.
    private void cambiarRaiz(NodoAVL nodo, NodoAVL padreNodo, NodoAVL h) {
        if (nodo == this.raiz) { // El nodo a balancear (o sea, "bajar") es la raiz.
            this.raiz = h;
        } else {
            if (padreNodo.getDerecho() != null) {
                if (padreNodo.getDerecho().getElem() == nodo.getElem()) {
                    padreNodo.setDerecho(h);
                } else {
                    padreNodo.setIzquierdo(h);
                }
            }
        }
    }

    // TODO: Eliminar raíz.
    public boolean eliminar(Comparable elem) {
        boolean exito = false;
        if (this.raiz != null) {
            exito = eliminarAux(this.raiz, null, elem, ' ');
        }
        return exito;
    }
    
    private boolean eliminarAux(NodoAVL nodo, NodoAVL nodoPadre, Comparable elem, char lado){
        boolean exito = false;
        if (elem.compareTo(nodo.getElem()) == 0){
            exito = true;
            eliminarNodo(nodo,nodoPadre,lado);
        } else {
            if (elem.compareTo(nodo.getElem()) < 0) {
                exito = eliminarAux(nodo.getIzquierdo(),nodo,elem,'I');
            } else {
                exito = eliminarAux(nodo.getDerecho(),nodo,elem,'D');
            }
        }
        if (exito){
            balancear(nodo,nodoPadre);
        }
        return exito;
    }

    private void eliminarNodo(NodoAVL nodo, NodoAVL nodoPadre, char lado) {
        if (nodo.getAltura() == 0) {    // El nodo a eliminar es una hoja.
            if (lado == 'I') {
                nodoPadre.setIzquierdo(null);   // El nodo a eliminar es el hijo izquierdo de nodoPadre.
            } else {
                nodoPadre.setDerecho(null); // El nodo a eliminar es el hijo derecho de nodoPadre.
            }
        } else {    //El nodo a eliminar no es una hoja.
            if (nodo.getDerecho() == null && nodo.getIzquierdo() != null) { // El nodo tiene UN subarbol a la izquierda.
                if (lado == 'I') {
                    nodoPadre.setIzquierdo(nodo.getIzquierdo());
                } else {
                    nodoPadre.setDerecho(nodo.getIzquierdo());
                }
            } else {
                if (nodo.getDerecho() != null && nodo.getIzquierdo() == null) { // El nodo tiene UN subarbol a la derecha.
                    if (lado == 'I') {
                        nodoPadre.setIzquierdo(nodo.getDerecho());
                    } else {
                        nodoPadre.setDerecho(nodo.getDerecho());
                    }
                } else {    // El nodo tiene DOS subarboles.
                    /*
                        nodoASubir = Este nodo reemplaza al que voy a borrar.
                        hiSubir = Hijo izquierdo de nodoASubir. (si no tiene es 'null')
                     */
                    NodoAVL nodoASubir, padreNodoASubir = null, hiSubir = null;
                    nodoASubir = nodo.getIzquierdo();
                    while (nodoASubir.getDerecho() != null) { // Busco el mayor valor de los que están a la Izq de 'nodo'. (o sea, busco el mayor menor a 'nodo').
                        padreNodoASubir = nodoASubir;
                        nodoASubir = nodoASubir.getDerecho();
                        hiSubir = nodoASubir.getIzquierdo();
                    }
                    if (padreNodoASubir != null) {
                        padreNodoASubir.setDerecho(null);  // Borro el nodo que subí.
                        padreNodoASubir.recalcularAltura(); // Recalculo la altura del padre. (ahora sin 'nodoASubir')
                    }
                    if (hiSubir != null) {   // Si el nodo que subí tenía algo a la izq.
                        hiSubir.setIzquierdo(nodo.getIzquierdo());
                        hiSubir.recalcularAltura();
                        balancear(hiSubir, nodoASubir);
                    } else { // Si el nodo que subí no tenía algo a la izquierda.
                        nodoASubir.setIzquierdo(nodo.getIzquierdo());
                    }
                    if (lado == 'I'){
                        nodoPadre.setIzquierdo(nodoASubir);   // Coloco el nodo que voy a subir a la izquierda de su padre.
                    } else {
                        nodoPadre.setDerecho(nodoASubir);
                    }
                    nodoASubir.setDerecho(nodo.getDerecho());   // Al nodo que acabo de subir, le enlazo lo que estaba a la derecha del nodo que quité.
                    nodoASubir.recalcularAltura();  // Recalculo su altura.
                    nodoPadre.recalcularAltura();   // Recalculo la altura del padre.
                    balancear(nodoASubir, nodoPadre);
                }
            }
        }
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
