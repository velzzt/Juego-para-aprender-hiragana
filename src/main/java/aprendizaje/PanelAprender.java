package aprendizaje;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelAprender extends JPanel{

   
    private JPanel menuPanel;


    public PanelAprender(JPanel menuPanel){
        this.menuPanel= menuPanel;

        setLayout(new GridLayout(0,1,0,5));
        iniciarComponentes();
        
    }

     private void iniciarComponentes(){
        
        colocarComponentes();

    }

    private void colocarComponentes(){


        JButton btnHiragana1 = new JButton("Hiragana 1");
        add(btnHiragana1);

        JButton btnHiragana2 = new JButton("Hiragana 2");
        add(btnHiragana2);

        JButton btnHiragana3 = new JButton("Hiragana 3");
        add(btnHiragana3);

        JButton btnHiragana4 = new JButton("Hiragana 4");
        add(btnHiragana4);

        JButton volverMenu = new JButton("Volver al menú principal");
        add(volverMenu);

        //EVENTOS

        //Botón para volver al menú principal

        ActionListener btnVolverMenu = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PanelAprender.this);
                if (frame != null) {
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(menuPanel);
                    frame.getContentPane().revalidate();
                    frame.getContentPane().repaint();
                }
            
            }
            
        };

        volverMenu.addActionListener(btnVolverMenu);

        //Botón Hiragana 1

        ActionListener botonHiragana = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               PanelLeccion h1 = new PanelLeccion(menuPanel);
               removeAll();
               add(h1);
               revalidate();
               repaint();
            }
            
        };

       btnHiragana1.addActionListener(botonHiragana);
       btnHiragana2.addActionListener(botonHiragana);
       btnHiragana3.addActionListener(botonHiragana);
       btnHiragana4.addActionListener(botonHiragana);

    }
    
}
