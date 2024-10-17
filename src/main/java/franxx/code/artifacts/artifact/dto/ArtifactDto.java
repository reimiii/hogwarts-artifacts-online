package franxx.code.artifacts.artifact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import franxx.code.artifacts.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(
    String id,

    @NotEmpty(message = "name is required") String name,

    @NotEmpty(message = "description is required") String description,

    @NotEmpty(message = "image url is required")
    String imageUrl,

    WizardDto owner
) {}
