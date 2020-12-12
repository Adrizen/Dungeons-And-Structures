package dungeonsstructures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dungeons2019 {

    public static void main(String[] args) { //La idea es tener un .txt para cada elemento del proyecto. Jugadores, Items, Lugares, etc.
        leerTxtJugadores();
    }

    public static void leerTxtJugadores() {
        try {
            Scanner datosJugadores = new Scanner(new FileReader(".\\src\\dungeonsstructures\\jugadores.txt"));
            datosJugadores.useDelimiter(";");
            Jugador[] jugadores = crearJugadores(datosJugadores);
            for (int i = 0; i < jugadores.length; i++) {
                System.out.println(jugadores[i].toString()); 
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error tipo: " + e.toString());
        }
    }
    
    
    public static Jugador[] crearJugadores(Scanner datosJugadores) {
        Jugador[] jugadores = new Jugador[3];
        int i = 0;
        String jota, nombre, tipo, categoria, equipo;
        while (datosJugadores.hasNext()) { //Iterar hasta que no haya más lineas de datos.
            jota = datosJugadores.next();
            nombre = datosJugadores.next();
            tipo = datosJugadores.next();
            categoria = datosJugadores.next();
            equipo = datosJugadores.next();
            Jugador jugador = new Jugador(nombre,tipo,categoria,null);
            jugadores[i] = jugador;
            i++;
        }
        return jugadores;
    }

}
