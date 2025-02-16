package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatSerializable;
import bg.sofia.uni.fmi.mjt.spotify.common.ServerResponse;
import com.google.gson.Gson;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private String streamingConnection;
    public static void main(String[] args) {
        Client client = new Client();
        client.runClient();
    }

    private void runClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress("localhost", 3000));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if ("disconnect".equals(message)) {
                    break;
                }

                if (message.startsWith("stop")) {
                    message += " " + streamingConnection;
                }

                System.out.println("Sending message <" + message + "> to the server...");

                writer.println(message);

                String reply = reader.readLine();

                if (message.startsWith("play")) {
                    Gson gson = new Gson();
                    ServerResponse response = gson.fromJson(reply, ServerResponse.class);

                    System.out.println(response);
                    AudioFormatSerializable formatSerializable = response.result();
                    AudioFormat format = new AudioFormat(
                        new AudioFormat.Encoding(formatSerializable.encoding()),
                        formatSerializable.sampleRate(),
                        formatSerializable.sampleSizeInBits(),
                        formatSerializable.channels(),
                        formatSerializable.frameSize(),
                        formatSerializable.frameRate(),
                        formatSerializable.bigEndian()
                    );

                    new Thread(() -> startStreaming(format, formatSerializable.songId())).start();
                }

                System.out.println("The server replied <" + reply + ">");
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private void startStreaming(AudioFormat audioFormat, String songId) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        System.out.println("Try to create connection");

        try (SocketChannel socketChannel = SocketChannel.open();
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
             AudioInputStream audioInputStream = new AudioInputStream(
                 Channels.newInputStream(socketChannel),
                 audioFormat,
                 AudioSystem.NOT_SPECIFIED
             )
        ) {
            socketChannel.connect(new InetSocketAddress("localhost", 8000));
            streamingConnection = UUID.randomUUID().toString();

            writer.write(streamingConnection);
            writer.flush();

            writer.write(songId);
            writer.flush();

            dataLine.open();
            dataLine.start();

            byte[] bufferBytes = new byte[4096];
            int readBytes;
            while ((readBytes = audioInputStream.read(bufferBytes)) != -1) {
                dataLine.write(bufferBytes, 0, readBytes);
            }

            dataLine.drain();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
