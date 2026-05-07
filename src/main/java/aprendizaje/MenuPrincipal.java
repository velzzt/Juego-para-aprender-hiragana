package aprendizaje;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame {

    public JPanel panel;
    
    public MenuPrincipal(){

        setSize(1280,800);
        setResizable(false);
        setTitle("Aprender hiragana");
        setDefaultCloseOperation(EXIT_ON_CLOSE); //para terminar el proceso
        setLocationRelativeTo(null); // ventana en el centro de la pantalla
        iniciarComponentes();
    }

    private void iniciarComponentes(){
        colocarPanel();
        colocarComponentes();

    }

    private void colocarPanel(){

        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
    }
    private void colocarComponentes(){

        JLabel titulo = new JLabel("Aprende Hiragana");
        titulo.setBounds(390,150,500,80);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD,50)); //en estilos pueode ser 0,1,2,3 
        panel.add(titulo);

        JButton btnAprender = new JButton("Aprender");
        btnAprender.setBounds(540, 350, 200, 40);
        panel.add(btnAprender);

        JButton btnJugar = new JButton("Jugar");
        btnJugar.setBounds(540, 450, 200, 40);
        panel.add(btnJugar);


        ActionListener e = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel menuPrincipal= panel; //guardamos una referencia de la ventana para usarlo en PanelAprender (boton volver)
                PanelAprender panelAprender = new PanelAprender(menuPrincipal);
                getContentPane().removeAll();
                getContentPane().add(panelAprender);
                getContentPane().revalidate();
                getContentPane().repaint();
            }
            
        };
        btnAprender.addActionListener(e);
    }

    
    
}

