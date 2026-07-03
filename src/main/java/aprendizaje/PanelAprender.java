package aprendizaje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PanelAprender extends JPanel {

    private MenuPrincipal menuPanel;

    //menuPanel es la referencia del MenuPrincipal
    public PanelAprender(MenuPrincipal menuPanel) {
        this.menuPanel = menuPanel;

        // BorderLayout deja el titulo arriba y el panel de botones en el centro
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(220, 240, 220));
        setBorder(new EmptyBorder(30, 40, 40, 40));
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        colocarComponentes();
    }

    private void colocarComponentes() {
        JLabel titulo = new JLabel("Selecciona una unidad", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(new Color(20, 90, 45));
        add(titulo, BorderLayout.NORTH);

        // Panel que agrupa todos los botones de las unidades y el botón para volver
        // GridLayout mantiene los 5 botones del mismo alto y los apila con separacion
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 250, 20, 250));

        // cada boton recibe un texto interno para la logica y dos sprites para lo visual
        // el primero es el estado normal y el segundo es el estado hover
        JButton btnHiragana1 = crearBotonEstilizado("Hiragana 1", "/menu/hiragana1.png",
                "/menu/hiragana1_hover.png");
        JButton btnHiragana2 = crearBotonEstilizado("Hiragana 2", "/menu/hiragana2.png",
                "/menu/hiragana2_hover.png");
        JButton btnHiragana3 = crearBotonEstilizado("Hiragana 3", "/menu/hiragana3.png",
                "/menu/hiragana3_hover.png");
        JButton btnHiragana4 = crearBotonEstilizado("Hiragana 4", "/menu/hiragana4.png",
                "/menu/hiragana4_hover.png");
        JButton volverMenu = crearBotonEstilizado("Volver al menu principal", "/menu/volver.png",
                "/menu/volver_hover.png");

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
                menuPanel.getContentPane().removeAll();
                menuPanel.getContentPane().add(h1);
                menuPanel.getContentPane().revalidate();
                menuPanel.getContentPane().repaint();
            }
        };

        btnHiragana1.addActionListener(abrirLeccion);
        btnHiragana2.addActionListener(abrirLeccion);
        btnHiragana3.addActionListener(abrirLeccion);
        btnHiragana4.addActionListener(abrirLeccion);
    }

    // crea un JButton utilizando sprites para los distintos estados del botón
    private JButton crearBotonEstilizado(String texto, String rutaNormal, String rutaHover) {
        JButton btn = new JButton();

        // ActionCommand guarda el nombre del boton aunque no mostremos texto encima del sprite
        btn.setActionCommand(texto);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        // Ocultamos el estilo default de JButton para que solo se vea la imagen del boton
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setRolloverEnabled(true);

        //intenta cargar las imágenes personalizadas del botón
        try {
            //todos los botones se dibujan en un lienzo comun para que queden simetricos
            int anchoBoton = 380;
            int altoBoton = 80;

            //icono normal, icono al pasar el mouse y icono al hacer click
            btn.setIcon(cargarIconoBoton(rutaNormal, anchoBoton, altoBoton));
            btn.setRolloverIcon(cargarIconoBoton(rutaHover, anchoBoton, altoBoton));
            btn.setPressedIcon(cargarIconoBoton(rutaHover, anchoBoton, altoBoton));
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

        // Elimina bordes transparentes antes de escalar para que todos se vean del mismo tamaño
        BufferedImage spriteRecortado = recortarTransparencia(imagenOriginal);

        // Lienzo transparente fijo, normal y hover ocupan exactamente la misma area
        BufferedImage lienzo = new BufferedImage(anchoBoton, altoBoton, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = lienzo.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(spriteRecortado.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH), 0, 0, null);
        g2.dispose();

        return new ImageIcon(lienzo);
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
