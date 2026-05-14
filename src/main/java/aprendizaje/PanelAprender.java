package aprendizaje;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelAprender extends JPanel {

    private MenuPrincipal menuPanel;

    public PanelAprender(MenuPrincipal menuPanel) { //menuPanel es la referencia del MenuPrincipal
        this.menuPanel = menuPanel;
        //Usamos GridLayout para que los botones sean del mismo tamaño y se apilen verticalmente con una separacion de 5px 
        setLayout(new GridLayout(0, 1, 0, 5));
        iniciarComponentes();

    }

    private void iniciarComponentes() {

        colocarComponentes();

    }

    private void colocarComponentes() {

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
        //Evento del botón para volver al menú principal
        ActionListener btnVolverMenu = new ActionListener() { //creamos evento

            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.mostrarMenu(); //llamamos a la referencia guardada del MenuPrincipal
            }

        };

        volverMenu.addActionListener(btnVolverMenu); //añadimos el evento al botón

        //Evento de los botones 'Hiragana 1', 'Hiragana 2', 'Hiragana 3' y 'Hiragana 4'
        ActionListener abrirLeccion = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JButton btn = (JButton) e.getSource();  //devuelve el objeto como un JButton (por el casting) que originó el evento (al pulsarlo)
                String texto = btn.getText(); //se obtiene el texto añadido al boton, ejem.'Hiragana 1'
                //para disparar el evento personalizado(mostrar una lista de lecciones dependiendo el boton)a cada botón se necesita obtener el numero que acompaña a 'Hiragana'
                //obtenemos este numero mediante el metodo substring para extraer la parte del texto en la que se encuentra el numero y lo convertimos a entero
                int numero = Integer.parseInt(texto.substring(texto.length() - 1));
                ListaLeccion lista = menuPanel.obtenerListaLeccion(numero); //asignamos la lista que el boton debe mostrar segun el numero que le corresponda
                PanelLeccion h1 = new PanelLeccion(menuPanel, lista, numero); //menuPanel se guarda como referencia en el constructor de PanelLeccion para regresar a panelAprender
                menuPanel.getContentPane().removeAll(); //removemos los elementos del frame
                menuPanel.getContentPane().add(h1); //añadimos el nuevo panel
                menuPanel.getContentPane().revalidate();
                menuPanel.getContentPane().repaint();
            }

        };

        //asignamos los eventos a los botones
        btnHiragana1.addActionListener(abrirLeccion);
        btnHiragana2.addActionListener(abrirLeccion);
        btnHiragana3.addActionListener(abrirLeccion);
        btnHiragana4.addActionListener(abrirLeccion);

    }

}
