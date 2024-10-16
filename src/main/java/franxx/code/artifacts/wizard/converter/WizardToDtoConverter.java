package franxx.code.artifacts.wizard.converter;

import franxx.code.artifacts.wizard.Wizard;
import franxx.code.artifacts.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToDtoConverter implements Converter<Wizard, WizardDto> {

  @Override
  public WizardDto convert(Wizard source) {
    return new WizardDto(
        source.getId(),
        source.getName(),
        source.getNumberOfArtifacts()
    );
  }

}
