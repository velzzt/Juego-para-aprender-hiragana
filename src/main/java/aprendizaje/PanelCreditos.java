package aprendizaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelCreditos extends JPanel {
    private MenuPrincipal menu;

    public PanelCreditos(MenuPrincipal menu) {
        this.menu = menu;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel label = new JLabel("Créditos", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 48));
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.CENTER);

        JButton volver = new JButton("Volver al Menú");
        volver.addActionListener(e -> menu.mostrarMenu());
        add(volver, BorderLayout.SOUTH);
    }
}