
package dungeonsstructures.ArbolAVL;

/**
 *
 * @author Guillermo Andrés Pereyra.
 */
public class test {


    public static void main(String[] args) {
        ArbolAVL arbolito = new ArbolAVL();
        // Eliminar sin cambio de raíz y rotaciones: der-izq(26).
        arbolito.insertar(15,null);
        arbolito.insertar(6,null);
        arbolito.insertar(50,null);
        arbolito.insertar(4,null);
        arbolito.insertar(7,null);
        arbolito.insertar(71,null);
        arbolito.insertar(23,null);
        arbolito.insertar(1,null);
        arbolito.insertar(5,null);
        System.out.println("Original: " + '\n' + arbolito.toString());
        Comparable valorAEliminar = 6;
        arbolito.eliminar(valorAEliminar);
        System.out.println("Luego de eliminar el : " + valorAEliminar + '\n' + arbolito.toString());
        
    }
    
    /*
    --------------------Pruebas insertar(elem)----------------
        //rotDer (con cambio de raíz). done
        arbolito.insertar(14);
        arbolito.insertar(7);
        arbolito.insertar(1);
    
        //rotIzq (con cambio de raiz). done
        arbolito.insertar(14);
        arbolito.insertar(15);
        arbolito.insertar(16);
    
        //rotIzq-Der (sin cambio de raíz). done
        arbolito.insertar(75);
        arbolito.insertar(20);
        arbolito.insertar(80);
        arbolito.insertar(15);
        arbolito.insertar(77);
        arbolito.insertar(93);
        arbolito.insertar(18);
    
        //rotDer-Izq (sin cambio de raíz). done
        arbolito.insertar(75);
        arbolito.insertar(18);
        arbolito.insertar(80);
        arbolito.insertar(15);
        arbolito.insertar(20);
        arbolito.insertar(85);
        arbolito.insertar(83);
    
        //rotDer. (cambio de raiz aquí)
        //rotDer. done
        arbolito.insertar(14);
        arbolito.insertar(7);
        arbolito.insertar(20);
        arbolito.insertar(3);
        arbolito.insertar(8);
        arbolito.insertar(2);
        arbolito.insertar(1);
    
        //rotIzq. (cambio de raiz aquí)
        //rotIzq. done
        arbolito.insertar(14);
        arbolito.insertar(7);
        arbolito.insertar(20);
        arbolito.insertar(16);
        arbolito.insertar(25);
        arbolito.insertar(30);
        arbolito.insertar(33);
    
        //rotIzq (sin cambio de raiz). done
        arbolito.insertar(20);
        arbolito.insertar(14);
        arbolito.insertar(25);
        arbolito.insertar(7);
        arbolito.insertar(16);
        arbolito.insertar(30);
        arbolito.insertar(45);
    
        //rotDer (sin cambio de raiz). done
        arbolito.insertar(7);
        arbolito.insertar(3);
        arbolito.insertar(14);
        arbolito.insertar(2);
        arbolito.insertar(8);
        arbolito.insertar(20);
        arbolito.insertar(1);
    
        //rotIzq-Der (con cambio de raíz). done
        arbolito.insertar(30);
        arbolito.insertar(25);
        arbolito.insertar(27);
    
        //rotDer-Izq (con cambio de raíz). done.
        arbolito.insetar(30);
        arbolito.insertar(38);
        arbolito.insertar(35);
        -----------------------------------------------------
        ---------------Pruebas eliminar(elem)----------------
        // Dos subarboles. (ambos hojas, sin rotaciones)
        arbolito.insertar(14);
        arbolito.insertar(11);
        arbolito.insertar(20);
        arbolito.insertar(10);
        arbolito.insertar(13);
        arbolito.insertar(19);
        arbolito.insertar(23);
        arbolito.eliminar(20);
    
        // Dos subarboles. (ambos hojas, sin rotaciones)
        arbolito.insertar(14);
        arbolito.insertar(11);
        arbolito.insertar(20);
        arbolito.insertar(10);
        arbolito.insertar(13);
        arbolito.insertar(19);
        arbolito.eliminar(11);
    
        // Eliminar sin cambio de raiz y rotación simple derecha. (Elimino un nodo a la izquierda de la raíz)
        arbolito.insertar(14);
        arbolito.insertar(11);
        arbolito.insertar(20);
        arbolito.insertar(5);
        arbolito.insertar(12);
        arbolito.insertar(19);
        arbolito.insertar(25);
        arbolito.insertar(4);
        arbolito.insertar(7);
        arbolito.insertar(13);
        arbolito.insertar(23);
        arbolito.insertar(30);
        arbolito.insertar(6);
        System.out.println("Original: " + '\n' + arbolito.toString());
        arbolito.eliminar(11);
        System.out.println("Luego de eliminar el 11: " + '\n' + arbolito.toString());
    
        // Eliminar sin cambio de raíz y rotación simple derecha. (Elimino un nodo a la derecha de la raíz)
        arbolito.insertar(14);
        arbolito.insertar(11);
        arbolito.insertar(25);
        arbolito.insertar(5);
        arbolito.insertar(12);
        arbolito.insertar(19);
        arbolito.insertar(30);
        arbolito.insertar(4);
        arbolito.insertar(7);
        arbolito.insertar(13);
        arbolito.insertar(17);
        arbolito.insertar(22);
        arbolito.insertar(27);
        arbolito.insertar(40);
        arbolito.insertar(6);
        //arbolito.insertar(21);
        System.out.println("Original: " + '\n' + arbolito.toString());
        arbolito.eliminar(25);
        System.out.println("Luego de eliminar el 25: " + '\n' + arbolito.toString());
    
    
        // Eliminar con cambio de raíz y rotaciones: izq-der(80), der(75).
        arbolito.insertar(75);
        arbolito.insertar(18);
        arbolito.insertar(80);
        arbolito.insertar(14);
        arbolito.insertar(20);
        arbolito.insertar(77);
        arbolito.insertar(93);
        arbolito.insertar(13);
        arbolito.insertar(15);
        arbolito.insertar(25);
        arbolito.insertar(78);
        arbolito.insertar(16);
        System.out.println("Original: " + '\n' + arbolito.toString());
        arbolito.eliminar(93);
        System.out.println("Luego de eliminar el 93: " + '\n' + arbolito.toString());
        
        -----------------------------------------------------
    */

}
