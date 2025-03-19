package tech.zerofiltre.testing.calcul.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BatchCalculationFileServiceImplTest {

    private final BatchCalculationFileServiceImpl service = new BatchCalculationFileServiceImpl();

    @TempDir
    Path tempDir;

    @Test
    @SuppressWarnings("java:S6204")
    void testRead_ValidFile() throws IOException {
        // GIVEN - Création d'un fichier temporaire avec des données de test
        Path testFile = tempDir.resolve("testFile.txt");
        List<String> lines = List.of("2 + 2", "3 - 2", "2022 / 4");
        Files.write(testFile, lines);

        // WHEN
        try (Stream<String> result = service.read(testFile.toString())) {
            List<String> resultList = result.collect(Collectors.toUnmodifiableList());

            // THEN
            assertThat(resultList).containsExactlyElementsOf(lines);
        }
    }
    @Test
    void read_StreamIsClosedProperly() throws IOException {
        // GIVEN
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, List.of("2 + 2"));

        // WHEN & THEN (vérification implicite via try-with-resources)
        assertDoesNotThrow(() -> {
            try (Stream<String> stream = service.read(testFile.toString())) {
                // Utilisez le stream normalement
                assertThat(stream).containsExactly("2 + 2");
            } // Le stream est automatiquement fermé ici
        });
    }

    @Test
    void testRead_FileNotFound() {
        // GIVEN
        String nonExistentFile = tempDir.resolve("nonexistent.txt").toString();

        // WHEN / THEN - Vérification que l'exception est bien levée
        assertThrows(IOException.class, () -> service.read(nonExistentFile));
    }

    @Test
    void read_ThrowsUncheckedIOExceptionWhenClosingFails_WithSpy() throws Exception {
        // GIVEN
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, List.of("2 + 2"));

        // Création d'un spy du service
        BatchCalculationFileServiceImpl spyService = spy(new BatchCalculationFileServiceImpl());

        // Mock du BufferedReader qui échoue à la fermeture
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.lines()).thenReturn(Stream.of("2 + 2"));
        doThrow(new IOException("Simulated close error")).when(mockReader).close();

        // Remplacement du BufferedReader via reflection (pour éviter de modifier la classe)
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            // Mock Files.newBufferedReader()
            filesMock.when(() -> Files.newBufferedReader(any(Path.class)))
                    .thenReturn(mockReader);

            // WHEN & THEN
            assertThrows(UncheckedIOException.class, () -> {
                try (Stream<String> stream = spyService.read(testFile.toString())) {
                    stream.close(); // Forcer la fermeture pour déclencher l'exception
                }
            });
        }
    }

}
