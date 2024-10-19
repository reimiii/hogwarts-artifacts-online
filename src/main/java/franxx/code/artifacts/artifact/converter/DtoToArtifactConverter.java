package franxx.code.artifacts.artifact.converter;

import franxx.code.artifacts.artifact.Artifact;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {

  @Override
  public Artifact convert(ArtifactDto source) {
    return new Artifact(
        source.id(),
        source.name(),
        source.description(),
        source.imageUrl()
    );
  }

}
