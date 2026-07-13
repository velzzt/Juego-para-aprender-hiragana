package aprendizaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // Componentes que necesitamos mover
    private JLabel lblEtiqueta;
    private JTextField txtNombre;
    private JButton btnIzquierda;
    private JButton btnDerecha;
    private JButton btnListo;
    private JButton btnSiguiente;
    private JButton btnVolverMenu;
    private Font fuentePixel;
    private Font fuentePixelBold;

    // Variables para dimensiones de la vista previa y diálogo (las usaremos en paintComponent)
    private int prevX, prevY, prevW = 160, prevH = 100;
    private int dialogoX, dialogoY, dialogoW = 1080, dialogoH = 180;

    //sistema de fases y guion 
    private enum Fase {
        SELECCION, TUTORIAL
    }
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
        this.fuentePixel = cargarFuente(
                "/fuentes/PixelOperator.ttf",
                Font.PLAIN
        );
        this.fuentePixelBold = cargarFuente(
                "/fuentes/PixelOperator-Bold.ttf",
                Font.BOLD
        );
        this.setLayout(null);
        this.setSize(1280, 800);
        inicializarComponentesEstiloJuego();

        // Listener para redimensionar
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centrarComponentes();
                repaint(); // para que paintComponent use las nuevas coordenadas
            }
        });
        iniciarEscritura(guionActual()[indiceLinea].texto); // NUEVO: arranca el efecto de escritura de la primera línea
    }

    //devuelve el guion correspondiente a la fase actual
    private Linea[] guionActual() {
        return faseActual == Fase.SELECCION ? guionSeleccion : guionTutorial;
    }

    private void inicializarComponentesEstiloJuego() {

        // --- 2. SELECTOR DE PAISAJES ---
        btnIzquierda = crearBotonFlecha(
                "/menu/flecha_paisaje_izquierda.png",
                "/menu/flecha_paisaje_izquierda_hover.png",
                75,
                68
        );
        add(btnIzquierda);

        btnDerecha = crearBotonFlecha(
                "/menu/flecha_paisaje_derecha.png",
                "/menu/flecha_paisaje_derecha_hover.png",
                75,
                68
        );
        add(btnDerecha);

        btnVolverMenu = crearBotonFlecha(
                "/menu/boton_volver.png",
                "/menu/boton_volver_hover.png",
                70,
                70
        );
        btnVolverMenu.addActionListener(e -> {
            Sonido.reproducirClick();
            ventanaPrincipal.mostrarMenu();
        });
        add(btnVolverMenu);

        // --- 3. BOTÓN COMENZAR ---
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

        Color colorVerdeBase = new Color(110, 180, 80);
        Color colorVerdeBrillante = new Color(135, 210, 100);

        btnListo.setBackground(colorVerdeBase);
        btnListo.setForeground(Color.WHITE);
        configurarEfectoHover(btnListo, colorVerdeBase, colorVerdeBrillante);
        add(btnListo);

        // 3.5 BOTÓN SIGUIENTE (avanza el texto del diálogo)
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

        Color colorNaranjaBase = new Color(255, 224, 178);
        Color colorNaranjaHover = new Color(255, 205, 140);
        btnSiguiente.setBackground(colorNaranjaBase);
        configurarEfectoHover(btnSiguiente, colorNaranjaBase, colorNaranjaHover);
        add(btnSiguiente);

        // Eventos
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

        // btnSiguiente avanza el texto del diálogo en cualquier fase
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
                String nombreIngresado = "Jugador";
                String rutaFondo = imagenesFondo[indiceFondo];
                ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, rutaFondo);
            }
        });

        // Llamar a centrarComponentes una vez al inicio para posicionar correctamente
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

    //efecto de escritura letra por letra
    private void iniciarEscritura(String texto) {
        textoMostrado = "";
        charIndex = 0;
        if (timerEscritura != null) {
            timerEscritura.stop();
        }
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

        if (ancho == 0 || alto == 0) {
            return;
        }

        // Flecha izquierda
        btnIzquierda.setBounds(ancho/2 - 170, alto/2 - 54, 75, 68);

        // Flecha derecha
        btnDerecha.setBounds(ancho/2 + 95, alto/2 - 54, 75, 68);

        // Regreso al menú principal
        btnVolverMenu.setBounds(14, 14, 70, 70);

        // Botón Comenzar
        btnListo.setBounds(ancho / 2 - 110, alto / 2 + 80, 220, 50);

        // Guardar las coordenadas de la vista previa (se dibujará en paintComponent)
        prevX = (ancho - prevW) / 2;
        prevY = alto / 2 - 70; // un poco más arriba que las flechas

        // Guardar coordenadas del cuadro de diálogo
        dialogoX = (ancho - dialogoW) / 2;
        dialogoY = alto - 200; // 200px desde abajo, ajusta según necesites
    }

    private JButton crearBotonFlecha(
            String rutaNormal,
            String rutaHover,
            int ancho,
            int alto
    ) {
        JButton boton = new JButton();
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
        boton.setRolloverEnabled(true);

        try {
            BufferedImage normal = ImageIO.read(getClass().getResource(rutaNormal));
            BufferedImage hover = ImageIO.read(getClass().getResource(rutaHover));
            int[] limitesNormal = limitesVisibles(normal);
            int[] limitesHover = limitesVisibles(hover);
            int minX = Math.min(limitesNormal[0], limitesHover[0]);
            int minY = Math.min(limitesNormal[1], limitesHover[1]);
            int maxX = Math.max(limitesNormal[2], limitesHover[2]);
            int maxY = Math.max(limitesNormal[3], limitesHover[3]);

            ImageIcon iconoNormal = escalarFlecha(
                    normal, minX, minY, maxX, maxY, ancho, alto);
            ImageIcon iconoHover = escalarFlecha(
                    hover, minX, minY, maxX, maxY, ancho, alto);

            boton.setIcon(iconoNormal);
            boton.setRolloverIcon(iconoHover);
            boton.setPressedIcon(iconoHover);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudieron cargar las flechas de paisaje.");
        }

        return boton;
    }

    private int[] limitesVisibles(BufferedImage imagen) {
        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = 0;
        int maxY = 0;

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

    private ImageIcon escalarFlecha(
            BufferedImage imagen,
            int minX,
            int minY,
            int maxX,
            int maxY,
            int ancho,
            int alto
    ) {
        BufferedImage escalada = new BufferedImage(
                ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = escalada.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int anchoFuente = maxX - minX + 1;
        int altoFuente = maxY - minY + 1;
        double escala = Math.min(
                ancho / (double) anchoFuente,
                alto / (double) altoFuente
        );
        int anchoDibujado = Math.max(1, (int) Math.round(anchoFuente * escala));
        int altoDibujado = Math.max(1, (int) Math.round(altoFuente * escala));
        int x = (ancho - anchoDibujado) / 2;
        int y = (alto - altoDibujado) / 2;

        g2.drawImage(
                imagen,
                x, y, x + anchoDibujado, y + altoDibujado,
                minX, minY, maxX + 1, maxY + 1,
                null);
        g2.dispose();
        return new ImageIcon(escalada);
    }

    private void configurarEfectoHover(JButton boton, Color colorBase, Color colorHover) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBase);
            }
        });
    }

    private Font cargarFuente(String ruta, int estiloRespaldo) {
        try (InputStream fuenteStream = getClass().getResourceAsStream(ruta)) {
            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la fuente: " + ruta);
        }

        return new Font(Font.MONOSPACED, estiloRespaldo, 22);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Dibujar el Fondo Completo
        try {
            ImageIcon fondoVentana = new ImageIcon(getClass().getResource(imagenesFondo[indiceFondo]));
            g2d.drawImage(fondoVentana.getImage(), 0, 0, getWidth(), getHeight(), this);
        } catch (Exception e) {
            g2d.setColor(new Color(185, 225, 175));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // 2. Dibujar la Vista Previa Centrada (usando prevX, prevY)
        if (faseActual == Fase.SELECCION) {
            try {
                ImageIcon iconPaisajePequeno = new ImageIcon(getClass().getResource(imagenesFondo[indiceFondo]));
                g2d.setClip(new RoundRectangle2D.Float(prevX, prevY, prevW, prevH, 15, 15));
                g2d.drawImage(iconPaisajePequeno.getImage(), prevX, prevY, prevW, prevH, this);
                g2d.setClip(null);

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(prevX, prevY, prevW, prevH, 15, 15);
            } catch (Exception e) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRoundRect(prevX, prevY, prevW, prevH, 15, 15);
            }
        }

        // 3. Cuadro de Diálogo Inferior (usando dialogoX, dialogoY)
        try {
            ImageIcon iconCuadro = new ImageIcon(getClass().getResource("/imagenes/cuadro_dialogo.png"));
            g2d.drawImage(iconCuadro.getImage(), dialogoX, dialogoY, dialogoW, dialogoH, this);
        } catch (Exception e) {
            g2d.setColor(new Color(245, 235, 215));
            g2d.fillRoundRect(dialogoX, dialogoY, dialogoW, dialogoH, 20, 20);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(new Color(160, 40, 40));
            g2d.drawRoundRect(dialogoX, dialogoY, dialogoW, dialogoH, 20, 20);
        }
        
        Linea lineaActual = guionActual()[indiceLinea];
        g2d.setFont(fuentePixelBold.deriveFont(18f));
        FontMetrics fmNombre = g2d.getFontMetrics();
        int anchoNametag = fmNombre.stringWidth(lineaActual.hablante) + 30;

        g2d.setColor(new Color(190, 40, 40));
        g2d.fillRoundRect(dialogoX + 25, dialogoY - 18, anchoNametag, 34, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(dialogoX + 25, dialogoY - 18, anchoNametag, 34, 12, 12);

        g2d.setColor(Color.WHITE);
        g2d.drawString(lineaActual.hablante, dialogoX + 40, dialogoY + 6);

        // --- 4. FUENTES ANCESTRALES CON TAMAÑO AGRANDADO ---
        g2d.setColor(new Color(35, 35, 35));
        int textX = dialogoX + 40; // margen izquierdo dentro del cuadro
        int textY1 = dialogoY + 85; // ajusta según necesidad
        int textY2 = dialogoY + 135;

        g2d.setFont(fuentePixelBold.deriveFont(24f));
        g2d.drawString(textoMostrado, textX, textY1);
    }

}
