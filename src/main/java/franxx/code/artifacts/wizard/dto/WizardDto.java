package franxx.code.artifacts.wizard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WizardDto(
    Integer id,
    String name,
    @JsonProperty("number_of_artifact") Integer numberOfArtifact
) {}
