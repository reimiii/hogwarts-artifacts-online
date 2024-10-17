package franxx.code.artifacts.artifact;

import franxx.code.artifacts.artifact.converter.ArtifactToDtoConverter;
import franxx.code.artifacts.artifact.converter.DtoToArtifactConverter;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
import franxx.code.artifacts.system.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtifactController {

  private final ArtifactService artifactService;
  private final ArtifactToDtoConverter artifactToDtoConverter;
  private final DtoToArtifactConverter dtoToArtifactConverter;

  public ArtifactController(ArtifactService artifactService, ArtifactToDtoConverter artifactToDtoConverter, DtoToArtifactConverter dtoToArtifactConverter) {
    this.artifactService = artifactService;
    this.artifactToDtoConverter = artifactToDtoConverter;
    this.dtoToArtifactConverter = dtoToArtifactConverter;
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

  @GetMapping(
      path = "/api/v1/artifacts",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<List<ArtifactDto>> findAllArtifacts() {
    List<Artifact> foundArtifacts = this.artifactService.findAll();

    // convert foundArtifacts to a list of ArtifactDto...
    List<ArtifactDto> artifactDtos = foundArtifacts.stream()
        .map(this.artifactToDtoConverter::convert)
        .toList();

    return new Result<>(true, HttpStatus.OK.value(), "find all success", artifactDtos);
  }

  @PostMapping(
      path = "/api/v1/artifacts",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<ArtifactDto> addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
    Artifact converted = this.dtoToArtifactConverter.convert(artifactDto);
    Artifact saved = this.artifactService.save(converted);
    ArtifactDto convertedDto = this.artifactToDtoConverter.convert(saved);

    return new Result<>(true, HttpStatus.OK.value(), "add success", convertedDto);
  }
}
