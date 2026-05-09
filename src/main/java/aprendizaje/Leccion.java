package aprendizaje;
public class Leccion{

    private String rutaGIf;
    private String rutaAudio;
    private String romaji;

    public Leccion (String rutaGif,String rutaAudio, String romaji){
        this.rutaGIf=rutaGif;
        this.rutaAudio=rutaAudio;
        this.romaji=romaji;
    }

    public String getRutaGIf() {
        return rutaGIf;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public String getRomaji() {
        return romaji;
    }


}
