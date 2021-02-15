package dungeonsstructures.Lista;

import dungeonsstructures.Nodo;


public class Lista {

    private Nodo cabecera;

    public Lista() {
        this.cabecera = null;
    }

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
                cadenita += aux.getElemento().toString() + " \n ";
                aux = aux.getEnlace();
            }
        }
        while (aux != null) {
            cadenita += aux.getElemento().toString() + " ";
            aux = aux.getEnlace();
        }
        return cadenita;
    }

    public Lista obtenerMultiplos(int num) {
        Lista nueva = new Lista();
        Nodo aux = this.cabecera, aux2 = null;
        int cont = 1;

        while (aux != null) {
            if ((cont % num) == 0) {
                if (nueva.cabecera == null) {
                    Nodo nuevo = new Nodo(aux.getElemento(), null);
                    nueva.cabecera = nuevo;
                    aux2 = nueva.cabecera;
                } else {
                    Nodo nuevo = new Nodo(aux.getElemento(), null);
                    aux2.setEnlace(nuevo);
                    aux2 = aux2.getEnlace();
                }
            }
            aux = aux.getEnlace();
            cont++;
        }
        return nueva;
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
