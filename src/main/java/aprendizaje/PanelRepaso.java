/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aprendizaje;

/**
 *
 * @author Jack
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PanelRepaso extends JPanel {

    private MenuPrincipal menu;
    private int seccionActual;
    private JLabel lblPregunta;

    private JButton btnOpcion1;
    private JButton btnOpcion2;
    private JButton btnOpcion3;
    private JButton btnOpcion4;

    // Controla la pregunta actual
    private int indice = 0;

    private String[][] preguntas;
    private BufferedImage imagenFondo;
    private BufferedImage imagenFondoEscalada;
    private int anchoFondoEscalado = -1;
    private int altoFondoEscalado = -1;
    private Font fuentePixel;
    private ImageIcon iconoOpcionNormal;
    private ImageIcon iconoOpcionHover;

    public PanelRepaso(MenuPrincipal menu, int numeroBoton) {

        this.menu = menu;
        this.seccionActual=numeroBoton;
        this.fuentePixel = cargarFuentePixel();
        setLayout(new BorderLayout(20, 20));

        cargarFondoRepaso();

        // Hiragana 1
        if (numeroBoton == 1) {

            preguntas = new String[][]{
                {"a.png", "i ", "a", "u", "e", "a"},// la respuesta correcta es a
                {"i.png", "u", "a", "i", "o", "i"},// la respuedsta correcta es i
                {"u.png", "e", "u", "a", "i", "u"}// la respuesta correcta es u
            };
        }

        // Hiragana 2
        if (numeroBoton == 2) {

            preguntas = new String[][]{
                {"e.png", "o", "ka", "e", "a", "e"},// la respuesta correcta es e
                {"o.png", "e", "u", "ka", "o", "o"},// la respuesta correcta es o
                {"ka.png", "i", "ka", "o", "e", "ka"}// la respues correcta es ka
            };
        }

         // Hiragana 3
        if (numeroBoton == 3) {

            preguntas = new String[][]{
                {"sa.png", "si ", "sa", "su", "se", "sa"},// la respuesta correcta es a
                {"ki.png", "ku", "sa", "ki", "o", "ki"},// la respuedsta correcta es i
                {"shi.png", "se", "u", "so", "shi", "shi"},// la respuesta correcta es u
                {"ku.png", "ke", "u", "ku", "shi", "ku"}// la respuesta correcta es u
            };
        }

        // Hiragana 4
        if (numeroBoton == 4) {

            preguntas = new String[][]{
                {"su.png", "su", "ka", "so", "ko", "su"},// la respuesta correcta es e
                {"ke.png", "ke", "su", "ka", "o", "ke"},// la respuesta correcta es o
                {"se.png", "i", "se", "ke", "ka", "se"},// la respuesta correcta es ka
                {"ko.png", "ki", "ko", "shi", "ke", "ko"},// la respues correcta es ka
                {"so.png", "ki", "su", "so", "se", "so"}// la respues correcta es ka
            };
        }


        iniciarComponentes();

        mostrarPregunta();
    }

    private void cargarFondoRepaso() {
        try {
            imagenFondo = ImageIO.read(
                    getClass().getResource("/menu/fondo_repaso.png")
            );
        } catch (IOException | IllegalArgumentException e) {
            imagenFondo = null;
            System.err.println("No se pudo cargar /menu/fondo_repaso.png");
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

    private void iniciarComponentes() {

        //Titulo
        JLabel titulo = new JLabel(
                "¿Cómo se pronuncia?",
                SwingConstants.CENTER
        );

        titulo.setFont(fuentePixel.deriveFont(Font.PLAIN, 38f));
        titulo.setForeground(java.awt.Color.BLACK);
        titulo.setBorder(new EmptyBorder(65, 0, 0, 0));

        JButton btnVolverUnidades = new JButton();
        configurarBotonVolverUnidades(btnVolverUnidades);
        btnVolverUnidades.addActionListener(e -> {
            Sonido.reproducirClick();
            menu.mostrarPanelAprender();
        });

        JPanel zonaVolver = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );
        zonaVolver.setOpaque(false);
        zonaVolver.add(btnVolverUnidades);

        JPanel compensadorDerecho = new JPanel();
        compensadorDerecho.setOpaque(false);
        compensadorDerecho.setPreferredSize(new Dimension(95, 1));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(zonaVolver, BorderLayout.WEST);
        encabezado.add(titulo, BorderLayout.CENTER);
        encabezado.add(compensadorDerecho, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        // Label donde se muestra el hiragana
        lblPregunta = new JLabel("", SwingConstants.CENTER);
        lblPregunta.setFont(new Font("SansSerif", Font.BOLD, 120));
        lblPregunta.setBorder(new EmptyBorder(0, 0, 220, 0));

        add(lblPregunta, BorderLayout.CENTER);

        // Panel para las opciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 8, 8));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(528, 182));

        btnOpcion1 = new JButton();
        btnOpcion2 = new JButton();
        btnOpcion3 = new JButton();
        btnOpcion4 = new JButton();

        iconoOpcionNormal = cargarIconoOpcion("/menu/boton_inicio.png");
        iconoOpcionHover = cargarIconoOpcion("/menu/boton_hover.png");
        configurarBotonOpcion(btnOpcion1);
        configurarBotonOpcion(btnOpcion2);
        configurarBotonOpcion(btnOpcion3);
        configurarBotonOpcion(btnOpcion4);

        panelBotones.add(btnOpcion1);
        panelBotones.add(btnOpcion2);
        panelBotones.add(btnOpcion3);
        panelBotones.add(btnOpcion4);

        JPanel contenedorBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );
        contenedorBotones.setOpaque(false);
        contenedorBotones.add(panelBotones);
        add(contenedorBotones, BorderLayout.SOUTH);

        ActionListener evento = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JButton boton = (JButton) e.getSource();

                // Aqui se verifica si la opcion seleccionada es correcta
                // La respuesta correcta se guarda en la posición [5]
                if (boton.getText().equals(preguntas[indice][5])) {

                    Sonido.reproducirBuena();
                    indice++;

                    // Mostrar siguiente pregunta
                    if (indice < preguntas.length) {

                        mostrarPregunta();

                    } else {

                        mostrarFinal();
                    }
                //si se marca la alternativa incorrecta se reinicia
                }else{ 
                    Sonido.reproducirMala();
                    mensajeIntentalo();
                }
            }
        };

        btnOpcion1.addActionListener(evento);
        btnOpcion2.addActionListener(evento);
        btnOpcion3.addActionListener(evento);
        btnOpcion4.addActionListener(evento);
    }

    private void configurarBotonOpcion(JButton boton) {
        Dimension tamano = new Dimension(260, 87);
        boton.setPreferredSize(tamano);
        boton.setMinimumSize(tamano);
        boton.setMaximumSize(tamano);
        boton.setFont(fuentePixel.deriveFont(Font.PLAIN, 30f));
        boton.setForeground(new Color(82, 48, 29));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
        boton.setHorizontalTextPosition(JButton.CENTER);
        boton.setVerticalTextPosition(JButton.CENTER);
        boton.setIconTextGap(0);

        if (iconoOpcionNormal != null) {
            boton.setIcon(iconoOpcionNormal);
        }
        if (iconoOpcionHover != null) {
            boton.setRolloverIcon(iconoOpcionHover);
            boton.setPressedIcon(iconoOpcionHover);
            boton.setRolloverEnabled(true);
        }
    }

    private void configurarBotonVolverUnidades(JButton boton) {
        int ancho = 75;
        int alto = 68;
        Dimension tamano = new Dimension(ancho, alto);

        boton.setPreferredSize(tamano);
        boton.setMinimumSize(tamano);
        boton.setMaximumSize(tamano);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);

        ImageIcon normal = cargarIconoRecortado(
                "/menu/boton_anterior.png",
                ancho,
                alto
        );
        ImageIcon hover = cargarIconoRecortado(
                "/menu/boton_anterior_hover.png",
                ancho,
                alto
        );

        boton.setIcon(normal);
        boton.setRolloverIcon(hover);
        boton.setPressedIcon(hover);
        boton.setRolloverEnabled(true);
    }

    private ImageIcon cargarIconoRecortado(String ruta, int ancho, int alto) {
        try {
            BufferedImage original = ImageIO.read(getClass().getResource(ruta));
            int minX = original.getWidth();
            int minY = original.getHeight();
            int maxX = -1;
            int maxY = -1;

            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    int alpha = (original.getRGB(x, y) >>> 24) & 0xff;
                    if (alpha > 10) {
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                    }
                }
            }

            BufferedImage visible = maxX >= 0
                    ? original.getSubimage(
                            minX,
                            minY,
                            maxX - minX + 1,
                            maxY - minY + 1
                    )
                    : original;

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
            g2.drawImage(visible, 0, 0, ancho, alto, null);
            g2.dispose();
            return new ImageIcon(escalada);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar el botón para volver a unidades.");
            return null;
        }
    }

    private ImageIcon cargarIconoOpcion(String ruta) {
        try {
            BufferedImage original = ImageIO.read(getClass().getResource(ruta));
            int ancho = 260;
            int alto = 87;
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
            g2.drawImage(original, 0, 0, ancho, alto, null);
            g2.dispose();
            return new ImageIcon(escalada);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar el botón de opción: " + ruta);
            return null;
        }
    }

    private Font cargarFuentePixel() {
        try (InputStream fuenteStream = getClass().getResourceAsStream(
                "/fuentes/PixelOperator.ttf")) {

            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar PixelOperator en PanelRepaso.");
        }

        return new Font(Font.MONOSPACED, Font.BOLD, 38);
    }

    // Muestra la pregunta actual
    private void mostrarPregunta() {

        ImageIcon icono = new ImageIcon(
                getClass().getResource("/letras_png/" + preguntas[indice][0])
        );
        
        Image imagen = icono.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);

        lblPregunta.setIcon(new ImageIcon(imagen));

        btnOpcion1.setText(preguntas[indice][1]);
        btnOpcion2.setText(preguntas[indice][2]);
        btnOpcion3.setText(preguntas[indice][3]);
        btnOpcion4.setText(preguntas[indice][4]);
    }

    private void mensajeIntentalo(){
        removeAll();

        setLayout(new GridBagLayout());

        JLabel lblFinal = new JLabel("¡Inténtalo otra vez!", SwingConstants.CENTER);
        lblFinal.setFont(fuentePixel.deriveFont(Font.PLAIN, 40f));
        lblFinal.setForeground(Color.BLACK);

        JButton volver = new JButton("Volver");
        configurarBotonOpcion(volver);

        volver.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                PanelRepaso repaso = new PanelRepaso(menu, seccionActual);
                menu.mostrarVista(repaso);
              
            }
        });

        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;
        gbcTitulo.weightx = 1.0;
        gbcTitulo.weighty = 0.38;
        gbcTitulo.anchor = GridBagConstraints.SOUTH;
        gbcTitulo.insets = new Insets(0, 0, 18, 0);
        add(lblFinal, gbcTitulo);

        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.gridx = 0;
        gbcBoton.gridy = 1;
        gbcBoton.weightx = 1.0;
        gbcBoton.weighty = 0.62;
        gbcBoton.anchor = GridBagConstraints.NORTH;
        add(volver, gbcBoton);

        revalidate();
        repaint();
    }

    // Pantalla final del repaso
    private void mostrarFinal() {

        removeAll();

        setLayout(new GridBagLayout());

        JLabel lblFinal = new JLabel("¡Repaso completado!", SwingConstants.CENTER);
        lblFinal.setFont(fuentePixel.deriveFont(Font.PLAIN, 40f));
        lblFinal.setForeground(Color.BLACK);

        JButton volver = new JButton("Volver");
        configurarBotonOpcion(volver);

        volver.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                menu.mostrarPanelAprender();
            }
        });

        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;
        gbcTitulo.weightx = 1.0;
        gbcTitulo.weighty = 0.38;
        gbcTitulo.anchor = GridBagConstraints.SOUTH;
        gbcTitulo.insets = new Insets(0, 0, 18, 0);
        add(lblFinal, gbcTitulo);

        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.gridx = 0;
        gbcBoton.gridy = 1;
        gbcBoton.weightx = 1.0;
        gbcBoton.weighty = 0.62;
        gbcBoton.anchor = GridBagConstraints.NORTH;
        add(volver, gbcBoton);

        revalidate();
        repaint();
    }
}
