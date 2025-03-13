package tech.zerofiltre.testing.calcul.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleIllegalArgumentException() {
        // GIVEN
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // WHEN
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Invalid argument");
    }

    @Test
    void testHandleGeneralException() {
        // GIVEN
        Exception exception = new Exception("Something went wrong");

        // WHEN
        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(exception);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isEqualTo("An unexpected error occurred : Something went wrong");
    }
}
