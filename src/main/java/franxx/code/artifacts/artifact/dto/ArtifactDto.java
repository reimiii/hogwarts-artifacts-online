package franxx.code.artifacts.artifact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import franxx.code.artifacts.wizard.dto.WizardDto;

public record ArtifactDto(
    String id,
    String name,
    String description,
    @JsonProperty("image_url") String imageUrl,
    WizardDto owner
) {}
