package aprendizaje;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelLeccion extends JPanel {

   private JPanel panelAprender;
   private JPanel columnaComponentes;
   private String rutaGif;
   private String rutaAudio;
   private String texto;

    public PanelLeccion(JPanel panelAprender){
        this.panelAprender= panelAprender; //guardado como referencia

        setLayout(new BorderLayout(10,10));
        iniciarComponentes();

    }

    public PanelLeccion(int indice, String rutaGif, String texto, String rutaAudio){
        setLayout(new BorderLayout(10,10));
        iniciarComponentes();

    }

    private void iniciarComponentes(){

        colocarComponentes();
    }

    private void colocarComponentes(){

        //Panel central. En este panel va el gif, boton para reproducir audio y el label de texto
        columnaComponentes = new JPanel();
        columnaComponentes.setLayout(new BoxLayout(columnaComponentes, BoxLayout.Y_AXIS)); //panel con BoxLayout
        columnaComponentes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gif = new JLabel("gif");
        ImageIcon gifIcon = new ImageIcon(rutaGif);
        gif.setIcon(gifIcon);
        gif.setAlignmentX(CENTER_ALIGNMENT);
        gif.setBackground(Color.blue);
        columnaComponentes.add(gif);

        JButton btnReproducirAudio= new JButton("Reproducir audio");
        btnReproducirAudio.setAlignmentX(CENTER_ALIGNMENT);
        columnaComponentes.add(btnReproducirAudio);

        JLabel letra = new JLabel("letra");
        letra.setAlignmentX(CENTER_ALIGNMENT);
        columnaComponentes.add(letra);



        //Panel sur. En este panel hay una fila de botones: 'atrás', 'volver y 'Continuar'
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAtras = new JButton("Atrás");
        JButton btnVolver = new JButton("Volver");
        JButton btnContinuar = new JButton("Continuar");
        filaBotones.add(btnAtras);
        filaBotones.add(btnVolver);
        filaBotones.add(btnContinuar);

        //Colocar el panel central y panel sur en el panel principal 
        add(columnaComponentes, BorderLayout.CENTER);
        add(filaBotones, BorderLayout.SOUTH);

        ActionListener botonVolver = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelReferencia = panelAprender;
                PanelAprender panelAprender= new PanelAprender(panelReferencia);
                 removeAll();
                 add(panelAprender);
                 revalidate();
                 repaint();
            }
            
        };

        btnVolver.addActionListener(botonVolver);

       



    
    }
}