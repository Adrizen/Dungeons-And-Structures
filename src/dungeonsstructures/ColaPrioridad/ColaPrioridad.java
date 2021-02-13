package dungeonsstructures.ColaPrioridad;


/**
 *
 * @author Guillermo Andrés Pereyra.
 */

/* Se va a usar un arreglo para las 3 prioridades: Profesionales, Aficionados y Novatos. Ya que si las cantidad de
   prioridades son pocas (como en este caso) es más eficiente hacer la ColaPrioridad con arreglo que con heap. */
public class ColaPrioridad {
    private Cola[] arreglo = new Cola[3];
    

    public ColaPrioridad(){
        for (int i = 0; i < arreglo.length; i++) {  // Se inicializan las colas en cada casilla del arreglo.
            arreglo[i] = new Cola();
        }
    }
    
 /* En la posición 0 del arreglo va la Cola de PROFESIONALES (mayor prioridad), posición 1 los AFICIONADOS y
    luego los NOVATOS. */
    public boolean insertar(Object jugador, String categoria) {
        int prioridad = devolverPrioridadNumerica(categoria);
        arreglo[prioridad].poner(jugador);
        return true;
    }
    
    /* Método auxiliar de insertar (ver arriba).
       Este método toma un String de categoria (PROFESIONAL, AFICIONADO o NOVATO 
       y devuelve su correspondiente propridad númerica. */
    private int devolverPrioridadNumerica(String categoria) {
        int prioridad = -1;
        switch (categoria) {
            case "PROFESIONAL":
                prioridad = 0;
                break;
            case "AFICIONADO":
                prioridad = 1;
                break;
            case "NOVATO":
                prioridad = 2;
                break;
        }
        return prioridad;
    }

    // Quita el primer elemento que encuentra, empezando de la prioridad más alta a la más baja.
    public boolean eliminarFrente(){
        boolean exito = false;
        int i = 0;
        while (!exito && i < 4) {
            if (!arreglo[i].esVacia()) {
                arreglo[i].sacar();
                exito = true;
            } else {
                i++;
            }
        }
        return exito;
    }
    
    // Busca y devuelve el primer elemento que encuentra, empezando de la prioridad más alta.
    public Object obtenerFrente(){
        Object jugador = null;
        int i = 0;
        while (i < 3 && jugador == null){
            jugador = arreglo[i].obtenerFrente();
            i++;
        }
        return jugador;
    }
    
    // Busca alguna cola que no esté vacia en el arreglo, si no hay entonces la ColaPrioridad está vacia.
    public boolean esVacio(){
        boolean respuesta = true;
        int i = 0;
        while (respuesta && i < 3){
            if (!arreglo[i].esVacia()){
                respuesta = false;
            } else {
                i++;
            }
        }
        return respuesta;
    }

    @Override
    public String toString(){
        String cadenita = "";
        Cola[] aux = this.arreglo;
        int i = 0;
        while (i < 3) {
            cadenita = cadenita + "Prioridad" + i + ": ";
            cadenita = cadenita + aux[i].toString();
            cadenita = cadenita + "\n";
            i++;
        }
        return cadenita;
    }
    
}
