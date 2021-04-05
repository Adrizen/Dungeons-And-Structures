package dungeonsstructures.Grafo;

import dungeonsstructures.Lista.Lista;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class test {

    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        grafo.insertarVertice("E");
        grafo.insertarVertice("D");
        grafo.insertarVertice("C");
        grafo.insertarVertice("B");
        grafo.insertarVertice("A");
        grafo.insertarArco("D", "E", 3);
        grafo.insertarArco("A", "B", 12);
        grafo.insertarArco("A", "C", 10);
        grafo.insertarArco("A", "D", 13);
        grafo.insertarArco("C", "E", 10);
        System.out.println("Grafo: " + "\n" + grafo.toString());
        Lista lista = grafo.encontrarCaminoMenosDistancia("A", "E");
        //nombreTemporal("A", "E", lista);
        System.out.println(lista.toString());
    }

    // Esto es original de Dungeons2019, está aquí esctrictamente para testear.
    public static void nombreTemporal(String origen, String destino, Lista lista){
        int longitud = lista.longitud();
        System.out.println("El camino de " + origen + " a " + destino + " con la menor distancia posible (" + lista.recuperar(1) +"km) es: ");
        System.out.print("(" + origen + ")" +  " - (" +origen + ")");
        for (int i = longitud; i > 1; i--) {    // Iterado de atras hacia delante.
            NodoAdy nodo = (NodoAdy) lista.recuperar(i);
            System.out.print(" " + nodo.getEtiqueta() + " km (" + nodo.getVertice().getElem() + ") - (" + nodo.getVertice().getElem() + ")" );
        }
        System.out.println();
    }

}
