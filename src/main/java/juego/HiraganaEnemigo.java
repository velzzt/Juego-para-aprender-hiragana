package juego;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import aprendizaje.MenuPrincipal;

public class HiraganaEnemigo extends JPanel implements ActionListener, KeyListener {
	private JFrame frameContenedor;
    private panelPausa panelPausa;
    private Image fondoImagen;
	private final int anchoRect = 100;
    private final int altoRect = 100;
	private Timer temporizador;
    private int velocidad = 2;
    private MenuPrincipal menu;
    private Font fuentePixel;

    private String[] caracteres = {"あ","い","う","え","お","か","き","く","け","こ",
                                   "さ","し","す","せ","そ"};
    private String[] romajiList = {"a","i","u","e","o","ka","ki","ku","ke","ko",
                                   "sa","shi","su","se","so"};
    // Arreglos para múltiples caracteres en pantalla
    private String[] chars = new String[20];
    private String[] romaji = new String[20];
    private int[] posX = new int[20];
    private int[] posY = new int[20];
    private int cantidad = 0;

    // Estado del juego
    private int vidas = 3;
    private long puntaje = 0;
    private int nivel = 1;
    private int aciertos = 0; 
    private boolean juegoTerminado = false;
    private boolean pausado = false;

    // para subir de nivel
    private final int ACIERTOS_NIVEL_2 = 10;
    private final int ACIERTOS_NIVEL_3 = 15;

    // Escritura
    private StringBuilder entradaActual = new StringBuilder();
    private int contadorError = 0;

    private int contadorTicks = 0;
    private int INTERVALO_APARICION = 150; // segundos

