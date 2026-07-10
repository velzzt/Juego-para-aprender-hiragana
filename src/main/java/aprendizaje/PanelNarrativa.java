package aprendizaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelNarrativa extends javax.swing.JPanel {

    private MenuPrincipal ventanaPrincipal;
    private int indiceFondo = 0;

    private String[] imagenesFondo = {
        "/images_narrativa/fondo1.png",
        "/images_narrativa/fondo2.png",
        "/images_narrativa/fondo3.png"
    };

    private JTextField txtNombre;
    private JButton btnIzquierda;
    private JButton btnDerecha;
    private JButton btnListo;

    public PanelNarrativa(MenuPrincipal ventana) {
        this.ventanaPrincipal = ventana;
        this.setLayout(null);
        this.setSize(1280, 800);
        inicializarComponentesEstiloJuego();
    }

    private void inicializarComponentesEstiloJuego() {
        // --- 1. BLOQUE DE ENTRADA DE NOMBRE ---
        JLabel lblEtiqueta = new JLabel("Ingresa un nombre:", SwingConstants.CENTER);
        lblEtiqueta.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 22));
        lblEtiqueta.setForeground(new Color(40, 40, 40));
        lblEtiqueta.setBounds(515, 230, 250, 30);
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
        txtNombre.setFont(new Font("Serif", Font.PLAIN, 20));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        txtNombre.setOpaque(false);
        txtNombre.setBorder(new EmptyBorder(0, 10, 0, 10));
        txtNombre.setBounds(515, 270, 250, 35);
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
        btnIzquierda.setBounds(490, 360, 60, 40);
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
        btnDerecha.setBounds(730, 360, 60, 40);
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
        btnListo.setFont(new Font("Impact", Font.PLAIN, 26));
        btnListo.setFocusPainted(false);
        btnListo.setBorderPainted(false);
        btnListo.setContentAreaFilled(false);

        Color colorVerdeBase = new Color(110, 180, 80);
        Color colorVerdeBrillante = new Color(135, 210, 100);

        btnListo.setBackground(colorVerdeBase);
        btnListo.setForeground(Color.WHITE);
        btnListo.setBounds(960, 480, 220, 50);
        configurarEfectoHover(btnListo, colorVerdeBase, colorVerdeBrillante);
        add(btnListo);

        // Eventos
        btnDerecha.addActionListener(e -> {
            indiceFondo = (indiceFondo + 1) % imagenesFondo.length;
            repaint();
        });

        btnIzquierda.addActionListener(e -> {
            indiceFondo = (indiceFondo - 1 + imagenesFondo.length) % imagenesFondo.length;
            repaint();
        });

        btnListo.addActionListener(e -> {
            String nombreIngresado = txtNombre.getText().trim();
            if (nombreIngresado.isEmpty()) {
                nombreIngresado = "Héroe";
            }
            ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, indiceFondo);
        });
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

        // 2. Dibujar la Vista Previa Centrada
        try {
            ImageIcon iconPaisajePequeno = new ImageIcon(getClass().getResource(imagenesFondo[indiceFondo]));
            g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(560, 330, 160, 100, 15, 15));
            g2d.drawImage(iconPaisajePequeno.getImage(), 560, 330, 160, 100, this);
            g2d.setClip(null);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(560, 330, 160, 100, 15, 15);
        } catch (Exception e) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRoundRect(560, 330, 160, 100, 15, 15);
        }

        // 3. Cuadro de Diálogo Inferior
        try {
            ImageIcon iconCuadro = new ImageIcon(getClass().getResource("/imagenes/cuadro_dialogo.png"));
            g2d.drawImage(iconCuadro.getImage(), 100, 550, 1080, 180, this);
        } catch (Exception e) {
            g2d.setColor(new Color(245, 235, 215));
            g2d.fillRoundRect(100, 550, 1080, 180, 20, 20);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(new Color(160, 40, 40));
            g2d.drawRoundRect(100, 550, 1080, 180, 20, 20);
        }

        // --- 4. FUENTES ANCESTRALES CON TAMAÑO AGRANDADO ---
        g2d.setColor(new Color(35, 35, 35));

        // Primera línea aumentada de 22 a 26 puntos. Bajamos su coordenada Y a 635 para centrarla.
        g2d.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        g2d.drawString("¿Te gustaría aprender un idioma fuera de lo cotidiano? Pues empecemos...", 140, 635);

        // Segunda línea aumentada de 19 a 22 puntos. Ajustamos su coordenada Y a 685.
        g2d.setFont(new Font("Georgia", Font.ITALIC, 22));
        g2d.drawString("Usa las flechas para elegir tu paisaje favorito.", 140, 685);
    }

    private void initComponents() {
    }
}
