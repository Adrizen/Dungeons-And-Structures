package dungeonsstructures.ColaPrioridad;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class test {

    public static void main(String[] args) {
        ColaPrioridad colaPrioridad = new ColaPrioridad();
        colaPrioridad.insertar("Jugador1", "NOVATO");
        colaPrioridad.insertar("Jugador2", "AFICIONADO");
        colaPrioridad.insertar("Jugador3", "NOVATO");
        colaPrioridad.insertar("Jugador4", "PROFESIONAL");
        System.out.println(colaPrioridad.toString());
        colaPrioridad.insertar("Jugador5", "NOVATO");
        System.out.println(colaPrioridad.toString());
        colaPrioridad.eliminarFrente();
        System.out.println(colaPrioridad.toString());
    }

}
