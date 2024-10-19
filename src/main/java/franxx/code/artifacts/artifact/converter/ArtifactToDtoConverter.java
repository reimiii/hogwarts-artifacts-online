package franxx.code.artifacts.artifact.converter;

import franxx.code.artifacts.artifact.Artifact;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
import franxx.code.artifacts.wizard.converter.WizardToDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToDtoConverter implements Converter<Artifact, ArtifactDto> {

  private final WizardToDtoConverter toDtoConverter;

  public ArtifactToDtoConverter(WizardToDtoConverter toDtoConverter) {
    this.toDtoConverter = toDtoConverter;
  }

  @Override
  public ArtifactDto convert(Artifact source) {
    return new ArtifactDto(
        source.getId(),
        source.getName(),
        source.getDescription(),
        source.getImageUrl(),
        source.getWizard() != null
            ? this.toDtoConverter.convert(source.getWizard())
            : null
    );
  }

}
