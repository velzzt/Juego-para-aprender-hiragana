package aprendizaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class PanelNarrativa extends JPanel {

    private MenuPrincipal ventanaPrincipal;
    private int indiceFondo = 0;

    private String[] imagenesFondo = {
        "/images_narrativa/FONDO_1.png",
        "/images_narrativa/FONDO_2.png",
        "/images_narrativa/FONDO_3.png"
    };

    // Componentes
    private JButton btnIzquierda;
    private JButton btnDerecha;
    private JButton btnListo;
    private JButton btnSiguiente;
    private JButton btnVolverMenu;
    private Font fuentePixel;
    private Font fuentePixelBold;

    // Dimensiones de la vista previa y diálogo
    private int prevX, prevY, prevW = 160, prevH = 100;
    private int dialogoX, dialogoY, dialogoW = 1080, dialogoH = 180;

    // Sistema de fases y guion
    private enum Fase { SELECCION, TUTORIAL }
    private Fase faseActual = Fase.SELECCION;

    private class Linea {
        String hablante;
        String texto;
        Linea(String hablante, String texto) {
            this.hablante = hablante;
            this.texto = texto;
        }
    }

    private Linea[] guionSeleccion = {
        new Linea("SENSEI", "¿Listo para adentrarte en un idioma fuera de lo cotidiano? El viaje comienza aquí..."),
        new Linea("NARRADOR", "Usa las flechas y elige el paisaje que hablará contigo en este camino.")
    };

    private Linea[] guionTutorial = {
        new Linea("SENSEI", "¡Es hora de poner a prueba lo aprendido!"),
        new Linea("SENSEI", "Un símbolo aparecerá por el lado derecho. Tú tienes el poder de detenerlo."),
        new Linea("SENSEI", "Presiona en tu teclado la letra que le corresponde... ¡antes de que te alcance!"),
        new Linea("SENSEI", "Cada error te costará un corazón. Solo tienes tres oportunidades."),
        new Linea("SENSEI", "Entre más rápido y certero seas, más alto subirá tu puntuación."),
        new Linea("NARRADOR", "Respira hondo. El primer símbolo ya se está formando...")
    };

    private int indiceLinea = 0;
    private String textoMostrado = "";
    private int charIndex = 0;
    private Timer timerEscritura;

    public PanelNarrativa(MenuPrincipal ventana) {
        this.ventanaPrincipal = ventana;
        this.fuentePixel = cargarFuente("/fuentes/PixelOperator.ttf", Font.PLAIN);
        this.fuentePixelBold = cargarFuente("/fuentes/PixelOperator-Bold.ttf", Font.BOLD);
        this.setLayout(null);
        this.setSize(1280, 800);
        inicializarComponentes();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centrarComponentes();
                repaint();
            }
        });

        iniciarEscritura(guionActual()[indiceLinea].texto);
    }

    private Linea[] guionActual() {
        return faseActual == Fase.SELECCION ? guionSeleccion : guionTutorial;
    }

    private void inicializarComponentes() {
        // --- Flechas ---
        btnIzquierda = crearBotonFlecha(
                "/menu/flecha_paisaje_izquierda.png",
                "/menu/flecha_paisaje_izquierda_hover.png",
                75, 68
        );
        add(btnIzquierda);

        btnDerecha = crearBotonFlecha(
                "/menu/flecha_paisaje_derecha.png",
                "/menu/flecha_paisaje_derecha_hover.png",
                75, 68
        );
        add(btnDerecha);

        // --- Botón volver al menú ---
        btnVolverMenu = crearBotonFlecha(
                "/menu/boton_volver.png",
                "/menu/boton_volver_hover.png",
                70, 70
        );
        btnVolverMenu.addActionListener(e -> {
            Sonido.reproducirClick();
            ventanaPrincipal.mostrarMenu();
        });
        add(btnVolverMenu);

        // --- Botón COMENZAR ---
        btnListo = new JButton("COMENZAR...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnListo.setFont(fuentePixelBold.deriveFont(24f));
        btnListo.setFocusPainted(false);
        btnListo.setBorderPainted(false);
        btnListo.setContentAreaFilled(false);
        btnListo.setBackground(new Color(110, 180, 80));
        btnListo.setForeground(Color.WHITE);
        configurarEfectoHover(btnListo, new Color(110, 180, 80), new Color(135, 210, 100));
        add(btnListo);

        // --- Botón SIGUIENTE ---
        btnSiguiente = new JButton("siguiente") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2d.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(230, 140, 40));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                g2d.dispose();
            }
        };
        btnSiguiente.setFont(fuentePixelBold.deriveFont(18f));
        btnSiguiente.setFocusPainted(false);
        btnSiguiente.setBorderPainted(false);
        btnSiguiente.setContentAreaFilled(false);
        btnSiguiente.setForeground(new Color(90, 60, 20));
        btnSiguiente.setBackground(new Color(255, 224, 178));
        configurarEfectoHover(btnSiguiente, new Color(255, 224, 178), new Color(255, 205, 140));
        add(btnSiguiente);

        // --- Eventos ---
        btnDerecha.addActionListener(e -> {
            Sonido.reproducirClick();
            indiceFondo = (indiceFondo + 1) % imagenesFondo.length;
            repaint();
        });

        btnIzquierda.addActionListener(e -> {
            Sonido.reproducirClick();
            indiceFondo = (indiceFondo - 1 + imagenesFondo.length) % imagenesFondo.length;
            repaint();
        });

        btnListo.addActionListener(e -> {
            Sonido.reproducirClick();
            pasarAFaseTutorial();
        });

        btnSiguiente.addActionListener(e -> {
            Sonido.reproducirClick();
            Linea[] guion = guionActual();

            if (timerEscritura != null && timerEscritura.isRunning()) {
                timerEscritura.stop();
                textoMostrado = guion[indiceLinea].texto;
                repaint();
                return;
            }

            if (indiceLinea < guion.length - 1) {
                indiceLinea++;
                iniciarEscritura(guion[indiceLinea].texto);
                if (indiceLinea == guion.length - 1 && faseActual == Fase.TUTORIAL) {
                    btnSiguiente.setText("¡A JUGAR!");
                }
            } else if (faseActual == Fase.TUTORIAL) {
                 Sonido.reproducirMusicaJugar(); 
                String nombreIngresado = "Jugador";
                String rutaFondo = imagenesFondo[indiceFondo];
                ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, rutaFondo);
            }
        });

        centrarComponentes();
    }

    private void pasarAFaseTutorial() {
        faseActual = Fase.TUTORIAL;
        indiceLinea = 0;
        btnSiguiente.setText("siguiente");
        btnIzquierda.setVisible(false);
        btnDerecha.setVisible(false);
        btnListo.setVisible(false);
        iniciarEscritura(guionActual()[indiceLinea].texto);
        repaint();
    }

    private void iniciarEscritura(String texto) {
        textoMostrado = "";
        charIndex = 0;
        if (timerEscritura != null) timerEscritura.stop();
        timerEscritura = new Timer(25, e -> {
            if (charIndex < texto.length()) {
                textoMostrado += texto.charAt(charIndex);
                charIndex++;
                repaint();
            } else {
                timerEscritura.stop();
            }
        });
        timerEscritura.start();
    }

    private void centrarComponentes() {
        int ancho = getWidth();
        int alto = getHeight();
        if (ancho == 0 || alto == 0) return;

        // Flechas
        btnIzquierda.setBounds(ancho/2 - 170, alto/2 - 54, 75, 68);
        btnDerecha.setBounds(ancho/2 + 95, alto/2 - 54, 75, 68);

        // Botón volver
        btnVolverMenu.setBounds(14, 14, 70, 70);

        // Botón Comenzar
        btnListo.setBounds(ancho/2 - 110, alto/2 + 80, 220, 50);

        // Vista previa (se dibuja en paintComponent)
        prevX = (ancho - prevW) / 2;
        prevY = alto/2 - 70;

        // Cuadro de diálogo
        dialogoX = (ancho - dialogoW) / 2;
        dialogoY = alto - 200;

        // Botón Siguiente
        btnSiguiente.setBounds(dialogoX + dialogoW - 160, dialogoY + dialogoH - 50, 130, 36);
    }

    // ========== MÉTODOS PARA SPRITES ==========
    private JButton crearBotonFlecha(String rutaNormal, String rutaHover, int ancho, int alto) {
        JButton boton = new JButton();
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
        boton.setRolloverEnabled(true);

        try {
            BufferedImage normal = ImageIO.read(getClass().getResource(rutaNormal));
            BufferedImage hover = ImageIO.read(getClass().getResource(rutaHover));
            int[] limNormal = limitesVisibles(normal);
            int[] limHover = limitesVisibles(hover);
            int minX = Math.min(limNormal[0], limHover[0]);
            int minY = Math.min(limNormal[1], limHover[1]);
            int maxX = Math.max(limNormal[2], limHover[2]);
            int maxY = Math.max(limNormal[3], limHover[3]);

            ImageIcon iconNormal = escalarFlecha(normal, minX, minY, maxX, maxY, ancho, alto);
            ImageIcon iconHover = escalarFlecha(hover, minX, minY, maxX, maxY, ancho, alto);
            boton.setIcon(iconNormal);
            boton.setRolloverIcon(iconHover);
            boton.setPressedIcon(iconHover);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudieron cargar las flechas.");
        }
        return boton;
    }

    private int[] limitesVisibles(BufferedImage imagen) {
        int minX = imagen.getWidth(), minY = imagen.getHeight(), maxX = 0, maxY = 0;
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                if (((imagen.getRGB(x, y) >>> 24) & 0xff) > 10) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        return new int[]{minX, minY, maxX, maxY};
    }

    private ImageIcon escalarFlecha(BufferedImage img, int minX, int minY, int maxX, int maxY, int ancho, int alto) {
        BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = escalada.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int anchoFuente = maxX - minX + 1;
        int altoFuente = maxY - minY + 1;
        double escala = Math.min(ancho / (double) anchoFuente, alto / (double) altoFuente);
        int anchoDib = Math.max(1, (int) Math.round(anchoFuente * escala));
        int altoDib = Math.max(1, (int) Math.round(altoFuente * escala));
        int x = (ancho - anchoDib) / 2;
        int y = (alto - altoDib) / 2;
        g2.drawImage(img, x, y, x + anchoDib, y + altoDib, minX, minY, maxX + 1, maxY + 1, null);
        g2.dispose();
        return new ImageIcon(escalada);
    }

    private void configurarEfectoHover(JButton boton, Color base, Color hover) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(hover);
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(base);
            }
        });
    }

    private Font cargarFuente(String ruta, int estilo) {
        try (InputStream is = getClass().getResourceAsStream(ruta)) {
            if (is != null) return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            System.err.println("No se pudo cargar fuente: " + ruta);
        }
        return new Font(Font.MONOSPACED, estilo, 22);
    }

    // ========== DIBUJO ==========
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fondo
        try {
            ImageIcon fondo = new ImageIcon(getClass().getResource(imagenesFondo[indiceFondo]));
            g2d.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
        } catch (Exception e) {
            g2d.setColor(new Color(185, 225, 175));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Vista previa (solo fase selección)
        if (faseActual == Fase.SELECCION) {
            try {
                ImageIcon prev = new ImageIcon(getClass().getResource(imagenesFondo[indiceFondo]));
                g2d.setClip(new RoundRectangle2D.Float(prevX, prevY, prevW, prevH, 15, 15));
                g2d.drawImage(prev.getImage(), prevX, prevY, prevW, prevH, this);
                g2d.setClip(null);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(prevX, prevY, prevW, prevH, 15, 15);
            } catch (Exception e) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRoundRect(prevX, prevY, prevW, prevH, 15, 15);
            }
        }

        // Cuadro de diálogo
        try {
            ImageIcon cuadro = new ImageIcon(getClass().getResource("/imagenes/cuadro_dialogo.png"));
            g2d.drawImage(cuadro.getImage(), dialogoX, dialogoY, dialogoW, dialogoH, this);
        } catch (Exception e) {
            g2d.setColor(new Color(245, 235, 215));
            g2d.fillRoundRect(dialogoX, dialogoY, dialogoW, dialogoH, 20, 20);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(new Color(160, 40, 40));
            g2d.drawRoundRect(dialogoX, dialogoY, dialogoW, dialogoH, 20, 20);
        }

        // Nombre del hablante
        Linea actual = guionActual()[indiceLinea];
        g2d.setFont(fuentePixelBold.deriveFont(18f));
        FontMetrics fm = g2d.getFontMetrics();
        int anchoTag = fm.stringWidth(actual.hablante) + 30;
        g2d.setColor(new Color(190, 40, 40));
        g2d.fillRoundRect(dialogoX + 25, dialogoY - 18, anchoTag, 34, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(dialogoX + 25, dialogoY - 18, anchoTag, 34, 12, 12);
        g2d.setColor(Color.WHITE);
        g2d.drawString(actual.hablante, dialogoX + 40, dialogoY + 6);

        // Texto del diálogo (con efecto de escritura)
        g2d.setColor(new Color(35, 35, 35));
        g2d.setFont(fuentePixelBold.deriveFont(24f));
        int textX = dialogoX + 40;
        int textY = dialogoY + 85;
        g2d.drawString(textoMostrado, textX, textY);
    }
}