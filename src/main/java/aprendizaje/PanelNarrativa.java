package aprendizaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;

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
    private Font fuentePixel;
    private Font fuentePixelBold;

    // Variables para dimensiones de la vista previa y diálogo (las usaremos en paintComponent)
    private int prevX, prevY, prevW = 160, prevH = 100;
    private int dialogoX, dialogoY, dialogoW = 1080, dialogoH = 180;

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
    }

    private void inicializarComponentesEstiloJuego() {
        // --- 1. BLOQUE DE ENTRADA DE NOMBRE ---
        lblEtiqueta = new JLabel("Ingresa un nombre:", SwingConstants.CENTER);
        lblEtiqueta.setFont(fuentePixelBold.deriveFont(24f));
        lblEtiqueta.setForeground(new Color(40, 40, 40));
        add(lblEtiqueta);

        txtNombre = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(140, 140, 140));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                g2d.dispose();
            }
        };
        txtNombre.setFont(fuentePixel.deriveFont(22f));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        txtNombre.setOpaque(false);
        txtNombre.setBorder(new EmptyBorder(0, 10, 0, 10));
        add(txtNombre);

        // --- 2. SELECTOR DE PAISAJES ---
        btnIzquierda = new JButton("<") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                g2d.dispose();
            }
        };
        btnIzquierda.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnIzquierda.setFocusPainted(false);
        btnIzquierda.setContentAreaFilled(false);
        configurarEfectoHover(btnIzquierda, new Color(245, 245, 245), new Color(215, 215, 215));
        add(btnIzquierda);

        btnDerecha = new JButton(">") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                g2d.dispose();
            }
        };
        btnDerecha.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnDerecha.setFocusPainted(false);
        btnDerecha.setContentAreaFilled(false);
        configurarEfectoHover(btnDerecha, new Color(245, 245, 245), new Color(215, 215, 215));
        add(btnDerecha);

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
            String nombreIngresado = txtNombre.getText().trim();
            if (nombreIngresado.isEmpty()) {
                nombreIngresado = "Héroe";
            }
            String rutaFondo = imagenesFondo[indiceFondo];
            ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, rutaFondo);
        });

        // Llamar a centrarComponentes una vez al inicio para posicionar correctamente
        centrarComponentes();
    }

    private void centrarComponentes() {
        int ancho = getWidth();
        int alto = getHeight();

        if (ancho == 0 || alto == 0) return;

        // Etiqueta
        lblEtiqueta.setBounds(ancho/2 - 125, alto/2 - 170, 250, 30);

        // Campo de texto
        txtNombre.setBounds(ancho/2 - 125, alto/2 - 130, 250, 35);

        // Flecha izquierda
        btnIzquierda.setBounds(ancho/2 - 150, alto/2 - 40, 60, 40);

        // Flecha derecha
        btnDerecha.setBounds(ancho/2 + 90, alto/2 - 40, 60, 40);

        // Botón Comenzar
        btnListo.setBounds(ancho/2 - 110, alto/2 + 80, 220, 50);

        // Guardar las coordenadas de la vista previa (se dibujará en paintComponent)
        prevX = (ancho - prevW) / 2;
        prevY = alto/2 - 70; // un poco más arriba que las flechas

        // Guardar coordenadas del cuadro de diálogo
        dialogoX = (ancho - dialogoW) / 2;
        dialogoY = alto - 200; // 200px desde abajo, ajusta según necesites
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

        // --- 4. FUENTES ANCESTRALES CON TAMAÑO AGRANDADO ---
        g2d.setColor(new Color(35, 35, 35));
        int textX = dialogoX + 40; // margen izquierdo dentro del cuadro
        int textY1 = dialogoY + 85; // ajusta según necesidad
        int textY2 = dialogoY + 135;

        g2d.setFont(fuentePixelBold.deriveFont(24f));
        g2d.drawString("¿Te gustaría aprender un idioma fuera de lo cotidiano? Pues empecemos...", textX, textY1);

        g2d.setFont(fuentePixel.deriveFont(22f));
        g2d.drawString("Usa las flechas para elegir tu paisaje favorito.", textX, textY2);
    }

}
