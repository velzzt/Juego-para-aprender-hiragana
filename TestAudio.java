import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;

public class TestAudio {
  public static void main(String[] args) throws Exception {
    File f = new File("src/main/resources/menu/musica_menu.wav");
    System.out.println("exists=" + f.exists());
    if (!f.exists()) return;
    AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
    System.out.println("type=" + aff.getType());
    AudioInputStream ais = AudioSystem.getAudioInputStream(f);
    AudioFormat fmt = ais.getFormat();
    System.out.println("format=" + fmt.toString());
    System.out.println("encoding=" + fmt.getEncoding());
    System.out.println("channels=" + fmt.getChannels());
    System.out.println("sampleRate=" + fmt.getSampleRate());
    System.out.println("sampleSizeBits=" + fmt.getSampleSizeInBits());
    System.out.println("frameRate=" + fmt.getFrameRate());
    System.out.println("frameSize=" + fmt.getFrameSize());
    System.out.println("bigEndian=" + fmt.isBigEndian());
    Clip clip = AudioSystem.getClip();
    clip.open(ais);
    System.out.println("clip_opened=" + clip.isOpen());
    clip.close();
  }
}
