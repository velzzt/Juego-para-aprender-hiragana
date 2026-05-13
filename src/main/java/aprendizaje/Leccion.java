package aprendizaje;
public class Leccion{

    private String rutaGif;
    private String rutaAudio;
    private String romaji;

    public Leccion (String rutaGif,String rutaAudio, String romaji){
        this.rutaGif=rutaGif;
        this.rutaAudio=rutaAudio;
        this.romaji=romaji;
    }

    public String getRutaGif() {
        return rutaGif;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public String getRomaji() {
        return romaji;
    }


}
