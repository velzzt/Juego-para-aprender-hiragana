package aprendizaje;


import java.awt.Color;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
/*

Para entender el codigo recomiendo leer en el siguiente orden:
Leccion -> ListaLeccion -> MenuPrincipal -> PanelAprender -> PanelLeccion -> Tarjeta

 */
//Panel principal que contiene los botones 'Aprender' y 'Jugar'
public class MenuPrincipal extends JFrame {

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
// Rutas de los sprites utilizados por los botones
// Cada botón dispone de una imagen normal y otra para el efecto hover
    private final String rutaAprenderNormal = "/menu/btn_aprender.png";
    private final String rutaAprenderHover = "/menu/btn_aprender_hover.png";
    private final String rutaJugarNormal = "/menu/btn_jugar.png";
    private final String rutaJugarHover = "/menu/btn_jugar_hover.png";

    public MenuPrincipal() {

        setSize(1280, 800);
        setResizable(false);
        setTitle("Aprender hiragana");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // para terminar el proceso
        setLocationRelativeTo(null); // ventana en el centro de la pantalla
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        colocarPanel();
        colocarComponentes();
        inicializarLecciones();

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

                    if (anchoPanel > 0 && altoPanel > 0 && anchoImagen > 0 && altoImagen > 0) {
                        double ratioPanel = (double) anchoPanel / altoPanel;
                        double ratioImagen = (double) anchoImagen / altoImagen;

                        int anchoDibujado;
                        int altoDibujado;
                        if (ratioPanel > ratioImagen) {
                            anchoDibujado = anchoPanel;
                            altoDibujado = (int) Math.round(anchoPanel / ratioImagen);
                        } else {
                            altoDibujado = altoPanel;
                            anchoDibujado = (int) Math.round(altoPanel * ratioImagen);
                        }
                        // Solo se vuelve a escalar la imagen cuando cambia el tamaño necesario
                        // asi se evita realizar esta operación en cada repaint()
                        if (imagenFondoEscalada == null || anchoDibujado != anchoFondoEscalado || altoDibujado != altoFondoEscalado) {
                            imagenFondoEscalada = imagenFondo.getScaledInstance(anchoDibujado, altoDibujado,
                                    Image.SCALE_SMOOTH);
                            anchoFondoEscalado = anchoDibujado;
                            altoFondoEscalado = altoDibujado;
                        }

                        int x = (anchoPanel - anchoDibujado) / 2;
                        int y = (altoPanel - altoDibujado) / 2;
                        if (imagenFondoEscalada != null) {
                            g.drawImage(imagenFondoEscalada, x, y, this);
                        }
                    }
                }
            }
        };
        panel.setLayout(null); // Desactivamos el layout por defecto para darle una posición fija a los componentes
        panel.setBackground(new Color(185, 225, 175)); // fondo suave verde

        // carga la imagen de fondo desde la carpeta de recursos
        try {
            imagenFondo = new ImageIcon(getClass().getResource("/menu/fondo_inicio.png")).getImage();
        } catch (NullPointerException e) {
            imagenFondo = null;
        }

        getContentPane().add(panel); // añade 'panel' al frame
    }

    // Crea los botones del menú y configura su comportamiento
    private void colocarComponentes() {
        //se crean los botones utilizando sprites en lugar del estilo por defecto de Swing
        btnAprender = crearBotonConSprite("Aprender", 0, 0, 400, 84, rutaAprenderNormal, rutaAprenderHover);
        panel.add(btnAprender);

        btnJugar = crearBotonConSprite("Jugar", 0, 0, 400, 84, rutaJugarNormal, rutaJugarHover);
        panel.add(btnJugar);

        // Reposiciona y redimensiona los botones cuando cambia el tamaño del panel
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                actualizarPosicionesComponentes();
            }
        });

        actualizarPosicionesComponentes();

        ActionListener e = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                //guardamos una referencia de la ventana para usarlo en PanelAprender (boton volver)
                PanelAprender panelAprender = new PanelAprender(MenuPrincipal.this);
                getContentPane().removeAll();//se borra el contenido del frame (osea 'panel')
                getContentPane().add(panelAprender);//se añade panelAprender al frame
                getContentPane().revalidate();//recalcula la posicion y tamaño de los componentes
                getContentPane().repaint();//pinta los componentes
            }
        };
        btnAprender.addActionListener(e);

        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sonido.reproducirClick();
                // Carga la narrativa antes del juego
                PanelNarrativa panelNarrativa = new PanelNarrativa(MenuPrincipal.this);
                getContentPane().removeAll();
                getContentPane().add(panelNarrativa);
                getContentPane().revalidate();
                getContentPane().repaint();
            }
        };
        btnJugar.addActionListener(a);
    }

    // Calcula automáticamente el tamaño y la posición de los botones
    // según las dimensiones actuales del panel
    private void actualizarPosicionesComponentes() {
        if (panel == null || btnAprender == null || btnJugar == null) {
            return;
        }

        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        // se limita el tamaño mínimo y máximo para mantener una apariencia simetrica
        int anchoBoton = Math.max(320, Math.min(400, panelWidth / 4));
        int altoBoton = Math.max(72, Math.min(96, panelHeight / 10));
        int xBotones = (panelWidth - anchoBoton) / 2;
        int yPrimerBoton = (int) Math.round(panelHeight * 0.58);
        int espacio = Math.max(16, altoBoton / 3);

        btnAprender.setBounds(xBotones, yPrimerBoton, anchoBoton, altoBoton);
        btnJugar.setBounds(xBotones, yPrimerBoton + altoBoton + espacio, anchoBoton, altoBoton);

        actualizarIconoBoton(btnAprender, rutaAprenderNormal, rutaAprenderHover, anchoBoton, altoBoton);
        actualizarIconoBoton(btnJugar, rutaJugarNormal, rutaJugarHover, anchoBoton, altoBoton);
    }

    // actualiza los iconos del botón para que coincidan con su tamaño actual
    private void actualizarIconoBoton(JButton btn, String rutaNormal, String rutaHover, int anchoBoton, int altoBoton) {
        try {
            btn.setIcon(cargarIconoBoton(rutaNormal, anchoBoton, altoBoton));
            btn.setRolloverIcon(cargarIconoBoton(rutaHover, anchoBoton, altoBoton));
            btn.setPressedIcon(cargarIconoBoton(rutaHover, anchoBoton, altoBoton));
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
    }

    // crea un botón basado en imágenes (sprites) manteniendo toda la funcionalidad
    // de un JButton, incluyendo eventos y hover
    private JButton crearBotonConSprite(String texto, int x, int y, int anchoBoton, int altoBoton,
            String rutaNormal, String rutaHover) {
        JButton btn = new JButton();

        // El JButton conserva su area clickeable aunque el dibujo venga de una imagen
        btn.setBounds(x, y, anchoBoton, altoBoton);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        // Estas opciones ocultan el fondo/borde default para que solo se vea el sprite
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setRolloverEnabled(true);

        try {
            // Cargamos dos versiones del mismo boton, normal y hover.
            ImageIcon iconoNormal = cargarIconoBoton(rutaNormal, anchoBoton, altoBoton);
            ImageIcon iconoHover = cargarIconoBoton(rutaHover, anchoBoton, altoBoton);

            // setRolloverIcon cambia la imagen automaticamente al pasar el mouse encima
            // setPressedIcon reutiliza el hover mientras el boton esta presionado
            btn.setIcon(iconoNormal);
            btn.setRolloverIcon(iconoHover);
            btn.setPressedIcon(iconoHover);

        } catch (IllegalArgumentException | NullPointerException e) {
            // Si falta una imagen o la ruta esta mal, el boton sigue funcionando con estilo clasico
            btn.setText(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
            btn.setBackground(new Color(110, 180, 80));
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 245, 200), 2));
        }

        return btn;
    }

    // carga un sprite desde los recursos y lo adapta al tamaño del botón
    private ImageIcon cargarIconoBoton(String ruta, int ancho, int alto) {
        // las imagenes se cargan desde src/main/resources usando rutas que empiezan con "/"
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));

        // escalamos todos los sprites al mismo tamaño para evitar saltos entre normal y hover
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    private void inicializarLecciones() {

        lista1 = new ListaLeccion();
        lista1.agregarLeccion(new Leccion("/gifs_simples/a.gif", "/vocales simples/a.wav", "a")); // agregar lecciones
                                                                                                  // (a, i , u)
        lista1.agregarLeccion(new Leccion("/gifs_simples/i.gif", "/vocales simples/i.wav", "i"));
        lista1.agregarLeccion(new Leccion("/gifs_simples/u.gif", "/vocales simples/u.wav", "u"));

        lista2 = new ListaLeccion();
        lista2.agregarLeccion(new Leccion("/gifs_simples/e.gif", "/vocales simples/e.wav", "e")); // cambiar y agregar
                                                                                                  // lecciones (e, o,
                                                                                                  // ka)
        lista2.agregarLeccion(new Leccion("/gifs_simples/o.gif", "/vocales simples/o.wav", "o"));
        lista2.agregarLeccion(new Leccion("/gifs_k/ka.gif", "/vocales con k-/ka.wav", "ka"));

        lista3 = new ListaLeccion();
        lista3.agregarLeccion(new Leccion("/gifs_s/sa.gif", "/vocales con s-/sa.wav", "sa")); // cambiar y agregar
                                                                                              // lecciones (sa, ki, shi,
                                                                                              // ku)
        lista3.agregarLeccion(new Leccion("/gifs_k/ki.gif", "/vocales con k-/ki.wav", "ki"));
        lista3.agregarLeccion(new Leccion("/gifs_s/shi.gif", "/vocales con s-/shi.wav", "shi"));// falta
        lista3.agregarLeccion(new Leccion("/gifs_k/ku.gif", "/vocales con k-/ku.wav", "ku"));

        lista4 = new ListaLeccion();
        lista4.agregarLeccion(new Leccion("/gifs_s/su.gif", "/vocales con s-/su.wav", "su")); // cambiar y agregar
                                                                                              // lecciones (su, ke, se,
                                                                                              // ko, so)
        lista4.agregarLeccion(new Leccion("/gifs_k/ke.gif", "/vocales con k-/ke.wav", "ke")); // falta
        lista4.agregarLeccion(new Leccion("/gifs_s/se.gif", "/vocales con s-/se.wav", "se")); // falta
        lista4.agregarLeccion(new Leccion("/gifs_k/ko.gif", "/vocales con k-/ko.wav", "ko")); // falta
        lista4.agregarLeccion(new Leccion("/gifs_s/so.gif", "/vocales con s-/so.wav", "so")); // falta

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
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    // Método que restaura la vista del panelAprender (se utiliza en PanelLeccion)

    public void mostrarPanelAprender() {
        getContentPane().removeAll();
        getContentPane().add(new PanelAprender(this));
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // Adopta los datos ingresados para empezar el juego
    public void iniciarJuegoConfirmado(String nombre, int trajeSeleccionado) {

        System.out.println("Iniciando partida para: " + nombre + " con traje ID: " + trajeSeleccionado);

        // Ejecuta el código original para iniciar el juego
        HiraganaEnemigo ah = new HiraganaEnemigo();
        getContentPane().removeAll();
        getContentPane().add(ah);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

}
