package aprendizaje;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ContenedorLeccion extends JPanel{

    //Panel de las 'cartas' usando CardLayout
    //cada carta contiene un panel con su gif, audio y boton

    private CardLayout card;
    private List<PanelLeccion>lecciones = new ArrayList<>();
    private JPanel contenedorTarjetas;



    public ContenedorLeccion(){

        iniciarComponentes();   


        
    }

    public void iniciarComponentes(){
       cargarLecciones();

    }     

    public void cargarLecciones(){

         String[]rutaGif ={ "a_imagen.png", "a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png",
                            "a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png","a_imagen.png"};
        String[]texto={"a","i","u","e","o","a","a","a","a","a","a","a","a","a","a"};
        String[]rutaAudio={};

        for(int i=0; i<15;i++){

            PanelLeccion panel = new PanelLeccion(i,rutaGif[i],texto[i],rutaAudio[i]);
            lecciones.add(panel);
        }
    }
    
    private void crearCardLayout(){

        card= new CardLayout();
        contenedorTarjetas = new JPanel(card);

        for (int i=0; i<lecciones.size();i++){

            contenedorTarjetas.add(lecciones.get(i),"leccion "+i);
        }
        add(contenedorTarjetas,BorderLayout.CENTER);
    }
}
