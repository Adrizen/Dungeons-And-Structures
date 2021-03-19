package dungeonsstructures;

import dungeonsstructures.ArbolAVL.ArbolAVL;
import dungeonsstructures.ColaPrioridad.ColaPrioridad;
import dungeonsstructures.Grafo.Grafo;
import dungeonsstructures.Lista.Lista;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;

public class Dungeons2019 {

    public static void main(String[] args) { //La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
        ColaPrioridad armadoEquipos = new ColaPrioridad();  // "Cola de prioridad para los jugadores esperando entrar en un equipo."
        int[] jugadoresEsperandoEquipo = new int[1];    // Una forma de saber cuantos jugadores están esperando ser parte de un equipo. Si hay 3 (o más) se arma un team.
        
        Scanner datosJugadores = leerTxtJugadores();    // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        ArbolAVL jugadores = crearJugadores(datosJugadores, armadoEquipos, jugadoresEsperandoEquipo); // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        //System.out.println(jugadores.toString());   // debug.
        
        // Crear equipos desde la cola de prioridad.
        //System.out.println(armadoEquipos.toString());
        
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        ArbolAVL items = crearItems(datosItems);    // Creo los items y los coloco en el AVL.
        //System.out.println(items.toString());   // debug.
        
        Scanner datosMapa = leerTxtLocalizaciones();
        ArrayList localizaciones = new ArrayList(); // Esto se usa a la hora de elegir aleatoriamente una localizacion al crear un equipo.
        Grafo mapa = crearMapa(datosMapa, localizaciones);
        
        HashMap equipos = crearEquipos(armadoEquipos,jugadoresEsperandoEquipo,localizaciones);
        //System.out.println(equipos.toString());
        
        Scanner datosCaminos = leerTxtCaminos();
        crearCaminos(datosCaminos,mapa);
        //System.out.println(mapa.toString());
        
        //System.out.println(armadoEquipos.toString());   // debug.
        
        Equipo equipo1 = (Equipo) equipos.get("-ADRIZEN-ATHEMAS-HANUM-");
        Equipo equipo2 = (Equipo) equipos.get("-MORPEN-GOPS-DOU-");
        iniciarBatalla(equipo1,equipo2);
    }
    
    public static void menu(){
        
    }
    
    public static void iniciarBatalla(Equipo equipo1, Equipo equipo2){  // Siempre comienza el 1° jugador del equipo1 (tiene <= categoria al equipo2).
        int cantidadRondas = 0; // limite máximo = 2 rondas.
        Lista listaJE1 = equipo1.getJugadores(), listaJE2 = equipo2.getJugadores(); // listaJugadoresEquipoX = Lista de jugadores del equipo x
        boolean seguir = true;
        while (seguir && cantidadRondas <= 2) {
            pelear(listaJE1,listaJE2);
        }
    }
    