    // Constructor
    public HiraganaEnemigo(JFrame frame, String rutaFondo, MenuPrincipal menu) {
        this.menu=menu;
        fuentePixel = cargarFuentePixel();
        try {
        fondoImagen = new ImageIcon(getClass().getResource(rutaFondo)).getImage();
    } catch (Exception e) {
        fondoImagen = null;
    }
        
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        temporizador = new Timer(10, this);
        temporizador.start();

        // Panel de pausa
        panelPausa = new panelPausa(this, frameContenedor,menu);
        panelPausa.setBounds(0, 0, 1024, 768);
        panelPausa.setVisible(false);
        add(panelPausa);

         // Agregar el primer carácter cuando el panel esté listo
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (cantidad == 0) {
                    agregarCaracter();
                }
                removeComponentListener(this); // se elimina para que no se ejecute varias veces
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelPausa.setBounds(0, 0, getWidth(), getHeight());
            }
        });
    }

    // Obtener romaji
    private String obtenerRomaji(String c) {
        for (int i = 0; i < caracteres.length; i++) {
            if (caracteres[i].equals(c)) return romajiList[i];
        }
        return "";
    }

    // Agregar nuevo carácter según el nivel
    private void agregarCaracter() {
        if (vidas <= 0 || cantidad >= 20) return;

        int maxIndice;
        switch (nivel) {
            case 1: maxIndice = 4; break;      // 5 caracteres
            case 2: maxIndice = 8; break;      // 9 caracteres
            case 3: maxIndice = caracteres.length - 1; break; // todos
            default: maxIndice = 4;
        }
        int indice = (int)(Math.random() * (maxIndice + 1));
        String nuevoChar = caracteres[indice];
        String nuevoRomaji = romajiList[indice];

        int x = getWidth() - anchoRect;
        int y = (int)(Math.random() * (getHeight() - altoRect));

        chars[cantidad] = nuevoChar;
        romaji[cantidad] = nuevoRomaji;
        posX[cantidad] = x;
        posY[cantidad] = y;
        cantidad++;
    }

    // --- Dibujo ---
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Dibujar el fondo
        if (fondoImagen != null) {
            g2d.drawImage(fondoImagen, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Dibujar caracteres activos
        for (int i = 0; i < cantidad; i++) {
            g2d.setColor(contadorError > 0 ? Color.RED : Color.GRAY);
            g2d.fillRect(posX[i], posY[i], anchoRect, altoRect);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(posX[i], posY[i], anchoRect, altoRect);

            Font fuente = new Font("MS Mincho", Font.BOLD, 100);
            g2d.setFont(fuente);
            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics(fuente);
            int cx = posX[i] + (anchoRect - fm.stringWidth(chars[i])) / 2;
            int cy = posY[i] + ((altoRect - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(chars[i], cx, cy);
        }

        // Puntuación centrada
        g2d.setFont(fuentePixel.deriveFont(Font.BOLD, 48f));
        g2d.setColor(Color.WHITE);
        String textoPuntaje = "Puntuación: " + puntaje;
        FontMetrics fmPunt = g2d.getFontMetrics();
        int anchoPunt = fmPunt.stringWidth(textoPuntaje);
        int xPunt = (getWidth() - anchoPunt) / 2;
        g2d.drawString(textoPuntaje, xPunt, 70);

        // Corazones (vidas) en pixel art
        int corX = 20, corY = 20;
        int pixelSize = 6;
        int anchoCorazon = CORAZON[0].length * pixelSize;
        int separacion = 8;
        for (int i = 0; i < vidas; i++) {
            dibujarCorazon(g2d, corX + i * (anchoCorazon + separacion), corY, pixelSize);
        }

        // Mostrar nivel
        g2d.setFont(fuentePixel.deriveFont(Font.BOLD, 30f));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Nivel: " + nivel, getWidth() - 150, 30);

        // Game Over
        if (juegoTerminado) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setFont(fuentePixel.deriveFont(Font.BOLD, 120f));
            g2d.setColor(Color.RED);
            String texto = "GAME OVER";
            int ancho = g2d.getFontMetrics().stringWidth(texto);
            g2d.drawString(texto, (getWidth() - ancho) / 2, getHeight() / 2);
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 40));
            g2d.setColor(Color.WHITE);
            String reinicio = "Presiona ENTER para reiniciar";
            int anchoReinicio = g2d.getFontMetrics().stringWidth(reinicio);
            g2d.drawString(reinicio, (getWidth() - anchoReinicio) / 2, getHeight() / 2 + 40);
        }
    }

    // Lógica del temporizador
    @Override
    public void actionPerformed(ActionEvent e) {
        if (juegoTerminado || pausado) {
            repaint();
            return;
        }

        // Mover todos los caracteres
        for (int i = 0; i < cantidad; i++) {
            posX[i] -= velocidad;
        }

        if (contadorError > 0) contadorError--;

        // Eliminar los que salieron por la izquierda
        int i = 0;
        while (i < cantidad) {
            if (posX[i] + anchoRect < 0) {
                chars[i] = chars[cantidad - 1];
                romaji[i] = romaji[cantidad - 1];
                posX[i] = posX[cantidad - 1];
                posY[i] = posY[cantidad - 1];
                cantidad--;
                perderVida();
            } else {
                i++;
            }
        }

        // Generar nuevo carácter cada 3 segundos
        if (vidas > 0) {
            contadorTicks++;
            if (contadorTicks >= INTERVALO_APARICION) {
                contadorTicks = 0;
                agregarCaracter();
            }
        }

        repaint();
    }

    private void perderVida() {
        vidas--;
        if (vidas <= 0) {
            vidas = 0;
            juegoTerminado = true;
            temporizador.stop();
        }
    }

    // Reiniciar juego 
    public void reiniciarJuego() {
        vidas = 3;
        puntaje = 0;
        nivel = 1;
        aciertos = 0;
        velocidad = 2;
        contadorTicks = 0;
        entradaActual.setLength(0);
        contadorError = 0;
        juegoTerminado = false;
        pausado = false;
        cantidad = 0;
        panelPausa.setVisible(false);
        agregarCaracter();
        temporizador.start();
        repaint();
    }

    public void volverMenu() {
    temporizador.stop();
    if (menu != null) {
        menu.mostrarMenu();
        menu.requestFocusInWindow(); 
    } else {
        if (frameContenedor != null) {
            MenuPrincipal menu = new MenuPrincipal();
            frameContenedor.getContentPane().removeAll();
            frameContenedor.getContentPane().add(menu.getPanel());
            frameContenedor.revalidate();
            frameContenedor.repaint();
        }
    }
}

    public void continuarJuego() {
        pausado = false;
        panelPausa.setVisible(false);
        temporizador.start();
        repaint();
    }

    // Matriz del corazón (pixel art)
    private static final int[][] CORAZON = {
        {0, 2, 2, 0, 0, 2, 2, 0},
        {2, 1, 1, 2, 2, 1, 1, 2},
        {2, 1, 1, 1, 1, 1, 1, 2},
        {2, 1, 1, 1, 1, 1, 1, 2},
        {0, 2, 1, 1, 1, 1, 2, 0},
        {0, 0, 2, 1, 1, 2, 0, 0},
        {0, 0, 0, 2, 2, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };

    // Método para dibujar un corazón estilo pixelado
    private void dibujarCorazon(Graphics2D g2d, int x, int y, int pixelSize) {
        for (int fila = 0; fila < CORAZON.length; fila++) {
            for (int col = 0; col < CORAZON[fila].length; col++) {
                int valor = CORAZON[fila][col];
                if (valor == 0) continue;
                Color color = (valor == 1) ? new Color(237, 28, 36)   
                                           : new Color(140, 15, 20);  
                g2d.setColor(color);
                g2d.fillRect(x + col * pixelSize, y + fila * pixelSize, pixelSize, pixelSize);
            }
        }
    }

    // Manejo de teclado
    @Override
    public void keyTyped(KeyEvent e) {
        if (juegoTerminado || pausado) return;

        char tecla = e.getKeyChar();
        if (!Character.isLetter(tecla) && tecla != '\b') return;

        if (tecla == '\b') {
            if (entradaActual.length() > 0) {
                entradaActual.deleteCharAt(entradaActual.length() - 1);
            }
            return;
        }

        tecla = Character.toLowerCase(tecla);
        entradaActual.append(tecla);
        String textoEscrito = entradaActual.toString();

        boolean encontrado = false;
        for (int i = 0; i < cantidad; i++) {
            if (romaji[i].equals(textoEscrito)) {
                // Eliminar el carácter acertado
                chars[i] = chars[cantidad - 1];
                romaji[i] = romaji[cantidad - 1];
                posX[i] = posX[cantidad - 1];
                posY[i] = posY[cantidad - 1];
                cantidad--;
                puntaje += 10;
                aciertos++;
                entradaActual.setLength(0);
                encontrado = true;

                // Verificar cambio de nivel
                if (nivel == 1 && aciertos >= ACIERTOS_NIVEL_2) {
                    INTERVALO_APARICION = 80;
                    nivel = 2;
                    velocidad = 4;
                    aciertos = 0;
                    contadorTicks = 0;
                    //System.out.println("¡Nivel 2! Velocidad: " + velocidad);
                } else if (nivel == 2 && aciertos >= ACIERTOS_NIVEL_3) {
                    INTERVALO_APARICION = 50;
                    nivel = 3;
                    velocidad = 7;
                    aciertos = 0;
                    contadorTicks = 0;
                    //System.out.println("¡Nivel 3! Velocidad: " + velocidad);
                }
                break;
            }
        }

        if (!encontrado) {
            boolean algunoEmpieza = false;
            for (int i = 0; i < cantidad; i++) {
                if (romaji[i].startsWith(textoEscrito)) {
                    algunoEmpieza = true;
                    break;
                }
            }
            if (!algunoEmpieza) {
                entradaActual.setLength(0);
                contadorError = 20;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (juegoTerminado && e.getKeyCode() == KeyEvent.VK_ENTER) {
            reiniciarJuego();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!juegoTerminado) {
                pausado = !pausado;
                panelPausa.setVisible(pausado);
                if (pausado) temporizador.stop();
                else temporizador.start();
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private Font cargarFuentePixel() {
    try (java.io.InputStream is = getClass().getResourceAsStream("/fuentes/PixelOperator.ttf")) {
        if (is != null) return Font.createFont(Font.TRUETYPE_FONT, is);
    } catch (Exception e) {}
    return new Font(Font.MONOSPACED, Font.BOLD, 60); // fallback
}
}