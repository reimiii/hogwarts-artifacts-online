package franxx.code.artifacts.system.exception;

import franxx.code.artifacts.artifact.Artifact;
import franxx.code.artifacts.artifact.ArtifactNotFoundException;
import franxx.code.artifacts.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ArtifactNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  Result<Void> handleArtifactNotFoundException(ArtifactNotFoundException ex) {
    return new Result<>(false, HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

}
