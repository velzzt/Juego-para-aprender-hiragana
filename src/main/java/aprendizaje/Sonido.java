package aprendizaje;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

// Clase encargada de gestionar los efectos de sonido de la interfaz
// Actualmente reproduce el sonido de click de los botones
public class Sonido {

    //configuracion del efecto de sonido de los botones
    private static final String RUTA_CLICK = "/menu/sonido_click.wav";
    private static final String RUTA_MUSICA_MENU = "/menu/musica_menu.wav";
    private static final String RUTA_MUSICA_APRENDER = "/menu/musica_aprender.wav";
    private static final String RUTA_MUSICA_JUGAR = "/menu/musica_jugar.wav";
    private static final String RUTA_PUNTOS = "/menu/puntos.wav";
    private static final String RUTA_GAME_OVER = "/menu/game_over.wav";
    private static final String RUTA_PIERDE_CORAZON = "/menu/pierde_corazon.wav";
    private static final String RUTA_MALA = "/menu/mala.wav";
    private static final String RUTA_BUENA = "/menu/buena.wav";
    // Tres clips permiten reproducir varios clicks seguidos sin que el sonido se corte
    private static final int CANTIDAD_CLIPS_CLICK = 3;
    //hilo dedicado a reproducir audio sin bloquear la interfaz gráfica
    private static final ExecutorService HILO_AUDIO = Executors.newSingleThreadExecutor(runnable -> {
        Thread hilo = new Thread(runnable, "audio-ui");
        hilo.setDaemon(true);
        return hilo;
    });

    // hilo separado para la música de fondo para que no quede atada a la interfaz gráfica
    private static final ExecutorService HILO_MUSICA = Executors.newSingleThreadExecutor(runnable -> {
        Thread hilo = new Thread(runnable, "audio-music");
        hilo.setDaemon(true);
        return hilo;
    });

    //Pool de clips reutilizables para evitar cargar el sonido en cada reproducción
    private static Clip[] clipsClick;
    private static Clip musicaMenu;
    private static Clip musicaAprender;
    private static Clip musicaJugar;
    private static Clip sonidoPuntos;
    private static Clip sonidoGameOver;
    private static Clip sonidoPierdeCorazon;
    private static Clip sonidoMala;
    private static Clip sonidoBuena;
    private static int siguienteClip;
    private static boolean sonidoActivado = true;

    private Sonido() {
    }

    //carga los clips de audio una única vez al iniciar su uso
    public static synchronized void precargar() {
        try {
            // Si los clips ya fueron cargados, no es necesario volver a hacerlo
            if (clipsClick == null) {
                clipsClick = new Clip[CANTIDAD_CLIPS_CLICK];
                // Se crean varios clips para permitir clicks consecutivos sin cortes
                for (int i = 0; i < clipsClick.length; i++) {
                    clipsClick[i] = cargarClip(RUTA_CLICK);
                }
            }

            if (sonidoPuntos == null) {
                sonidoPuntos = cargarClip(RUTA_PUNTOS);
                ajustarVolumen(sonidoPuntos, 0.35f);
            }
            if (sonidoGameOver == null) {
                sonidoGameOver = cargarClip(RUTA_GAME_OVER);
                ajustarVolumen(sonidoGameOver, 0.45f);
            }
            if (sonidoPierdeCorazon == null) {
                sonidoPierdeCorazon = cargarClip(RUTA_PIERDE_CORAZON);
                ajustarVolumen(sonidoPierdeCorazon, 0.40f);
            }
            if (sonidoMala == null) {
                sonidoMala = cargarClip(RUTA_MALA);
                ajustarVolumen(sonidoMala, 1.25f);
            }
            if (sonidoBuena == null) {
                sonidoBuena = cargarClip(RUTA_BUENA);
                ajustarVolumen(sonidoBuena, 1.25f);
            }
        } catch (Exception e) {
            clipsClick = null;
            // El sonido es decorativo, si falla, el juego debe seguir funcionando
        }
    }

