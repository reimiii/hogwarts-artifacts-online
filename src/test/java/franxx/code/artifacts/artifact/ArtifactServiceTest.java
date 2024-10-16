package franxx.code.artifacts.artifact;

import franxx.code.artifacts.wizard.Wizard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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

  List<Artifact> artifacts;

  @BeforeEach
  void setUp() {

    Artifact a1 = new Artifact();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    a1.setImageUrl("imageUrl");

    Artifact a2 = new Artifact();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImageUrl("imageUrl");

    this.artifacts = new ArrayList<>();

    this.artifacts.add(a1);
    this.artifacts.add(a2);
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

    given(this.artifactRepository.findById("1122")).willReturn(Optional.of(artifact));

    // when. act on target behavior, when step should cover the method to be tested
    Artifact returnArtifact = this.artifactService.findById("1122");

    // then assert expected outcome
    assertThat(returnArtifact.getId()).isEqualTo(artifact.getId());
    assertThat(returnArtifact.getName()).isEqualTo(artifact.getName());
    assertThat(returnArtifact.getWizard()).isEqualTo(wizard);

    verify(this.artifactRepository, times(1)).findById("1122");
  }

  @Test
  void testFindByIdNotFound() {
    // given
    given(this.artifactRepository.findById(Mockito.any(String.class)))
        .willReturn(Optional.empty());

    // when
    Throwable thrown = Assertions.catchThrowable(() -> {
      Artifact returnArtifact = this.artifactService.findById("1122");
    });

    // then
    assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class)
        .hasMessage("could not found artifact with id: 1122");

    verify(this.artifactRepository, times(1)).findById("1122");
  }

  @Test
  void testFindAllSuccess() {
    // given
    given(this.artifactRepository.findAll()).willReturn(this.artifacts);

    // when
    List<Artifact> actualArtifacts = this.artifactService.findAll();

    // then
    assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
    verify(this.artifactRepository, times(1)).findAll();
  }
}