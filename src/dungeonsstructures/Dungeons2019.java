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
        int[] jugadoresEsperandoEquipo = new int[1];
        
        Scanner datosJugadores = leerTxtJugadores();    // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        ArbolAVL jugadores = crearJugadores(datosJugadores, armadoEquipos, jugadoresEsperandoEquipo); // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        System.out.println(jugadores.toString());   // debug.
        
        // Crear equipos desde la cola de prioridad.
        System.out.println(armadoEquipos.toString());
        
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        ArbolAVL items = crearItems(datosItems);    // Creo los items y los coloco en el AVL.
        //System.out.println(items.toString());   // debug.
        
        Scanner datosMapa = leerTxtLocalizaciones();
        ArrayList localizaciones = new ArrayList(); // Esto se usa a la hora de elegir aleatoriamente una localizacion al crear un equipo.
        Grafo mapa = crearMapa(datosMapa, localizaciones);
        
        HashMap equipos = crearEquipos(armadoEquipos,jugadoresEsperandoEquipo,localizaciones);
        System.out.println(equipos.toString());
        
        Scanner datosCaminos = leerTxtCaminos();
        crearCaminos(datosCaminos,mapa);
        //System.out.println(mapa.toString());
        
        //System.out.println(armadoEquipos.toString());   // debug.
    }
    
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
                jugadoresEsperandoEquipo[0]--;
                armadoEquipos.eliminarFrente();
            }
            localizacion = localizacionAleatoria(localizaciones);   // Se debe asignar una localización aleatoria al crear un equipo.
            Equipo equipo = new Equipo(nombreEquipo, categoria, localizacion.toUpperCase(), listaJugadores);
            equipos.put(nombreEquipo, equipo);
        }
        return equipos;
    }
    
    // Método auxiliar para determinar la menor categoria de un equipo.
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


