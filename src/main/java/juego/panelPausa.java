package juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import aprendizaje.MenuPrincipal;

public class panelPausa extends JPanel {
    private HiraganaEnemigo juego;
    private Font fuentePixel;

    public panelPausa(HiraganaEnemigo juego, JFrame frame, MenuPrincipal menu) {
        this.juego = juego;
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180));

        fuentePixel = cargarFuentePixel();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Título "PAUSA" con fuente pixel
        gbc.gridy = 0;
        JLabel lblPausa = new JLabel("PAUSA", SwingConstants.CENTER);
        lblPausa.setFont(fuentePixel.deriveFont(Font.BOLD, 60f));
        lblPausa.setForeground(Color.WHITE);
        add(lblPausa, gbc);

        // Botón Continuar
        gbc.gridy = 1;
        JButton btnContinuar = crearBotonEstilizado("Continuar", 220, 150);
        btnContinuar.addActionListener(e -> juego.continuarJuego());
        add(btnContinuar, gbc);

        // Botón Volver al Menú
        gbc.gridy = 2;
        JButton btnMenu = crearBotonEstilizado("Volver al Menú", 300, 150);
        btnMenu.addActionListener(e -> juego.volverMenu());
        add(btnMenu, gbc);
    }

    // Crea un JButton con sprites y fuente pixel (mismo estilo que PanelAprender)
    private JButton crearBotonEstilizado(String texto, int ancho, int alto) {
        JButton btn = new JButton();
        btn.setActionCommand(texto);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setRolloverEnabled(true);

        // Estilo del texto
        btn.setFont(fuentePixel.deriveFont(Font.PLAIN, 25f));
        btn.setForeground(new Color(82, 48, 29));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setIconTextGap(0);

        try {
            // Cargar sprites desde recursos
            ImageIcon iconoNormal = cargarIconoBoton("/menu/boton_inicio.png", ancho, alto);
            ImageIcon iconoHover = cargarIconoBoton("/menu/boton_hover.png", ancho, alto);

            btn.setIcon(iconoNormal);
            btn.setRolloverIcon(iconoHover);
            btn.setPressedIcon(iconoHover);

            // El texto se dibuja sobre el sprite
            btn.setText(texto);

        } catch (Exception e) {
            // Fallback: botón clásico si no se cargan los sprites
            btn.setText(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
            btn.setBackground(new Color(110, 180, 80));
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
        }

        return btn;
    }

    // Carga el sprite, lo escala y lo devuelve como ImageIcon
    private ImageIcon cargarIconoBoton(String ruta, int ancho, int alto) throws IOException {
        BufferedImage imagenOriginal = ImageIO.read(getClass().getResource(ruta));
        BufferedImage lienzo = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = lienzo.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imagenOriginal, 0, 0, ancho, alto, null);
        g2.dispose();
        return new ImageIcon(lienzo);
    }

    // Carga la fuente PixelOperator desde recursos
    private Font cargarFuentePixel() {
        try (InputStream fuenteStream = getClass().getResourceAsStream("/fuentes/PixelOperator.ttf")) {
            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar PixelOperator en panelPausa.");
        }
        return new Font(Font.MONOSPACED, Font.BOLD, 25);
    }
}