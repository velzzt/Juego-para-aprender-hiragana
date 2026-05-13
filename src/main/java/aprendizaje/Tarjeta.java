package aprendizaje;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Este panel Tarjeta se añade en el panel central de PanelLeccion
public class Tarjeta extends JPanel {

    private CardLayout card;
    private List<Leccion> lecciones; //lista que guarda objetos de tipo Leccion
    private int indiceActual=1;
    public Tarjeta(List<Leccion> lecciones) {
        this.lecciones = lecciones;

        card = new CardLayout(); //creamos el CardLayout
        setLayout(card); // le asignamos un layout de tipo CardLayout a Tarjeta
        crearTarjetas();

    }

    //Creamos el 'molde' de Tarjeta que contiene el gif,audio y el label de texto
    public void crearTarjetas() {

        //Se crea un panel para cada tarjeta y se le añaden sus componentes (gif, boton para reproducir el audio y el label de texto con la pronunciacion en español del caracter)
        for (int i = 0; i < lecciones.size(); i++) {

            JPanel p = new JPanel();

            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); //panel con BoxLayout para organizar los componentes en una sola columna sin espacio entre ellos
            p.setAlignmentX(Component.CENTER_ALIGNMENT); //alineamos los componentes en el centro del panel 'p'

            //GIF
            JLabel gif = new JLabel();
            gif.setAlignmentX(CENTER_ALIGNMENT);

            URL url = getClass().getResource(lecciones.get(i).getRutaGif());

            System.out.println("GIF URL: " + url);

            if (url != null) {

                ImageIcon icon = new ImageIcon(url);

                gif.setIcon(icon);

            } else {
                gif.setText("NO SE ENCUENTRA GIF");
            }

            gif.setPreferredSize(new Dimension(500, 300));
            gif.setHorizontalAlignment(JLabel.CENTER);

            p.add(gif);
            p.add(Box.createVerticalStrut(50));

            //AUDIO
            JButton btnReproducirAudio = new JButton();
            btnReproducirAudio.setSize(80, 40); //asignamos tamaño
            ImageIcon icon = new ImageIcon("play_icon.png"); //creamos una imagen que usaremos como icono del boton
            //añadimos icono al boton
            btnReproducirAudio.setIcon(new ImageIcon(icon.getImage().getScaledInstance(btnReproducirAudio.getWidth(), btnReproducirAudio.getHeight(), Image.SCALE_SMOOTH)));
            btnReproducirAudio.setAlignmentX(CENTER_ALIGNMENT);
            
            //Eliminar el borde que aparece al hacer click
            btnReproducirAudio.setFocusPainted(false);
                        
            //Evento de reproducir audio
            Leccion leccionActual = lecciones.get(i);
            btnReproducirAudio.addActionListener(e -> reproducirSonido(leccionActual.getRutaAudio()));            
            
            p.add(btnReproducirAudio); //añadimos el boton al panel 'p'
            p.add(Box.createVerticalStrut(40)); //añadimos espacio entre el boton y el label
            
            //ROMAJI muestra como suenan las silabas japonesas) 
            JLabel letra = new JLabel();
            letra.setText(lecciones.get(i).getRomaji());
            letra.setAlignmentX(CENTER_ALIGNMENT);
            
            //Diseño de las vocales en español
            letra.setFont(new java.awt.Font("SansSerif",java.awt.Font.BOLD,35));

            p.add(letra);//añadimos el label a 'p'

            add(p, "tarjeta" + (i + 1)); //agregamos el panel 'p' a Tarjeta
        }

    }

    //metodos que permiten cambiar de tarjetas
    public void mostrarSiguiente() {
       if(indiceActual<lecciones.size()){
        indiceActual++;
        card.show(this, "tarjeta"+indiceActual);
       }

    }

    public void mostrarAnterior() {
        if(indiceActual>0){
            indiceActual--;
            card.show(this, "tarjeta"+indiceActual);
        }
    }
    
    //Metodo para reproducir el audio
    private void reproducirSonido(String rutaAudio) {
        try {
            URL url = getClass().getResource(rutaAudio);
            if (url != null) {
                javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
                javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } else {
                System.out.println("No se encontró el archivo de audio: " + rutaAudio);
            }
        } catch (Exception ex) {
            System.out.println("Error al reproducir audio.");
            ex.printStackTrace();
        }
    }
}
