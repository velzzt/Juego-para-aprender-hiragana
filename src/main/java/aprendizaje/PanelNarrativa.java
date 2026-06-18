package aprendizaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelNarrativa extends javax.swing.JPanel {

    private MenuPrincipal ventanaPrincipal;
    private int indiceRopa = 0;
    private String[] nombresRopa = {"Ropa Verde", "Ropa Azul", "Ropa Roja"};

    // Nombres de las imagenes en arreglos
    private String[] imagenesRopa = {"/images_narrativa/traje_verde.png", "/images_narrativa/traje_azul.png", "/images_narrativa/traje_rojo.png"};
    private String[] imagenPersonaje = {"/images_narrativa/traje_basico.png", "/images_narrativa/traje_basico.png", "/images_narrativa/traje_basico.png"};

    // Interfaces
    private JTextField txtNombre;
    private JButton btnIzquierda;
    private JButton btnDerecha;
    private JButton btnListo;

    public PanelNarrativa(MenuPrincipal ventana) {
        this.ventanaPrincipal = ventana;

        // Desactivamos el predeterminado para acomodar manualmente
        this.setLayout(null);
        this.setSize(1280, 800);

        inicializarComponentesEstiloJuego();
    }

    private void inicializarComponentesEstiloJuego() {
        // Campo para ingresar un nombre de usuario o jugador
        JLabel lblEtiqueta = new JLabel("Ingresa un nombre: ");
        lblEtiqueta.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblEtiqueta.setForeground(Color.BLACK);
        lblEtiqueta.setBounds(200, 480, 250, 30);
        add(lblEtiqueta);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Monospaced", Font.PLAIN, 18));
        txtNombre.setBounds(450, 480, 200, 30);
        add(txtNombre);

        // Flechas para cambiar entre los trajes
        btnIzquierda = new JButton("<");
        btnIzquierda.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnIzquierda.setBounds(680, 250, 60, 40);
        add(btnIzquierda);

        btnDerecha = new JButton(">");
        btnDerecha.setFont(new Font("Monospaced", Font.BOLD, 20));
        btnDerecha.setBounds(880, 250, 60, 40);
        add(btnDerecha);

        // Boton para comenzar a jugar
        btnListo = new JButton("COMENZAR ✓");
        btnListo.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnListo.setBackground(new Color(110, 180, 80));
        btnListo.setForeground(Color.WHITE);
        btnListo.setBounds(920, 470, 220, 50);

        add(btnListo);

        //Eventos para mostrar los trajes dependiendo del traje que toque
        btnDerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceRopa = (indiceRopa + 1) % imagenesRopa.length;
                repaint(); // Redibuja el panel para mostrar el nuevo avatar cambiado
            }
        });

        btnIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceRopa = (indiceRopa - 1 + imagenesRopa.length) % imagenesRopa.length;
                repaint(); // Redibuja el panel para mostrar el traje cambiado
            }
        });

        btnListo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreIngresado = txtNombre.getText().trim();
                if (nombreIngresado.isEmpty()) {
                    nombreIngresado = "Héroe";
                }
                ventanaPrincipal.iniciarJuegoConfirmado(nombreIngresado, indiceRopa);
            }
        });
    }

    //Fondo y diseño mas atractivo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Tono verde
        g2d.setColor(new Color(185, 225, 175)); 
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Mostrar el avatar que se seleccione
        try {
            // El selector pequeño de la derecha sigue usando el arreglo original
            ImageIcon iconAvatar = new ImageIcon(getClass().getResource(imagenesRopa[indiceRopa]));
            g2d.drawImage(iconAvatar.getImage(), 750, 180, 120, 150, this);

            // Personaje con traje basico
            ImageIcon iconAvatarGrande = new ImageIcon(getClass().getResource(imagenPersonaje[indiceRopa]));
            g2d.drawImage(iconAvatarGrande.getImage(), 200, 120, 250, 320, this);

        } catch (Exception e) {

        }

        // Cuadro de introduccion/motivacion
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

        // Escribir el texto dentro del recuadro
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22)); // Fuente tipo consola de comandos / retro
        g2d.drawString("¿Te gustaria aprender un idioma fuera de lo cotidiano?"
                + " Pues empecemos...", 150, 630);
        g2d.drawString("Usa las flechas para elegir tu ropa favorita.", 150, 670);
    }

    // Metodo predeterminado de netbeans 
    private void initComponents() {
    }
}
