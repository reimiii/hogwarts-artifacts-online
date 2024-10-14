package franxx.code.artifacts.artifact;

import franxx.code.artifacts.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtifactController {

  private final ArtifactService artifactService;

  public ArtifactController(ArtifactService artifactService) {
    this.artifactService = artifactService;
  }

  @GetMapping(
      path = "/api/v1/artifacts/{artifactId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<Artifact> findArtifactById(@PathVariable String artifactId) {
    Artifact artifact = this.artifactService.findById(artifactId);
    return new Result<>(true, HttpStatus.OK.value(), "find one success", artifact);
  }
}
