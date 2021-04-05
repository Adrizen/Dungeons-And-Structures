package dungeonsstructures.Lista;

import dungeonsstructures.Nodo;


public class Lista {

    private Nodo cabecera;

    public Lista() {
        this.cabecera = null;
    }

    // Dado una posición y un objeto. Inserta el objeto en dicha posición
    // Si puede insertar el objeto devuelve true, de lo contrario devuelve false.
    public boolean insertar(Object obj, int pos) {
        boolean bandera = false;
        if (pos >= 1 && pos <= longitud() + 1) {
            bandera = true;
            if (pos == 1) {
                Nodo nuevo = new Nodo(obj, this.cabecera);
                this.cabecera = nuevo;
            } else {
                Nodo aux = this.cabecera;
                for (int i = 1; i != pos - 1; i++) {
                    aux = aux.getEnlace();
                }
                Nodo nuevo = new Nodo(obj, aux.getEnlace());
                aux.setEnlace(nuevo);
            }
        }
        return bandera;
    }

    // Dada una posición, elimina el objeto en dicha posición.
    // Si puede eliminar el objeto devuelve true, de lo contrario devuelve false.
    public boolean eliminar(int pos) {
        boolean bandera = false;
        if (pos >= 1 && pos <= longitud() && !esVacia()) {
            if (pos == 1) {
                this.cabecera = this.cabecera.getEnlace();
                bandera = true;
            } else {
                Nodo aux = this.cabecera;
                bandera = true;
                for (int i = 1; i < pos - 1; i++) {
                    aux = aux.getEnlace();
                }
                aux.setEnlace((aux.getEnlace()).getEnlace());
            }
        }
        return bandera;
    }
    
    // Método para eliminar un nodo dado un objeto, sin saber en qué posición está.
    // Si se puede eliminar el nodo, devuelve true. Si no devuelve false.
    public boolean eliminar(Object obj){
        Nodo nodito = this.cabecera, padreNodito = null;
        boolean exito = false;
        while (nodito != null && !exito){
            if (nodito.getElemento().equals(obj)){
                exito = true;
                if (padreNodito == null){   // El nodo a eliminar es la cabecera de la lista.
                    this.cabecera = nodito.getEnlace();
                } else {    // El nodo a eliminar no es la cabecera de la lista.
                    padreNodito.setEnlace(nodito.getEnlace());
                }
            } else {
                padreNodito = nodito;
                nodito = nodito.getEnlace();
            }
        }
        return exito;
    }
    
    // Dado un objeto, devuelve true si el objeto está en la lista. De lo contrario devuelve false.
    public boolean pertenece(Object obj){
        boolean exito = false;
        Nodo nodo = this.cabecera;
        while (!exito && nodo != null){
            if (nodo.getElemento().equals(obj)){
                exito = true;
            }
            nodo = nodo.getEnlace();
        }
        return exito;
    }

    // Dada una posición, devuelve el objeto en dicha posición.
    // Si tiene éxito devuelve el objeto, de lo contrario devuelve null.
    public Object recuperar(int pos) {
        if (pos < 1 || pos > longitud()) {
            return null;
        } else {
            Nodo aux = this.cabecera;
            for (int i = 1; i != pos; i++) {
                aux = aux.getEnlace();
            }
            return aux.getElemento();
        }
    }

    // Dado un objeto devuelve la posición de ese objeto en la lista.
    // Si tiene éxito devuelve la posición, de lo contrario devuelve -1.
    public int localizar(Object obj) {
        int posicion = -1, longitud = longitud();
        boolean encontrado = false;
        Nodo aux = this.cabecera;
        for (int i = 1; i <= longitud && !encontrado; i++) {
            if (aux.getElemento().equals(obj)) {
                encontrado = true;
                posicion = i;
            }
            aux = aux.getEnlace();
        }
        return posicion;
    }

    public void vaciar() {
        this.cabecera = null;
    }

    public boolean esVacia() {
        return (this.cabecera == null);
    }

    public Lista clone() {
        Lista clon = new Lista();
        if (this.cabecera != null) {
            Nodo aux = this.cabecera;
            Nodo aux2 = new Nodo(aux.getElemento(), null);
            clon.cabecera = aux2;
            while (aux != null) {
                aux = aux.getEnlace();
                if (aux != null) {
                    Nodo nuevo = new Nodo(aux.getElemento(), null);
                    aux2.setEnlace(nuevo);
                    aux2 = nuevo;
                }

            }
        }
        return clon;
    }

    // Recorre la lista y devuelve su longitud.
    public int longitud() {
        int longitud = 0;
        Nodo aux = this.cabecera;
        while (aux != null) {
            longitud++;
            aux = aux.getEnlace();
        }
        return longitud;
    }

    public String toString() {
        String cadenita = "";
        Nodo aux = this.cabecera;
        if (aux == null) {
            return "Lista vacia";
        } else {
            while (aux != null) {
                cadenita += aux.getElemento().toString() + "\n";
                aux = aux.getEnlace();
            }
        }
        return cadenita;
    }


    public void eliminarApariciones(Object x) {
        Nodo aux = this.cabecera, aux2 = null;
        while (aux != null) {
            if (this.cabecera.getElemento().equals(x)) {
                this.cabecera = this.cabecera.getEnlace();
            } else {
                if (aux.getElemento().equals(x)) {
                    aux2.setEnlace(aux.getEnlace());
                }
            }
            aux2 = aux;
            aux = aux.getEnlace();
        }
    }
}
