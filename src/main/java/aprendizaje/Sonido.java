package aprendizaje;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Clase encargada de gestionar los efectos de sonido de la interfaz
// Actualmente reproduce el sonido de click de los botones
public class Sonido {

    //configuracion del efecto de sonido de los botones
    private static final String RUTA_CLICK = "/menu/sonido_click.wav";
    // Tres clips permiten reproducir varios clicks seguidos sin que el sonido se corte
    private static final int CANTIDAD_CLIPS_CLICK = 3;
    //hilo dedicado a reproducir audio sin bloquear la interfaz gráfica
    private static final ExecutorService HILO_AUDIO = Executors.newSingleThreadExecutor(runnable -> {
        Thread hilo = new Thread(runnable, "audio-ui");
        hilo.setDaemon(true);
        return hilo;
    });

    //Pool de clips reutilizables para evitar cargar el sonido en cada reproducción
    private static Clip[] clipsClick;
    private static int siguienteClip;
    private static boolean sonidoActivado = true;

    private Sonido() {
    }

    //carga los clips de audio una única vez al iniciar su uso
    public static synchronized void precargar() {
        try {
            // Si los clips ya fueron cargados, no es necesario volver a hacerlo
            if (clipsClick != null) {
                return;
            }

            clipsClick = new Clip[CANTIDAD_CLIPS_CLICK];
            // Se crean varios clips para permitir clicks consecutivos sin cortes
            for (int i = 0; i < clipsClick.length; i++) {
                clipsClick[i] = cargarClip(RUTA_CLICK);
            }
        } catch (Exception e) {
            clipsClick = null;
            // El sonido es decorativo, si falla, el juego debe seguir funcionando
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

    // Activa o desactiva globalmente los efectos de sonido
    public static void activarSonido(boolean activar) {
        sonidoActivado = activar;
    }

    //carga un archivo .wav desde los recursos y lo prepara para su reproducción
    private static Clip cargarClip(String ruta) throws Exception {
        // Busca el archivo dentro de src/main/resources
        URL recurso = Sonido.class.getResource(ruta);

        if (recurso == null) {
            return null;
        }

        // Se abre el archivo de audio y se carga completamente en memoria
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(recurso)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        }
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
    }
}
