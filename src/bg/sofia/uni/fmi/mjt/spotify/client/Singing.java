package bg.sofia.uni.fmi.mjt.spotify.client;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Singing {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File audioFile = new File("C:\\Users\\krist\\Downloads\\converted.wav");
        AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);

        SourceDataLine dataLine = AudioSystem.getSourceDataLine(stream.getFormat());
        dataLine.open();
        dataLine.start();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = stream.read(buffer)) != -1) {
            dataLine.write(buffer, 0, bytesRead);
        }

        dataLine.drain();
        dataLine.close();
        stream.close();
    }
}
