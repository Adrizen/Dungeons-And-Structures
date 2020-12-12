
package dungeonsstructures.Grafo;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class test {


    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        grafo.insertarVertice("D");
        grafo.insertarVertice("C");
        grafo.insertarVertice("B");
        grafo.insertarVertice("A");
        grafo.insertarArco("A", "B", 12);
        grafo.insertarArco("B", "C", 48);
        grafo.insertarArco("A", "D", 5);
        grafo.insertarArco("B", "D", 33);
        System.out.println("Original " + "\n" + grafo.toString());
        System.out.println("Eliminar arco de C y A " + grafo.eliminarArco("A", "C"));
        System.out.println(grafo.toString());
    }
    
}
