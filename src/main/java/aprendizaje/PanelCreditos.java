package aprendizaje;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelCreditos extends JPanel {

    private MenuPrincipal menu;
    private Font fuentePixel;

    public PanelCreditos(MenuPrincipal menu) {
        this.menu = menu;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        fuentePixel = cargarFuentePixel();

        // Botón de volver (esquina superior izquierda)
        JButton btnVolver = crearBotonVolver();
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setOpaque(false);
        panelSuperior.add(btnVolver);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel central con GridBagLayout para centrar el texto ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        // Área de texto con los créditos en cascada
        JTextArea txtCreditos = new JTextArea();
        txtCreditos.setEditable(false);
        txtCreditos.setOpaque(false);
        txtCreditos.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        txtCreditos.setForeground(Color.WHITE);
        txtCreditos.setBorder(new EmptyBorder(20, 20, 20, 20)); // margen interno opcional

        String creditos =
            "  THE DEVELOPMENT TEAM\n\n" +
            "  Producer and Project Director\n" +
            "  Warren Spector\n\n" +
            "  Lead Programmer and Assistant Director\n" +
            "  Chris Norden\n\n" +
            "  Programmers\n" +
            "  Scott Martin\n" +
            "  Albert Yarussu\n\n" +
            "  Lead Designer";

        txtCreditos.setText(creditos);
        // Ajusta el tamaño preferido para que no se estire demasiado
        txtCreditos.setPreferredSize(new Dimension(500, 800));

        // Centrar el JTextArea en el panelCentral usando GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentral.add(txtCreditos, gbc);

        // Añadir el panel central al centro del BorderLayout
        add(panelCentral, BorderLayout.CENTER);
    }
    private JButton crearBotonVolver() {
        JButton btn = new JButton();
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);

        try {
            int ancho = 170, alto = 100;
            ImageIcon normal = cargarIcono("/menu/boton_anterior.png", ancho, alto);
            ImageIcon hover = cargarIcono("/menu/boton_anterior_hover.png", ancho, alto);
            btn.setIcon(normal);
            btn.setRolloverIcon(hover);
            btn.setPressedIcon(hover);
            btn.setPreferredSize(new Dimension(ancho, alto));
        } catch (Exception e) {
            // Fallback: botón con texto
            btn.setText("← Volver");
            btn.setFont(new Font("SansSerif", Font.BOLD, 30));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(110, 180, 80));
            btn.setContentAreaFilled(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
        }

        btn.addActionListener(e -> {
            Sonido.reproducirClick();
            Sonido.reanudarMusicaMenu();
            menu.mostrarMenu();
        });

        return btn;
    }

    private ImageIcon cargarIcono(String ruta, int ancho, int alto) throws IOException {
        BufferedImage img = ImageIO.read(getClass().getResource(ruta));
        BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = escalada.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(img, 0, 0, ancho, alto, null);
        g2.dispose();
        return new ImageIcon(escalada);
    }

    private Font cargarFuentePixel() {
        try (InputStream is = getClass().getResourceAsStream("/fuentes/PixelOperator.ttf")) {
            if (is != null) return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {}
        return new Font(Font.MONOSPACED, Font.BOLD, 22);
    }
}