/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aprendizaje;

/**
 *
 * @author Jack
 */
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;

public class PanelRepaso extends JPanel {

    private MenuPrincipal menu;

    private JLabel lblPregunta;

    private JButton btnOpcion1;
    private JButton btnOpcion2;
    private JButton btnOpcion3;
    private JButton btnOpcion4;

    // Controla la pregunta actual
    private int indice = 0;

    private String[][] preguntas;

    public PanelRepaso(MenuPrincipal menu, int numeroBoton) {

        this.menu = menu;

        setLayout(new BorderLayout(20, 20));

        // Hiragama 1
        if (numeroBoton == 1) {

            preguntas = new String[][]{
                {"a.png", "i ", "a", "u", "e", "a"},// la respuesta correcta es a
                {"i.png", "u", "a", "i", "o", "i"},// la respuedsta correcta es i
                {"u.png", "e", "u", "a", "i", "u"}// la respuesta correcta es u
            };
        }

        // Hiragama 2
        if (numeroBoton == 2) {

            preguntas = new String[][]{
                {"e.png", "o", "ka", "e", "a", "e"},// la respuesta correcta es e
                {"o.png", "e", "u", "ka", "o", "o"},// la respuesta correcta es o
                {"ka.png", "i", "ka", "o", "e", "ka"}// la respues correcta es ka
            };
        }

        iniciarComponentes();

        mostrarPregunta();
    }

    private void iniciarComponentes() {

        //Titulo
        JLabel titulo = new JLabel(
                "Selecciona la alternativa correcta",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 35));

        add(titulo, BorderLayout.NORTH);

        // Label donde se muestra el hiragana
        lblPregunta = new JLabel("", SwingConstants.CENTER);
        lblPregunta.setFont(new Font("SansSerif", Font.BOLD, 120));

        add(lblPregunta, BorderLayout.CENTER);

        // Panel para las opciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 20, 20));

        btnOpcion1 = new JButton();
        btnOpcion2 = new JButton();
        btnOpcion3 = new JButton();
        btnOpcion4 = new JButton();

        panelBotones.add(btnOpcion1);
        panelBotones.add(btnOpcion2);
        panelBotones.add(btnOpcion3);
        panelBotones.add(btnOpcion4);

        add(panelBotones, BorderLayout.SOUTH);

        ActionListener evento = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JButton boton = (JButton) e.getSource();

                // Aqui se verifica si la opcion seleccionada es correcta
                // La respuesta correcta se guarda en la posición [5]
                if (boton.getText().equals(preguntas[indice][5])) {

                    indice++;

                    // Mostrar siguiente pregunta
                    if (indice < preguntas.length) {

                        mostrarPregunta();

                    } else {

                        mostrarFinal();
                    }
                }
            }
        };

        btnOpcion1.addActionListener(evento);
        btnOpcion2.addActionListener(evento);
        btnOpcion3.addActionListener(evento);
        btnOpcion4.addActionListener(evento);
    }

    // Muestra la pregunta actual
    private void mostrarPregunta() {

        ImageIcon icono = new ImageIcon(
                getClass().getResource("/letras_png/" + preguntas[indice][0])
        );
        
        Image imagen = icono.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);

        lblPregunta.setIcon(new ImageIcon(imagen));

        btnOpcion1.setText(preguntas[indice][1]);
        btnOpcion2.setText(preguntas[indice][2]);
        btnOpcion3.setText(preguntas[indice][3]);
        btnOpcion4.setText(preguntas[indice][4]);
    }

    // Pantalla final del repaso
    private void mostrarFinal() {

        removeAll();

        setLayout(new BorderLayout());

        JLabel lblFinal = new JLabel("¡Repaso completado!", SwingConstants.CENTER);
        lblFinal.setFont(new Font("SansSerif", Font.BOLD, 40));

        JButton volver = new JButton("Volver");

        volver.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                menu.mostrarPanelAprender();
            }
        });

        add(lblFinal, BorderLayout.CENTER);
        add(volver, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}