    public static void pelear(Lista listaJE1, Lista listaJE2) {
        Random r = new Random();
        for (int i = 1; i < 4; i++) {
            Jugador jugadorEquipo1 = obtenerSiguienteJugador(listaJE1, i);
            Jugador jugadorEquipo2 = obtenerSiguienteJugador(listaJE2, i);
            if (jugadorEquipo1 != null) {
                if (jugadorEquipo2 != null) {
                    int puntosAtaque = calcularPuntosAtaque(jugadorEquipo1);
                    int puntosDefensa = calcularPuntosDefensa(jugadorEquipo2);
                    double valorAleatorio = 0.5 + (1.5 - 0.5) * r.nextDouble(); // Valor aleatorio para el ataque.
                    System.out.println(String.format("%.2f", valorAleatorio));  // Double to String format. "2f" son dos decimales.
                    int totalAtaque = (int) (puntosAtaque * valorAleatorio);    // El ataque se ve afectado por un valor aleatorio entre 0,5 y 1,5.
                    if (totalAtaque > puntosDefensa) {  // Ataque exitoso.
                        System.out.println("Ataque tripiante");
                        System.out.println("Ataque: " + totalAtaque + " Defensa: " + puntosDefensa);
                        jugadorEquipo2.setSalud(jugadorEquipo2.getSalud() - (totalAtaque - puntosDefensa)); // Se resta la salud perdida al atacado.

                    } else {    // Ataque fallido.
                        System.out.println("Ataque no tripiante");
                        System.out.println("Ataque: " + totalAtaque + " Defensa: " + puntosDefensa);
                    }
                    // pierden durabilidad los items.
                } else {
                    // equipo1 win
                }
            } else {
                // equipo2 win.
            }

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
                System.out.println( jugador.getNombre() + " " + indice);        // debug
                i++;
            }
        }
        System.out.println();   // debug
        return jugador;
    }

    private static int calcularPuntosAtaque(Jugador jugador){
        int puntosAtaque = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("GUERRERO")){
            puntosAtaque = 100;
        } else {    // El jugador es defensor.
            puntosAtaque = 25;
        }
        puntosAtaque = puntosAtaque + (puntosAtaque * devolverMultiplicador(jugador.getCategoria()));
        puntosAtaque = puntosAtaque + calcularStatsItems(itemsDelJugador,'A');
        return puntosAtaque;
    }
    
    private static int calcularPuntosDefensa(Jugador jugador){
        int puntosDefensa = 0;
        Lista itemsDelJugador = jugador.getItems();
        if (jugador.getTipo().equals("DEFENSOR")){
            puntosDefensa = 90;
        } else {
            puntosDefensa = 25;
        }
        puntosDefensa = puntosDefensa + (puntosDefensa * devolverMultiplicador(jugador.getCategoria()));
        puntosDefensa = puntosDefensa + calcularStatsItems(itemsDelJugador,'D');
        return puntosDefensa;
    }
    
        private static int calcularStatsItems(Lista itemsDelJugador, char tipoStats) {
        int stats = 0;
        if (itemsDelJugador != null) {
            int longitud = itemsDelJugador.longitud();
            for (int i = 0; i < longitud; i++) {
                if (tipoStats == 'A'){  // La estadistica de interés es el ataque que dan los items.
                    stats = stats + ((Item) itemsDelJugador.recuperar(i)).getPuntosAtaque();
                } else {    // La estadistica de interés es la defensa que dan los items.
                    stats = stats + ((Item) itemsDelJugador.recuperar(i)).getPuntosDefensa();
                }
            }
        }
        return stats;
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
        
        for (int i = 0; i < 3; i++) {      // Máximo dos rondas.
            for (int j = 0; j < 4; j++) {   // Máximo de jugadores.
                
            }
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
    public static ArbolAVL crearJugadores(Scanner datosJugadores, ColaPrioridad armadoEquipos, int[] jugadoresEsperandoEquipo) {
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
            jugadoresEsperandoEquipo[0]++;  // Contador que uso después, a la hora de crear equipos.
        }
        return jugadores;
    }
    
    public static HashMap crearEquipos(ColaPrioridad armadoEquipos, int[] jugadoresEsperandoEquipo, ArrayList localizaciones){
        HashMap equipos = new HashMap();
        while (jugadoresEsperandoEquipo[0] >= 3){ // Si hay 3 o más jugadores esperando, se arma un equipo.
            Lista listaJugadores = new Lista();
            String nombreEquipo = "-", categoria = "", localizacion;
            for (int i = 0; i < 3; i++) {
                Jugador temp = (Jugador) armadoEquipos.obtenerFrente();
                listaJugadores.insertar(temp, 1);
                nombreEquipo = nombreEquipo + temp.getNombre() + "-";   // El nombre del equipo será el nombre de los jugadores separados por un "-".
                categoria = determinarCategoria(categoria,temp.getCategoria());
                jugadoresEsperandoEquipo[0]--;  // Contador estático de jugadores esperando un equipo.
                armadoEquipos.eliminarFrente();
            }
            localizacion = localizacionAleatoria(localizaciones);   // Se debe asignar una localización aleatoria al crear un equipo.
            Equipo equipo = new Equipo(nombreEquipo, categoria, localizacion.toUpperCase(), listaJugadores);
            equipos.put(nombreEquipo, equipo);
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
        }
    }
    
}


