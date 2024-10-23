package franxx.code.artifacts.system.exception;

public class ObjectNotFoundException extends RuntimeException {
  public ObjectNotFoundException(String objectName, String id) {
    super("could not found " + objectName + " with id: " + id);
  }

  public ObjectNotFoundException(String objectName, Integer id) {
    super("could not found " + objectName + " with id: " + id);
  }
}
