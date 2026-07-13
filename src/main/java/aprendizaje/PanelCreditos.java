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

        // --- Panel central con BoxLayout para apilar líneas centradas ---
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Añadir un "glue" para centrar verticalmente el contenido
        panelCentral.add(Box.createVerticalGlue());

        // Línea 1: Título en amarillo
        JLabel lblTitulo = new JLabel("NUESTRO EQUIPO DE DESARROLLO");
        lblTitulo.setFont(fuentePixel.deriveFont(Font.PLAIN, 40f));
        lblTitulo.setForeground(Color.YELLOW);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblTitulo);
        panelCentral.add(Box.createVerticalStrut(40));

        // Línea 2: "Programmers" en amarillo
        JLabel lblProgrammers = new JLabel("Programación");
        lblProgrammers.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        lblProgrammers.setForeground(Color.YELLOW);
        lblProgrammers.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblProgrammers);
        panelCentral.add(Box.createVerticalStrut(10));

        // Nombres de programadores en blanco
        String[] programadores = {"Elizabet Quispe", "Franco Donayre", "Leonardo Arellan", "Jack Hilario"};
        for (String nombre : programadores) {
            JLabel lbl = new JLabel(nombre);
            lbl.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCentral.add(lbl);
        }
        panelCentral.add(Box.createVerticalStrut(20));
        // Línea "Design" en amarillo
        JLabel lblDesign = new JLabel("Diseño");
        lblDesign.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        lblDesign.setForeground(Color.YELLOW);
        lblDesign.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblDesign);
        panelCentral.add(Box.createVerticalStrut(10));

        // Nombres de diseñadores en blanco
        String[] disenadores = {"Franco Donayre", "Leonardo Arellan"};
        for (String nombre : disenadores) {
            JLabel lbl = new JLabel(nombre);
            lbl.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCentral.add(lbl);
        }
        panelCentral.add(Box.createVerticalStrut(20));

        // Línea "Narrativa" en amarillo
        JLabel lblNarrativa = new JLabel("Narrativa");
        lblNarrativa.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        lblNarrativa.setForeground(Color.YELLOW);
        lblNarrativa.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblNarrativa);
        panelCentral.add(Box.createVerticalStrut(10));

        // Nombre de narrador en blanco
        JLabel lblNarrativa1 = new JLabel("Jack Hilario");
        lblNarrativa1.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        lblNarrativa1.setForeground(Color.WHITE);
        lblNarrativa1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblNarrativa1);

        // Añadir otro "glue" para centrar verticalmente
        panelCentral.add(Box.createVerticalGlue());

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