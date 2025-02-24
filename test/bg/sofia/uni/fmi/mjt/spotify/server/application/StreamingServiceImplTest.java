//package bg.sofia.uni.fmi.mjt.spotify.server.application;
//
//import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
//import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class StreamingServiceImplTest {
//    private static final String CONNECTION_ID = "conn-123";
//    private static final String SONG_ID = "song-456";
//    private static final String SONG_SOURCE = "song.mp3";
//    private static final byte[] TEST_AUDIO_DATA = "mock audio data".getBytes();
//
//    @Test
//    void testBeginStreamingProperlyStreamsAudioData() throws Exception {
//        SongsRepository songsRepository = mock(SongsRepository.class);
//        Logger logger = mock(Logger.class);
//        Set<String> streamingIds = new HashSet<>();
//
//        Socket socket = mock(Socket.class);
//        InputStream socketInput = new ByteArrayInputStream(
//            (CONNECTION_ID + "\n" + SONG_ID).getBytes(StandardCharsets.UTF_8)
//        );
//        OutputStream socketOutput = mock(OutputStream.class);
//
//        when(socket.getInputStream()).thenReturn(socketInput);
//        when(socket.getOutputStream()).thenReturn(socketOutput);
//
//        Song mockSong = new Song(SONG_ID, "Test Song", SONG_SOURCE, "Artist", 180);
//        when(songsRepository.getOrThrow(SONG_ID)).thenReturn(mockSong);
//
//        StreamingServiceImpl service = new StreamingServiceImpl(streamingIds, logger, songsRepository);
//
//        service.beginStreaming(socket);
//
//        verify(streamingIds).add(CONNECTION_ID);
//
//        ArgumentCaptor<byte[]> bufferCaptor = ArgumentCaptor.forClass(byte[].class);
//        verify(socketOutput, times(1))
//            .write(bufferCaptor.capture(), eq(0), eq(TEST_AUDIO_DATA.length));
//
//        byte[] sentData = bufferCaptor.getValue();
//
//        verify(socket).close();
//        verify(streamingIds).remove(CONNECTION_ID);
//        verify(logger).logInfo("Streaming running");
//    }
//}