package aprendizaje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PanelAprender extends JPanel {

    private MenuPrincipal menuPanel;
    private BufferedImage imagenFondo;
    private BufferedImage imagenFondoEscalada;
    private int anchoFondoEscalado = -1;
    private int altoFondoEscalado = -1;
    private Font fuentePixel;

    //menuPanel es la referencia del MenuPrincipal
    public PanelAprender(MenuPrincipal menuPanel) {
        this.menuPanel = menuPanel;

        // BorderLayout deja el titulo arriba y el panel de botones en el centro
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(220, 240, 220));
        setBorder(new EmptyBorder(30, 40, 40, 40));
        cargarFondo();
        fuentePixel = cargarFuentePixel();
        iniciarComponentes();
    }

    private void cargarFondo() {
        try {
            imagenFondo = ImageIO.read(
                    getClass().getResource("/menu/fondo_aprender.png")
            );
        } catch (IOException | IllegalArgumentException e) {
            imagenFondo = null;
            System.err.println("No se pudo cargar /menu/fondo_aprender.png");
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

            Graphics2D g2Escala = imagenFondoEscalada.createGraphics();
            g2Escala.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );
            g2Escala.drawImage(imagenFondo, 0, 0, ancho, alto, null);
            g2Escala.dispose();

            anchoFondoEscalado = ancho;
            altoFondoEscalado = alto;
        }

        int x = (getWidth() - ancho) / 2;
        int y = (getHeight() - alto) / 2;
        g.drawImage(imagenFondoEscalada, x, y, this);
    }

    private void iniciarComponentes() {
        colocarComponentes();
    }

    private void colocarComponentes() {
        // Conserva el espacio que antes ocupaba el título, pero sin mostrar
        // texto. Así los botones mantienen su distribución anterior y no
        // cubren la parte importante de la zona superior del fondo.
        JPanel espacioSuperior = new JPanel();
        espacioSuperior.setOpaque(false);
        espacioSuperior.setPreferredSize(new Dimension(0, 45));
        add(espacioSuperior, BorderLayout.NORTH);

        // Panel que agrupa todos los botones de las unidades y el botón para volver
        // GridLayout mantiene los 5 botones del mismo alto y los apila con separacion
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 250, 20, 250));

        // cada boton recibe un texto interno para la logica y dos sprites para lo visual
        // el primero es el estado normal y el segundo es el estado hover
        JButton btnHiragana1 = crearBotonEstilizado("Hiragana 1");
        JButton btnHiragana2 = crearBotonEstilizado("Hiragana 2");
        JButton btnHiragana3 = crearBotonEstilizado("Hiragana 3");
        JButton btnHiragana4 = crearBotonEstilizado("Hiragana 4");
        JButton volverMenu = crearBotonEstilizado("Volver");

        panelBotones.add(btnHiragana1);
        panelBotones.add(btnHiragana2);
        panelBotones.add(btnHiragana3);
        panelBotones.add(btnHiragana4);
        panelBotones.add(volverMenu);

        add(panelBotones, BorderLayout.CENTER);

        // Evento del botón para volver al menú principal
        ActionListener btnVolverMenu = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                Sonido.reanudarMusicaMenu();
                menuPanel.mostrarMenu();
            }
        };

        volverMenu.addActionListener(btnVolverMenu);

        ActionListener abrirLeccion = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                // getSource() devuelve el componente que generó el evento
                // Se convierte a JButton para acceder a su información
                JButton btn = (JButton) e.getSource();

                // Como el texto visible esta oculto por el sprite, leemos el texto interno
                String texto = btn.getActionCommand();

                // Extrae el numero final de "Hiragana 1", "Hiragana 2", etc.
                int numero = Integer.parseInt(texto.substring(texto.length() - 1));
                ListaLeccion lista = menuPanel.obtenerListaLeccion(numero);
                // Se crea el panel correspondiente a la unidad seleccionada
                PanelLeccion h1 = new PanelLeccion(menuPanel, lista, numero);
                menuPanel.mostrarVista(h1);
            }
        };

        btnHiragana1.addActionListener(abrirLeccion);
        btnHiragana2.addActionListener(abrirLeccion);
        btnHiragana3.addActionListener(abrirLeccion);
        btnHiragana4.addActionListener(abrirLeccion);
    }

    // crea un JButton utilizando sprites para los distintos estados del botón
    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);

        // ActionCommand guarda el nombre del boton aunque no mostremos texto encima del sprite
        btn.setActionCommand(texto);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        // Ocultamos el estilo default de JButton para que solo se vea la imagen del boton
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setRolloverEnabled(true);
        btn.setFont(fuentePixel.deriveFont(Font.PLAIN, 25f));
        btn.setForeground(new Color(82, 48, 29));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setIconTextGap(0);

        //intenta cargar las imágenes personalizadas del botón
        try {
            //todos los botones se dibujan en un lienzo comun para que queden simetricos
            int anchoBoton = 330;
            int altoBoton = 110;

            //icono normal, icono al pasar el mouse y icono al hacer click
            btn.setIcon(cargarIconoBoton("/menu/boton_inicio.png", anchoBoton, altoBoton));
            btn.setRolloverIcon(cargarIconoBoton("/menu/boton_hover.png", anchoBoton, altoBoton));
            btn.setPressedIcon(cargarIconoBoton("/menu/boton_hover.png", anchoBoton, altoBoton));
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            // fallback, si una imagen falta o la ruta esta mal, aparece un boton clasico
            // asi la navegacion no se rompe mientras ajustas los sprites
            btn.setText(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
            btn.setBackground(new Color(110, 180, 80));
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
            btn.setMargin(new Insets(14, 25, 14, 25));
        }

        return btn;
    }

    // Carga el sprite del botón, elimina márgenes transparentes
    // y lo adapta al tamaño utilizado por la interfaz.
    private ImageIcon cargarIconoBoton(String ruta, int anchoBoton, int altoBoton) throws IOException {
        // Carga la imagen desde src/main/resources por eso la ruta empieza con "/menu/...".
        BufferedImage imagenOriginal = ImageIO.read(getClass().getResource(ruta));

        // Lienzo transparente fijo, normal y hover ocupan exactamente la misma area
        BufferedImage lienzo = new BufferedImage(anchoBoton, altoBoton, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = lienzo.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imagenOriginal, 0, 0, anchoBoton, altoBoton, null);
        g2.dispose();

        return new ImageIcon(lienzo);
    }

    private Font cargarFuentePixel() {
        try (InputStream fuenteStream = getClass().getResourceAsStream(
                "/fuentes/PixelOperator.ttf")) {

            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar PixelOperator en PanelAprender.");
        }

        return new Font(Font.MONOSPACED, Font.BOLD, 25);
    }

    // Elimina los bordes transparentes de un sprite antes de escalarlo
    // asi todos los botones mantienen un tamaño visual uniforme
    private BufferedImage recortarTransparencia(BufferedImage imagen) {
        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int alpha = (imagen.getRGB(x, y) >> 24) & 0xff;

                // Ignora pixeles casi invisibles, esto evita que sombras o residuos transparentes
                // hagan que un boton parezca mas pequeño que los demas al escalarlo
                if (alpha > 10) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        //si toda la imagen es transparente, se devuelve la imagen original
        if (maxX == -1) {
            return imagen;
        }

        return imagen.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}
