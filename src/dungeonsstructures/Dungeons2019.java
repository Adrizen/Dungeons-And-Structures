package dungeonsstructures;

import dungeonsstructures.ArbolAVL.ArbolAVL;
import dungeonsstructures.ColaPrioridad.ColaPrioridad;
import dungeonsstructures.Grafo.*;
import dungeonsstructures.Lista.Lista;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class Dungeons2019 {
    
    static PrintWriter LOG;
    static int jugadoresEsperandoEquipo;
    static ArbolAVL items, jugadores;
    static Grafo mapa;
    static HashMap equipos, codigosItems;
    static ColaPrioridad armadoEquipos;
    static ArrayList localizaciones;
    static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        crearYCargarEstructuras();
        
        for (int i = 0; i < 10; i++) {
            Equipo equipo1 = (Equipo) equipos.get("Mercedes AMG Petronas e-Sports");
            Equipo equipo2 = (Equipo) equipos.get("Scuderia Ferrari Sport Elettronici");
            administrarBatalla(equipo1, equipo2);
        }

        LOG.flush();
        menu();
        System.out.println("-- Finalizando... gracias por jugar :) --");
        LOG.println("-- Finalizando sistema --");
        escribirEstructurasEnLOG();
        LOG.close();    // Cierro el LOG.txt
    }
    
    // Crea y carga (a partir de los txt) las distintas estructuras del sistema (AVL, Grafo, etc).
    // La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
    private static void crearYCargarEstructuras() {
        crearLog();   // Creo el archivo LOG.txt
        armadoEquipos = new ColaPrioridad();    // Cola de prioridad para los jugadores esperando entrar en un equipo."
        jugadoresEsperandoEquipo = 0;           // Cantidad de jugadores a la espera de formar parte de un equipo.
        // Items.
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        items = crearItems(datosItems);         // Creo los items y los coloco en el AVL.
        // Jugadores.
        Scanner datosJugadores = leerTxtJugadores();                // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        jugadores = crearJugadores(datosJugadores, armadoEquipos);  // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        // Mapa.
        Scanner datosMapa = leerTxtLocalizaciones();
        localizaciones = new ArrayList();       // ArrayList usado para elegir aleatoriamente una localización al crear un equipo.
        mapa = crearMapa(datosMapa);
        // Equipos.
        Scanner nombreEquipos = leerTxtEquipos();
        equipos = new HashMap();
        crearEquipos(nombreEquipos);
        // Caminos.
        Scanner datosCaminos = leerTxtCaminos();
        crearCaminos(datosCaminos);
        escribirEstructurasEnLOG();             // Escribír las estructuras (AVL, Grafo, etc) en el LOG.
    }
    
    // Escribo (toString()) todas las estructuras en el LOG.
    private static void escribirEstructurasEnLOG() {
        LOG.println("");
        LOG.println("-- ESTRUCTURAS DEL SISTEMA --");
        LOG.println("AVL Items: \n" + items.toString());
        LOG.println("AVL Jugadores: \n" + jugadores.toString());
        LOG.println("HashMap Equipos: \n" + equipos.toString());
        LOG.println("Grafo: \n" + mapa.toString());
        LOG.println("-- FIN ESTRUCTURAS DEL SISTEMA --");
        LOG.flush();
    }

    // Menú principal.
    public static void menu(){
        boolean seguir = true;
        sc.useDelimiter("\r?\n");   // Scanner acepta espacios como parte de la entrada.
        char seleccion;
        while (seguir){
            System.out.println("--- Menú - Eliga una opción ---");
            System.out.println("A. ABM de jugadores.");
            System.out.println("B. ABM de items.");
            System.out.println("C. ABM Locaciones.");
            System.out.println("D. Alta de un jugador en la cola de espera por un equipo.");
            System.out.println("E. Creación automática de un equipo.");
            System.out.println("F. Crear una batalla entre dos equipos.");
            System.out.println("G. Consultar sobre un equipo.");
            System.out.println("H. Consultar sobre items.");
            System.out.println("I. Consultar sobre jugadores.");
            System.out.println("J. Consultar sobre locaciones.");
            System.out.println("K. Consultas generales.");
            System.out.println("L. Mostrar sistema.");
            System.out.println("Z. Salir del juego.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            seguir = !seleccionarOpcion(seleccion);
            LOG.flush();
        }
    }

    private static boolean seleccionarOpcion(char seleccion) {
        boolean exito = false;
        switch (seleccion) {
            case 'A':
                ABMJugador();
                break;
            case 'B':
                ABMItem();
                break;
            case 'C':
                ABMLocaciones();
                break;
            case 'D':
                System.out.println("TBD");
                break;
            case 'E':
                crearEquipos(null);
                break;
            case 'F':
                crearBatalla();
                break;
            case 'G':
                consultarEquipo();
                break;
            case 'H':
                consultarItems();
                break;
            case 'I':
                consultarJugadores();
                break;
            case 'J':
                consultarLocaciones();
                break;
            case 'K':
                consultasGenerales();
                break;
            case 'L':
                mostrarSistema();
                break;
            case 'Z':
                exito = true;
                break;
            default:
                System.out.println("Selección no válida, elija una correcta.");
        }
        return exito;
    }
    
    // Método para seleccionar qué se desea hacer a algún jugador/jugadores.
    private static void ABMJugador() {
        boolean seguir = true;
        char seleccion;
        while (seguir) {
            System.out.println("A. Alta de un jugador.");
            System.out.println("B. Baja de un jugador.");
            System.out.println("C. Modificación de un jugador.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("-- Dar de alta a un jugador --");
                    altaJugador();
                    break;
                case 'B':
                    System.out.println("-- Dar de baja a un jugador --");
                    bajaJugador();
                    break;
                case 'C':
                    System.out.println("-- Modificar a un jugador --");
                    modificarJugador();
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
            LOG.flush();
        }
    }
    
    // Agregar manualmente a un jugador pidiendo sus datos individualmente.
    private static void altaJugador(){
        String nombre, tipo, categoria;
        System.out.println("Ingrese el nombre del jugador");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (!jugadores.pertenece(nombre)){  // Si no existe en el AVL pido sus datos y lo agrego.
            tipo = elegirTipoDeJugador();
            categoria = elegirCategoriaDeJugador();
            Jugador jugador = new Jugador(nombre,tipo,categoria,null);
            jugadores.insertar(nombre,jugador);         // Agrego al jugador al AVL.
            armadoEquipos.insertar(jugador, categoria); // Agrego al jugador a la cola de prioridad usada para armar los equipos.
            jugadoresEsperandoEquipo++;
            System.out.println("Jugador " + nombre + " agregado con éxito.");
            LOG.println("Agregado jugador " + nombre + ".");
        } else {
            System.out.println("El jugador " + nombre + " ya existe.");
        }
    }
    
    // Seleccionar el tipo de un jugador al darlo de alta.
    private static String elegirTipoDeJugador(){
        String respuesta = "";
        boolean seguir = true;
        char seleccion;
        System.out.println("Seleccione el tipo de jugador.");
        System.out.println("A. Guerrero.");
        System.out.println("B. Defensor.");
        while (seguir){
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion){
                case 'A':
                    respuesta = "GUERRERO";
                    seguir = false;
                    break;
                case 'B':
                    respuesta = "DEFENSOR";
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
        return respuesta;
    }
    
    // Seleccionar la categoría de un jugador al darlo de alta.
    private static String elegirCategoriaDeJugador(){
        String respuesta = "";
        boolean seguir = true;
        char seleccion;
        System.out.println("Seleccione la categoria del jugador.");
        System.out.println("A. Novato.");
        System.out.println("B. Aficionado.");
        System.out.println("C. Profesional");
        while (seguir){
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion){
                case 'A':
                    respuesta = "NOVATO";
                    seguir = false;
                    break;
                case 'B':
                    respuesta = "AFICIONADO";
                    seguir = false;
                    break;
                case 'C':
                    respuesta = "PROFESIONAL";
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
        return respuesta;
    }

    // Eliminar a un jugador del sistema.
    private static void bajaJugador() {
        String nombre;
        System.out.println("Ingrese el nombre del jugador a eliminar.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        Jugador jugador = (Jugador) jugadores.obtener(nombre);
        if (jugador != null){   // Si no es nulo, el jugador existe.
            jugadores.eliminar(nombre);                     // Elimino al jugador del AVL.
            Equipo equipoJugador = jugador.getEquipo();     // Obtengo el equipo del jugador.
            if (equipoJugador != null) {                    // Si el jugador eliminado tenía equipo.
                equipos.remove(equipoJugador.getNombre());  // Su equipo ya no está completo y no puede pelear con otros equipos. Lo quito del HashMap.
                Lista listaJugadores = equipoJugador.getJugadores();
                for (int i = 1; i < 3; i++) {               // Los dos jugadores que quedaban se quedan sin equipo y vuelven a la cola de prioridad.
                    Jugador auxJugador = (Jugador) listaJugadores.recuperar(i);
                    auxJugador.setEquipo(null);
                    jugadoresEsperandoEquipo++;
                    String categoria = auxJugador.getCategoria();
                    armadoEquipos.insertar(auxJugador, categoria);  // Agrego al jugador a la cola de prioridad de armado de equipos.
                }
            }
            System.out.println("Jugador " + nombre + " eliminado correctamente.");
            LOG.println("Se eliminó al jugador " + nombre + ".");
        } else {
            System.out.println("Jugador " + nombre + " no existe.");
        }
    }
    
    // Menú para listar las modificaciones posibles a un jugador.
    private static char menuModificarJugador(){
        char seleccion;
        System.out.println("A. Cambiar el tipo del jugador.");
        System.out.println("B. Cambiar el dinero del jugador.");
        System.out.println("C. Cambiar la categoria del jugador.");
        System.out.println("D. Cambiar las veces derrotado del jugador.");
        System.out.println("E. Cambiar las victorias del jugador.");
        System.out.println("Z. Salir.");
        seleccion = sc.next().charAt(0);
        seleccion = Character.toUpperCase(seleccion);
        return seleccion;
    }

    // Cambiar atributos a un jugador.
    private static void modificarJugador(){
        String nombre;
        boolean seguir = true;
        System.out.println("Ingrese el nombre del jugador a modificar.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        Jugador jugador = (Jugador) jugadores.obtener(nombre);
        if (jugador != null) {
            System.out.println("¿Qué desea hacer con " + nombre + "?");
            while (seguir) {
                switch (menuModificarJugador()) {
                    case 'A':
                        String nuevoTipo = elegirTipoDeJugador();
                        jugador.setTipo(nuevoTipo);
                        System.out.println("Tipo de " + nombre + " cambió a " + nuevoTipo);
                        LOG.println("Jugador " + nombre + " cambió su tipo a " + nuevoTipo);
                        break;
                    case 'B':
                        System.out.println("Ingrese la cantidad de dinero del jugador.");
                        int dinero = sc.nextInt();
                        jugador.setDinero(dinero);
                        System.out.println("Dinero de " + nombre + " cambió a " + dinero);
                        LOG.println("Jugador " + nombre + " cambió su dinero a " + dinero);
                        break;
                    case 'C':
                        String nuevaCategoria = elegirCategoriaDeJugador();
                        jugador.setCategoria(nuevaCategoria);
                        System.out.println("Categoría de " + nombre + " cambió a " + nuevaCategoria);
                        LOG.println("Jugador " + nombre + " cambió su categoría a " + nuevaCategoria);
                        break;
                    case 'D':
                        System.out.println("Ingrese la cantidad de veces derrotado del jugador");
                        int vecesDerrotado = sc.nextInt();
                        jugador.setVecesDerrotado(vecesDerrotado);
                        System.out.println("Derrotas de " + nombre + " cambió a " + vecesDerrotado);
                        LOG.println("Jugador " + nombre + " cambió su cantidad de derrotas a " + vecesDerrotado);
                        break;
                    case 'E':
                        System.out.println("Ingrese la cantidad de batallas ganadas del jugador");
                        int batallasGanadas = sc.nextInt();
                        jugador.setBatallasGanadas(batallasGanadas);
                        System.out.println("Batallas ganadas de " + nombre + " cambió a " + batallasGanadas);
                        LOG.println("Jugador " + nombre + " cambió su cantidad de victorias a " + batallasGanadas);
                        break;
                    case 'Z':
                        seguir = false;
                        break;
                    default:
                        System.out.println("Selección no válida, seleccione una correcta.");
                }
            }
        } else {
            System.out.println("Ese jugador no existe.");
        }
    }
    
    // Método para seleccionar qué se desea hacer a algún item.
    private static void ABMItem() {
        boolean seguir = true;
        char seleccion;
        while (seguir) {
            System.out.println("A. Alta de un item.");
            System.out.println("B. Baja de un item.");
            System.out.println("C. Modificación de un item.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("-- Dar de alta a un item --");
                    altaItem();
                    break;
                case 'B':
                    System.out.println("-- Dar de baja a un item --");
                    bajaItem();
                    break;
                case 'C':
                    System.out.println("-- Modificar a un item --");
                    modificarItem();
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
            LOG.flush();
        }
    }

    // Agregar manualmente un item pidiendo sus datos individualmente.
    private static void altaItem() {
        String codigo, nombre;
        int precio, puntosAtaque, puntosDefensa, copias;
        System.out.println("Ingrese el código del item.");
        codigo = sc.next();
        codigo = codigo.toUpperCase();
        if (!codigosItems.containsKey(codigo)) {
            System.out.println("Ingrese el nombre del item.");
            nombre = sc.next();
            System.out.println("Ingrese el precio del item.");
            precio = sc.nextInt();
            System.out.println("Ingrese los puntos de ataque del item.");
            puntosAtaque = sc.nextInt();
            System.out.println("Ingrese los puntos de defensa del item.");
            puntosDefensa = sc.nextInt();
            System.out.println("Ingrese las copias del item.");
            copias = sc.nextInt();
            Item item = new Item(codigo,nombre,precio,puntosAtaque,puntosDefensa,copias);
            Lista lista = (Lista) items.obtener(precio);
            // Como los items se ordenan por precio y puede haber más de un item con el mismo precio, en cada nodo del AVL Items se guarda una lista...
            // que contiene los items de dicho precio.
            if (lista == null) {    // No existe un item con ese precio. Creo una lista nueva.
                lista = new Lista();
                lista.insertar(item, 1);
                items.insertar(precio, lista);  // Agrego la lista (con el único item) al AVL.
            } else {    // Ya existía un item con ese precio. Agrego el item nuevo a la lista existente.
                lista.insertar(item, 1);
            }
            codigosItems.put(codigo, item); // Agrego el item al HashMap.
            System.out.println("Item código " + codigo + " agregado con éxito.");
            LOG.println("Agregado item código " + nombre + ".");
        } else {
            System.out.println("El item " + codigo + " ya existe.");
        }
    }
    
    // Eliminar un item del sistema.
    private static void bajaItem(){
        String codigo;
        System.out.println("Ingrese el código del item a eliminar.");
        codigo = sc.next();
        codigo = codigo.toUpperCase();
        Item item = (Item) codigosItems.get(codigo);
        if (item != null){
            int precio = item.getPrecio();
            Lista lista = (Lista) items.obtener(precio);
            lista.eliminar(item);
            if (lista.esVacia()){   // No quedan items con ese precio (acabo de eliminar el último), elimino la lista vacia.
                items.eliminar(precio);
            }
            codigosItems.remove(codigo);    // Elimino el item del HashMap.
            System.out.println("Item código " + codigo + " removido con éxito.");
            LOG.println("Removido item código " + codigo + ".");
        } else {
            System.out.println("El item con el código " + codigo + " no existe.");
        }
    }
    
    // Cambiar atributos a un item.
    private static void modificarItem(){
        String codigo;
        boolean seguir = true;
        System.out.println("Ingrese el código del item a modificar.");
        codigo = sc.next();
        codigo = codigo.toUpperCase();
        Item item = (Item) codigosItems.get(codigo);
        if (item != null){
            System.out.println("¿Qué desea hacer con el item código " + codigo + "?");
            while (seguir){
                switch (menuModificarItem()){
                    case 'A':
                        System.out.println("Ingrese el nuevo nombre para el item.");
                        String nuevoNombre = sc.next();
                        item.setNombre(nuevoNombre);
                        System.out.println("Nombre del item cambiado a " + nuevoNombre);
                        LOG.println("Item código " + codigo + " cambió su nombre a " + nuevoNombre);
                        break;
                    case 'B':
                        System.out.println("Ingrese los puntos de ataque del item.");
                        int puntosAtaque = sc.nextInt();
                        item.setPuntosAtaque(puntosAtaque);
                        System.out.println("Puntos de ataque cambiados a " + puntosAtaque);
                        LOG.println("Item código " + codigo + " cambió sus puntos de ataque a " + puntosAtaque);
                        break;
                    case 'C':
                        System.out.println("Ingrese los puntos de defensa del item.");
                        int puntosDefensa = sc.nextInt();
                        item.setPuntosDefensa(puntosDefensa);
                        System.out.println("Puntos de defensa cambiados a " + puntosDefensa);
                        LOG.println("Item código " + codigo + " cambió sus puntos de ataque a " + puntosDefensa);
                        break;
                    case 'D':
                        System.out.println("Ingrese la cantidad de copias del item.");
                        int cantidadCopias = sc.nextInt();
                        item.setCopias(cantidadCopias);
                        System.out.println("Cantidad de copias cambiadas a " + cantidadCopias);
                        LOG.println("Item código " + codigo + " cambió su cantidad de copias a " + cantidadCopias);
                        break;
                    case 'Z':
                        seguir = false;
                        break;
                    default:
                        System.out.println("Selección no válida, seleccione una correcta.");
                }
            }
        }
    }
    
    // Menú para listar las modificaciones posibles a un item.
    private static char menuModificarItem() {
        char seleccion;
        System.out.println("A. Cambiar el nombre a un item.");
        System.out.println("B. Cambiar los puntos de ataque de un item.");
        System.out.println("C. Cambiar los puntos de defensa de un item.");
        System.out.println("D. Cambiar la cantidad de copias de un item.");
        System.out.println("Z. Salir");
        seleccion = sc.next().charAt(0);
        seleccion = Character.toUpperCase(seleccion);
        return seleccion;
    }
    
    // Método para seleccionar qué se desea hacer con alguna locación.
    private static void ABMLocaciones(){
        boolean seguir = true;
        char seleccion;
        while (seguir){
            System.out.println("A. Alta de una locación.");
            System.out.println("B. Baja de una locación.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion){
                    case 'A':
                    System.out.println("-- Dar de alta una locación --");
                    altaLocacion();
                    break;
                case 'B':
                    System.out.println("-- Dar de baja una locación --");
                    bajaLocacion();
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
    }
    
    // Agregar manualmente una locación pidiendo sus datos individualmente.
    private static void altaLocacion(){
        String nombre;
        System.out.println("Ingrese el nombre de la locación.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (!localizaciones.contains(nombre)){
            mapa.insertarVertice(nombre);    // Agrego la locación al grafo.
            localizaciones.add(nombre);      // Agrego la locación al ArrayList
            System.out.println("Locación " + nombre + " agregada con éxito.");
            LOG.println("Agregada locación " + nombre + ".");
        } else {
            System.out.println("La localización " + nombre + " ya existe.");
        }
    }
    
    // Eliminar una locación del sistema.
    private static void bajaLocacion(){
        String nombre;
        System.out.println("Ingrese el nombre de la locación a eliminar.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (localizaciones.contains(nombre)){
            localizaciones.remove(nombre);  // Elimino la locación del ArrayList.
            mapa.eliminarVertice(nombre);   // Elimino el vértice y sus caminos del grafo.
            System.out.println("Localización " + nombre + " removida con éxito.");
            LOG.println("Removida localización " + nombre + ".");
        } else {
            System.out.println("La localización " + nombre + " no existe.");
        }
    }
    
    // Debug. Para ver las distintas estructuras del sistema en su estado actual.
    private static void mostrarSistema() {
        char seleccion;
        boolean seguir = true;
        while (seguir) {
        System.out.println("-- DEBUG --");
        System.out.println("A. Mostrar AVL de Jugadores.");
        System.out.println("B. Mostrar AVL de Items.");
        System.out.println("C. Mostrar Grafo (mapa).");
        System.out.println("D. Mostrar HashMap de Equipos.");
        System.out.println("E. Mostrar HashMap de Items.");
        System.out.println("F. Mostrar ColaPrioridad de equipos.");
        System.out.println("Z. Salir.");
        seleccion = sc.next().charAt(0);
        seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("AVL Jugadores: ");
                    System.out.println(jugadores.toString());
                    System.out.println("----------------");
                    break;
                case 'B':
                    System.out.println("AVL Items: ");
                    System.out.println(items.toString());
                    System.out.println("----------------");
                    break;
                case 'C':
                    System.out.println("Grafo (mapa):");
                    System.out.println(mapa.toString());
                    System.out.println("----------------");
                    break;
                case 'D':
                    System.out.println("HashMap Equipos: ");
                    System.out.println(equipos.toString());
                    System.out.println("----------------");
                    break;
                case 'E':
                    System.out.println("HashMap Item: ");
                    System.out.println(codigosItems.toString());
                    System.out.println("----------------");
                    break;
                case 'F':
                    System.out.println("ColaPrioridad de equipos: ");
                    System.out.println(armadoEquipos.toString());
                    System.out.println("----------------");
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, seleccione una correcta.");
            }
        }
    }

    // Creación del archivo LOG.
    private static void crearLog(){
        try {
            LOG = new PrintWriter(new File(".\\src\\dungeonsstructures\\log.txt"));
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error de tipo: " + e);
        }
    }
    
    // Crear una batalla entre dos equipos.
    private static void crearBatalla(){
        String nombreEquipo1, nombreEquipo2;
        System.out.println("Escriba el nombre de ambos equipos a pelear.");
        System.out.println("Equipos en el sistema: " + equipos.keySet().toString());
        System.out.println("Nombre del equipo1: ");
        nombreEquipo1 = sc.next();
        System.out.println("Nombre del equipo2: ");
        nombreEquipo2 = sc.next();
        // Los nombres de los equipos deben ser distintos. (No es válido que un equipo peleé contra él mismo)
        if (!nombreEquipo1.equals(nombreEquipo2)) {
            // Obtengo ambos equipos del HashMap.
            Equipo equipo1 = (Equipo) equipos.get(nombreEquipo1);
            Equipo equipo2 = (Equipo) equipos.get(nombreEquipo2);
            if (equipo1 != null && equipo2 != null) {   // Los equipos deben existir en el sistema. (ser distintos de null)
                String localizacionEquipo1, localizacionEquipo2;
                localizacionEquipo1 = equipo1.getLocalizacion();
                localizacionEquipo2 = equipo2.getLocalizacion();
                if (localizacionEquipo1.equals(localizacionEquipo2)) {  // Los equipos deben estar en la misma locación.
                    String categoriaEquipo1 = equipo1.getCategoria();
                    String categoriaEquipo2 = equipo1.getCategoria();
                    // De acuerdo a la puntación de la categoría de cada equipo determino cual tiene mayor/menor categoría. (y ataca primero)
                    int puntuacionCategoriaEquipo1 = obtenerPuntuacionCategoria(categoriaEquipo1);
                    int puntuacionCategoriaEquipo2 = obtenerPuntuacionCategoria(categoriaEquipo2);
                    if (puntuacionCategoriaEquipo1 <= puntuacionCategoriaEquipo2) {  // El equipo1 tiene menor o igual categoría al equipo2. Equipo1 empieza la pelea.
                        administrarBatalla(equipo1, equipo2);
                    } else {    // El equipo2 tiene menor categoría al equipo1. Equipo2 empieza la pelea.
                        administrarBatalla(equipo2, equipo1);
                    }
                } else {
                    System.out.println("Los equipos no están en la misma localización.");
                }
            } else {
                System.out.println("Al menos uno de los equipos ingresado no es válido..");
            }
        } else {
            System.out.println("¡No se puede crear una batalla entre el mismo equipo >:( !");
        }
    }
    
    // Devuelve un puntaje de acuerdo a la categoría ingresada. Utilizado para determinar qué equipo tiene menor/mayor categoría a otro.
    private static int obtenerPuntuacionCategoria(String categoria){
        int puntuacion = -1;
        switch (categoria) {
            case "NOVATO":
                puntuacion = 1;
                break;
            case "AFICIONADO":
                puntuacion = 2;
                break;
            case "PROFESIONAL":
                puntuacion = 3;
                break;
        }
        return puntuacion;
    }
    
    // Administra la batalla entre dos equipos. Siempre comienza el 1° jugador del equipo1. (tiene menor o igual categoria al equipo2)
    private static void administrarBatalla(Equipo equipo1, Equipo equipo2) {  
        String nombreEquipo1 = equipo1.getNombre(), nombreEquipo2 = equipo2.getNombre();
        LOG.println();  // Espacio en blanco en el LOG. Para separar el inicio y fin de una pelea.
        System.out.println("-- Comienza una batalla entre " + nombreEquipo1 + " y " + nombreEquipo2 + " --");
        LOG.println("-- Comienza una batalla entre " + nombreEquipo1 + " y " + nombreEquipo2 + " --");
        int cantidadAtaques = 0, derrotadosEquipo1 = 0, derrotadosEquipo2 = 0;
        Lista listaJE1 = equipo1.getJugadores(), listaJE2 = equipo2.getJugadores(); // listaJE1 o listaJE2 = Lista Jugadores del Equipo x.
        
        // Si un equipo es derrotado o si se llega a 4 instancias de ataques (que son 2 rondas), la pelea termina.
        while ((derrotadosEquipo1 != 3) && (derrotadosEquipo2 != 3) && cantidadAtaques < 4) { 
            
            if (cantidadAtaques % 2 == 0) { // Ataca el equipo1 al equipo2.
                LOG.println(">" + nombreEquipo1 + " ataca a " + nombreEquipo2 + "<");
                derrotadosEquipo2 = pelear(listaJE1, listaJE2, derrotadosEquipo2);
                
            } else {                        // Ataca el equipo2 al equipo1.
                LOG.println(">" + nombreEquipo2 + " ataca a " + nombreEquipo1 + "<");
                derrotadosEquipo1 = pelear(listaJE2, listaJE1, derrotadosEquipo1); 
            }
            cantidadAtaques++;
        }
        
        // Dependiendo del resultado de la pelea se reparten las distintas recompensas a los equipos.
        if (derrotadosEquipo2 == 3) {       // Ganó el equipo1.
            System.out.println("-- " + nombreEquipo1 + " GANA LA BATALLA." + " --");
            LOG.println("-- " + nombreEquipo1 + " GANA LA BATALLA." + " --");
            repartirRecompensas(listaJE1, listaJE2, 1000, -500);
        } else {                        
            if (derrotadosEquipo1 == 3) {   // Ganó el equipo2.
                System.out.println("-- " + nombreEquipo2 + " GANA LA BATALLA." + " --");
                LOG.println("-- " + nombreEquipo2 + " GANA LA BATALLA." + " --");
                repartirRecompensas(listaJE2, listaJE1, 1000, -500);
            } else {                        // Empate.
                System.out.println("-- " + "LA BATALLA TERMINA EN EMPATE" + " --");
                LOG.println("-- " + "LA BATALLA TERMINA EN EMPATE" + " --");
                repartirRecompensas(listaJE1, listaJE2, 500, 500);
            }
        }
        LOG.println("----------------- Fin de batalla -----------------");
    }

    // listaJE1 (lista Jugadores del Equipo1) ataca a la listaJE2 (lista Jugadores del Equipo2) que se defiende. 
    // El método devuelve la cantidad de jugadores que listaJE1 derrotó de listaJE2. Para así determinar al ganador. (Si lo hubiera)
    private static int pelear(Lista listaJE1, Lista listaJE2, int jugadoresEnemigosDerrotados) {
        Random r = new Random();    // Valor aleatorio usado en los ataques.
        
        // Tres ataques/defensas por equipo en cada ronda.
        for (int i = 1; i < 4; i++) {
            // A continución se busca un jugador válido para atacar o defender, según corresponda.
            // Los jugadores pertenecientes a listaJE1 atacaran y los de listaJE2 se defienden.
            Jugador jugadorEquipo1 = obtenerSiguienteJugador(listaJE1, i);
            Jugador jugadorEquipo2 = obtenerSiguienteJugador(listaJE2, i);
            
            if (jugadorEquipo2 != null) {   // Si jugadorEquipo2 no es nulo es porque el equipo2 aún tiene jugadores para defenderse. La batalla continúa.
                int puntosAtaque = calcularPuntosAtaque(jugadorEquipo1);
                int totalDefensa = calcularPuntosDefensa(jugadorEquipo2);
                double valorAleatorio = 0.5 + (1.5 - 0.5) * r.nextDouble(); // Valor aleatorio para el ataque.
                int totalAtaque = (int) (puntosAtaque * valorAleatorio);    // El ataque se ve afectado por un valor aleatorio entre 0,5 y 1,5.
                LOG.println(" " + jugadorEquipo1.getNombre() + "(" + totalAtaque + ")" + " ataca a " + jugadorEquipo2.getNombre() + "(" + totalDefensa + ")" + ". (" + (totalAtaque - totalDefensa) + ") total ataque.");
                
                if (totalAtaque > totalDefensa) {  // Ataque exitoso.
                    jugadorEquipo2.setSalud(jugadorEquipo2.getSalud() - (totalAtaque - totalDefensa)); // Se resta la salud perdida al atacado.
                    
                    // Si la salud del jugador atacado es menor a 0, ese jugador fue derrotado.
                    if (jugadorEquipo2.getSalud() <= 0) {
                        LOG.println(jugadorEquipo1.getNombre() + " derrotó a " + jugadorEquipo2.getNombre());
                        jugadoresEnemigosDerrotados++;                                              // Se derrotó a jugadorEquipo2.
                        jugadorEquipo1.setDinero(jugadorEquipo1.getDinero() + 1000);                // Bono de $1000 por derrotar a un jugador.
                        jugadorEquipo1.setBatallasGanadas(jugadorEquipo1.getBatallasGanadas() + 1); // Actualizar victorias.
                        jugadorEquipo2.setVecesDerrotado(jugadorEquipo2.getVecesDerrotado() + 1);   // Actualizar derrotas.
                    }
                }
            }
        }
        return jugadoresEnemigosDerrotados;
    }

    // Dada una lista y un índice, devuelve el 1° jugador disponible para pelear buscando a partir de ese índice.
    // Si no hay jugadores de la lista que puedan pelear, devuelve null. Se itera sobre la lista hasta un máximo de 3 veces para buscar. (son 3 jugadores por equipo)
    private static Jugador obtenerSiguienteJugador(Lista listaJugadores, int indice) {
        int i = 0;
        Jugador jugador = null;
        boolean encontrado = false;
        // Mientras no se haya encontrado a un jugador o se haya llegado a iterar 3 veces.
        while (!encontrado && i < 3) {
            jugador = (Jugador) listaJugadores.recuperar(indice);
            if (jugador.getSalud() > 0) {   // Encontró un jugador que puede pelear.
                encontrado = true;
            } else {                        // Seguír buscando jugador válido.  
                indice = (indice % 3) + 1;  // Se controla que el índice no se salga del rango de la lista.
                jugador = null;             // Se debe seguir buscando un jugador que pueda pelear.
                i++;
            }
        }
        return jugador;
    }
    
    // Se reparten las recompensas a cada miembro de equipo. Si hay un ganador, siempre son los jugadores de "listaJE1" (por como se invoca a este método).
    // En caso de empate no importa el orden de las listas ya que reciben exactamente la misma recompensa.
    private static void repartirRecompensas(Lista listaJE1, Lista listaJE2, int cantidadGanador, int cantidadPerdedor){
        int longitud = listaJE1.longitud();
        for (int i = 1; i < longitud + 1; i++) {
            Jugador jugadorGanador = (Jugador) listaJE1.recuperar(i);   // Obtengo un jugador de la lista de ganadores.
            Jugador jugadorPerdedor = (Jugador) listaJE2.recuperar(i);  // Obtengo un jugador de la lista de perdedores.
            LOG.println("Jugador " +jugadorGanador.getNombre() + " terminó la pelea con " + jugadorGanador.getSalud() + " puntos de salud.");
            LOG.println("Jugador " +jugadorPerdedor.getNombre() + " terminó la pelea con " + jugadorPerdedor.getSalud() + " puntos de salud.");
            jugadorGanador.setDinero(jugadorGanador.getDinero() + cantidadGanador);       // Actualizar dinero ganador.
            jugadorPerdedor.setDinero(jugadorPerdedor.getDinero() + cantidadPerdedor);    // Actualizar dinero perdedor.
            // Luego de la pelea, todos los jugadores recuperan su salud.
            jugadorGanador.setSalud(100);   
            jugadorPerdedor.setSalud(100);
        }
    }
    
    // Calcula los puntos de ataque de un jugador teniendo en cuenta su tipo, categoría e items.
    private static int calcularPuntosAtaque(Jugador jugador) {
        int puntosAtaque = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("GUERRERO")) { // El jugador es guerrero.
            puntosAtaque = 100;
        } else {                                    // El jugador es defensor.
            puntosAtaque = 25;
        }
        // Obtengo el multiplicador del jugador en base a su categoría y lo aplico al ataque final total.
        puntosAtaque = puntosAtaque + (puntosAtaque * devolverMultiplicador(jugador.getCategoria()));
        // Obtengo los puntos de ataque otorgados por los items y los sumo al ataque final total.
        puntosAtaque = puntosAtaque + calcularStatsItems(itemsDelJugador,'A',jugador.getNombre());
        return puntosAtaque;
    }
    
    // Calcula los puntos de defensa de un jugador teniendo en cuenta su tipo, categoría e items.
    private static int calcularPuntosDefensa(Jugador jugador) {
        int puntosDefensa = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("DEFENSOR")) { // El jugador es defensor.
            puntosDefensa = 90;
        } else {                                    // El jugador es guerrero.
            puntosDefensa = 25;
        }
        // Obtengo el multiplicador del jugador en base a su categoría y lo aplico a la defensa final total.
        puntosDefensa = puntosDefensa + (puntosDefensa * devolverMultiplicador(jugador.getCategoria()));
        // Obtengo los puntos de defensa otorgados por los items y los sumo a la defensa final total.
        puntosDefensa = puntosDefensa + calcularStatsItems(itemsDelJugador, 'D', jugador.getNombre());
        return puntosDefensa;
    }
    
        // Devuelve el multiplicador correspondiete a la categoría de un jugador.
    private static int devolverMultiplicador(String categoria){
        int multiplicador = 0;
        switch (categoria){
            case "NOVATO":
                multiplicador = 3;
                break;
            case "AFICIONADO":
                multiplicador = 4;
                break;
            case "PROFESIONAL":
                multiplicador = 5;
                break;
        }
        return multiplicador;
    }

    // Calcula ataque o defensa (depende del char) de una lista de items.
    private static int calcularStatsItems(Lista itemsDelJugador, char tipoStats, String nombreJugador) {
        int stats = 0;
        if (itemsDelJugador != null) {
            for (int i = 1; i <= itemsDelJugador.longitud(); i++) {
                if (tipoStats == 'A') {  // La estadistica de interés es el ataque que dan los items.
                    Item item = (Item) itemsDelJugador.recuperar(i);
                    stats = stats + item.getPuntosAtaque();
                    desgastarItem(item,itemsDelJugador,nombreJugador,i);   // Desgastar items del jugador que ataca.
                } else {                 // La estadistica de interés es la defensa que dan los items.
                    stats = stats + ((Item) itemsDelJugador.recuperar(i)).getPuntosDefensa();
                }
            }
        }
        return stats;
    }

    // Utilizado para desgastar los items luego de atacar con ellos.
    private static void desgastarItem(Item item, Lista itemsDelJugador, String nombreJugador, int posicion) {
        item.setPuntosAtaque(item.getPuntosAtaque() - 10);
        item.setPuntosDefensa(item.getPuntosDefensa() - 10);
        if (item.getPuntosAtaque() <= 0 && item.getPuntosDefensa() <= 0) {   // Si el item tiene 0 de ataque y defensa, debo quitarlo.
            itemsDelJugador.eliminar(posicion); // Quito el item de la lista.
            LOG.println(nombreJugador + " descartó " + item.getNombre());
        }
    }

    // Mostrar los datos de un equipo a partir de su nombre.
    private static void consultarEquipo(){
        String nombre;
        System.out.println("Equipos en el sistema: " + equipos.keySet().toString());
        System.out.println("Ingrese el nombre del equipo.");
        nombre = sc.next();
        Equipo equipo = (Equipo) equipos.get(nombre);
        if (equipo != null){
            System.out.println(equipo.toString());
        } else {
            System.out.println("Ese equipo no existe.");
        }
    }
    
    // Distintas consultas para realizar sobre items.
    private static void consultarItems(){
        char seleccion;
        boolean seguir = true;
        Lista lista;
        while (seguir) {
            System.out.println("Consultar sobre items: ");
            System.out.println("A. Dado un monto de dinero mostrar todos los items que puede comprar el jugador.");
            System.out.println("B. Dado un rango de dinero mostrar todos los items que puede comprar el jugador.");
            System.out.println("C. Dado un código de un item, mostrar sus atributos.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("Ingrese el monto.");
                    int monto = sc.nextInt();
                    lista = items.listarRango(0, monto);    // Obtengo una lista de items de valor con un rango desde 0 hasta 'monto'.
                    System.out.println(lista.toString());
                    break;
                case 'B':
                    System.out.println("Ingrese el monto mínimo.");
                    int montoMinimo = sc.nextInt();
                    System.out.println("Ingrese el monto máximo.");
                    int montoMaximo = sc.nextInt();
                    lista = items.listarRango(montoMinimo,montoMaximo);    // Obtengo una lista de items de valor con un rango desde 'montoMinimo' hasta 'montoMaximo'.
                    System.out.println(lista.toString());
                    break;
                case 'C':
                    System.out.println("Ingrese el código del item.");
                    String codigo = sc.next();
                    codigo = codigo.toUpperCase();
                    mostrarAtributosItem(codigo);
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
    }

    // Dado el código de un item muestro sus datos por pantalla.
    private static void mostrarAtributosItem(String codigo){
        Item item = (Item) codigosItems.get(codigo);    // Obtengo el item desde el HashMap ingresando su código.
        if (item != null){
            System.out.println(item.toString());
        } else {
            System.out.println("El item con el código " + codigo + " no existe.");
        }
    }
    
    // Distintas consultas para realizar sobre jugadores.
    private static void consultarJugadores() {
        char seleccion;
        boolean seguir = true;
        String nombre;
        while (seguir) {
            System.out.println("Consultar sobre jugadores: ");
            System.out.println("A. Dado un nombre de usuario, mostrar todos sus atributos.");
            System.out.println("B. Dada una subcadena, mostrar todos los nombres de usuarios que comienzan con esa subcadena.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("Ingrese el nombre del jugador.");
                    nombre = sc.next();
                    nombre = nombre.toUpperCase();
                    mostrarAtributosJugador(nombre);
                    break;
                case 'B':
                    System.out.println("Ingrese la subcadena.");
                    nombre = sc.next();
                    nombre = nombre.toUpperCase();
                    listarJugadoresSubcadena(nombre);
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
    }
    
    // Dado el nombre de un jugador muestro sus datos por pantalla.
    private static void mostrarAtributosJugador(String nombre){
        Jugador jugador = (Jugador) jugadores.obtener(nombre);      // Obtengo el jugador desde el AVL ingresando su nombre.
        if (jugador != null){
            System.out.println(jugador.toString());
            System.out.println("Items: " + jugador.getItems().toString());
        } else {
            System.out.println("Ese jugador no existe.");
        }
    }
    
    // Dada una subcadena muestro los jugadores cuyos nombres empiezan con esa dicha subcadena.
    private static void listarJugadoresSubcadena(String subcadena){
        Lista lista = jugadores.listar();
        int longitud = lista.longitud();
        String respuesta = "";
        for (int i = 1; i <= longitud; i++) {
            Jugador jugador = (Jugador) lista.recuperar(i);
            String nombre = jugador.getNombre();
            if (nombre.startsWith(subcadena)){
                respuesta = nombre + " - " + respuesta;
            }
        }
        if (!respuesta.equals("")){
            System.out.println("Nombre de jugadores que comienzan con " + subcadena + ": " + respuesta);
        } else {
            System.out.println("No se encontraron jugadores que empiecen con la subcadena " + subcadena);
        }
    }

    // Distintas consultas para realizar sobre locaciones.
    private static void consultarLocaciones() {
        boolean seguir = true;
        char seleccion;
        String locacionA, locacionB;
        Lista lista;
        while (seguir) {
            System.out.println("Consultar sobre locaciones: ");
            System.out.println("A. Dado una locación, mostrar a cuales puede moverse un equipo después de ganar una batalla allí");
            System.out.println("B. Obtener el camino para ir desde A hasta B en la menor distancia posible.");
            System.out.println("C. Obtener el camino que llegue de A a B pasando por la mínima cantidad de locaciones.");
            System.out.println("D. Obtener todos los caminos para llegar de A a B con menos de una cantidad X de km dada.");
            System.out.println("E. Obtener el camino para llegar de A a B que pase por menos locaciones y que no pase por una locación C dada.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion) {
                case 'A':
                    System.out.println("Ingrese el nombre de la locación.");
                    locacionA = sc.next();
                    locacionA = locacionA.toUpperCase();
                    listarUbicacionesAdyacentes(locacionA);
                    break;
                case 'B':
                    System.out.println("Ingrese el nombre de la locación A.");
                    locacionA = sc.next();
                    locacionA = locacionA.toUpperCase();
                    System.out.println("Ingrese el nombre de la locación B.");
                    locacionB = sc.next();
                    locacionB = locacionB.toUpperCase();
                    lista = mapa.encontrarCamino(locacionA, locacionB, 'D', -1, null);
                    System.out.println("El camino más corto de " + locacionA + " hasta " + locacionB + " es: ");
                    mostrarCamino(lista, 'D');
                    break;
                case 'C':
                    System.out.println("Ingrese el nombre de la locación A.");
                    locacionA = sc.next();
                    locacionA = locacionA.toUpperCase();
                    System.out.println("Ingrese el nombre de la locación B.");
                    locacionB = sc.next();
                    locacionB = locacionB.toUpperCase();
                    lista = mapa.encontrarCamino(locacionA, locacionB, 'L', -1, null);
                    System.out.println("El camino de " + locacionA + " hasta " + locacionB + " que pasa por menos locaciones es: ");
                    mostrarCamino(lista, 'L');
                    break;
                case 'D':
                    System.out.println("Ingrese el nombre de la locación A.");
                    locacionA = sc.next();
                    locacionA = locacionA.toUpperCase();
                    System.out.println("Ingrese el nombre de la locación B.");
                    locacionB = sc.next();
                    locacionB = locacionB.toUpperCase();
                    System.out.println("Ingrese la cantidad máxima de kms.");
                    int maximo = sc.nextInt();
                    lista = mapa.encontrarCamino(locacionA, locacionB, 'D', maximo,null);
                    System.out.println("El camino más corto de " + locacionA + " hasta " + locacionB + " es: ");
                    mostrarCamino(lista,'D');
                    break;
                case 'E':
                    System.out.println("Ingrese el nombre de la locación A.");
                    locacionA = sc.next();
                    locacionA = locacionA.toUpperCase();
                    System.out.println("Ingrese el nombre de la locación B.");
                    locacionB = sc.next();
                    locacionB = locacionB.toUpperCase();
                    System.out.println("Ingrese la locación prohibida.");
                    String locacionC = sc.next();
                    locacionC = locacionC.toUpperCase();
                    lista = mapa.encontrarCamino(locacionA, locacionB, 'L', -1, locacionC);
                    System.out.println("El camino de " + locacionA + " hasta " + locacionB + " que pasa por menos locaciones y no pasa por " + locacionC + " es: ");
                    mostrarCamino(lista, 'L');
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
    }
    
    // Dada una lista (de nombres de locaciones) y un char indicando: 'D' = distancia o 'L' = locaciones.
    // Se muestran las locaciones recorridas y dependiendo del char distancia o número de locaciones recorridas.
    private static void mostrarCamino(Lista lista, char letra){
        int longitud = lista.longitud();
        if (!lista.esVacia()) {
            // La lista se recorre de atras hacia delante porque se insertó siempre en posición 1 y se generó recursivamente.
            for (int i = longitud; i > 1; i--) {
                System.out.print(lista.recuperar(i) + " - ");
            }
            System.out.println();
            switch (letra){
                case 'D':
                    System.out.println("Con una longitud total de: " + lista.recuperar(1) + " kms.");
                    break;
                case 'L':
                    System.out.println("Pasando por: " + lista.recuperar(1) + " locaciones.");
                    break;
            }
        } else {
            System.out.println("No se pudo encontrar un camino que cumpla esa condición.");
        }
    }

    // Dado el nombre de una locación se muestran las locaciones adyacentes a ella.
    private static void listarUbicacionesAdyacentes(String nombreLocacion) {
        Lista lista = mapa.ubicacionesAdyacentes(nombreLocacion);   // Obtengo una lista de nodosVert.
        int longitud = lista.longitud();
        if (!lista.esVacia()) {
            System.out.print("Desde " + nombreLocacion + " se puede ir a: ");
            for (int i = 1; i <= longitud; i++) {
                NodoVert nodo = (NodoVert) lista.recuperar(i);
                System.out.print(nodo.getElem() + " - ");   // Concateno el nombre de las locaciones.
            }
        } else {    // Si la locación no existe o no hay locaciones adyacentes.
            System.out.print("No hay locaciones adyacentes a " + nombreLocacion);
        }
        System.out.println();
    }
    
    // Distintas consultas generales.
    private static void consultasGenerales() {
        char seleccion;
        boolean seguir = true;
        while (seguir) {
            System.out.println("Consultas generales: ");
            System.out.println("A. Mostrar un ranking de los jugadores con más batallas individuales ganadas.");
            System.out.println("B. Mostrar un listado de todos los ítems de los que hay sólo uno disponible.");
            System.out.println("Z. Salir.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            switch (seleccion){
                case 'A':
                    rankingJugadores();
                    break;
                case 'B':
                    mostrarItemsCopia();
                    break;
                case 'Z':
                    seguir = false;
                    break;
                default:
                    System.out.println("Selección no válida, elija una correcta.");
            }
        }
    }
    
    // Muestra un top de jugadores con más victorias.
    private static void rankingJugadores(){
        ArbolAVL ranking = jugadores.ranking();
        Lista lista = ranking.listar();
        System.out.println("Top 3 jugadores con más victorias: ");
        for (int i = 1; i <= 3; i++) {
            Jugador jugador = (Jugador) lista.recuperar(i);
            System.out.println(jugador.getNombre() + ": " + jugador.getBatallasGanadas());
        }
    }
    
    // Muestra los items del sistema de los que solo queda una copia.
    private static void mostrarItemsCopia(){
        Lista lista = items.listarItemsCopia();
        System.out.println("Listado items con una sola copia: ");
        System.out.println(lista.toString());
    }
    
    // Lee el .txt de los jugadores y devuelve tipo Scanner. De aquí se obtienen los datos de los jugadores.
    private static Scanner leerTxtJugadores() {
        Scanner datosJugadores = null;
        try {
            datosJugadores = new Scanner(new FileReader(".\\src\\dungeonsstructures\\jugadores.txt"));
            datosJugadores.useDelimiter(";");   // Separador de campos.
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosJugadores;
    }
    
    // Toma el Scanner anterior y lo procesa para ir creando los jugadores del .txt
    private static ArbolAVL crearJugadores(Scanner datosJugadores, ColaPrioridad armadoEquipos) {
        jugadores = new ArbolAVL();
        String nombre, tipo, categoria, codigosItemsJugador;
        Lista lista;
        while (datosJugadores.hasNext()) {      //Iterar hasta que no haya más lineas de datos.
            nombre = datosJugadores.next();
            nombre = nombre.toUpperCase();
            tipo = datosJugadores.next();
            categoria = datosJugadores.next();
            codigosItemsJugador = datosJugadores.next();
            datosJugadores.nextLine();  // Bajo a la siguiente línea (siguiente jugador).
            lista = parserItems(codigosItemsJugador);
            Jugador jugador = new Jugador(nombre, tipo, categoria, lista);
            jugadores.insertar(nombre, jugador);    // Agrego al jugador al árbol AVL.
            armadoEquipos.insertar(jugador, categoria.toUpperCase());   // Agrego al jugador a la cola de espera para armar equipos.
            jugadoresEsperandoEquipo++;  // Contador que uso después, a la hora de crear equipos.
            LOG.println("Jugador cargado: " + nombre);
        }
        return jugadores;
    }
    
    // Analiza los códigos de item que tiene un jugador cuando es cargado desde el txt y crea una lista con esos items asignándosela.
    private static Lista parserItems(String codigosItemsJugador){
        Lista lista = new Lista();
        codigosItemsJugador = codigosItemsJugador.substring(1); // Quito el "<".
        while (codigosItemsJugador.length() > 1){
            String codigo = codigosItemsJugador.substring(0,4);
            Item item = (Item) codigosItems.get(codigo);        // Con el código puedo obtener el item desde el HashMap.
            
            // Necesito clonar el item, caso contrario todos los jugadores tendrían una referencia al mismo item y
            // si realizan cambios a su durabilidad esta se vería reflejada en todas las referencias.
            Item nuevoItem = item.clone();  
            lista.insertar(nuevoItem, 1);
            codigosItemsJugador = codigosItemsJugador.substring(5); // Recorto el código ya analizado y lo preparo para analizar el siguiente.
        }
        return lista;
    }
    
    // Lee el .txt de los equipos y devuelve tipo Scanner. De aquí se obtienen los nombres de los equipos.
    private static Scanner leerTxtEquipos() {
        Scanner nombreEquipos = null;
        try {
            nombreEquipos = new Scanner(new FileReader(".\\src\\dungeonsstructures\\equipos.txt"));
            nombreEquipos.useDelimiter(";");   // Separador de campos.
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return nombreEquipos;
    }
    
    // Al iniciar el sistema este método obtiene los nombres de los equipos del txt y crea cada equipo con los jugadores de la cola de espera para un equipo.
    // También se usa al seleccionar la opción 'E' del menú principal. En ese caso primero verifica que haya 3 jugadores esperando por un equipo para poder armarlo.
    private static void crearEquipos(Scanner nombreEquipos) {
        if (jugadoresEsperandoEquipo >= 3) {
            while (jugadoresEsperandoEquipo >= 3) { // Si hay 3 o más jugadores esperando, se arma un equipo.
                Lista listaJugadores = new Lista();
                String nombreEquipo, categoria = "", localizacion;
                if (nombreEquipos != null) {    // Si aún no se acabaron los nombres del archivo equipos.txt, estos se ingresan automáticamente.
                    nombreEquipo = nombreEquipos.next();
                    nombreEquipos.nextLine();
                } else {                        // Si ya se acabaron los nombres del archivo equipos.txt, estos se ingresan manualmente.
                    System.out.println("Ingrese el nombre del equipo.");
                    nombreEquipo = sc.next();
                    System.out.println("Equipo creado con éxito.");
                }
                localizacion = localizacionAleatoria();   // Se debe asignar una localización aleatoria al crear un equipo.
                Equipo equipo = new Equipo(nombreEquipo, "", localizacion.toUpperCase(), listaJugadores);
                equipos.put(nombreEquipo, equipo);        // Agrego el equipo al HashMap.
                
                // Creo la lista de 3 jugadores y a medida que los agrego a ella reviso la categoría de cada uno para determinar la categoría del equipo.
                for (int i = 0; i < 3; i++) {
                    Jugador temp = (Jugador) armadoEquipos.obtenerFrente(); // Obtengo al jugador de la cola de espera para un equipo.
                    temp.setEquipo(equipo);                                 // Setteo el equipo al jugador.
                    listaJugadores.insertar(temp, 1);                       // Agrego al jugadora la lista.
                    // A continuación determino la categoría del equipo usando la categoría del jugador actual ('temp') y la categoría del jugador analizado antes.
                    // De esta forma me aseguro que el equipo tenga la menor categoría de sus integrantes.
                    categoria = determinarCategoria(categoria, temp.getCategoria());
                    jugadoresEsperandoEquipo--;         // Contador estático de jugadores esperando un equipo.
                    armadoEquipos.eliminarFrente();     // Quito al jugador de la cola de espera para un equipo.
                }
                equipo.setCategoria(categoria);
                LOG.println("Equipo cargado: " + nombreEquipo + " " + equipo.toString());
            }
        } else {
            System.out.println("No hay suficientes jugadores esperando para armar un equipo. Actualmente hay: " + jugadoresEsperandoEquipo);
        }
    }

    // Método auxiliar para determinar la categoria de un equipo.
    // (basado en la menor categoria de sus integrantes)
    private static String determinarCategoria(String categoriaEquipo, String categoriaJugador) {
        String categoriaNueva;
        if (!categoriaEquipo.equals("NOVATO")) {
            if (categoriaJugador.equals("NOVATO")) {
                categoriaNueva = "NOVATO";
            } else {
                if (categoriaEquipo.equals("PROFESIONAL") && categoriaJugador.equals("AFICIONADO")) {
                    categoriaNueva = "AFICIONADO";
                } else {
                    categoriaNueva = "PROFESIONAL";
                }
            }
        } else {
            categoriaNueva = "NOVATO";
        }
        return categoriaNueva;
    }

    // Elige una localización aleatoria de todas las que están en el ArrayList.
    private static String localizacionAleatoria(){
        Random random = new Random();
        int longitudArray = localizaciones.size();
        int resultado = random.nextInt(longitudArray);
        String localizacion = (String) localizaciones.get(resultado);
        return localizacion;
    }

    // Lee el .txt de los items y devuelve tipo Scanner. De aquí se obtienen los datos de los items.
    private static Scanner leerTxtItems(){
        Scanner datosItems = null;
        try{
            datosItems = new Scanner (new FileReader (".\\src\\dungeonsstructures\\items.txt"));
            datosItems.useDelimiter(";");   // Separador de campos.
            
        } catch(FileNotFoundException e){
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosItems;
    }
    
    // Toma el Scanner con los datos de los items y lo procesa para ir creando los items automáticamente.
    private static ArbolAVL crearItems(Scanner datosItems){
        items = new ArbolAVL();         // AVL que contiene los items. La clave es el precio.
        codigosItems = new HashMap();   // HashMap que contiene la tupla (código,item). Usado para recuperar un item dado su código.
        String codigo, nombre;
        int precio, puntosAtaque, puntosDefensa, copias;
        while (datosItems.hasNext()){   // Iterar hasta que no haya más lineas de datos.
            codigo = datosItems.next();
            nombre = datosItems.next();
            precio = Integer.parseInt(datosItems.next());
            puntosAtaque = Integer.parseInt(datosItems.next());
            puntosDefensa = Integer.parseInt(datosItems.next());
            copias = Integer.parseInt(datosItems.next());
            Item item = new Item(codigo,nombre,precio,puntosAtaque,puntosDefensa,copias);
            
            // Como los items se ordenan por precio y puede haber más de un item con el mismo precio, en cada nodo del AVL Items se guarda una lista...
            // que contiene los items de dicho precio.
            Lista lista = (Lista) items.obtener(precio);
            if (lista == null) {    // No existe un item con ese precio.
                lista = new Lista();
                lista.insertar(item, 1);
                items.insertar(precio, lista);
            } else {    // Ya existía un item con ese precio.
                lista.insertar(item, 1);
            }
            codigosItems.put(codigo, item); // Agrego el item al HashMap.
            datosItems.nextLine();          // Bajo a la siguiente línea. (siguiente item)
            LOG.println("Item cargado: " + nombre);
        }
        return items;
    }
    
    // Lee el .txt de locaciones y devuelve tipo Scanner. De aquí se obtienen los nombres de las locaciones.
    private static Scanner leerTxtLocalizaciones(){
        Scanner datosLocalizaciones = null;
        try{
            datosLocalizaciones = new Scanner (new FileReader (".\\src\\dungeonsstructures\\localizaciones.txt"));
            datosLocalizaciones.useDelimiter(";");   // Separador de campos.
        } catch(FileNotFoundException e){
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosLocalizaciones;
    }
    
    // Toma el Scanner con los datos de las locaciones y lo procesa para ir armando el Grafo.
    private static Grafo crearMapa(Scanner datosMapa){
        mapa = new Grafo();
        String nombre;
        while (datosMapa.hasNext()){       // Iterar hasta que no haya más lineas de datos.
            nombre = datosMapa.next();
            nombre = nombre.toUpperCase();
            datosMapa.nextLine();          // Bajo a la siguiente línea. (siguiente locación)
            mapa.insertarVertice(nombre);  // Inserto el vértice (locación) en el grafo.
            localizaciones.add(nombre);    // También agrego la locación al ArrayList.
            LOG.println("Localización cargada: " + nombre);
        }
        return mapa;
    }
    
    // Lee el .txt de los caminos y devuelve tipo Scanner. De aquí se obtienen los caminos del Grafo.
    private static Scanner leerTxtCaminos() {
        Scanner datosCaminos = null;
        try {
            datosCaminos = new Scanner(new FileReader(".\\src\\dungeonsstructures\\caminos.txt"));
            datosCaminos.useDelimiter(";");   // Separador de campos.
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosCaminos;
    }
    
    // Toma el Scanner con los datos de los caminos y lo procesa para ir armando el Grafo.
    private static void crearCaminos(Scanner datosCaminos){
        String desde, hasta;
        int distancia;
        while (datosCaminos.hasNext()){       // Iterar hasta que no haya más lineas de datos.
            desde = datosCaminos.next();
            desde = desde.toUpperCase();
            hasta = datosCaminos.next();
            hasta = hasta.toUpperCase();
            distancia = Integer.parseInt(datosCaminos.next());
            mapa.insertarArco(desde, hasta, distancia); // Inserta el camino en el Grafo.
            datosCaminos.nextLine();    // Bajo a la siguiente línea. (siguiente camino)
            LOG.println("Camino cargado: " + desde + " - " + hasta + " (" + distancia + "kms)");
        }
    }
    
}
