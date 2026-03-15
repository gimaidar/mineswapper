package ru.aidar.mineswapper.controller.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.aidar.mineswapper.exception.BadRequestException;
import ru.aidar.minesweeper.dto.ErrorResponse;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.engine.path.PathImpl.createPathFromString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@SpringJUnitConfig(RestExceptionHandler.class)
class RestExceptionHandlerTest {

    @Autowired
    private RestExceptionHandler handler;

    @DisplayName("Should handle BadRequestException")
    @Test
    void handleBadRequestExceptionTest() {
        // Given
        BadRequestException ex = new BadRequestException("Game not found");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Game not found");
    }

    @DisplayName("Should handle MethodArgumentNotValidException")
    @Test
    void handleMethodArgumentNotValidExceptionTest() {
        // Given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "width", "must be <= 50"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("width: must be <= 50");
    }

    @DisplayName("Should handle ConstraintViolationException")
    @Test
    void handleConstraintViolationExceptionTest() {
        // Given
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("must be >= 2");
        when(violation.getPropertyPath()).thenReturn(createPathFromString("width"));
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        // When
        ResponseEntity<ErrorResponse> response = handler.handleConstraintViolationException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("width: must be >= 2");
    }

    @DisplayName("Should handle Exception")
    @Test
    void handleCommonExceptionTest() {
        // Given
        Exception ex = new RuntimeException("Unexpected");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleCommonException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal server error");
    }

}