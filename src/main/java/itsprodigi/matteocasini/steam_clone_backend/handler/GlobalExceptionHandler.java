package itsprodigi.matteocasini.steam_clone_backend.handler;

import itsprodigi.matteocasini.steam_clone_backend.dto.ErrorResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                "Risorsa non trovata",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidUUID(MethodArgumentTypeMismatchException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Parametro non valido: formato errato",
                                List.of("ID non valido: deve essere un UUID valido"),
                                request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex,
                        WebRequest request) {
                List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.toList());

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Errore di validazione dei dati",
                                errors,
                                request);
        }

        @ExceptionHandler(DuplicateUsernameException.class)
        public ResponseEntity<ErrorResponseDTO> handleDuplicateUsername(DuplicateUsernameException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Errore di registrazione",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(DuplicateEmailException.class)
        public ResponseEntity<ErrorResponseDTO> handleDuplicateEmail(DuplicateEmailException ex, WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Errore di registrazione",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                "Errore di autenticazione",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(InvalidRoleException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidRole(InvalidRoleException ex, WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.FORBIDDEN,
                                "Accesso negato",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.FORBIDDEN,
                                "Accesso negato",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Si è verificato un errore interno al server",
                                List.of(ex.getMessage()),
                                request);
        }

        private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message,
                        List<String> errors, WebRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                errors,
                                request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(errorResponse, status);
        }

        @ExceptionHandler(GameAlreadyInLibraryException.class)
        public ResponseEntity<ErrorResponseDTO> handleGameAlreadyInLibrary(GameAlreadyInLibraryException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "Gioco già presente nella libreria",
                                List.of(ex.getMessage()),
                                request);
        }

        @ExceptionHandler(InvalidUserProfileDataException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidUserProfileData(InvalidUserProfileDataException ex,
                        WebRequest request) {
                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Errore di validazione del profilo utente",
                                List.of(ex.getMessage()),
                                request);
        }

}