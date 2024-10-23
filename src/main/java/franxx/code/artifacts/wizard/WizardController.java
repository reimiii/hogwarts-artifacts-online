package franxx.code.artifacts.wizard;

import franxx.code.artifacts.system.Result;
import franxx.code.artifacts.wizard.converter.DtoToWizardConverter;
import franxx.code.artifacts.wizard.converter.WizardToDtoConverter;
import franxx.code.artifacts.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping(path = "${api.endpoint.base-url}/wizards")
public class WizardController {
  private final WizardService wizardService;
  private final WizardToDtoConverter wizardToDtoConverter;
  private final DtoToWizardConverter dtoToWizardConverter;

  public WizardController(WizardService wizardService, WizardToDtoConverter wizardToDtoConverter, DtoToWizardConverter dtoToWizardConverter) {
    this.wizardService = wizardService;
    this.wizardToDtoConverter = wizardToDtoConverter;
    this.dtoToWizardConverter = dtoToWizardConverter;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<List<WizardDto>> findAllWizard() {
    List<Wizard> wizards = this.wizardService.findAll();
    List<WizardDto> wizardDtos = wizards.stream()
        .map(this.wizardToDtoConverter::convert)
        .toList();

    return new Result<>(true, HttpStatus.OK.value(), "find all success", wizardDtos);
  }

  @GetMapping(
      path = "/{wizardId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<WizardDto> findWizardById(@PathVariable Integer wizardId) {
    Wizard wizard = this.wizardService.findById(wizardId);
    WizardDto wizardDto = this.wizardToDtoConverter.convert(wizard);
    return new Result<>(true, HttpStatus.OK.value(), "find one success", wizardDto);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<WizardDto> addWizard(@Valid @RequestBody WizardDto wizardDto) {
    Wizard convert = this.dtoToWizardConverter.convert(wizardDto);
    Wizard saved = this.wizardService.save(convert);
    WizardDto dto = this.wizardToDtoConverter.convert(saved);

    return new Result<>(true, HttpStatus.OK.value(), "add success", dto);
  }

  @PutMapping(
      path = "/{wizardId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<WizardDto> updateWizard(
      @PathVariable Integer wizardId,
      @Valid @RequestBody WizardDto wizardDto
  ) {
    Wizard convert = this.dtoToWizardConverter.convert(wizardDto);
    Wizard updated = this.wizardService.update(wizardId, convert);
    WizardDto dto = this.wizardToDtoConverter.convert(updated);
    return new Result<>(true, HttpStatus.OK.value(), "update success", dto);
  }

  @DeleteMapping(
      path = "/{wizardId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Result<Void> deleteWizard(@PathVariable Integer wizardId) {
    this.wizardService.delete(wizardId);
    return new Result<>(true, HttpStatus.OK.value(), "delete success");
  }
}
