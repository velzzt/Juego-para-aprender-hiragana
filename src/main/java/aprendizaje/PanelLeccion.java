package aprendizaje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
public class PanelLeccion extends JPanel {

    private MenuPrincipal menuPrincipal;
    private Tarjeta tarjeta; //panel con CardLayout
    private int numeroHiragama;
    private BufferedImage imagenFondo;
    private BufferedImage imagenFondoEscalada;
    private int anchoFondoEscalado = -1;
    private int altoFondoEscalado = -1;

    public PanelLeccion(MenuPrincipal menuPanel, ListaLeccion lista, int numeroHiragama) {
        this.menuPrincipal = menuPanel; //guardado como referencia
        this.numeroHiragama = numeroHiragama;//
        tarjeta = new Tarjeta(
                lista.getLecciones(),
                true
        ); //tarjeta obtiene la lista con objetos de Leccion

        cargarFondoUnidad();
        tarjeta.setFondoTransparente();
        //asiganamos un BorderLayout a PanelLeccion para dividir el contenedor en 5 regiones (de los cuales usaremos 2, centro y sur)
        setLayout(new BorderLayout(20, 20));
        // Se añade un margen exterior y mayor separación entre componentes
        // para mejorar la presentación de la pantalla
        setBackground(new Color(220, 240, 220));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        colocarComponentes();

    }

    private void cargarFondoUnidad() {
        try {
            imagenFondo = ImageIO.read(
                    getClass().getResource("/menu/fondo_repaso.png")
            );
        } catch (IOException | IllegalArgumentException e) {
            imagenFondo = null;
            System.err.println("No se pudo cargar /menu/fondo_unidad.png");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo == null || getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        double escala = Math.max(
                getWidth() / (double) imagenFondo.getWidth(),
                getHeight() / (double) imagenFondo.getHeight()
        );
        int ancho = Math.max(1, (int) Math.ceil(imagenFondo.getWidth() * escala));
        int alto = Math.max(1, (int) Math.ceil(imagenFondo.getHeight() * escala));

        if (imagenFondoEscalada == null
                || ancho != anchoFondoEscalado
                || alto != altoFondoEscalado) {
            imagenFondoEscalada = new BufferedImage(
                    ancho,
                    alto,
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = imagenFondoEscalada.createGraphics();
            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );
            g2.drawImage(imagenFondo, 0, 0, ancho, alto, null);
            g2.dispose();
            anchoFondoEscalado = ancho;
            altoFondoEscalado = alto;
        }

        int x = (getWidth() - ancho) / 2;
        int y = (getHeight() - alto) / 2;
        g.drawImage(imagenFondoEscalada, x, y, this);
    }

    private void colocarComponentes() {

        //Colocar el panel tarjeta y el panel filaBotones en el PanelLeccion
        add(tarjeta, BorderLayout.CENTER);

        // ajuste de separación entre botones para no empujar la tarjeta
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        filaBotones.setOpaque(false);
        // Reserva más espacio debajo que encima para elevar visualmente la fila.
        filaBotones.setBorder(new EmptyBorder(0, 0, 50, 0));

        JButton btnAnterior = crearBotonEstilizado("Anterior");
        JButton btnVolver = crearBotonEstilizado("Volver");
        JButton btnSiguiente = crearBotonEstilizado("Siguiente");

        configurarBotonNavegacion(
                btnAnterior,
                "/menu/boton_anterior.png",
                "/menu/boton_anterior_hover.png"
        );
        configurarBotonNavegacion(
                btnVolver,
                "/menu/boton_volver.png",
                "/menu/boton_volver_hover.png"
        );
        configurarBotonSiguiente(btnSiguiente);

        filaBotones.add(btnAnterior);
        filaBotones.add(btnVolver);
        filaBotones.add(btnSiguiente);

        add(filaBotones, BorderLayout.SOUTH);

        //Evento para el botonVolver
        ActionListener botonVolver = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                menuPrincipal.mostrarPanelAprender(); // regresa a PanelAprender
            }

        };

        btnVolver.addActionListener(botonVolver); //añadimos el evento al boton

        //Añadir eventos que permiten cambiar de tarjetas(las que contienen el gif, audio y label de texto) al pulsar los botones 'Anterior' y 'Siguiente'
        btnAnterior.addActionListener(e -> {
            Sonido.reproducirClick();
            tarjeta.mostrarAnterior();
        });
        btnSiguiente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();

                // Si aún hay tarjetas disponibles
                if (!tarjeta.esUltimaTarjeta()) {

                    tarjeta.mostrarSiguiente();

                } else {

                    // Cuando terminan las tarjetas se abre el repaso
                    PanelRepaso panel = new PanelRepaso(menuPrincipal, numeroHiragama);

                    menuPrincipal.mostrarVista(panel);
                }
            }
        });
        
    }

    // Se personaliza la apariencia del JButton manteniendo intacto
    // su comportamiento y los eventos asociados
    private JButton crearBotonEstilizado(String texto) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btn.setBackground(new Color(110, 180, 80));
    btn.setForeground(Color.WHITE);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setFocusPainted(false);
    btn.setMargin(new Insets(8, 20, 8, 20));
    btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
    return btn;
    }

    private void configurarBotonSiguiente(JButton boton) {
        configurarBotonNavegacion(
                boton,
                "/menu/boton_siguiente.png",
                "/menu/boton_siguiente_hover.png"
        );
    }

    private void configurarBotonNavegacion(
            JButton boton,
            String rutaNormal,
            String rutaHover
    ) {
        try {
            int ancho = 75;
            int alto = 68;
            ImageIcon iconoNormal = cargarIconoNavegacion(
                    rutaNormal,
                    ancho,
                    alto
            );
            ImageIcon iconoHover = cargarIconoNavegacion(
                    rutaHover,
                    ancho,
                    alto
            );

            Dimension tamano = new Dimension(ancho, alto);
            boton.setText(null);
            boton.setIcon(iconoNormal);
            boton.setRolloverIcon(iconoHover);
            boton.setPressedIcon(iconoHover);
            boton.setRolloverEnabled(true);
            boton.setPreferredSize(tamano);
            boton.setMinimumSize(tamano);
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setOpaque(false);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar el botón de navegación: " + rutaNormal);
        }
    }

    private ImageIcon cargarIconoNavegacion(
            String ruta,
            int ancho,
            int alto
    ) throws IOException {
        BufferedImage original = ImageIO.read(getClass().getResource(ruta));
        BufferedImage recortada = recortarTransparencia(original);
        BufferedImage escalada = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = escalada.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        g2.drawImage(recortada, 0, 0, ancho, alto, null);
        g2.dispose();
        return new ImageIcon(escalada);
    }

    private BufferedImage recortarTransparencia(BufferedImage imagen) {
        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int alpha = (imagen.getRGB(x, y) >>> 24) & 0xff;
                if (alpha > 10) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < 0) {
            return imagen;
        }

        return imagen.getSubimage(
                minX,
                minY,
                maxX - minX + 1,
                maxY - minY + 1
        );
    }
}
