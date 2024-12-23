package franxx.code.artifacts.artifact;

import franxx.code.artifacts.artifact.converter.ArtifactToDtoConverter;
import franxx.code.artifacts.artifact.converter.DtoToArtifactConverter;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
import franxx.code.artifacts.system.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping(path = "${api.endpoint.base-url}/artifacts")
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
      path = "/{artifactId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<ArtifactDto> findArtifactById(@PathVariable String artifactId) {
    Artifact artifact = this.artifactService.findById(artifactId);
    ArtifactDto converted = this.artifactToDtoConverter.convert(artifact);
    return new Result<>(true, HttpStatus.OK.value(), "find one success", converted);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<List<ArtifactDto>> findAllArtifacts() {
    List<Artifact> foundArtifacts = this.artifactService.findAll();

    // convert foundArtifacts to a list of ArtifactDto...
    List<ArtifactDto> artifactDtos = foundArtifacts.stream()
        .map(this.artifactToDtoConverter::convert)
        .toList();

    return new Result<>(true, HttpStatus.OK.value(), "find all success", artifactDtos);
  }

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<ArtifactDto> addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
    Artifact converted = this.dtoToArtifactConverter.convert(artifactDto);
    Artifact saved = this.artifactService.save(converted);
    ArtifactDto convertedDto = this.artifactToDtoConverter.convert(saved);

    return new Result<>(true, HttpStatus.OK.value(), "add success", convertedDto);
  }

  @PutMapping(
      path = "/{artifactId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<ArtifactDto> updateArtifact(
      @PathVariable String artifactId,
      @Valid @RequestBody ArtifactDto artifactDto
  ) {

    Artifact convert = this.dtoToArtifactConverter.convert(artifactDto);
    Artifact updated = this.artifactService.update(artifactId, convert);
    ArtifactDto dto = this.artifactToDtoConverter.convert(updated);

    return new Result<>(true, HttpStatus.OK.value(), "update success", dto);
  }

  @DeleteMapping(
      path = "/{artifactId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<Void> deleteArtifact(@PathVariable String artifactId) {
    this.artifactService.delete(artifactId);
    return new Result<>(true, HttpStatus.OK.value(), "delete success");
  }
}
