package aprendizaje;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Este panel Tarjeta se añade en el panel central de PanelLeccion
public class Tarjeta extends JPanel{

    private CardLayout card; 
    private List<Leccion>lecciones; //lista que guarda objetos de tipo Leccion
    
    public Tarjeta(List<Leccion>lecciones){
        this.lecciones=lecciones;

        card = new CardLayout(); //creamos el CardLayout
        setLayout(card); // le asignamos un layout de tipo CardLayout a Tarjeta
        crearTarjetas();
        
    }
    //Creamos el 'molde' de Tarjeta que contiene el gif,audio y el label de texto
    public void crearTarjetas(){

        //Se crea un panel para cada tarjeta y se le añaden sus componentes (gif, boton para reproducir el audio y el label de texto con la pronunciacion en español del caracter)

        for(int i=0;i<lecciones.size();i++){

        JPanel p = new JPanel(); 

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); //panel con BoxLayout para organizar los componentes en una sola columna sin espacio entre ellos
        p.setAlignmentX(Component.CENTER_ALIGNMENT); //alineamos los componentes en el centro del panel 'p'
        

        //GIF
        JLabel gif = new JLabel();
        ImageIcon image = new ImageIcon(lecciones.get(i).getRutaGIf()); //obtenemos la imagen de la lista
        //añadimos el gif al label 'gif' redimensionando la imagen original a 500 x 300 px
        gif.setIcon(new ImageIcon(image.getImage().getScaledInstance(500,300, Image.SCALE_SMOOTH))); 
        gif.setAlignmentX(CENTER_ALIGNMENT);
        p.add(gif); //añadimos el gif al panel 'p'
        p.add(Box.createVerticalStrut(50)); //añadimos espacio entre el gif y el boton
        


        //AUDIO
        JButton btnReproducirAudio= new JButton();
        btnReproducirAudio.setSize(80,40); //asignamos tamaño
        ImageIcon icon = new ImageIcon("play_icon.png"); //creamos una imagen que usaremos como icono del boton
        //añadimos icono al boton
        btnReproducirAudio.setIcon(new ImageIcon(icon.getImage().getScaledInstance(btnReproducirAudio.getWidth(), btnReproducirAudio.getHeight(), Image.SCALE_SMOOTH)));
        btnReproducirAudio.setAlignmentX(CENTER_ALIGNMENT);
        p.add(btnReproducirAudio); //añadimos el boton al panel 'p'
        p.add(Box.createVerticalStrut(40)); //añadimos espacio entre el boton y el label


        //ROMAJI muestra como suenan las silabas japonesas) 
        JLabel letra = new JLabel();
        letra.setText(lecciones.get(i).getRomaji());
        letra.setAlignmentX(CENTER_ALIGNMENT);

        p.add(letra);//añadimos el label a 'p'

        add(p,"tarjeta "+(i+1)); //agregamos el panel 'p' a Tarjeta
        }

    } 

    //metodos que permiten cambiar de tarjetas
    public void mostrarSiguiente() { card.next(this); }
    public void mostrarAnterior() { card.previous(this); }   
}
