package franxx.code.artifacts.artifact;

import franxx.code.artifacts.artifact.converter.ArtifactToDtoConverter;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
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
  private final ArtifactToDtoConverter artifactToDtoConverter;

  public ArtifactController(ArtifactService artifactService, ArtifactToDtoConverter artifactToDtoConverter) {
    this.artifactService = artifactService;
    this.artifactToDtoConverter = artifactToDtoConverter;
  }

  @GetMapping(
      path = "/api/v1/artifacts/{artifactId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<ArtifactDto> findArtifactById(@PathVariable String artifactId) {
    Artifact artifact = this.artifactService.findById(artifactId);
    ArtifactDto converted = this.artifactToDtoConverter.convert(artifact);
    return new Result<>(true, HttpStatus.OK.value(), "find one success", converted);
  }
}
