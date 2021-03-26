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
    
    public static void main(String[] args) { //La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
        crearLog();   // Creo el archivo LOG.txt
        
        ColaPrioridad armadoEquipos = new ColaPrioridad();  // "Cola de prioridad para los jugadores esperando entrar en un equipo."
        jugadoresEsperandoEquipo = 0;    // Cantidad de jugadores a la espera de formar parte de un equipo.
        
        // Items.
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        ArbolAVL items = crearItems(datosItems);    // Creo los items y los coloco en el AVL.
        
        // Jugadores.
        Scanner datosJugadores = leerTxtJugadores();    // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        ArbolAVL jugadores = crearJugadores(datosJugadores, armadoEquipos); // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        
        // Mapa.
        Scanner datosMapa = leerTxtLocalizaciones();
        ArrayList localizaciones = new ArrayList(); // Para elegir aleatoriamente una localizacion al crear un equipo.
        Grafo mapa = crearMapa(datosMapa, localizaciones);
        
        // Equipos.
        Scanner nombreEquipos = leerTxtEquipos();
        HashMap equipos = crearEquipos(armadoEquipos,localizaciones,nombreEquipos);
        
        // Caminos.
        Scanner datosCaminos = leerTxtCaminos();
        crearCaminos(datosCaminos,mapa);
        
        Jugador gops = new Jugador("GOPS","",""); //(String nombre, String tipo, String categoria)
        System.out.println(jugadores.obtener(gops).toString());
        
        Equipo equipo1 = (Equipo) equipos.get("Mercedes AMG e-Sports");
        Equipo equipo2 = (Equipo) equipos.get("Scuderia Ferrari Sport Elettronici");
        administrarBatalla(equipo1,equipo2);
        
        LOG.close();    // Cierro el LOG.txt
    }
    
    public static void menu(){
        boolean seguir = true;
        while (seguir){
            // Muchas weas.
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
        ArbolAVL jugadores = new ArbolAVL();
        String nombre, tipo, categoria;
        while (datosJugadores.hasNext()) { //Iterar hasta que no haya más lineas de datos.
            nombre = datosJugadores.next();
            tipo = datosJugadores.next();
            categoria = datosJugadores.next();
            datosJugadores.nextLine();  // Bajo a la siguiente línea.
            Jugador jugador = new Jugador(nombre,tipo,categoria);
            jugadores.insertar(jugador);    // Agrego al jugador al árbol AVL.
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
    
    public static HashMap crearEquipos(ColaPrioridad armadoEquipos, ArrayList localizaciones, Scanner nombreEquipos){
        HashMap equipos = new HashMap();
        while (jugadoresEsperandoEquipo >= 3){ // Si hay 3 o más jugadores esperando, se arma un equipo.
            Lista listaJugadores = new Lista();
            String nombreEquipo, categoria = "", localizacion;
            nombreEquipo = nombreEquipos.next();
            nombreEquipos.nextLine();
            localizacion = localizacionAleatoria(localizaciones);   // Se debe asignar una localización aleatoria al crear un equipo.
            Equipo equipo = new Equipo(nombreEquipo, categoria, localizacion.toUpperCase(), listaJugadores);
            equipos.put(nombreEquipo, equipo);
            for (int i = 0; i < 3; i++) {
                Jugador temp = (Jugador) armadoEquipos.obtenerFrente();
                temp.setEquipo(equipo);
                listaJugadores.insertar(temp, 1);
                categoria = determinarCategoria(categoria,temp.getCategoria());
                jugadoresEsperandoEquipo--;  // Contador estático de jugadores esperando un equipo.
                armadoEquipos.eliminarFrente();
            }
            LOG.println("Equipo cargado: " + nombreEquipo + equipo.toString());
        }
        return equipos;
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
    public static String localizacionAleatoria(ArrayList localizaciones){
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
        ArbolAVL items = new ArbolAVL();
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
            items.insertar(item);   // Agrego el item al árbol AVL.
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
    
    public static Grafo crearMapa(Scanner datosMapa, ArrayList localizaciones){
        Grafo mapa = new Grafo();
        String nombre;
        while (datosMapa.hasNext()){
            nombre = datosMapa.next();
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
    
    public static void crearCaminos(Scanner datosCaminos, Grafo mapa){
        String desde, hasta;
        int distancia;
        while (datosCaminos.hasNext()){
            desde = datosCaminos.next();
            hasta = datosCaminos.next();
            distancia = Integer.parseInt(datosCaminos.next());
            mapa.insertarArco(desde, hasta, distancia);
            datosCaminos.nextLine();
            LOG.println("Camino cargado: " + desde + " - " + hasta + " (" + distancia + "km)");
        }
    }
    
}