    // Carga explícitamente la música de fondo para que quede lista al abrir el menú
    public static synchronized void precargarMusicaMenu() {
        try {
            if (musicaMenu != null) {
                return;
            }

            musicaMenu = cargarClip(RUTA_MUSICA_MENU);
            ajustarVolumen(musicaMenu, 0.70f);
            System.out.println("Musica de menú cargada: " + (musicaMenu != null));
        } catch (Exception e) {
            musicaMenu = null;
            e.printStackTrace();
        }
    }

    public static synchronized void precargarMusicaAprender() {
        try {
            if (musicaAprender == null) {
                musicaAprender = cargarClip(RUTA_MUSICA_APRENDER);
            }
        } catch (Exception e) {
            musicaAprender = null;
            e.printStackTrace();
        }
    }

    public static synchronized void precargarMusicaJugar() {
        try {
            if (musicaJugar == null) {
                musicaJugar = cargarClip(RUTA_MUSICA_JUGAR);
            }
        } catch (Exception e) {
            musicaJugar = null;
            e.printStackTrace();
        }
    }

    //reproduce el efecto de sonido asociado al click de un boton
    public static void reproducirClick() {
        if (!sonidoActivado) {
            return;
        }

        // Usamos un hilo fijo de audio. Crear un Thread nuevo en cada click puede producir lag.
        HILO_AUDIO.execute(() -> {
            try {
                precargar();

                Clip clip = obtenerClipClick();

                if (clip == null) {
                    return;
                }

                //Evita que dos reproducciones simultáneas utilicen el mismo clip
                synchronized (clip) {
                    if (clip.isRunning()) {
                        clip.stop();
                    }

                    clip.setFramePosition(0);
                    clip.start();
                }
            } catch (Exception e) {
                // Si falla el audio, ignoramos el error para no afectar la navegacion.
            }
        });
    }

