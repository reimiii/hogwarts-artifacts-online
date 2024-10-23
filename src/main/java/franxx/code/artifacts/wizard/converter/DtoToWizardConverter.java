package franxx.code.artifacts.wizard.converter;

import franxx.code.artifacts.wizard.Wizard;
import franxx.code.artifacts.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToWizardConverter implements Converter<WizardDto, Wizard> {
  @Override
  public Wizard convert(WizardDto source) {
    return new Wizard(source.id(), source.name());
  }
}
