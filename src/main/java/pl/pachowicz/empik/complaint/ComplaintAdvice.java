package pl.pachowicz.empik.complaint;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
class ComplaintAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Complaint> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseEntity.notFound().build();
    }

}
