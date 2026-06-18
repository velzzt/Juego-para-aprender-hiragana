package aprendizaje;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HiraganaEnemigo extends JPanel implements ActionListener, KeyListener {

    private String[] caracteres = {"あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ",
                                   "さ", "し", "す", "せ", "そ"};

    private String[] chars = new String[20];
    private String[] romaji = new String[20];
    private int[] posX = new int[20];
    private int[] posY = new int[20];
    private int cantidad = 0;

    private boolean partidaIniciada = false;

    private int altoRect = 100;
    private int anchoRect = 100;

    private int puntaje = 0;
    private int vidas = 3;
    private boolean juegoTerminado = false;

    private StringBuilder entradaActual = new StringBuilder();
    private int contadorError = 0;

    private Timer temporizador;
    private int velocidad = 2;

    private int contadorTicks = 0;
    private final int INTERVALO_APARICION = 300;

    private String obtenerRomaji(String c) {
        switch (c) {
            case "あ": return "a";
            case "い": return "i";
            case "う": return "u";
            case "え": return "e";
            case "お": return "o";
            case "か": return "ka";
            case "き": return "ki";
            case "く": return "ku";
            case "け": return "ke";
            case "こ": return "ko";
            case "さ": return "sa";
            case "し": return "shi";
            case "す": return "su";
            case "せ": return "se";
            case "そ": return "so";
            default: return "";
        }
    }

    private void agregarCaracter() {
        if (vidas <= 0 || cantidad >= 20) return;

        int indice = (int) (Math.random() * caracteres.length);
        String nuevoChar = caracteres[indice];
        String nuevoRomaji = obtenerRomaji(nuevoChar);

        int x = getWidth() - anchoRect;
        int y = (int) (Math.random() * (getHeight() - altoRect));

        chars[cantidad] = nuevoChar;
        romaji[cantidad] = nuevoRomaji;
        posX[cantidad] = x;
        posY[cantidad] = y;
        cantidad++;
    }

    public HiraganaEnemigo() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(1024, 768));
        setFocusable(true);
        addKeyListener(this);

        // Pedir foco automáticamente cuando se muestre
        addHierarchyListener(e -> {
            if (e.getID() == HierarchyEvent.HIERARCHY_CHANGED && isShowing()) {
                requestFocusInWindow();
            }
        });

        temporizador = new Timer(10, this);
        temporizador.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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

        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Puntaje: " + puntaje, 20, 30);
        g2d.drawString("Vidas: " + vidas, 20, 60);
        g2d.drawString("Escribiendo: " + entradaActual.toString(), 20, 90);

        // Mensaje de inicio si la partida no ha comenzado
        if (!partidaIniciada && !juegoTerminado) {
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.setColor(Color.YELLOW);
            String msg = "Presiona ENTER para empezar";
            int anchoMsg = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - anchoMsg) / 2, getHeight() / 2);
        }

        if (juegoTerminado) {
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            g2d.setColor(Color.RED);
            String texto = "GAME OVER";
            int anchoTexto = g2d.getFontMetrics().stringWidth(texto);
            g2d.drawString(texto, (getWidth() - anchoTexto) / 2, getHeight() / 2);

            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.setColor(Color.WHITE);
            String reinicio = "Presiona ENTER para reiniciar";
            int anchoReinicio = g2d.getFontMetrics().stringWidth(reinicio);
            g2d.drawString(reinicio, (getWidth() - anchoReinicio) / 2, getHeight() / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (juegoTerminado) return;

        if (partidaIniciada) {
            // Mover todos
            for (int i = 0; i < cantidad; i++) {
                posX[i] -= velocidad;
            }

            // Reducir error
            if (contadorError > 0) contadorError--;

            // Eliminar los que salieron
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

            // Generar nuevo cada 3 segundos
            if (vidas > 0) {
                contadorTicks++;
                if (contadorTicks >= INTERVALO_APARICION) {
                    contadorTicks = 0;
                    agregarCaracter();
                }
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

    private void reiniciarJuego() {
        puntaje = 0;
        vidas = 3;
        juegoTerminado = false;
        partidaIniciada = false; // reiniciar estado
        cantidad = 0;
        entradaActual.setLength(0);
        contadorError = 0;
        contadorTicks = 0;
        agregarCaracter();
        temporizador.start();
    }

    // --- Teclado ---
    @Override
    public void keyTyped(KeyEvent e) {
        if (!partidaIniciada || juegoTerminado) return; // bloquear si no ha empezado

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
                chars[i] = chars[cantidad - 1];
                romaji[i] = romaji[cantidad - 1];
                posX[i] = posX[cantidad - 1];
                posY[i] = posY[cantidad - 1];
                cantidad--;
                puntaje++;
                entradaActual.setLength(0);
                encontrado = true;
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

        if (!partidaIniciada && e.getKeyCode() == KeyEvent.VK_ENTER) {
            partidaIniciada = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}