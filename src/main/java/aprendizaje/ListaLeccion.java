package aprendizaje;
import java.util.ArrayList;
import java.util.List;

//Clase que permite el manejo de una lista que guarda objetos de tipo Leccion

public class ListaLeccion {

        private List<Leccion>lecciones;
        
        //constructor
        public ListaLeccion(){
            lecciones= new ArrayList<>();
            
        }

        //metodo para agregar una leccion
        public void agregarLeccion(Leccion leccion){
            lecciones.add(leccion);
        }

        //metodo que retorna la lista de lecciones
        public List<Leccion> getLecciones(){

            return lecciones;
        }
        
}