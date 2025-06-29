package itsprodigi.matteocasini.steam_clone_backend.handler;

import itsprodigi.matteocasini.steam_clone_backend.dto.ErrorResponseDTO; // Importa il tuo DTO per la risposta di errore
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // Importa la tua eccezione personalizzata

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Indica a Spring che questa classe gestirà le eccezioni globalmente
public class GlobalExceptionHandler {

    // Gestisce le eccezioni ResourceNotFoundException (status 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Risorsa non trovata",
                List.of(ex.getMessage()), // Aggiungiamo il messaggio dell'eccezione come dettaglio
                request.getDescription(false).replace("uri=", "") // Pulisce il path
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Gestisce le eccezioni di validazione (@Valid sui DTO in ingresso) (status 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Errore di validazione dei dati",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Gestisce tutte le altre eccezioni non gestite specificamente (status 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Si è verificato un errore interno al server",
                List.of(ex.getMessage()), // Messaggio generico o dettagli dell'eccezione
                request.getDescription(false).replace("uri=", "")
        );
        // Potresti voler loggare 'ex' qui per scopi di debug, ma non esporre troppi dettagli al client
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}