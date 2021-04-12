package dungeonsstructures.Grafo;

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
        //System.out.println("Grafo: " + "\n" + grafo.toString());
        //Lista lista = grafo.encontrarCaminoMenosDistancia("A", "E");
        //nombreTemporal("A", "E", lista);
        //System.out.println(lista.toString());
    }

}
