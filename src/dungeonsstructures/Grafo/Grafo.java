package dungeonsstructures.Grafo;

import dungeonsstructures.Lista.*;

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
                    if (vertice.getPrimerAdy() == adyacente) { // Caso especial si es el 1° adyacente del vértice.
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
                if (nodoVert.getPrimerAdy() == auxAdyacente) {   // Si el arco a eliminar es el 1° adyacente del vértice.
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

    // Usado en la opcion 'J' del menú principal. Dependiendo del 'char' se analiza una u otra cosa.
    public Lista encontrarCamino(Object origen, Object destino, char letra, int maximo, Object locacionProhibida) {
        NodoVert nodoVerticeOrigen = ubicarVertice(origen);
        NodoVert nodoVerticeDestino = ubicarVertice(destino);
        Lista visitados = new Lista(), camino = new Lista();
        int[] valorMaximo = new int[1];
        if (nodoVerticeOrigen != null && nodoVerticeDestino != null) {  // Origen y destino existen en el grafo.
            if (maximo == -1) {     // No existe un valor máximo para la distancia o la cantidad de locaciones.
                valorMaximo[0] = 999999999;
            } else {                // Existe un valor máximo de distancia a recorrer o cantidad de locaciones a visitar.
                valorMaximo[0] = maximo;
            }
            
            if (locacionProhibida != null) {    // Si es distinto de nulo, existe una locación prohibida por lo que no puedo pasar a la hora de recorrer el grafo.
                visitados.insertar(locacionProhibida, 1);   // Si agrego la locación como "visitada", entonces me evito recorrerla.
            }
            
            camino = auxEncontrarCamino(nodoVerticeOrigen, destino, camino, visitados, 0, valorMaximo, letra);
            
            if (locacionProhibida != null) {
                camino.eliminar(locacionProhibida);
            }
            if (!camino.esVacia()) {
                camino.insertar(valorMaximo[0], 1);
            }
        }
        return camino;
    }

    // Método auxiliar recursivo que visita cada nodo y dependiendo de sus parámetros analiza una u otra cosa.
    // Usado en la opción 'J' del menú principal.
    private Lista auxEncontrarCamino(NodoVert nodo, Object destino, Lista camino, Lista visitados, int acumulador, int[] valorMaximo, char letra) {
        if (nodo != null) {
            if (!visitados.pertenece(nodo.getElem())) {     // Si la locación actual no está en la lista de visitados.
                if (nodo.getElem().equals(destino)) {       // Llegué a destino.
                    if (acumulador < valorMaximo[0]) {      // Comprobar si los valores que fui acumulando al recorrer el grafo no superan el valor máximo provisto.
                        
                        // Como encontré un camino que cumple las condiciones dadas es necesario clonar la lista de visitados, porque si solo la "asigno" ...
                        // solo señalo referencias y si llego a modificar la lista de visitados (por ejemplo, recorriendo otras posibilidades) también ...
                        // estaría cambiando la lista 'camino'.
                        camino = visitados.clone();             
                        camino.insertar(nodo.getElem(), 1);
                        valorMaximo[0] = acumulador;    // Ahora el valor máximo (sea distancia o cantidad de locaciones) es el camino que acabo de encontrar. El mejor actualmente.
                    }
                } else {    // Si no encontré mi 'destino'
                    visitados.insertar(nodo.getElem(), 1);
                    NodoAdy siguiente = nodo.getPrimerAdy();    // Tomo el primer adyacente del nodo actual.
                    while (siguiente != null) {     // Recorro los nodos adyacentes llamando recursivamente y buscando caminos válidos.
                        
                        if (letra == 'D') { // El char es 'D' entonces el valor de interés es la distancia entre locaciones y 'acumulador' irá acumulando distancias.
                            camino = auxEncontrarCamino(siguiente.getVertice(), destino, camino, visitados, acumulador + siguiente.getEtiqueta(), valorMaximo, letra);
                        } else {            // El char es 'L' entonces el valor de interés es la cantidad de locaciones recorridas y 'acumulador' irá acumulando locaciones recorridas.
                            camino = auxEncontrarCamino(siguiente.getVertice(), destino, camino, visitados, acumulador + 1, valorMaximo, letra);
                        }
                        siguiente = siguiente.getSigAdyacente();    // Obtengo el siguiente adyacente.
                    }
                    visitados.eliminar(nodo.getElem()); // Elimino el nodo visitado de la lista en caso de que exista otro camino que me lleve a él.
                }
            }
        }
        return camino;
    }

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

    // Dado el nombre de una locación devuelve una lista con las locaciones adyacentes a ella.
    public Lista ubicacionesAdyacentes(String nombre) {
        Lista lista = new Lista();  // Esta será una lista de nodos vértices.
        NodoVert nodoVertice = ubicarVertice(nombre);
        if (nodoVertice != null) {  // Si nodo vértice es distinto de nulo, entonces existe en el grafo.
            NodoAdy nodoAdyacente = nodoVertice.getPrimerAdy(); // Obtengo el primer adyacente del nodo vértice.
            while (nodoAdyacente != null) {                     // Recorro los nodos adyacentes para obtener las locaciones adyacentes.
                NodoVert auxNodoVert = nodoAdyacente.getVertice();
                lista.insertar(auxNodoVert, 1);
                nodoAdyacente = nodoAdyacente.getSigAdyacente();
            }
        }
        return lista;
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
