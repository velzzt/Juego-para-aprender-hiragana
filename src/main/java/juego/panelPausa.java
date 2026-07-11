package juego;

import javax.swing.*;

import aprendizaje.MenuPrincipal;

import java.awt.*;

public class panelPausa extends JPanel {
    private HiraganaEnemigo juego;

    public panelPausa(HiraganaEnemigo juego, JFrame frame, MenuPrincipal menu) {
        this.juego = juego;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // apila verticalmente
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        add(Box.createVerticalGlue());

        // Botón Continuar
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 28));
        btnContinuar.setPreferredSize(new Dimension(220, 70));
        btnContinuar.setMaximumSize(new Dimension(220, 70));
        btnContinuar.setAlignmentX(CENTER_ALIGNMENT);
        btnContinuar.addActionListener(e -> juego.continuarJuego());
        add(btnContinuar);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Botón Volver al Menú
        JButton btnMenu = new JButton("Volver al Menú");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 28));
        btnMenu.setPreferredSize(new Dimension(300, 70));
        btnMenu.setMaximumSize(new Dimension(300, 70));
        btnMenu.setAlignmentX(CENTER_ALIGNMENT);
        btnMenu.addActionListener(e -> juego.volverMenu());
        add(btnMenu);

        add(Box.createVerticalGlue());
    }
}