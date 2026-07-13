package aprendizaje;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class PantallaCarga extends JPanel {

    // Panel personalizado para aplicar fade a todos los elementos.
    private JPanel panel;

    // Componentes visibles de la pantalla de carga.
    private JLabel lblLogo;
    private JLabel lblCargando;
    private JLabel lblSubtitulo;

    // Control de transparencia general.
    // 0 = completamente invisible
    // 1 = completamente visible
    private float alpha = 0f;

    // Temporizadores para las animaciones.
    private Timer timerFade;
    private Timer timerTexto;

    // Controla la cantidad de puntos en "Cargando..."
    private int cantidadPuntos = 0;

    // Duración mínima de la pantalla de carga.
    // Evita que aparezca y desaparezca demasiado rápido.
    private static final long DURACION_MINIMA_MS = 1800;
    private final Runnable alFinalizar;

    public PantallaCarga(Runnable alFinalizar) {
        this.alFinalizar = alFinalizar;
        setLayout(new BorderLayout());

        crearInterfaz();
    }

    public void iniciar() {
        javax.swing.SwingUtilities.invokeLater(this::iniciarFadeEntrada);
    }

    /**
     * Crea todos los elementos visuales de la pantalla.
     */
    private void crearInterfaz() {

        panel = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {

                // Dibujamos el fondo negro normalmente.
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                // Aplica la transparencia general a los componentes hijos.
                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                alpha));

                // Permite que JPanel dibuje sus elementos con el alpha actual.
                paintComponentesConAlpha(g2);

                g2.dispose();
            }

            /**
             * Dibuja manualmente los componentes internos para que respeten
             * la transparencia general de la pantalla.
             */
            private void paintComponentesConAlpha(Graphics2D g2) {
                super.paintChildren(g2);
            }

            @Override
            protected void paintChildren(Graphics g) {
                // Se evita el dibujo normal porque ya lo hacemos manualmente
                // dentro de paintComponent usando el alpha.
            }
        };

        panel.setBackground(Color.BLACK);

        // Carga la fuente Pixel Operator.
        Font fuentePixel = cargarFuentePixel();

        crearLogo();
        crearTextoCargando(fuentePixel);
        crearSubtitulo(fuentePixel);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Crea y posiciona el logo de la universidad.
     */
    private void crearLogo() {

        var recurso = getClass().getResource("/menu/logo_utp_pixel.png");

        if (recurso == null) {
            System.err.println(
                    "No se encontró el logo: /menu/logo_utp_pixel.png");
            return;
        }

        ImageIcon iconoOriginal = new ImageIcon(recurso);
        Image imagenOriginal = iconoOriginal.getImage();

        // Relación original:
        // 2172 x 724, aproximadamente 3:1
        int anchoLogo = 760;
        int altoLogo = (int) Math.round(
                anchoLogo * 724.0 / 2172.0);

        Image imagenEscalada = imagenOriginal.getScaledInstance(
                anchoLogo,
                altoLogo,
                Image.SCALE_SMOOTH);

        lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblLogo);
    }

    /**
     * Crea el texto "Cargando..." con estilo pixel art.
     */
    private void crearTextoCargando(Font fuentePixel) {

        lblCargando = new JLabel("Cargando");
        lblCargando.setForeground(new Color(245, 245, 245));
        lblCargando.setHorizontalAlignment(SwingConstants.CENTER);
        lblCargando.setFont(fuentePixel.deriveFont(Font.PLAIN, 34f));

        panel.add(lblCargando);
    }

    /**
     * Crea el subtítulo del proyecto.
     */
    private void crearSubtitulo(Font fuentePixel) {

        lblSubtitulo = new JLabel(
                "Algoritmos y estructuras de datos");

        lblSubtitulo.setForeground(new Color(170, 170, 170));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(
                fuentePixel.deriveFont(Font.PLAIN, 22f));

        panel.add(lblSubtitulo);
    }

    /**
     * Intenta cargar Pixel Operator desde resources.
     *
     * Coloca el archivo en:
     * src/main/resources/fuentes/PixelOperator.ttf
     */
    private Font cargarFuentePixel() {

        try (InputStream fuenteStream = getClass().getResourceAsStream(
                "/fuentes/PixelOperator.ttf")) {

            if (fuenteStream == null) {
                System.err.println(
                        "No se encontró PixelOperator.ttf. "
                        + "Se usará una fuente alternativa.");

                return new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        24);
            }

            Font fuente = Font.createFont(
                    Font.TRUETYPE_FONT,
                    fuenteStream);

            return fuente.deriveFont(Font.PLAIN, 24f);

        } catch (Exception e) {

            e.printStackTrace();

            return new Font(
                    Font.MONOSPACED,
                    Font.PLAIN,
                    24);
        }
    }

    /**
     * Ajusta posiciones cuando la ventana ya tiene dimensiones reales.
     */
    private void actualizarPosiciones() {

        int ancho = panel.getWidth();
        int alto = panel.getHeight();

        if (ancho <= 0 || alto <= 0) {
            return;
        }

        int anchoLogo = 760;
        int altoLogo = (int) Math.round(
                anchoLogo * 724.0 / 2172.0);

        int xLogo = (ancho - anchoLogo) / 2;
        int yLogo = (int) Math.round(alto * 0.30);

        if (lblLogo != null) {
            lblLogo.setBounds(
                    xLogo,
                    yLogo,
                    anchoLogo,
                    altoLogo);
        }

        if (lblCargando != null) {
            lblCargando.setBounds(
                    0,
                    (int) Math.round(alto * 0.70),
                    ancho,
                    50);
        }

        if (lblSubtitulo != null) {
            lblSubtitulo.setBounds(
                    0,
                    (int) Math.round(alto * 0.77),
                    ancho,
                    40);
        }
    }

    /**
     * Fade in suave.
     */
    private void iniciarFadeEntrada() {

        actualizarPosiciones();

        final long inicio = System.nanoTime();
        final long duracion = 900_000_000L;

        timerFade = new Timer(16, e -> {

            long transcurrido = System.nanoTime() - inicio;

            float t = Math.min(
                    transcurrido / (float) duracion,
                    1f);

            // Ease-out cúbico:
            // empieza más rápido y termina suavemente.
            float progreso = 1f
                    - (float) Math.pow(1f - t, 3);

            alpha = progreso;

            panel.repaint();

            if (t >= 1f) {
                alpha = 1f;
                timerFade.stop();

                iniciarAnimacionTexto();
                cargarRecursos();
            }
        });

        timerFade.start();
    }

    /**
     * Anima:
     * Cargando
     * Cargando.
     * Cargando..
     * Cargando...
     */
    private void iniciarAnimacionTexto() {

        cantidadPuntos = 0;

        timerTexto = new Timer(350, e -> {

            cantidadPuntos = (cantidadPuntos + 1) % 4;

            String puntos = ".".repeat(cantidadPuntos);

            lblCargando.setText("Cargando" + puntos);
        });

        timerTexto.start();
    }

    /**
     * Ejecuta las cargas pesadas fuera del hilo de Swing.
     */
    private void cargarRecursos() {

        final long inicioCarga = System.currentTimeMillis();

        SwingWorker<Void, String> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {

                publish("Preparando sonido");

                // Precarga efectos cortos.
                Sonido.precargar();

                publish("Preparando música");

                // Precarga la música del menú.
                Sonido.precargarMusicaMenu();
                Sonido.precargarMusicaAprender();
                Sonido.precargarMusicaJugar();

                // Decodifica el PNG grande del título mientras se muestra
                // la pantalla de carga.
                TituloAnimado.precargarLogo();

                /*
                 * Más adelante puedes añadir aquí:
                 *
                 * ResourceManager.cargarImagenes();
                 * ResourceManager.cargarFondos();
                 * ResourceManager.cargarSprites();
                 * ResourceManager.cargarFuentes();
                 */

                return null;
            }

            @Override
            protected void process(List<String> estados) {

                // De momento mantenemos "Cargando..."
                // en lugar de mostrar cada mensaje interno.
            }

            @Override
            protected void done() {

                long tiempoTranscurrido =
                        System.currentTimeMillis() - inicioCarga;

                long esperaRestante =
                        DURACION_MINIMA_MS - tiempoTranscurrido;

                if (esperaRestante > 0) {

                    Timer espera = new Timer(
                            (int) esperaRestante,
                            e -> {

                                ((Timer) e.getSource()).stop();
                                finalizarCarga();
                            });

                    espera.setRepeats(false);
                    espera.start();

                } else {
                    finalizarCarga();
                }
            }
        };

        worker.execute();
    }

    /**
     * Detiene el texto y comienza la salida.
     */
    private void finalizarCarga() {

        if (timerTexto != null) {
            timerTexto.stop();
        }

        lblCargando.setText("Listo");

        // Pequeña pausa para que "Listo" sea visible.
        Timer pausa = new Timer(250, e -> {

            ((Timer) e.getSource()).stop();
            iniciarFadeSalida();
        });

        pausa.setRepeats(false);
        pausa.start();
    }

    /**
     * Fade out suave antes de abrir el menú principal.
     */
    private void iniciarFadeSalida() {

        final long inicio = System.nanoTime();
        final long duracion = 700_000_000L;

        timerFade = new Timer(16, e -> {

            long transcurrido = System.nanoTime() - inicio;

            float t = Math.min(
                    transcurrido / (float) duracion,
                    1f);

            // Ease-in cúbico para desaparecer lentamente
            // al principio y más rápido al final.
            float progreso = t * t * t;

            alpha = 1f - progreso;

            panel.repaint();

            if (t >= 1f) {

                alpha = 0f;
                timerFade.stop();

                alFinalizar.run();
            }
        });

        timerFade.start();
    }

}
