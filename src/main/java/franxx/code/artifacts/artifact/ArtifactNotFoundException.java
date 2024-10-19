package franxx.code.artifacts.artifact;

public class ArtifactNotFoundException extends RuntimeException {

  public ArtifactNotFoundException(String artifactId) {
    super("could not found artifact with id: " + artifactId);
  }
}
