package juego;

import javax.swing.*;

import aprendizaje.MenuPrincipal;

import java.awt.*;

public class panelPausa extends JPanel {

    private HiraganaEnemigo juego;
    private MenuPrincipal menu;

    public panelPausa(HiraganaEnemigo juego, JFrame frame, MenuPrincipal menu) {
        this.juego = juego;
        this.menu=menu;
        //setOpaque(false);
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180)); // fondo semitransparente

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;



        // Botón Continuar
        gbc.gridy = 1;
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 28));
        btnContinuar.setPreferredSize(new Dimension(220, 70));
        btnContinuar.addActionListener(e -> juego.continuarJuego());
        add(btnContinuar, gbc);

        // Botón Volver al Menú
        gbc.gridy = 2;
        JButton btnMenu = new JButton("Volver al Menú");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 28));
        btnMenu.setPreferredSize(new Dimension(300, 70));
        btnMenu.addActionListener(e -> juego.volverMenu());
        add(btnMenu, gbc);
    }
}