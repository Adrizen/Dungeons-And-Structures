package dungeonsstructures;

import dungeonsstructures.ArbolAVL.ArbolAVL;
import dungeonsstructures.ColaPrioridad.ColaPrioridad;
import dungeonsstructures.Grafo.Grafo;
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


public class Dungeons2019 {
    
    static PrintWriter LOG;
    static int jugadoresEsperandoEquipo;
    static ArbolAVL items, jugadores;
    static Grafo mapa;
    static HashMap equipos, codigosItems;
    static ColaPrioridad armadoEquipos;
    static ArrayList localizaciones;
    static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) { //La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
        crearLog();   // Creo el archivo LOG.txt
        
        armadoEquipos = new ColaPrioridad();  // "Cola de prioridad para los jugadores esperando entrar en un equipo."
        jugadoresEsperandoEquipo = 0;    // Cantidad de jugadores a la espera de formar parte de un equipo.
        
        // Items.
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        items = crearItems(datosItems);    // Creo los items y los coloco en el AVL.
        
        // Jugadores.
        Scanner datosJugadores = leerTxtJugadores();    // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        jugadores = crearJugadores(datosJugadores, armadoEquipos); // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        
        // Mapa.
        Scanner datosMapa = leerTxtLocalizaciones();
        localizaciones = new ArrayList(); // Para elegir aleatoriamente una localizacion al crear un equipo.
        mapa = crearMapa(datosMapa);
        
        // Equipos.
        Scanner nombreEquipos = leerTxtEquipos();
        equipos = new HashMap();
        crearEquipos(nombreEquipos);
        
        // Caminos.
        Scanner datosCaminos = leerTxtCaminos();
        crearCaminos(datosCaminos);
        
        LOG.flush();    // Flush luego de cargar los .txt
        
        Equipo equipo1 = (Equipo) equipos.get("Mercedes AMG e-Sports");
        Equipo equipo2 = (Equipo) equipos.get("Scuderia Ferrari Sport Elettronici");
        administrarBatalla(equipo1,equipo2);
        
        menu();
        
        LOG.close();    // Cierro el LOG.txt
    }
    
    public static void menu(){
        boolean seguir = true;
        sc.useDelimiter("\r?\n");   // Scanner acepta espacios como parte de la entrada.
        char seleccion;
        while (seguir){
            System.out.println("--- Menú - Eliga una opción ---");
            System.out.println("A. ABM de jugadores.");
            System.out.println("B. ABM de items.");
            System.out.println("C. ABM Locaciones.");
            System.out.println("D. (Nope) Alta de un jugador en la cola de espera por un equipo.");
            System.out.println("E. Creación automática de un equipo.");
            
            System.out.println("L. Mostrar sistema.");
            System.out.println("Z. Salir del juego.");
            seleccion = sc.next().charAt(0);
            seleccion = Character.toUpperCase(seleccion);
            seguir = !seleccionarOpcion(seleccion);
            LOG.flush();
        }
    }

    public static boolean seleccionarOpcion(char seleccion) {
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
                System.out.println("KJJJJJJJJJJJJJJJJJJJ");
                break;
            case 'E':
                crearEquipos(null);
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
    public static void ABMJugador() {
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
    public static void altaJugador(){
        String nombre, tipo, categoria;
        System.out.println("Ingrese el nombre del jugador");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (!jugadores.pertenece(nombre)){  // Si no existe en el AVL pido sus datos y lo agrego.
            tipo = elegirTipoDeJugador();
            categoria = elegirCategoriaDeJugador();
            Jugador jugador = new Jugador(nombre,tipo,categoria);
            jugadores.insertar(nombre,jugador); // Agrego al jugador al AVL.
            armadoEquipos.insertar(jugador, categoria); // Agrego al jugador a la cola de prioridad usada para armar los equipos.
            jugadoresEsperandoEquipo++;
            System.out.println("Jugador " + nombre + " agregado con éxito.");
            LOG.println("Agregado jugador " + nombre + ".");
        } else {
            System.out.println("El jugador " + nombre + " ya existe.");
        }
    }
    
    public static String elegirTipoDeJugador(){
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
    
    public static String elegirCategoriaDeJugador(){
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
    public static void bajaJugador() {
        String nombre;
        System.out.println("Ingrese el nombre del jugador a eliminar.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        Jugador jugador = (Jugador) jugadores.obtener(nombre);
        if (jugador != null){
            jugadores.eliminar(nombre); // Elimino al jugador del AVL.
            Equipo equipoJugador = jugador.getEquipo(); // Obtengo el equipo del jugador.
            if (equipoJugador != null) {    // Si el jugador eliminado tenía equipo.
                equipos.remove(equipoJugador.getNombre()); // Su equipo ya no está completo y no puede pelear con otros equipos. Lo quito del HashMap.
                Lista listaJugadores = equipoJugador.getJugadores();
                for (int i = 1; i < 3; i++) {   // Los dos jugadores que quedaban se quedan sin equipo y vuelven a la cola de prioridad.
                    Jugador auxJugador = (Jugador) listaJugadores.recuperar(i);
                    auxJugador.setEquipo(null);
                    jugadoresEsperandoEquipo++;
                    String categoria = auxJugador.getCategoria();
                    armadoEquipos.insertar(auxJugador, categoria);  // Agrego al jugador a la cola de prioridad.
                }
            }
            System.out.println("Jugador " + nombre + " eliminado correctamente.");
            LOG.println("Se eliminó al jugador " + nombre + ".");
        } else {
            System.out.println("Jugador " + nombre + " no existe.");
        }
    }
    
    public static void modificarJugador(){
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
    
    // Menú para listar las modificaciones posibles a un jugador.
    public static char menuModificarJugador(){
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

    // Método para seleccionar qué se desea hacer a algún item.
    public static void ABMItem() {
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
    public static void altaItem() {
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
            // Como los items se ordenan por precio y puede haber más de un item con el mismo valor, en cada nodo del AVL Items se guarda una lista...
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
    
    public static void bajaItem(){
        String codigo;
        System.out.println("Ingrese el código del item a eliminar.");
        codigo = sc.next();
        codigo = codigo.toUpperCase();
        Item item = (Item) codigosItems.get(codigo);
        if (item != null){
            int precio = item.getPrecio();
            Lista lista = (Lista) items.obtener(precio);
            lista.eliminar(item);
            if (lista.esVacia()){   // No quedan items con ese precio, elimino la lista vacia.
                items.eliminar(precio);
            }
            codigosItems.remove(codigo);    // Elimino el item del HashMap.
            System.out.println("Item código " + codigo + " removido con éxito.");
            LOG.println("Removido item código " + codigo + ".");
        } else {
            System.out.println("El item con el código " + codigo + " no existe.");
        }
    }
    
    public static void modificarItem(){
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
    public static char menuModificarItem() {
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
    public static void ABMLocaciones(){
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
    
    public static void altaLocacion(){
        String nombre;
        System.out.println("Ingrese el nombre de la locación.");
        
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (!localizaciones.contains(nombre)){
            mapa.insertarVertice(nombre);
            localizaciones.add(nombre);
            System.out.println("Locación " + nombre + " agregada con éxito.");
            LOG.println("Agregada locación " + nombre + ".");
        } else {
            System.out.println("La localización " + nombre + " ya existe.");
        }
    }
    
    public static void bajaLocacion(){
        String nombre;
        System.out.println("Ingrese el nombre de la locación a eliminar.");
        nombre = sc.next();
        nombre = nombre.toUpperCase();
        if (localizaciones.contains(nombre)){
            localizaciones.remove(nombre);
            mapa.eliminarVertice(nombre);
            System.out.println("Localización " + nombre + " removida con éxito.");
            LOG.println("Removida localización " + nombre + ".");
        } else {
            System.out.println("La localización " + nombre + " no existe.");
        }
    }
    
    // Debug.
    public static void mostrarSistema() {
        char seleccion;
        boolean seguir = true;
        //TO DO: Seguir agregando.
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
                    System.out.println("HashMap Item");
                    System.out.println(codigosItems.toString());
                    System.out.println("----------------");
                    break;
                case 'F':
                    System.out.println("ColaPrioridad de equipos.");
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
    public static void crearLog(){
        try {
            LOG = new PrintWriter(new File(".\\src\\dungeonsstructures\\log.txt"));
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error de tipo: " + e);
        }
    }
    
    // Método principal que controla la batalla entre dos equipos.
    // Siempre comienza el 1° jugador del equipo1 (tiene <= categoria al equipo2).
    public static void administrarBatalla(Equipo equipo1, Equipo equipo2) {  
        String nombreEquipo1 = equipo1.getNombre(), nombreEquipo2 = equipo2.getNombre();
        LOG.println("-- Comienza una batalla entre " + nombreEquipo1 + " y " + nombreEquipo2 + " --");
        int cantidadAtaques = 0, derrotadosEquipo1 = 0, derrotadosEquipo2 = 0;
        Lista listaJE1 = equipo1.getJugadores(), listaJE2 = equipo2.getJugadores(); // listaJE1 o listaJE2 = Lista de Jugadores del Equipo n.
        
        // Si un equipo es derrotado o si se llega a 4 instancias de ataques (que son 2 rondas), la pelea termina.
        while ((derrotadosEquipo1 != 3) && (derrotadosEquipo2 != 3) && cantidadAtaques < 4) { 
            if (cantidadAtaques % 2 == 0) { // Ataca el equipo1 al equipo2.
                LOG.println(nombreEquipo1 + " ataca a " + nombreEquipo2);
                derrotadosEquipo2 = pelear(listaJE1, listaJE2, derrotadosEquipo2);
            } else {    // Ataca el equipo2 al equipo1.
                LOG.println(nombreEquipo2 + " ataca a " + nombreEquipo1);
                derrotadosEquipo1 = pelear(listaJE2, listaJE1, derrotadosEquipo1); 
            }
            cantidadAtaques++;
        }
        // Dependiendo del resultado de la pelea se reparten las distintas recompensas a los equipos.
        if (derrotadosEquipo2 == 3) {
            LOG.println(nombreEquipo1 + " GANA LA BATALLA.");
            repartirRecompensas(listaJE1, listaJE2, 1000, -500);
        } else {
            if (derrotadosEquipo1 == 3) {
                LOG.println(nombreEquipo2 + " GANA LA BATALLA.");
                repartirRecompensas(listaJE2, listaJE1, 1000, -500);
            } else {
                LOG.println("LA BATALLA TERMINA EN EMPATE");
                repartirRecompensas(listaJE1, listaJE2, 500, 500);
            }
        }
    }

    // listaJE1 ataca a la listaJE2 que se defiende. Devuelve la cantidad de jugadores que listaJE1 derrotó de listaJE2.
    private static int pelear(Lista listaJE1, Lista listaJE2, int jugadoresEnemigosDerrotados) {
        Random r = new Random();
        for (int i = 1; i < 4; i++) {   // Tres ataques/defensas por equipo en cada ronda.
            Jugador jugadorEquipo1 = obtenerSiguienteJugador(listaJE1, i);
            Jugador jugadorEquipo2 = obtenerSiguienteJugador(listaJE2, i);
            if (jugadorEquipo2 != null) {   // El jugadorEquipo2 aún no fue derrotado.
                int puntosAtaque = calcularPuntosAtaque(jugadorEquipo1);
                int puntosDefensa = calcularPuntosDefensa(jugadorEquipo2);
                double valorAleatorio = 0.5 + (1.5 - 0.5) * r.nextDouble(); // Valor aleatorio para el ataque.
                int totalAtaque = (int) (puntosAtaque * valorAleatorio);    // El ataque se ve afectado por un valor aleatorio entre 0,5 y 1,5.
                LOG.println(" " + jugadorEquipo1.getNombre() + "(" + totalAtaque + ")" + " ataca a " + jugadorEquipo2.getNombre() + "(" + puntosDefensa + ")" + ".(" + (totalAtaque - puntosDefensa) + ")");
                if (totalAtaque > puntosDefensa) {  // Ataque exitoso.
                    jugadorEquipo2.setSalud(jugadorEquipo2.getSalud() - (totalAtaque - puntosDefensa)); // Se resta la salud perdida al atacado.
                    if (jugadorEquipo2.getSalud() <= 0) {    // jugadorEquipo1 derrotó al jugadorEquipo2.
                        LOG.println(jugadorEquipo1.getNombre() + " derrotó a " + jugadorEquipo2.getNombre());
                        jugadoresEnemigosDerrotados++;  // Se derrotó a jugadorEquipo2.
                        jugadorEquipo1.setDinero(jugadorEquipo1.getDinero() + 1000);                // Bono de $1000 por derrotar a un jugador.
                        jugadorEquipo1.setBatallasGanadas(jugadorEquipo1.getBatallasGanadas() + 1); // Actualizar victorias.
                        jugadorEquipo2.setVecesDerrotado(jugadorEquipo2.getVecesDerrotado() + 1);   // Actualizar derrotas.
                    }
                }
            }
        }
        return jugadoresEnemigosDerrotados;
    }

    // Se reparten las recompensas a cada miembro de equipo. Si hay un ganador, siempre son los jugadores de "listaJE1".
    // En caso de empate no importa el orden de las listas ya que reciben exactamente la misma recompensa.
    private static void repartirRecompensas(Lista listaJE1, Lista listaJE2, int cantidadGanador, int cantidadPerdedor){
        int longitud = listaJE1.longitud();
        for (int i = 1; i < longitud + 1; i++) {
            Jugador jugadorGanador = (Jugador) listaJE1.recuperar(i);   // Obtengo un jugador de la lista de ganadores.
            Jugador jugadorPerdedor = (Jugador) listaJE2.recuperar(i);  // Obtengo un jugador de la lista de perdedores.
            jugadorGanador.setDinero(jugadorGanador.getDinero() + cantidadGanador);       // Actualizar dinero ganador.
            jugadorPerdedor.setDinero(jugadorPerdedor.getDinero() + cantidadPerdedor);    // Actualizar dinero perdedor.
            jugadorGanador.setSalud(100);   // Luego de la pelea, todos los jugadores recuperan su salud.
            jugadorPerdedor.setSalud(100);
        }
    }

    // Dada una lista y un indice, devuelve el 1° jugador disponible para pelear.
    // Si no hay jugadores de la lista que puedan pelear, devuelve null.
    private static Jugador obtenerSiguienteJugador(Lista listaJugadores, int indice) {
        int i = 0;
        Jugador jugador = null;
        boolean encontrado = false;
        while (!encontrado && i < 3) {
            jugador = (Jugador) listaJugadores.recuperar(indice);
            if (jugador.getSalud() > 0) {   // Encontró un jugador que puede pelear.
                encontrado = true;
            } else {    // Debe seguir buscando un jugador que pueda pelear.
                indice = (indice % 3) + 1;
                //System.out.println( jugador.getNombre() + " " + indice);        // debug
                jugador = null;
                i++;
            }
        }
        //System.out.println();   // debug
        return jugador;
    }

    private static int calcularPuntosAtaque(Jugador jugador) {
        int puntosAtaque = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("GUERRERO")) {
            puntosAtaque = 100;
        } else {    // El jugador es defensor.
            puntosAtaque = 25;
        }
        puntosAtaque = puntosAtaque + (puntosAtaque * devolverMultiplicador(jugador.getCategoria()));
        puntosAtaque = puntosAtaque + calcularStatsItems(itemsDelJugador,'A');
        return puntosAtaque;
    }
    
    private static int calcularPuntosDefensa(Jugador jugador) {
        int puntosDefensa = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("DEFENSOR")) {
            puntosDefensa = 90;
        } else {
            puntosDefensa = 25;
        }
        puntosDefensa = puntosDefensa + (puntosDefensa * devolverMultiplicador(jugador.getCategoria()));
        puntosDefensa = puntosDefensa + calcularStatsItems(itemsDelJugador, 'D');
        return puntosDefensa;
    }

    private static int calcularStatsItems(Lista itemsDelJugador, char tipoStats) {
        int stats = 0;
        if (itemsDelJugador != null) {
            int longitud = itemsDelJugador.longitud();
            for (int i = 0; i < longitud; i++) {
                if (tipoStats == 'A') {  // La estadistica de interés es el ataque que dan los items.
                    Item item = (Item) itemsDelJugador.recuperar(i);
                    stats = stats + item.getPuntosAtaque();
                    desgastarItems(item,itemsDelJugador);   // Desgastar items del jugador que ataca.
                } else {    // La estadistica de interés es la defensa que dan los items.
                    stats = stats + ((Item) itemsDelJugador.recuperar(i)).getPuntosDefensa();
                }
            }
        }
        return stats;
    }
    
    // Utilizado para desgastar los items luego de atacar con ellos.
    private static void desgastarItems(Item item, Lista itemsDelJugador){
        int longitudLista = itemsDelJugador.longitud();
        for (int i = 0; i < longitudLista; i++) {
            item.setPuntosAtaque(item.getPuntosAtaque() - 10);
            item.setPuntosDefensa(item.getPuntosDefensa() - 10);
            if (item.getPuntosAtaque() <= 0 && item.getPuntosDefensa() <= 0){   // Si el item tiene 0 de ataque y defensa, debo quitarlo.
                itemsDelJugador.eliminar(i); // Quito el item de la lista.
                LOG.println("Un jugador nazi descartó" + item.getNombre());
            }
        }
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
    
    // Lee el .txt de los jugadores y devuelve tipo Scanner.
    public static Scanner leerTxtJugadores() {
        Scanner datosJugadores = null;
        try {
            datosJugadores = new Scanner(new FileReader(".\\src\\dungeonsstructures\\jugadores.txt"));
            datosJugadores.useDelimiter(";");
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosJugadores;
    }
    
    // Toma el Scanner anterior y lo procesa para ir creando los jugadores del .txt
    public static ArbolAVL crearJugadores(Scanner datosJugadores, ColaPrioridad armadoEquipos) {
        jugadores = new ArbolAVL();
        String nombre, tipo, categoria;
        while (datosJugadores.hasNext()) { //Iterar hasta que no haya más lineas de datos.
            nombre = datosJugadores.next();
            nombre = nombre.toUpperCase();
            tipo = datosJugadores.next();
            categoria = datosJugadores.next();
            datosJugadores.nextLine();  // Bajo a la siguiente línea.
            Jugador jugador = new Jugador(nombre,tipo,categoria);
            jugadores.insertar(nombre,jugador);    // Agrego al jugador al árbol AVL.
            armadoEquipos.insertar(jugador, categoria.toUpperCase());   // Agrego al jugador a la cola de espera para armar equipos.
            jugadoresEsperandoEquipo++;  // Contador que uso después, a la hora de crear equipos.
            LOG.println("Jugador cargado: " + nombre);
        }
        return jugadores;
    }
    
    // Lee el .txt de los equipos y devuelve tipo Scanner. De aquí se obtienen los nombres de los equipos.
    public static Scanner leerTxtEquipos() {
        Scanner nombreEquipos = null;
        try {
            nombreEquipos = new Scanner(new FileReader(".\\src\\dungeonsstructures\\equipos.txt"));
            nombreEquipos.useDelimiter(";");
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return nombreEquipos;
    }
    
    public static void crearEquipos(Scanner nombreEquipos) {
        if (jugadoresEsperandoEquipo >= 3) {
            while (jugadoresEsperandoEquipo >= 3) { // Si hay 3 o más jugadores esperando, se arma un equipo.
                Lista listaJugadores = new Lista();
                String nombreEquipo, categoria = "", localizacion;
                if (nombreEquipos != null) {    // Si aún no se acabaron los nombres del archivo equipos.txt, se ingresan automáticamente.
                    nombreEquipo = nombreEquipos.next();
                    nombreEquipos.nextLine();
                } else {    // Si ya se acabaron los nombres del archivo equipos.txt, se ingresan manualmente.
                    System.out.println("Ingrese el nombre del equipo.");
                    nombreEquipo = sc.next();
                    System.out.println("Equipo creado con éxito.");
                }
                localizacion = localizacionAleatoria();   // Se debe asignar una localización aleatoria al crear un equipo.
                Equipo equipo = new Equipo(nombreEquipo, "", localizacion.toUpperCase(), listaJugadores);
                equipos.put(nombreEquipo, equipo);
                for (int i = 0; i < 3; i++) {
                    Jugador temp = (Jugador) armadoEquipos.obtenerFrente();
                    temp.setEquipo(equipo);
                    listaJugadores.insertar(temp, 1);
                    categoria = determinarCategoria(categoria, temp.getCategoria());
                    jugadoresEsperandoEquipo--;  // Contador estático de jugadores esperando un equipo.
                    armadoEquipos.eliminarFrente();
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
    public static String determinarCategoria(String categoriaEquipo, String categoriaJugador) {
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

    // Elige una localización aleatoria de todas las que están en el array.
    public static String localizacionAleatoria(){
        Random random = new Random();
        int longitudArray = localizaciones.size();
        int resultado = random.nextInt(longitudArray);
        String localizacion = (String) localizaciones.get(resultado);
        return localizacion;
    }

    public static Scanner leerTxtItems(){
        Scanner datosItems = null;
        try{
            datosItems = new Scanner (new FileReader (".\\src\\dungeonsstructures\\items.txt"));
            datosItems.useDelimiter(";");
            
        } catch(FileNotFoundException e){
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosItems;
    }
    
    public static ArbolAVL crearItems(Scanner datosItems){
        items = new ArbolAVL();
        codigosItems = new HashMap();
        String codigo, nombre;
        int precio, puntosAtaque, puntosDefensa, copias;
        while (datosItems.hasNext()){
            codigo = datosItems.next();
            nombre = datosItems.next();
            precio = Integer.parseInt(datosItems.next());
            puntosAtaque = Integer.parseInt(datosItems.next());
            puntosDefensa = Integer.parseInt(datosItems.next());
            copias = Integer.parseInt(datosItems.next());
            Item item = new Item(codigo,nombre,precio,puntosAtaque,puntosDefensa,copias);
            Lista lista = (Lista) items.obtener(precio);
            if (lista == null) {    // No existe un item con ese precio.
                lista = new Lista();
                lista.insertar(item, 1);
                items.insertar(precio, lista);
            } else {    // Ya existía un item con ese precio.
                lista.insertar(item, 1);
            }
            codigosItems.put(codigo, item); // Agrego el item al HashMap.
            datosItems.nextLine();  // Bajo a la siguiente línea.
            LOG.println(item.toString());
        }
        return items;
    }
    
    public static Scanner leerTxtLocalizaciones(){
        Scanner datosLocalizaciones = null;
        try{
            datosLocalizaciones = new Scanner (new FileReader (".\\src\\dungeonsstructures\\localizaciones.txt"));
            datosLocalizaciones.useDelimiter(";");
        } catch(FileNotFoundException e){
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosLocalizaciones;
    }
    
    public static Grafo crearMapa(Scanner datosMapa){
        mapa = new Grafo();
        String nombre;
        while (datosMapa.hasNext()){
            nombre = datosMapa.next();
            nombre = nombre.toUpperCase();
            datosMapa.nextLine();
            mapa.insertarVertice(nombre);
            localizaciones.add(nombre);
            LOG.println("Localización cargada: " + nombre);
        }
        return mapa;
    }

    public static Scanner leerTxtCaminos() {
        Scanner datosCaminos = null;
        try {
            datosCaminos = new Scanner(new FileReader(".\\src\\dungeonsstructures\\caminos.txt"));
            datosCaminos.useDelimiter(";");
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
        return datosCaminos;
    }
    
    public static void crearCaminos(Scanner datosCaminos){
        String desde, hasta;
        int distancia;
        while (datosCaminos.hasNext()){
            desde = datosCaminos.next();
            desde = desde.toUpperCase();
            hasta = datosCaminos.next();
            hasta = hasta.toUpperCase();
            distancia = Integer.parseInt(datosCaminos.next());
            mapa.insertarArco(desde, hasta, distancia);
            datosCaminos.nextLine();
            LOG.println("Camino cargado: " + desde + " - " + hasta + " (" + distancia + "kms)");
        }
    }
    
}


