package dungeonsstructures;

import dungeonsstructures.ArbolAVL.ArbolAVL;
import dungeonsstructures.ColaPrioridad.ColaPrioridad;
import dungeonsstructures.Grafo.Grafo;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.HashMap;

public class Dungeons2019 {

    public static void main(String[] args) { //La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
        ColaPrioridad armadoEquipos = new ColaPrioridad();  // "Cola de prioridad para los jugadores esperando entrar en un equipo."
        
        Scanner datosJugadores = leerTxtJugadores();    // Leo el .txt de Jugadores y devuelvo un tipo Scanner.
        ArbolAVL jugadores = crearJugadores(datosJugadores, armadoEquipos); // Creo los jugadores y los coloco en el AVL. También los pongo en la cola de prioridad.
        System.out.println(jugadores.toString());   // debug.
        
        // Crear equipos desde la cola de prioridad.
        
        
        Scanner datosItems = leerTxtItems();    // Leo el .txt de Items y devuelvo un tipo Scanner.
        ArbolAVL items = crearItems(datosItems);    // Creo los items y los coloco en el AVL.
        //System.out.println(items.toString());   // debug.
        
        Scanner datosMapa = leerTxtLocalizaciones();
        Grafo mapa = crearMapa(datosMapa);
        
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
        }
        return jugadores;
    }
    
    public static HashMap crearEquipos(ColaPrioridad armadoEquipos){
        HashMap equipos = new HashMap();
        
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
    
    public static Grafo crearMapa(Scanner datosMapa){
        Grafo mapa = new Grafo();
        String nombre;
        while (datosMapa.hasNext()){
            nombre = datosMapa.next();
            datosMapa.nextLine();
            mapa.insertarVertice(nombre);
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