    // Reproduce la música de fondo del menú principal en bucle
    public static void reproducirMusicaMenu() {
        if (!sonidoActivado) {
            return;
        }

        HILO_MUSICA.execute(() -> {
            try {
                precargarMusicaMenu();

                if (musicaMenu == null) {
                    return;
                }

                synchronized (musicaMenu) {
                    detenerMusicaAprender();
                    detenerMusicaJugar();
                    if (musicaMenu.isRunning()) {
                        return;
                    }

                    musicaMenu.stop();
                    musicaMenu.setFramePosition(0);
                    musicaMenu.loop(Clip.LOOP_CONTINUOUSLY);
                    musicaMenu.start();
                    System.out.println("Musica de menú iniciada: " + musicaMenu.isRunning());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Detiene la música de fondo del menú principal
    public static synchronized void detenerMusicaMenu() {
        if (musicaMenu != null) {
            musicaMenu.stop();
        }
    }

    public static void reproducirPuntos() {
        reproducirEfecto(() -> sonidoPuntos);
    }

    public static void reproducirGameOver() {
        reproducirEfecto(() -> sonidoGameOver);
    }

    public static void reproducirPierdeCorazon() {
        reproducirEfecto(() -> sonidoPierdeCorazon);
    }

    public static void reproducirMala() {
        reproducirEfecto(() -> sonidoMala);
    }

    public static void reproducirBuena() {
        reproducirEfecto(() -> sonidoBuena);
    }

    private static void reproducirEfecto(java.util.function.Supplier<Clip> proveedor) {
        if (!sonidoActivado) {
            return;
        }

        HILO_AUDIO.execute(() -> {
            try {
                precargar();
                Clip clip = proveedor.get();
                if (clip == null) {
                    return;
                }

                synchronized (clip) {
                    clip.stop();
                    clip.setFramePosition(0);
                    clip.start();
                }
            } catch (Exception ignored) {
                // Los efectos no deben interrumpir la partida si el audio falla.
            }
        });
    }

    public static void reproducirMusicaAprender() {
        if (!sonidoActivado) {
            return;
        }

        HILO_MUSICA.execute(() -> {
            try {
                precargarMusicaAprender();
                detenerMusicaMenu();
                detenerMusicaJugar();

                if (musicaAprender == null) {
                    return;
                }

                synchronized (musicaAprender) {
                    if (!musicaAprender.isRunning()) {
                        musicaAprender.setFramePosition(0);
                        musicaAprender.loop(Clip.LOOP_CONTINUOUSLY);
                        musicaAprender.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static synchronized void detenerMusicaAprender() {
        if (musicaAprender != null) {
            musicaAprender.stop();
        }
    }

    public static void reproducirMusicaJugar() {
        if (!sonidoActivado) {
            return;
        }

        HILO_MUSICA.execute(() -> {
            try {
                precargarMusicaJugar();
                detenerMusicaMenu();
                detenerMusicaAprender();

                if (musicaJugar == null) {
                    return;
                }

                synchronized (musicaJugar) {
                    if (!musicaJugar.isRunning()) {
                        musicaJugar.setFramePosition(0);
                        musicaJugar.loop(Clip.LOOP_CONTINUOUSLY);
                        musicaJugar.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static synchronized void detenerMusicaJugar() {
        if (musicaJugar != null) {
            musicaJugar.stop();
        }
    }

    // Reanuda la música de fondo del menú principal si estaba detenida
    public static void reanudarMusicaMenu() {
        reproducirMusicaMenu();
    }

    // Activa o desactiva globalmente los efectos de sonido
    public static void activarSonido(boolean activar) {
        sonidoActivado = activar;
    }

    //carga un archivo .wav desde los recursos y lo prepara para su reproducción
   private static Clip cargarClip(String ruta) throws Exception {

    URL recurso = Sonido.class.getResource(ruta);

    if (recurso == null) {
        System.out.println("No encontrado: " + ruta);
        return null;
    }

    AudioInputStream audio = AudioSystem.getAudioInputStream(recurso);

    Clip clip = AudioSystem.getClip();
    clip.open(audio);

    audio.close();

    return clip;
}

    /**
     * Ajusta el volumen de un clip usando una escala lineal de 0 a 1.
     * Internamente Java Sound trabaja en decibelios.
     */
    private static void ajustarVolumen(Clip clip, float volumen) {
        if (clip == null
                || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        // Se permite una ganancia moderada para efectos grabados a bajo nivel.
        float volumenSeguro = Math.max(0.0001f, Math.min(2f, volumen));
        float decibelios = 20f * (float) Math.log10(volumenSeguro);

        FloatControl control = (FloatControl) clip.getControl(
                FloatControl.Type.MASTER_GAIN
        );

        decibelios = Math.max(
                control.getMinimum(),
                Math.min(control.getMaximum(), decibelios)
        );
        control.setValue(decibelios);
    }

    private static synchronized Clip obtenerClipClick() {
        if (clipsClick == null || clipsClick.length == 0) {
            return null;
        }

        Clip clip = clipsClick[siguienteClip];
        siguienteClip = (siguienteClip + 1) % clipsClick.length;
        return clip;
    }

    public static synchronized void cerrar() {
        if (clipsClick != null) {
            for (Clip clip : clipsClick) {
                if (clip != null) {
                    clip.close();
                }
            }

            clipsClick = null;
        }

        if (musicaMenu != null) {
            musicaMenu.close();
            musicaMenu = null;
        }

        if (musicaAprender != null) {
            musicaAprender.close();
            musicaAprender = null;
        }

        if (musicaJugar != null) {
            musicaJugar.close();
            musicaJugar = null;
        }

        if (sonidoPuntos != null) {
            sonidoPuntos.close();
            sonidoPuntos = null;
        }

        if (sonidoGameOver != null) {
            sonidoGameOver.close();
            sonidoGameOver = null;
        }

        if (sonidoPierdeCorazon != null) {
            sonidoPierdeCorazon.close();
            sonidoPierdeCorazon = null;
        }

        if (sonidoMala != null) {
            sonidoMala.close();
            sonidoMala = null;
        }

        if (sonidoBuena != null) {
            sonidoBuena.close();
            sonidoBuena = null;
        }
    }

    
}
