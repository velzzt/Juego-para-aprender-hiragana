package aprendizaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        // --- 1. BLOQUE DE ENTRADA DE NOMBRE (CENTRADO) ---
        JLabel lblEtiqueta = new JLabel("Ingresa un nombre:", SwingConstants.CENTER);
        lblEtiqueta.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblEtiqueta.setForeground(Color.BLACK);
        lblEtiqueta.setBounds(515, 230, 250, 30);
        add(lblEtiqueta);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Monospaced", Font.PLAIN, 18));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        txtNombre.setBounds(515, 270, 250, 35);
        add(txtNombre);

        // --- 2. SELECTOR DE PAISAJES (CENTRADO ABAJO DEL NOMBRE) ---
        btnIzquierda = new JButton("<");
        btnIzquierda.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnIzquierda.setBounds(490, 360, 60, 40);
        add(btnIzquierda);

        btnDerecha = new JButton(">");
        btnDerecha.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnDerecha.setBounds(730, 360, 60, 40);
        add(btnDerecha);

        // --- 3. BOTÓN COMENZAR (ALINEADO AL BORDE DERECHO DEL DIÁLOGO) ---
        btnListo = new JButton("COMENZAR ✓");
        btnListo.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnListo.setBackground(new Color(110, 180, 80));
        btnListo.setForeground(Color.WHITE);
        // X=960 + Ancho=220 hace que termine exactamente en 1180 (el mismo borde que el cuadro de diálogo)
        btnListo.setBounds(960, 480, 220, 50); 
        add(btnListo);

        // Eventos de los botones
        btnDerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceFondo = (indiceFondo + 1) % imagenesFondo.length;
                repaint();
            }
        });

        btnIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceFondo = (indiceFondo - 1 + imagenesFondo.length) % imagenesFondo.length;
                repaint();
            }
        });

        btnListo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreIngresado = txtNombre.getText().trim();
                if (nombreIngresado.isEmpty()) {
                    nombreIngresado = "Héroe";
                }
                ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, indiceFondo);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. Dibujar el Fondo Completo de la Ventana
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
            g2d.drawImage(iconPaisajePequeno.getImage(), 560, 330, 160, 100, this);
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(560, 330, 160, 100);
        } catch (Exception e) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(560, 330, 160, 100);
        }

        // 3. Cuadro de Diálogo Inferior (Inicia en X=100, Ancho=1080 -> Termina en X=1180)
        try {
            ImageIcon iconCuadro = new ImageIcon(getClass().getResource("/imagenes/cuadro_dialogo.png"));
            g2d.drawImage(iconCuadro.getImage(), 100, 550, 1080, 180, this);
        } catch (Exception e) {
            g2d.setColor(new Color(245, 235, 215));
            g2d.fillRect(100, 550, 1080, 180);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(new Color(160, 40, 40));
            g2d.drawRect(100, 550, 1080, 180);
        }

        // Escribir el texto explicativo
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        g2d.drawString("¿Te gustaria aprender un idioma fuera de lo cotidiano? Pues empecemos...", 150, 630);
        g2d.drawString("Usa las flechas para elegir tu paisaje favorito.", 150, 670);
    }

    private void initComponents() { }
}
