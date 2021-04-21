package dungeonsstructures.Grafo;

import dungeonsstructures.Lista.Lista;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Grafo {

    private NodoVert inicio;

    // Grafo creado con lista de adyacencia.
    public Grafo() {
        this.inicio = null;
    }

    // Inserta un nuevo vértice en el grafo. Si el vértice no existe en el grafo, lo inserta y devuelve true. Si no devuelve false.
    public boolean insertarVertice(Object nuevoVertice) {
        boolean exito = false;
        NodoVert aux = ubicarVertice(nuevoVertice);
        if (aux == null) {
            if (inicio == null) { // El grafo estaba vacio.
                inicio = new NodoVert(nuevoVertice, null);
            } else {              // El grafo no estaba vacio.
                aux = new NodoVert(nuevoVertice, this.inicio);    // Coloca el nuevo vértice al inicio de la Lista de adyacencia.
                this.inicio = aux;
            }
            exito = true;
        }
        return exito;
    }

    // Método auxiliar para insertarVertice. Dado el nombre de un NodoVert, devuelve dicho NodoVert.
    // Si no existe el nodo en el grafo, devuelve null.
    private NodoVert ubicarVertice(Object buscado) {
        NodoVert aux = this.inicio;
        while (aux != null && !aux.getElem().equals(buscado)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    // Primero elimina los arcos relacionados el vértice a eliminar, luego eliminar el vértice en sí.
    public boolean eliminarVertice(Object verticeEliminar) {
        boolean exito = false;
        NodoVert aux = this.inicio;
        NodoVert anteriorAux = null;
        while (aux != null && !exito) {
            if (aux.getElem().equals(verticeEliminar)) {
                exito = true;
                NodoAdy ady = aux.getPrimerAdy();
                eliminarVerticeAux(ady);  // Método auxiliar explicado abajo.

                // Ya se eliminaron todos los arcos relacionados con el vértice a eliminar, ahora se elimina el vértice en sí.
                if (anteriorAux == null) {    // Caso especial si es el 1° vertice.
                    this.inicio = aux.getSigVertice();
                } else {                      // Para cualquier otro caso.
                    anteriorAux.setSigVertice(aux.getSigVertice());
                }
            } else {
                anteriorAux = aux;
                aux = aux.getSigVertice();
            }
        }
        return exito;
    }

    // Este método auxiliar elimina los arcos que "van" hacía el vértice que quiero eliminar.
    // Su parámetro es el 1° nodo adyacente del Vértice que quiero eliminar.
    private void eliminarVerticeAux(NodoAdy ady) {
        while (ady != null) {
            NodoVert vertice = ady.getVertice();
            NodoAdy adyacente = vertice.getPrimerAdy();
            NodoAdy anteriorAdyacente = vertice.getPrimerAdy();
            boolean encontrado = false;
            while (!encontrado) { // Este bucle se va a detener siempre.
                if (adyacente.getEtiqueta() == ady.getEtiqueta()) {
                    encontrado = true;
                    if (vertice.getPrimerAdy().equals(adyacente)) { // Caso especial si es el 1° adyacente del vértice.
                        vertice.setPrimerAdy(adyacente.getSigAdyacente());
                    } else {     // Para cualquier otro caso.
                        anteriorAdyacente.setSiguienteAdy(adyacente.getSigAdyacente());
                    }
                } else {
                    anteriorAdyacente = adyacente;
                    adyacente = adyacente.getSigAdyacente();
                }
            }
            ady = ady.getSigAdyacente();
        }
    }

    // Dados un origen y destino se elimina el arco que los conecta. Si lo puede borrar devuelve true, si no devuelve false.
    public boolean eliminarArco(Object origen, Object destino) {
        boolean exito = false, encontrado = false;
        NodoVert aux = this.inicio;
        int etiqueta;
        if (existeArco(origen, destino)) {
            while (aux != null && !exito) {
                if (aux.getElem().equals(origen)) {
                    exito = true;
                    NodoAdy auxAdyacente = aux.getPrimerAdy();
                    while (!encontrado && auxAdyacente != null) {
                        if (auxAdyacente.getVertice().getElem().equals(destino)) {
                            encontrado = true;
                            etiqueta = auxAdyacente.getEtiqueta();
                            eliminarArcoAux(auxAdyacente.getVertice(), etiqueta);
                            eliminarArcoAux(aux, etiqueta);
                        } else {
                            auxAdyacente = auxAdyacente.getSigAdyacente();
                        }
                    }
                } else {
                    aux = aux.getSigVertice();
                }
            }
        }
        return encontrado;
    }

    // Método auxiliar que dado un NodoVert y una etiqueta elimina el NodoAdy que tenga esa etiqueta.
    private void eliminarArcoAux(NodoVert nodoVert, int etiqueta) {
        NodoAdy auxAdyacente = nodoVert.getPrimerAdy(); // puntero del NodoAdy actual que pregunta si la etiqueta coincide.
        NodoAdy anteriorAuxAdyacente = null;            // puntero del NodoAdy anterior a 'auxAdyacente'.
        boolean exito = false;
        while (!exito) {
            if (auxAdyacente.getEtiqueta() == etiqueta) {
                exito = true;
                if (nodoVert.getPrimerAdy().equals(auxAdyacente)) {   // Si el arco a eliminar es el 1° adyacente del vértice.
                    nodoVert.setPrimerAdy(auxAdyacente.getSigAdyacente());
                } else {                                        // en cualquier otro caso.
                    anteriorAuxAdyacente.setSiguienteAdy(auxAdyacente.getSigAdyacente());
                }
            } else {    // Si la etiqueta no coincide, avanzar al siguiente NodoAdy moviendo ambos punteros.
                anteriorAuxAdyacente = auxAdyacente;
                auxAdyacente = auxAdyacente.getSigAdyacente();
            }
        }
    }

    // Dado un vértice, devuelve true si dicho vértice existe. Si no devuelve false.
    public boolean existeVertice(Object objetoVertice) {
        boolean exito = false;
        NodoVert aux = this.inicio;
        while (aux != null && !exito) {
            if (aux.getElem().equals(objetoVertice)) {
                exito = true;
            }
            aux = aux.getSigVertice();
        }
        return exito;
    }

    // Inserta un arco que une a 'origen' y 'destino' con su correspondiente 'distancia'.
    public boolean insertarArco(Object origen, Object destino, int distancia) {
        boolean exito = false;
        // verifica que los vértices a unir existan;
        NodoVert auxO = null, auxD = null, auxInicio = this.inicio;
        while ((auxO == null || auxD == null) && auxInicio != null) {
            if (auxInicio.getElem().equals(origen)) {
                auxO = auxInicio;
            }
            if (auxInicio.getElem().equals(destino)) {
                auxD = auxInicio;
            }
            auxInicio = auxInicio.getSigVertice();
        }
        if (auxO != null && auxD != null) { // Los vértices a unir existen.
            NodoAdy arcoIda = new NodoAdy(auxD, auxO.getPrimerAdy(), distancia);
            NodoAdy arcoVuelta = new NodoAdy(auxO, auxD.getPrimerAdy(), distancia);
            auxO.setPrimerAdy(arcoIda);
            auxD.setPrimerAdy(arcoVuelta);
            exito = true;
        }
        return exito;
    }
    
    // Dada una lista con un camino devuelve el peso (kms) recorridos.
    public int calcularPeso(Lista lista){
        int acumulador = 0;
        for (int i = 1; i < lista.longitud(); i++) {
            NodoVert nodo = (NodoVert) lista.recuperar(i);
            NodoVert siguienteANodo = (NodoVert) lista.recuperar(i + 1);
            NodoAdy adyNodo = nodo.getPrimerAdy();  // Obtengo el primer adyacente de nodo.
            while (!siguienteANodo.equals(adyNodo.getVertice())){ // Itero 'adyNodo' hasta que coincida con 'siguienteANodo'.
                adyNodo = adyNodo.getSigAdyacente();
            }
            acumulador = acumulador + adyNodo.getEtiqueta();    // Sumo la distancia al acumulador.
        }
        return acumulador;
    }
    
    // Dada una lista con un camino devuelve la distancia (cantidad de locaciones) recorridas.
    public int calcularDistancia(Lista lista) {
        int acumulador = 0;
        for (int i = 1; i < lista.longitud(); i++) {
            NodoVert nodo = (NodoVert) lista.recuperar(i);
            NodoVert siguienteANodo = (NodoVert) lista.recuperar(i + 1);
            NodoAdy adyNodo = nodo.getPrimerAdy();  // Obtengo el primer adyacente de nodo.
            while (!siguienteANodo.equals(adyNodo.getVertice())) { // Itero 'adyNodo' hasta que coincida con 'siguienteANodo'.
                adyNodo = adyNodo.getSigAdyacente();
            }
            acumulador = acumulador + 1;    // Sumo la localización recorrida al acumulador.
        }
        return acumulador;
    }
    
    // Dado el nombre de una locación devuelve una lista con las locaciones adyacentes a ella. J.a
    public Lista ubicacionesAdyacentes(String nombre) {
        Lista lista = new Lista();  // Esta será una lista de nodos vértices.
        NodoVert nodoVertice = ubicarVertice(nombre);
        if (nodoVertice != null) {  // Si nodo vértice es distinto de nulo, entonces existe en el grafo.
            NodoAdy nodoAdyacente = nodoVertice.getPrimerAdy(); // Obtengo el primer adyacente del nodo vértice.
            while (nodoAdyacente != null) {                     // Recorro los nodos adyacentes para obtener las locaciones adyacentes.
                NodoVert auxNodoVert = nodoAdyacente.getVertice();
                lista.insertar(auxNodoVert.getElem(), 1);
                nodoAdyacente = nodoAdyacente.getSigAdyacente();
            }
        }
        return lista;
    }

    // Devuelve una lista con el camino con menor distancia (kms) para ir de origen a destino. J.b
    public Lista encontrarCaminoMasLiviano(Object origen, Object destino) {
        Lista camino = encontrarCamino(origen, destino, 'D', -1, null, true);
        return camino;
    }
    
    // Devuelve una lista con el camino más corto (menos locaciones) para ir desde origen a destino. J.c
    public Lista encontrarCaminoMasCorto(Object origen, Object destino){
        Lista camino = encontrarCamino(origen, destino, 'L', -1, null, true);
        return camino;
    }
    
    // Devuelve una lista de listas. Cada lista interna contiene un camino para ir de origen a destino con menos de unas distancia (kms) dada. J.d
    public Lista encontrarCaminosConDistanciaMaxima(Object origen, Object destino, int maximo){
        Lista camino = encontrarCamino(origen, destino, 'D', maximo, null, false);
        return camino;
    }
    
    // Devuelve una lista con el camino más corto (menos locaciones) para ir desde origen a destino que no pase por una locación. J.e
    public Lista encontrarCaminoMasCortoLocacionProhibida(Object origen, Object destino, Object locacionProhibida){
        Lista camino = encontrarCamino(origen, destino, 'L', -1, locacionProhibida, true);
        return camino;
    }
    
    // Todos los métodos para encontrar caminos convergen aquí. Este método ajusta los parámetros necesarios para buscar el camino deseado
    // en el método recursivo debajo de este.
    private Lista encontrarCamino(Object origen, Object destino, char letra, int maximo, Object locacionProhibida, boolean minimo) {
        NodoVert nodoVerticeOrigen = ubicarVertice(origen), nodoLocacionProhibida = ubicarVertice(locacionProhibida);   // Checkear antes por null
        NodoVert nodoVerticeDestino = ubicarVertice(destino);
        Lista visitados = new Lista(), camino = new Lista();
        int[] valorMaximo = new int[1];
        if (nodoVerticeOrigen != null && nodoVerticeDestino != null) {  // Origen y destino existen en el grafo.
            if (maximo == -1) {     // No existe un valor máximo para la distancia o la cantidad de locaciones.
                valorMaximo[0] = 999999999;
            } else {                // Existe un valor máximo de distancia a recorrer. Opción J.d
                valorMaximo[0] = maximo;
            }
            
            if (nodoLocacionProhibida != null) {    // Si es distinto de nulo, existe una locación prohibida por lo que no puedo pasar a la hora de recorrer el grafo.
                visitados.insertar(nodoLocacionProhibida, 1);   // Si agrego la locación como "visitada", entonces me evito recorrerla.
                
            }
            
            camino = auxEncontrarCamino(nodoVerticeOrigen, destino, camino, visitados, 0, valorMaximo, letra, minimo);
            
            if (nodoLocacionProhibida != null) {
                camino.eliminar(nodoLocacionProhibida); // Eliminar primera aparición.
            }
        }
        return camino;
    }
    
    // Método auxiliar recursivo que visita cada nodo y dependiendo de sus parámetros analiza una u otra cosa.
    // Usado en la opción 'J' del menú principal.
    private Lista auxEncontrarCamino(NodoVert nodo, Object destino, Lista camino, Lista visitados, int acumulador, int[] valorMaximo, char letra, boolean minimo) {
        if (nodo != null) {
            if (acumulador < valorMaximo[0]) {  // Seguir preguntando solo si lo que llevo recorriendo es menor al máximo.
                if (!visitados.pertenece(nodo)) {     // Si la locación actual no está en la lista de visitados.
                    //System.out.println(tabs(visitados.longitud()) + nodo.getElem());  // Debug.
                    if (nodo.getElem().equals(destino)) {       // Llegué a destino.
                        if (minimo) {    // Busco un único camino con peso o distancia mínimo.
                            camino = visitados.clone();
                            //System.out.print(acumulador);   // Debug.
                            //System.out.println();           // Debug.
                            camino.insertar(nodo, 1);
                            valorMaximo[0] = acumulador;    // Ahora el valor máximo (sea distancia o cantidad de locaciones) es el camino que acabo de encontrar. El mejor actualmente.
                        } else {        // Busco varios caminos.
                            Lista aux = visitados.clone();
                            aux.insertar(nodo, 1);
                            camino.insertar(aux, 1);
                        }
                    } else {    // Si no encontré mi 'destino'
                        visitados.insertar(nodo, 1);
                        NodoAdy siguiente = nodo.getPrimerAdy();    // Tomo el primer adyacente del nodo actual.
                        while (siguiente != null) {     // Recorro los nodos adyacentes llamando recursivamente y buscando caminos válidos.
                            if (letra == 'D') { // El char es 'D' entonces el valor de interés es la distancia entre locaciones y 'acumulador' irá acumulando distancias.
                                camino = auxEncontrarCamino(siguiente.getVertice(), destino, camino, visitados, acumulador + siguiente.getEtiqueta(), valorMaximo, letra, minimo);
                            } else {            // El char es 'L' entonces el valor de interés es la cantidad de locaciones recorridas y 'acumulador' irá acumulando locaciones recorridas.
                                camino = auxEncontrarCamino(siguiente.getVertice(), destino, camino, visitados, acumulador + 1, valorMaximo, letra, minimo);
                            }
                            siguiente = siguiente.getSigAdyacente();    // Obtengo el siguiente adyacente.
                        }
                        visitados.eliminar(nodo); // Elimino el nodo visitado de la lista en caso de que exista otro camino que me lleve a él.
                    }
                }
            }
        }
        return camino;
    }
    
        // Método auxiliar para debug. Dado un número devuelve un String con tantos espacios como el número recibido.
//    private String tabs(int numero) {
//        String espacio = "";
//        for (int i = 0; i < numero; i++) {
//            espacio = espacio + " ";
//        }
//        return espacio;
//    }

    // Devuelve una lista en profundidad del grafo.
    public Lista listarEnProfundidad() {
        Lista visitados = new Lista();
        // define un vértice donde comenzar a recorrer.
        NodoVert aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                // si el vértice no fue visitado aún, avanza en profundidad.
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    // Método recursivo auxiliar para listar en profundidad el grafo.
    private void listarEnProfundidadAux(NodoVert n, Lista vis) {
        if (n != null) {
            // marca el vértice n como visitado.
            vis.insertar(n.getElem(), vis.longitud() + 1);
            NodoAdy ady = n.getPrimerAdy();
            while (ady != null) {
                // visita en profundidad los adyacentes de n aún no visitados.
                if (vis.localizar(ady.getVertice().getElem()) < 0) {
                    listarEnProfundidadAux(ady.getVertice(), vis);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    // Dado un origen y destino verifica si existe un camino entre ellos.
    public boolean existeArco(Object origen, Object destino) {
        boolean exito = false;
        // verifica si ambos vertices existe;
        NodoVert auxO = null, auxD = null, aux = this.inicio;
        while ((auxO == null || auxD == null) && aux != null) {
            if (aux.getElem().equals(origen)) {
                auxO = aux;
            }
            if (aux.getElem().equals(destino)) {
                auxD = aux;
            }
            aux = aux.getSigVertice();
        }
        if (auxO != null && auxD != null) {
            // si ambos vértices existen busca si existe camino entre ambos.
            Lista visitados = new Lista();
            exito = existeArcoAux(auxO, destino, visitados);
        }
        return exito;
    }

    // Método recursivo auxiliar para comprobar si existe un camino.
    private boolean existeArcoAux(NodoVert n, Object dest, Lista vis) {
        boolean exito = false;
        if (n != null) {
            // si vértice n es el destino: HAY CAMINO!.
            if (n.getElem().equals(dest)) {
                exito = true;
            } else {
                // si no es el destino verifica si hay camino entre n y destino.
                vis.insertar(n.getElem(), vis.longitud() + 1);
                NodoAdy ady = n.getPrimerAdy();
                while (!exito && ady != null) {
                    if (vis.localizar(ady.getVertice().getElem()) < 0) {
                        exito = existeArcoAux(ady.getVertice(), dest, vis);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }
        return exito;
    }

    // Si el grafo está vacio devuelve true, si no devuelve false.
    public boolean esVacio() {
        return (this.inicio == null);
    }

    @Override
    public String toString() {
        String cadenita = "";
        NodoVert aux = this.inicio;
        while (aux != null) {    // Concateno los vértices.
            cadenita = cadenita + aux.getElem() + " = ";
            NodoAdy adyacentes = aux.getPrimerAdy();
            while (adyacentes != null) { // Concateno los arcos.
                cadenita = cadenita + adyacentes.getVertice().getElem() + ": " + adyacentes.getEtiqueta() + ", ";
                adyacentes = adyacentes.getSigAdyacente();
            }
            cadenita = cadenita + "\n";
            aux = aux.getSigVertice();
        }
        return cadenita;
    }

}
