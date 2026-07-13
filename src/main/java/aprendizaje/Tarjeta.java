package aprendizaje;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Este panel Tarjeta se añade en el panel central de PanelLeccion
public class Tarjeta extends JPanel {

    private CardLayout card;
    private List<Leccion> lecciones; //lista que guarda objetos de tipo Leccion
    private int indiceActual = 1;
    private final boolean usarBotonPlay;
    private final Font fuentePixel;

    public Tarjeta(List<Leccion> lecciones, boolean usarBotonPlay) {
        this.lecciones = lecciones;
        this.usarBotonPlay = usarBotonPlay;
        this.fuentePixel = cargarFuentePixel();

        card = new CardLayout(); //creamos el CardLayout
        setLayout(card); // le asignamos un layout de tipo CardLayout a Tarjeta
        crearTarjetas();

    }

    //Creamos el 'molde' de Tarjeta que contiene el gif,audio y el label de texto
    public void crearTarjetas() {

        //Se crea un panel para cada tarjeta y se le añaden sus componentes
        for (int i = 0; i < lecciones.size(); i++) {

            JPanel p = new JPanel();

            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setAlignmentX(Component.CENTER_ALIGNMENT);
            p.setOpaque(false);

            // ==================== PERGAMINO ====================
            ImageIcon iconoPergamino = new ImageIcon(
                    getClass().getResource("/menu/CUADRO_GIF.png")
            );

            Image imagenPergamino = iconoPergamino.getImage().getScaledInstance(
                    460,
                    360,
                    Image.SCALE_SMOOTH
            );

            JLabel pergamino = new JLabel(new ImageIcon(imagenPergamino));
            pergamino.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Permite poner componentes encima del pergamino
            pergamino.setLayout(new java.awt.GridBagLayout());

            // ==================== GIF ====================
            JLabel gif = new JLabel();

            URL url = getClass().getResource(lecciones.get(i).getRutaGif());

            System.out.println("GIF URL: " + url);

            if (url != null) {

                ImageIcon icon = new ImageIcon(url);
                // Se conserva el ImageIcon original para mantener la animación.
                // getScaledInstance puede impedir el renderizado de algunos GIF.
                gif.setIcon(icon);

            } else {
                gif.setText("NO SE ENCUENTRA GIF");
            }

            gif.setHorizontalAlignment(JLabel.CENTER);
            gif.setOpaque(false);

            // Agregar el GIF encima del pergamino
            pergamino.add(gif);

            // Agregar el pergamino al panel
            p.add(pergamino);

            p.add(Box.createVerticalStrut(5));

            // ==================== AUDIO ====================
            JButton btnReproducirAudio = new JButton();
            configurarBotonAudio(btnReproducirAudio);
            btnReproducirAudio.setAlignmentX(Component.CENTER_ALIGNMENT);

            btnReproducirAudio.setFocusPainted(false);

            Leccion leccionActual = lecciones.get(i);
            btnReproducirAudio.addActionListener(
                    e -> reproducirSonido(leccionActual.getRutaAudio())
            );

            p.add(btnReproducirAudio);

            p.add(Box.createVerticalStrut(0));

            // ==================== ROMAJI ====================
            JLabel letra = new JLabel();
            letra.setText(lecciones.get(i).getRomaji());
            letra.setAlignmentX(Component.CENTER_ALIGNMENT);

            letra.setFont(fuentePixel.deriveFont(Font.PLAIN, 35f));
            letra.setForeground(Color.BLACK);

            p.add(letra);

            add(p, "tarjeta" + (i + 1));
        }
    }

    //metodos que permiten cambiar de tarjetas
    public void mostrarSiguiente() {
        if (indiceActual < lecciones.size()) {
            indiceActual++;
            card.show(this, "tarjeta" + indiceActual);
        }

    }

    public void mostrarAnterior() {
        if (indiceActual > 0) {
            indiceActual--;
            card.show(this, "tarjeta" + indiceActual);
        }
    }

    private void configurarBotonAudio(JButton boton) {
        Dimension tamano = usarBotonPlay
                ? new Dimension(180, 120)
                : new Dimension(80, 40);

        boton.setPreferredSize(tamano);
        boton.setMinimumSize(tamano);
        boton.setMaximumSize(tamano);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);

        if (!usarBotonPlay) {
            return;
        }

        ImageIcon iconoNormal = cargarIconoPlay(
                "/menu/boton_play.png",
                tamano
        );
        ImageIcon iconoHover = cargarIconoPlay(
                "/menu/boton_play_hover.png",
                tamano
        );

        if (iconoNormal == null) {
            boton.setText("Reproducir");
            return;
        }

        boton.setIcon(iconoNormal);
        if (iconoHover != null) {
            boton.setRolloverIcon(iconoHover);
            boton.setPressedIcon(iconoHover);
            boton.setRolloverEnabled(true);
        }
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
    }

    private ImageIcon cargarIconoPlay(String ruta, Dimension tamano) {
        URL recurso = getClass().getResource(ruta);
        if (recurso == null) {
            return null;
        }

        ImageIcon original = new ImageIcon(recurso);
        Image imagenEscalada = original.getImage().getScaledInstance(
                tamano.width,
                tamano.height,
                Image.SCALE_SMOOTH
        );
        return new ImageIcon(imagenEscalada);
    }

    private Font cargarFuentePixel() {
        try (InputStream fuenteStream = getClass().getResourceAsStream(
                "/fuentes/PixelOperator.ttf")) {

            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar PixelOperator en Tarjeta.");
        }

        return new Font(Font.MONOSPACED, Font.BOLD, 35);
    }

    /**
     * Hace transparentes la tarjeta y sus vistas para mostrar el fondo de la
     * unidad.
     */
    public void setFondoTransparente() {
        setOpaque(false);

        for (Component componente : getComponents()) {
            if (componente instanceof JPanel panel) {
                panel.setOpaque(false);
            }
        }
    }

    // Verifica si ya se llegó a la última tarjeta
    public boolean esUltimaTarjeta() {
        return indiceActual >= lecciones.size();
    }

    //Metodo para reproducir el audio
    private void reproducirSonido(String rutaAudio) {
        try {
            URL url = getClass().getResource(rutaAudio);
            if (url != null) {
                javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
                javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } else {
                System.out.println("No se encontró el archivo de audio: " + rutaAudio);
            }
        } catch (Exception ex) {
            System.out.println("Error al reproducir audio.");
            ex.printStackTrace();
        }
    }
}
