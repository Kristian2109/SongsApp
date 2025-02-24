package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging;

import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.*;

class LocalLoggerTest {
    private Serializer serializer;
    private FileWriter fileWriter;
    private LocalLogger localLogger;

    @BeforeEach
    void setUp() {
        serializer = mock(Serializer.class);
        fileWriter = mock(FileWriter.class);
        localLogger = new LocalLogger(serializer, fileWriter);
    }

    @Test
    void testLogInfoPairsWritesToFileWriter() throws IOException {
        Map<String, Object> logData = Map.of("key1", "value1", "key2", "value2");
        String serializedLog = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

        when(serializer.serialize(logData)).thenReturn(serializedLog);

        localLogger.logInfoPairs(logData);

        verify(serializer, times(1)).serialize(logData);
        verify(fileWriter, times(1)).append(serializedLog);
        verify(fileWriter, times(1)).append(System.lineSeparator());
        verify(fileWriter, times(1)).flush();
    }
}