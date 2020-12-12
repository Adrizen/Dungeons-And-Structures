package dungeonsstructures.Grafo;
import dungeonsstructures.Lista.*;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Grafo {
    private NodoVert inicio;
    
    public Grafo(){
        this.inicio = null;
    }

    public boolean insertarVertice(Object nuevoVertice) {
        boolean exito = false;
        NodoVert aux = ubicarVertice(nuevoVertice);
        if (aux == null) {
            if (inicio == null) { // Es el 1° NodoVert en insertarse.
                inicio = new NodoVert(nuevoVertice, null);
            } else {    // Ya había un NodoVert.
                aux = new NodoVert(nuevoVertice, this.inicio);    // Coloca el nuevo Vertice al inicio de la Lista de adyacencia.
                this.inicio = aux;
            }
            exito = true;
        }
        return exito;
    }

    // Método auxiliar para insertarVertice.
    private NodoVert ubicarVertice(Object buscado){
        NodoVert aux = this.inicio;
        while (aux != null && !aux.getElem().equals(buscado)){
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
                if (anteriorAux == null){    // Caso especial si es el 1° vertice.
                    this.inicio = aux.getSigVertice();
                } else {    // Para cualquier otro caso.
                    anteriorAux.setSigVertice(aux.getSigVertice());
                }
            } else {
                anteriorAux = aux;
                aux = aux.getSigVertice();
            }
        }
        return exito;
    }
    
    // Este método auxiliar elimina los arcos que "van" hacía el Vértice que quiero eliminar.
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
                    } else {    // Para cualquier otro caso.
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

    public boolean eliminarArco(Object origen, Object destino) {
        boolean exito = false, encontrado = false;
        NodoVert aux = this.inicio; // puntero vértices.
        int etiqueta;               // etiqueta del arco que quiero borrar.
        // podría hacer antes un check para comprobar que origen y destino existen en el grafo. Hay un método ya hecho pa eso.
        
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
        return encontrado;
    }
    
    // Método auxiliar que dado un NodoVert y una etiqueta elimina el NodoAdy que tenga esa etiqueta.
    private void eliminarArcoAux(NodoVert nodoVert, int etiqueta){
        NodoAdy auxAdyacente = nodoVert.getPrimerAdy(); // puntero del NodoAdy actual que pregunta si la etiqueta coincide.
        NodoAdy anteriorAuxAdyacente = null;            // puntero del NodoAdy anterior a 'auxAdyacente'.
        boolean exito = false;
        while (!exito){
            if (auxAdyacente.getEtiqueta() == etiqueta){
                exito = true;
                if (nodoVert.getPrimerAdy() == auxAdyacente){   // Si el arco a eliminar es el 1° adyacente del vértice.
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
    
    public boolean existeVertice(Object objetoVertice){
        boolean exito = false;
        NodoVert aux = this.inicio;
        while (aux != null && !exito){
            if (aux.getElem().equals(objetoVertice))
                exito = true;
            aux = aux.getSigVertice();
        }
        return exito;
    }
    
    public boolean insertarArco(Object origen, Object destino, int distancia){
        boolean exito = false;
        // verifica que los vértices a unir existan;
        NodoVert auxO = null, auxD = null, auxInicio = this.inicio;
        while ((auxO == null || auxD == null) && auxInicio != null){
            if (auxInicio.getElem().equals(origen))
                auxO = auxInicio;
            if (auxInicio.getElem().equals(destino))
                auxD = auxInicio;
            auxInicio = auxInicio.getSigVertice();
        }
        if (auxO != null && auxD != null){ // Los vértices a unir existen.
            NodoAdy arcoIda = new NodoAdy(auxD, auxO.getPrimerAdy(), distancia); // (vertice,adyacente,etiqueta)
            NodoAdy arcoVuelta = new NodoAdy(auxO, auxD.getPrimerAdy(), distancia);
            auxO.setPrimerAdy(arcoIda);
            auxD.setPrimerAdy(arcoVuelta);
            exito = true;
        }
        return exito;
    }
    
    public Lista listarEnProfundidad(){
        Lista visitados = new Lista();
        // define un vértice donde comenzar a recorrer.
        NodoVert aux = this.inicio;
        while (aux != null){
            if (visitados.localizar(aux.getElem()) < 0){
                // si el vértice no fue visitado aún, avanza en profundidad.
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }
    
    private void listarEnProfundidadAux(NodoVert n, Lista vis){
        if (n != null){
            // marca el vértice n como visitado.
            vis.insertar(n.getElem(), vis.longitud() + 1);
            NodoAdy ady = n.getPrimerAdy();
            while (ady != null){
                // visita en profundidad los adyacentes de n aún no visitados.
                if (vis.localizar(ady.getVertice().getElem()) < 0){
                    listarEnProfundidadAux(ady.getVertice(), vis);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }
    
    public boolean existeArco(Object origen, Object destino){
        boolean exito = false;
        // verifica si ambos vertices existe;
        NodoVert auxO = null, auxD = null, aux = this.inicio;
        
        while ((auxO == null || auxD == null) && aux != null){
            if (aux.getElem().equals(origen))
                auxO = aux;
            if (aux.getElem().equals(destino))
                auxD = aux;
            aux = aux.getSigVertice();
        }
        
        if (auxO != null && auxD != null){
            // si ambos vértices existen busca si existe camino entre ambos.
            Lista visitados = new Lista();
            exito = existeArcoAux(auxO, destino, visitados);
        }
        return exito;
    }
    
    // Método auxiliar para ...
    private boolean existeArcoAux(NodoVert n, Object dest, Lista vis){
        boolean exito = false;
        if (n != null){
            // si vértice n es el destino: HAY CAMINO!.
            if (n.getElem().equals(dest)){
                exito = true;
            } else {
                // si no es el destino verifica si hay camino entre n y destino.
                vis.insertar(n.getElem(), vis.longitud() + 1);
                NodoAdy ady = n.getPrimerAdy();
                
                while (!exito && ady != null){
                    if (vis.localizar(ady.getVertice().getElem()) < 0){
                        exito = existeArcoAux(ady.getVertice(), dest, vis);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }
        return exito;
    }
    
    public boolean esVacio(){
        return (this.inicio == null);
    }
    
    @Override
    public String toString(){
        String cadenita = "";
        NodoVert aux = this.inicio;
        while (aux != null){    // Concateno los vértices.
            cadenita = cadenita + aux.getElem() + " = ";
            NodoAdy adyacentes = aux.getPrimerAdy();
            while (adyacentes != null){ // Concateno los arcos.
                cadenita = cadenita + adyacentes.getVertice().getElem() + ": " + adyacentes.getEtiqueta() + ", ";
                adyacentes = adyacentes.getSigAdyacente();
            }
            cadenita = cadenita + "\n";
            aux = aux.getSigVertice();
        }
        return cadenita;
    }
    
}
