package aprendizaje;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Componente encargado de mostrar el logo del juego con una animación
 * de aparición (fade + desplazamiento vertical).
 */
public class TituloAnimado extends JComponent {

    // Imagen original del logo, cargada completamente en memoria.
    private static BufferedImage logoOriginal;

    // Imagen ya redimensionada.
    // Esta es la que se dibuja durante la animación.
    private BufferedImage logoEscalado;

    // Transparencia:
    // 0 = invisible
    // 1 = completamente visible
    private float alpha = 0f;

    // Posición vertical actual y final.
    private int yActual;
    private int yFinal;

    // Tamaño final del logo.
    private int anchoLogo;
    private int altoLogo;

    // Último ancho utilizado para generar la versión escalada.
    private int ultimoAnchoComponente = -1;

    // Temporizador de la animación.
    private Timer timer;

    // Duración de la animación en milisegundos.
    private static final int DURACION_MS = 1100;

    // Momento exacto en que comenzó la animación.
    private long inicioAnimacionNs;

    public TituloAnimado() {

        setOpaque(false);

        // Carga el archivo si todavía no fue precargado.
        cargarLogoOriginal();
    }

    /**
     * Precarga estáticamente el PNG del título.
     *
     * Este método puede llamarse desde PantallaCarga para que la imagen
     * esté decodificada antes de construir el menú.
     */
    public static synchronized void precargarLogo() {
        cargarLogoOriginal();
    }

    /**
     * Carga completamente el PNG utilizando ImageIO.
     *
     * ImageIcon puede realizar parte de la carga de forma diferida.
     * ImageIO.read devuelve una BufferedImage ya decodificada.
     */
    private static synchronized void cargarLogoOriginal() {

        if (logoOriginal != null) {
            return;
        }

        URL recurso = TituloAnimado.class.getResource(
                "/menu/titulo_logo.png"
        );

        if (recurso == null) {
            System.err.println(
                    "No se encontró el título: /menu/titulo_logo.png"
            );
            return;
        }

        try {
            logoOriginal = ImageIO.read(recurso);
        } catch (IOException e) {
            System.err.println(
                    "No se pudo cargar titulo_logo.png"
            );
            e.printStackTrace();
        }
    }

    /**
     * Prepara la versión escalada del logo.
     *
     * Debe ejecutarse cuando el componente ya tenga su tamaño definitivo,
     * antes de comenzar la animación.
     */
    public void prepararImagen() {

        if (logoOriginal == null || getWidth() <= 0) {
            return;
        }

        /*
         * Si el ancho del componente no cambió y la imagen ya existe,
         * no es necesario escalarla otra vez.
         */
        if (logoEscalado != null
                && ultimoAnchoComponente == getWidth()) {
            return;
        }

        ultimoAnchoComponente = getWidth();

        // El título ocupa aproximadamente el 48 % del ancho disponible.
        anchoLogo = Math.max(
                1,
                (int) Math.round(getWidth() * 0.48)
        );

        // Mantiene automáticamente la proporción original real.
        altoLogo = Math.max(
                1,
                (int) Math.round(
                        anchoLogo
                                * (double) logoOriginal.getHeight()
                                / logoOriginal.getWidth()
                )
        );

        /*
         * La imagen se escala UNA sola vez.
         *
         * Durante la animación únicamente se dibuja esta versión,
         * evitando reescalar el PNG grande en cada frame.
         */
        logoEscalado = new BufferedImage(
                anchoLogo,
                altoLogo,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = logoEscalado.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.drawImage(
                logoOriginal,
                0,
                0,
                anchoLogo,
                altoLogo,
                null
        );

        g2.dispose();
    }

    /**
     * Inicia la animación.
     */
    public void iniciarAnimacion() {

        // Evita que existan dos animaciones simultáneas.
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        /*
         * Prepara el logo antes de iniciar el Timer.
         *
         * El escalado ocurre una vez aquí, no durante la animación.
         */
        prepararImagen();

        if (logoEscalado == null) {
            return;
        }

        alpha = 0f;

        // Posición final del logo.
        yFinal = 55;

        // Empieza ligeramente más abajo.
        yActual = yFinal + 70;

        inicioAnimacionNs = System.nanoTime();

        timer = new Timer(16, e -> {

            long transcurridoNs =
                    System.nanoTime() - inicioAnimacionNs;

            float t = Math.min(
                    transcurridoNs
                            / (DURACION_MS * 1_000_000f),
                    1f
            );

            /*
             * Ease-out quint.
             *
             * Empieza con decisión, pero termina de forma muy suave.
             */
            float movimiento =
                    1f - (float) Math.pow(1f - t, 5);

            /*
             * El fade tarda un poco más en aparecer al comienzo.
             * Esto evita que el logo parezca aparecer bruscamente.
             */
            float progresoFade = Math.min(
                    t / 0.75f,
                    1f
            );

            alpha = progresoFade
                    * progresoFade
                    * (3f - 2f * progresoFade);

            yActual = Math.round(
                    yFinal + (1f - movimiento) * 70f
            );

            repaint();

            if (t >= 1f) {
                alpha = 1f;
                yActual = yFinal;

                timer.stop();
                repaint();
            }
        });

        // Evita acumular eventos si un frame tarda más de lo normal.
        timer.setCoalesce(true);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (logoEscalado == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        // Aplica únicamente la transparencia.
        // Ya no se redimensiona la imagen durante cada frame.
        g2.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        alpha
                )
        );

        int x = (getWidth() - anchoLogo) / 2;

        g2.drawImage(
                logoEscalado,
                x,
                yActual,
                null
        );

        g2.dispose();
    }
}