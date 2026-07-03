package aprendizaje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
public class PanelLeccion extends JPanel {

    private MenuPrincipal menuPrincipal;
    private Tarjeta tarjeta; //panel con CardLayout
    private int numeroHiragama;

    public PanelLeccion(MenuPrincipal menuPanel, ListaLeccion lista, int numeroHiragama) {
        this.menuPrincipal = menuPanel; //guardado como referencia
        this.numeroHiragama = numeroHiragama;//
        tarjeta = new Tarjeta(lista.getLecciones()); //tarjeta obtiene la lista con objetos de Leccion
        //asiganamos un BorderLayout a PanelLeccion para dividir el contenedor en 5 regiones (de los cuales usaremos 2, centro y sur)
        setLayout(new BorderLayout(20, 20));
        // Se añade un margen exterior y mayor separación entre componentes
        // para mejorar la presentación de la pantalla
        setBackground(new Color(220, 240, 220));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        colocarComponentes();

    }

    private void colocarComponentes() {

        // Se añade un borde decorativo alrededor de la tarjeta para integrarla
        // con el nuevo estilo visual del menú
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 180), 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        //Colocar el panel tarjeta y el panel filaBotones en el PanelLeccion
        add(tarjeta, BorderLayout.CENTER);

        // ajuste de separación entre botones para no empujar la tarjeta
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        filaBotones.setOpaque(false);
        filaBotones.setBorder(new EmptyBorder(12,0,12,0));

        JButton btnAnterior = crearBotonEstilizado("Anterior");
        JButton btnVolver = crearBotonEstilizado("Volver");
        JButton btnSiguiente = crearBotonEstilizado("Siguiente");

        filaBotones.add(btnAnterior);
        filaBotones.add(btnVolver);
        filaBotones.add(btnSiguiente);

        add(filaBotones, BorderLayout.SOUTH);

        //Evento para el botonVolver
        ActionListener botonVolver = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelAprender(); // regresa a PanelAprender
            }

        };

        btnVolver.addActionListener(botonVolver); //añadimos el evento al boton

        //Añadir eventos que permiten cambiar de tarjetas(las que contienen el gif, audio y label de texto) al pulsar los botones 'Anterior' y 'Siguiente'
        btnAnterior.addActionListener(e -> tarjeta.mostrarAnterior());
        btnSiguiente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // Si aún hay tarjetas disponibles
                if (!tarjeta.esUltimaTarjeta()) {

                    tarjeta.mostrarSiguiente();

                } else {

                    // Cuando terminan las tarjetas se abre el repaso
                    PanelRepaso panel = new PanelRepaso(menuPrincipal, numeroHiragama);

                    menuPrincipal.getContentPane().removeAll();
                    menuPrincipal.getContentPane().add(panel);
                    menuPrincipal.getContentPane().revalidate();
                    menuPrincipal.getContentPane().repaint();
                }
            }
        });
        
    }

    // Se personaliza la apariencia del JButton manteniendo intacto
    // su comportamiento y los eventos asociados
    private JButton crearBotonEstilizado(String texto) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btn.setBackground(new Color(110, 180, 80));
    btn.setForeground(Color.WHITE);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setFocusPainted(false);
    btn.setMargin(new Insets(8, 20, 8, 20));
    btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
    return btn;
    }
}
