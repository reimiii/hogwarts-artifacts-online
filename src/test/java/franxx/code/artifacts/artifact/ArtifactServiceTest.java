package franxx.code.artifacts.artifact;

import franxx.code.artifacts.wizard.Wizard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

  @Mock // object ini jangan pake yang aslinya
  ArtifactRepository artifactRepository;

  @InjectMocks
  ArtifactService artifactService;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindByIdSuccess() {
    // given. input and target define the behavior of the mock object artifactRepository
    /*
     *     "id": "1250808601744904192",
     *     "name": "Invisibility Cloak",
     *     "description": "An invisibility cloak is used to make the wearer invisible.",
     *     "imageUrl": "ImageUrl",
     *
     */

    Artifact artifact = new Artifact(
        "1122",
        "Invisibility",
        "An",
        "Image"
    );

    Wizard wizard = new Wizard(1, "Harry");

    artifact.setWizard(wizard);

    given(artifactRepository.findById("1122")).willReturn(Optional.of(artifact));

    // when. act on target behavior, when step should cover the method to be tested
    Artifact returnArtifact = artifactService.findById("1122");

    // then assert expected outcome
    assertThat(returnArtifact.getId()).isEqualTo(artifact.getId());
    assertThat(returnArtifact.getName()).isEqualTo(artifact.getName());
    assertThat(returnArtifact.getWizard()).isEqualTo(wizard);

    verify(artifactRepository, times(1)).findById("1122");
  }
}