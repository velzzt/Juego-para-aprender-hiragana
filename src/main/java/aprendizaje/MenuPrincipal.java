package aprendizaje;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import juego.HiraganaEnemigo;

/*

Para entender el codigo recomiendo leer en el siguiente orden:
Leccion -> ListaLeccion -> MenuPrincipal -> PanelAprender -> PanelLeccion -> Tarjeta

 */
//Panel principal que contiene los botones 'Aprender' y 'Jugar'
public class MenuPrincipal extends JFrame {

    private static final String TARJETA_CARGA = "carga";
    private static final String TARJETA_APLICACION = "aplicacion";
    private final CardLayout navegacion = new CardLayout();
    private final JPanel contenedorRaiz = new JPanel(navegacion);
    private final JPanel contenidoAplicacion = new JPanel(new BorderLayout());

    public JPanel panel;
    private ListaLeccion lista1, lista2, lista3, lista4;

    // Variables utilizadas para cargar y almacenar una version escalada del fondo
    // Se guarda la imagen ya escalada para evitar volver a recalcularla en cada repintado
    private Image imagenFondo;
    private Image imagenFondoEscalada;
    private int anchoFondoEscalado = -1;
    private int altoFondoEscalado = -1;

    // Referencias a los botones del menú
    // Se almacenan como atributos para poder modificar su posición e iconos dinámicamente
    private JButton btnAprender;
    private JButton btnJugar;
    private JButton btnCreditos;
    private TituloAnimado titulo;

    // Capa negra que oculta el menú mientras Swing termina de calcular
    // posiciones, escalados y el primer renderizado.
    private CortinaNegra cortinaNegra;

    // Rutas de los sprites utilizados por los botones
    // Cada botón dispone de una imagen normal y otra para el efecto hover
    private final String rutaAprenderNormal = "/menu/boton_inicio.png";
    private final String rutaAprenderHover = "/menu/boton_hover.png";
    private final String rutaJugarNormal = "/menu/boton_inicio.png";
    private final String rutaJugarHover = "/menu/boton_hover.png";

    public MenuPrincipal() {

        setSize(1280, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setTitle("Aprender hiragana");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // para terminar el proceso
        setLocationRelativeTo(null); // ventana en el centro de la pantalla

        PantallaCarga pantallaCarga = new PantallaCarga(this::cargaFinalizada);
        contenedorRaiz.add(pantallaCarga, TARJETA_CARGA);
        contenedorRaiz.add(contenidoAplicacion, TARJETA_APLICACION);
        setContentPane(contenedorRaiz);
        navegacion.show(contenedorRaiz, TARJETA_CARGA);

        setVisible(true);
        pantallaCarga.iniciar();
    }

    private void cargaFinalizada() {
        iniciarComponentes();
        mostrarPreparado();
    }

    private void iniciarComponentes() {
        colocarPanel();
        colocarComponentes();
        inicializarLecciones();

        /*
         * La música y la animación del título ya no se inician aquí.
         *
         * Se iniciarán en mostrarPreparado(), cuando Swing ya haya terminado
         * de maximizar la ventana y calcular las dimensiones reales.
         *
         * Esto evita que el usuario vea el fondo, los botones y el título
         * deformándose durante el primer segundo.
         */
    }

    private void colocarPanel() {

        // Se crea un JPanel personalizado para dibujar una imagen de fondo
        // ocupando toda la ventana
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // El fondo se escala manteniendo su proporción para evitar deformaciones
                if (imagenFondo != null) {
                    int anchoPanel = getWidth();
                    int altoPanel = getHeight();
                    int anchoImagen = imagenFondo.getWidth(this);
                    int altoImagen = imagenFondo.getHeight(this);

                    if (anchoPanel > 0 && altoPanel > 0
                            && anchoImagen > 0 && altoImagen > 0) {

                        double ratioPanel = (double) anchoPanel / altoPanel;
                        double ratioImagen = (double) anchoImagen / altoImagen;

                        int anchoDibujado;
                        int altoDibujado;

                        if (ratioPanel > ratioImagen) {
                            anchoDibujado = anchoPanel;
                            altoDibujado = (int) Math.round(
                                    anchoPanel / ratioImagen
                            );
                        } else {
                            altoDibujado = altoPanel;
                            anchoDibujado = (int) Math.round(
                                    altoPanel * ratioImagen
                            );
                        }

                        // Solo se vuelve a escalar la imagen cuando cambia el tamaño necesario
                        // asi se evita realizar esta operación en cada repaint()
                        if (imagenFondoEscalada == null
                                || anchoDibujado != anchoFondoEscalado
                                || altoDibujado != altoFondoEscalado) {

                            imagenFondoEscalada = imagenFondo.getScaledInstance(
                                    anchoDibujado,
                                    altoDibujado,
                                    Image.SCALE_SMOOTH
                            );

                            anchoFondoEscalado = anchoDibujado;
                            altoFondoEscalado = altoDibujado;
                        }

                        int x = (anchoPanel - anchoDibujado) / 2;
                        int y = (altoPanel - altoDibujado) / 2;

                        if (imagenFondoEscalada != null) {
                            g.drawImage(
                                    imagenFondoEscalada,
                                    x,
                                    y,
                                    this
                            );
                        }
                    }
                }
            }
        };

