package franxx.code.artifacts.system.exception;

import franxx.code.artifacts.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  Result<Void> handleObjectNotFoundException(ObjectNotFoundException ex) {
    return new Result<>(false, HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Result<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    Map<String, String> map = new HashMap<>(errors.size());
    errors.forEach((error) -> {
      String key = ((FieldError) error).getField();
      String val = error.getDefaultMessage();
      map.put(key, val);
    });
    return new Result<>(false, HttpStatus.BAD_REQUEST.value(), "provided arguments are invalid, see data for details.", map);
  }

}