        panel.setLayout(null); // Desactivamos el layout por defecto para darle una posición fija a los componentes
        panel.setBackground(new Color(185, 225, 175)); // fondo suave verde

        // carga la imagen de fondo desde la carpeta de recursos
        try {
            imagenFondo = new ImageIcon(
                    getClass().getResource("/menu/fondo_inicio.png")
            ).getImage();
        } catch (NullPointerException e) {
            imagenFondo = null;
        }

        contenidoAplicacion.add(panel, BorderLayout.CENTER);
    }

    // Crea los botones del menú y configura su comportamiento
    private void colocarComponentes() {

        //se crean los botones utilizando sprites en lugar del estilo por defecto de Swing
        btnAprender = crearBotonConSprite(
                "Aprender",
                0,
                0,
                400,
                133,
                rutaAprenderNormal,
                rutaAprenderHover
        );

        // Prueba del botón reutilizable: el PNG contiene solamente el fondo
        // y Swing dibuja el texto centrado por encima de la imagen.
        btnAprender.setText("Aprender");
        btnAprender.setFont(cargarFuentePixel().deriveFont(Font.PLAIN, 30f));
        btnAprender.setForeground(new Color(82, 48, 29));
        btnAprender.setHorizontalTextPosition(JButton.CENTER);
        btnAprender.setVerticalTextPosition(JButton.CENTER);
        btnAprender.setIconTextGap(0);
        panel.add(btnAprender);

        btnJugar = crearBotonConSprite(
                "Jugar",
                0,
                0,
                400,
                133,
                rutaJugarNormal,
                rutaJugarHover
        );

        btnJugar.setText("Jugar");
        btnJugar.setFont(cargarFuentePixel().deriveFont(Font.PLAIN, 30f));
        btnJugar.setForeground(new Color(82, 48, 29));
        btnJugar.setHorizontalTextPosition(JButton.CENTER);
        btnJugar.setVerticalTextPosition(JButton.CENTER);
        btnJugar.setIconTextGap(0);
        panel.add(btnJugar);

        btnCreditos = crearBotonConSprite(
                "Créditos",
                0, 0, 400, 133,
                rutaAprenderNormal,  // usa el mismo sprite que los otros
                rutaAprenderHover
        );
        btnCreditos.setText("Créditos");
        btnCreditos.setFont(cargarFuentePixel().deriveFont(Font.PLAIN, 30f));
        btnCreditos.setForeground(new Color(82, 48, 29));
        btnCreditos.setHorizontalTextPosition(JButton.CENTER);
        btnCreditos.setVerticalTextPosition(JButton.CENTER);
        btnCreditos.setIconTextGap(0);
        panel.add(btnCreditos);

        // Reposiciona y redimensiona los botones cuando cambia el tamaño del panel
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(
                    java.awt.event.ComponentEvent e
            ) {
                actualizarPosicionesComponentes();
            }
        });

        actualizarPosicionesComponentes();

        ActionListener e = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                Sonido.detenerMusicaMenu();
                Sonido.reproducirMusicaAprender();

                //guardamos una referencia de la ventana para usarlo en PanelAprender (boton volver)
                PanelAprender panelAprender =
                        new PanelAprender(MenuPrincipal.this);

                mostrarVista(panelAprender);
            }
        };

        btnAprender.addActionListener(e);

        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();

                // Carga la narrativa antes del juego
                PanelNarrativa panelNarrativa =
                        new PanelNarrativa(MenuPrincipal.this);

                mostrarVista(panelNarrativa);
            }
        };

        btnJugar.addActionListener(a);

        ActionListener i = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                PanelCreditos panelCreditos = new PanelCreditos(MenuPrincipal.this);
                mostrarVista(panelCreditos);

            }
        };

        btnCreditos.addActionListener(i);

        // Crea el logo animado
        titulo = new TituloAnimado();

        // Lo añade al panel
        panel.add(titulo);

        // El fondo se pinta directamente en el JPanel, así que el título
        // puede quedar detrás de los botones. Esto evita que su componente
        // transparente intercepte el mouse y bloquee el cursor HAND_CURSOR.
        panel.setComponentZOrder(
                titulo,
                panel.getComponentCount() - 1
        );

        /*
         * No se inicia la animación todavía.
         *
         * Si se inicia aquí, el panel aún puede tener dimensiones provisionales
         * y el logo puede verse deformado durante los primeros milisegundos.
         *
         * La animación se iniciará en mostrarPreparado().
         */
    }

    // Calcula automáticamente el tamaño y la posición de los botones
    // según las dimensiones actuales del panel
    private void actualizarPosicionesComponentes() {

        if (panel == null
                || btnAprender == null
                || btnJugar == null
                || btnCreditos == null) {
            return;
        }

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        // se limita el tamaño mínimo y máximo para mantener una apariencia simetrica
        int anchoBoton = Math.max(
                320,
                Math.min(370, panelWidth / 4)
        );

        // boton_inicio.png y boton_hover.png miden 1536 x 512 (3:1).
        // Mantener esa proporción evita que los botones se vean estirados.
        int altoBoton = Math.max(1, Math.round(anchoBoton / 3f));

        int xBotones = (panelWidth - anchoBoton) / 2;
        int yPrimerBoton =
                (int) Math.round(panelHeight * 0.54);

        // Los PNG ya contienen margen transparente arriba y abajo. Un pequeño
        // solapamiento de sus bounds acerca visualmente ambos marcos.
        int espacio = -Math.max(12, altoBoton / 3);

        btnAprender.setBounds(
                xBotones,
                yPrimerBoton,
                anchoBoton,
                altoBoton
        );

        btnJugar.setBounds(
                xBotones,
                yPrimerBoton + altoBoton + espacio,
                anchoBoton,
                altoBoton
        );

        btnCreditos.setBounds(
                xBotones,
                yPrimerBoton+2*(altoBoton+espacio),
                anchoBoton,
                altoBoton
        );

        actualizarIconoBoton(
                btnAprender,
                rutaAprenderNormal,
                rutaAprenderHover,
                anchoBoton,
                altoBoton
        );

        actualizarIconoBoton(
                btnJugar,
                rutaJugarNormal,
                rutaJugarHover,
                anchoBoton,
                altoBoton
        );

        actualizarIconoBoton(
                btnCreditos,
                rutaAprenderNormal,
                rutaAprenderHover,
                anchoBoton,
                altoBoton
        );

        // El componente del título ocupa todo el panel.
        // El propio TituloAnimado se encarga de centrar el logo.
        if (titulo != null) {
            titulo.setBounds(
                    0,
                    0,
                    panelWidth,
                    panelHeight
            );
        }
    }

    // actualiza los iconos del botón para que coincidan con su tamaño actual
    private void actualizarIconoBoton(
            JButton btn,
            String rutaNormal,
            String rutaHover,
            int anchoBoton,
            int altoBoton
    ) {
        try {
            btn.setIcon(
                    cargarIconoBoton(
                            rutaNormal,
                            anchoBoton,
                            altoBoton
                    )
            );

            btn.setRolloverIcon(
                    cargarIconoBoton(
                            rutaHover,
                            anchoBoton,
                            altoBoton
                    )
            );

            btn.setPressedIcon(
                    cargarIconoBoton(
                            rutaHover,
                            anchoBoton,
                            altoBoton
                    )
            );

        } catch (IllegalArgumentException
                 | NullPointerException ignored) {
        }
    }

    // crea un botón basado en imágenes (sprites) manteniendo toda la funcionalidad
    // de un JButton, incluyendo eventos y hover
    private JButton crearBotonConSprite(
            String texto,
            int x,
            int y,
            int anchoBoton,
            int altoBoton,
            String rutaNormal,
            String rutaHover
    ) {
        JButton btn = new JButton();

        // El JButton conserva su area clickeable aunque el dibujo venga de una imagen
        btn.setBounds(
                x,
                y,
                anchoBoton,
                altoBoton
        );

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setFocusPainted(false);

        // Estas opciones ocultan el fondo/borde default para que solo se vea el sprite
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setRolloverEnabled(true);

        try {
            // Cargamos dos versiones del mismo boton, normal y hover.
            ImageIcon iconoNormal =
                    cargarIconoBoton(
                            rutaNormal,
                            anchoBoton,
                            altoBoton
                    );

            ImageIcon iconoHover =
                    cargarIconoBoton(
                            rutaHover,
                            anchoBoton,
                            altoBoton
                    );

            // setRolloverIcon cambia la imagen automaticamente al pasar el mouse encima
            // setPressedIcon reutiliza el hover mientras el boton esta presionado
            btn.setIcon(iconoNormal);
            btn.setRolloverIcon(iconoHover);
            btn.setPressedIcon(iconoHover);

        } catch (IllegalArgumentException
                 | NullPointerException e) {

            // Si falta una imagen o la ruta esta mal, el boton sigue funcionando con estilo clasico
            btn.setText(texto);

            btn.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            22
                    )
            );

            btn.setBackground(
                    new Color(110, 180, 80)
            );

            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(true);

            btn.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(200, 245, 200),
                            2
                    )
            );
        }

        return btn;
    }

    // carga un sprite desde los recursos y lo adapta al tamaño del botón
    private ImageIcon cargarIconoBoton(
            String ruta,
            int ancho,
            int alto
    ) {

        // las imagenes se cargan desde src/main/resources usando rutas que empiezan con "/"
        ImageIcon iconoOriginal = new ImageIcon(
                getClass().getResource(ruta)
        );

        // Se dibuja en un buffer de tamaño fijo. A diferencia de
        // getScaledInstance(), este escalado no queda pendiente y ambos estados
        // ocupan exactamente los mismos píxeles al hacer hover.
        BufferedImage imagenEscalada = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = imagenEscalada.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        g2.drawImage(iconoOriginal.getImage(), 0, 0, ancho, alto, null);
        g2.dispose();

        return new ImageIcon(imagenEscalada);
    }

    /**
     * Carga PixelOperator para los textos dibujados sobre botones genéricos.
     */
    private Font cargarFuentePixel() {
        try (InputStream fuenteStream = getClass().getResourceAsStream(
                "/fuentes/PixelOperator.ttf")) {

            if (fuenteStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fuenteStream);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar PixelOperator para los botones.");
        }

        return new Font(Font.MONOSPACED, Font.BOLD, 30);
    }

    private void inicializarLecciones() {

        lista1 = new ListaLeccion();
        lista1.agregarLeccion(
                new Leccion(
                        "/gifs_simples/a.gif",
                        "/vocales simples/a.wav",
                        "a"
                )
        ); // agregar lecciones
        // (a, i , u)

        lista1.agregarLeccion(
                new Leccion(
                        "/gifs_simples/i.gif",
                        "/vocales simples/i.wav",
                        "i"
                )
        );

        lista1.agregarLeccion(
                new Leccion(
                        "/gifs_simples/u.gif",
                        "/vocales simples/u.wav",
                        "u"
                )
        );

        lista2 = new ListaLeccion();
        lista2.agregarLeccion(
                new Leccion(
                        "/gifs_simples/e.gif",
                        "/vocales simples/e.wav",
                        "e"
                )
        ); // cambiar y agregar
        // lecciones (e, o,
        // ka)

        lista2.agregarLeccion(
                new Leccion(
                        "/gifs_simples/o.gif",
                        "/vocales simples/o.wav",
                        "o"
                )
        );

        lista2.agregarLeccion(
                new Leccion(
                        "/gifs_k/ka.gif",
                        "/vocales con k-/ka.wav",
                        "ka"
                )
        );

        lista3 = new ListaLeccion();
        lista3.agregarLeccion(
                new Leccion(
                        "/gifs_s/sa.gif",
                        "/vocales con s-/sa.wav",
                        "sa"
                )
        ); // cambiar y agregar
        // lecciones (sa, ki, shi,
        // ku)

        lista3.agregarLeccion(
                new Leccion(
                        "/gifs_k/ki.gif",
                        "/vocales con k-/ki.wav",
                        "ki"
                )
        );

        lista3.agregarLeccion(
                new Leccion(
                        "/gifs_s/shi.gif",
                        "/vocales con s-/shi.wav",
                        "shi"
                )
        );// falta

        lista3.agregarLeccion(
                new Leccion(
                        "/gifs_k/ku.gif",
                        "/vocales con k-/ku.wav",
                        "ku"
                )
        );

        lista4 = new ListaLeccion();
        lista4.agregarLeccion(
                new Leccion(
                        "/gifs_s/su.gif",
                        "/vocales con s-/su.wav",
                        "su"
                )
        ); // cambiar y agregar
        // lecciones (su, ke, se,
        // ko, so)

        lista4.agregarLeccion(
                new Leccion(
                        "/gifs_k/ke.gif",
                        "/vocales con k-/ke.wav",
                        "ke"
                )
        ); // falta

        lista4.agregarLeccion(
                new Leccion(
                        "/gifs_s/se.gif",
                        "/vocales con s-/se.wav",
                        "se"
                )
        ); // falta

        lista4.agregarLeccion(
                new Leccion(
                        "/gifs_k/ko.gif",
                        "/vocales con k-/ko.wav",
                        "ko"
                )
        ); // falta

        lista4.agregarLeccion(
                new Leccion(
                        "/gifs_s/so.gif",
                        "/vocales con s-/so.wav",
                        "so"
                )
        ); // falta
    }

    // metodo que hace que los botones de PanelAprender muestren las lecciones que
    // les corresponden
    public ListaLeccion obtenerListaLeccion(int numero) {

        switch (numero) {
            case 1:
                return lista1;

            case 2:
                return lista2;

            case 3:
                return lista3;

            case 4:
                return lista4;

            default:
                return lista1;
        }
    }

    // Método que restaura la vista del menú principal (el que se guardo como
    // referencia en el constructor) en PanelAprender
    public void mostrarMenu() {
        Sonido.detenerMusicaAprender();
        Sonido.detenerMusicaJugar();
        Sonido.reproducirMusicaMenu();
        mostrarVista(panel);
    }

    // Método que restaura la vista del panelAprender (se utiliza en PanelLeccion)
    public void mostrarPanelAprender() {
        Sonido.detenerMusicaMenu();
        Sonido.detenerMusicaJugar();
        Sonido.reproducirMusicaAprender();
        mostrarVista(new PanelAprender(this));
    }

    // Adopta los datos ingresados para empezar el juego
    // En MenuPrincipal.java
    public void iniciarJuegoConfirmado(
            String nombre,
            String rutaFondo
    ) {
        System.out.println(
                "Iniciando partida para: "
                        + nombre
                        + " con fondo: "
                        + rutaFondo
        );

        HiraganaEnemigo ah =
                new HiraganaEnemigo(
                        this,
                        rutaFondo,
                        this
                ); // pasa this

        mostrarVista(ah);
        ah.requestFocusInWindow();
    }

    public void mostrarVista(java.awt.Component vista) {
        contenidoAplicacion.removeAll();
        contenidoAplicacion.add(vista, BorderLayout.CENTER);
        contenidoAplicacion.revalidate();
        contenidoAplicacion.repaint();
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Muestra el menú oculto inicialmente por una capa negra.
     *
     * Swing puede tardar unos milisegundos en maximizar la ventana,
     * escalar el fondo, calcular los botones y preparar el logo.
     *
     * Esta capa evita que el usuario vea ese proceso.
     */
    public void mostrarPreparado() {
        // Instala una capa negra por encima de todo el contenido.
        cortinaNegra = new CortinaNegra();
        setGlassPane(cortinaNegra);

        cortinaNegra.setAlpha(1f);
        cortinaNegra.setVisible(true);

        // Cambia de panel dentro de la misma ventana. La cortina evita que
        // se vea la preparación del primer renderizado del menú.
        navegacion.show(contenedorRaiz, TARJETA_APLICACION);

        /*
         * Este invokeLater permite que Swing termine primero de:
         *
         * - maximizar la ventana;
         * - calcular el tamaño real del panel;
         * - crear el primer layout;
         * - preparar el primer renderizado.
         */
        SwingUtilities.invokeLater(() -> {

            panel.revalidate();
            panel.doLayout();

            // Ahora el panel ya debería tener sus dimensiones definitivas.
            actualizarPosicionesComponentes();

            // Prepara los píxeles del fondo y los componentes debajo de la cortina.
            panel.paintImmediately(
                    0,
                    0,
                    panel.getWidth(),
                    panel.getHeight()
            );

            /*
             * Esta pequeña espera queda completamente oculta por la cortina.
             * Permite que el escalado y el primer renderizado terminen antes
             * de revelar el menú.
             */
            Timer esperaPreparacion = new Timer(
                    220,
                    e -> {

                        ((Timer) e.getSource()).stop();

                        // El título comienza cuando el panel ya tiene tamaño estable.
                        if (titulo != null) {
                            titulo.iniciarAnimacion();
                        }

                        // La música fue precargada en PantallaCarga.
                        // Aquí solamente empieza a reproducirse.
                        Sonido.reproducirMusicaMenu();

                        // Revela suavemente el menú.
                        iniciarFadeEntradaMenu();
                    }
            );

            esperaPreparacion.setRepeats(false);
            esperaPreparacion.start();
        });
    }

    /**
     * Hace desaparecer la cortina negra para revelar el menú.
     */
    private void iniciarFadeEntradaMenu() {

        if (cortinaNegra == null) {
            return;
        }

        final long inicio = System.nanoTime();

        // Duración de la transición en nanosegundos.
        // 700 millones de nanosegundos = 700 milisegundos.
        final long duracion = 700_000_000L;

        Timer timerFadeMenu = new Timer(
                16,
                null
        );

        timerFadeMenu.addActionListener(e -> {

            long transcurrido =
                    System.nanoTime() - inicio;

            float t = Math.min(
                    transcurrido / (float) duracion,
                    1f
            );

            /*
             * Curva Smoothstep.
             *
             * Comienza lentamente, acelera en el centro
             * y termina otra vez de forma suave.
             */
            float progreso =
                    t * t * (3f - 2f * t);

            cortinaNegra.setAlpha(
                    1f - progreso
            );

            if (t >= 1f) {
                timerFadeMenu.stop();

                cortinaNegra.setAlpha(0f);
                cortinaNegra.setVisible(false);
            }
        });

        timerFadeMenu.start();
    }

    /**
     * Capa negra utilizada para ocultar el primer renderizado del menú.
     */
    private static class CortinaNegra extends JComponent {

        // 1 = completamente negra
        // 0 = completamente transparente
        private float alpha = 1f;

        public CortinaNegra() {
            setOpaque(false);
        }

        public void setAlpha(float alpha) {
            this.alpha = Math.max(
                    0f,
                    Math.min(1f, alpha)
            );

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha
                    )
            );

            g2.setColor(Color.BLACK);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            g2.dispose();
        }
    }
}
