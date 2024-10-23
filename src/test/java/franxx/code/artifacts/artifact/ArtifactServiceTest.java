package franxx.code.artifacts.artifact;

import franxx.code.artifacts.artifact.utils.IdWorker;
import franxx.code.artifacts.system.exception.ObjectNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

  @Mock // object ini jangan pake yang aslinya
  ArtifactRepository artifactRepository;

  @Mock IdWorker idWorker;

  @InjectMocks ArtifactService artifactService;

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
    Throwable thrown = Assertions.catchThrowable(() -> this.artifactService.findById("1122"));

    // then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
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

  @Test
  void testSaveSuccess() {
    // given
    Artifact artifact = new Artifact();
    artifact.setName("Artifact Mock");
    artifact.setDescription("Description...");
    artifact.setImageUrl("Image....");

    given(this.idWorker.nextId()).willReturn(1234L);
    given(this.artifactRepository.save(artifact)).willReturn(artifact);

    // when

    Artifact savedArtifact = this.artifactService.save(artifact);

    // then
    assertThat(savedArtifact.getId()).isEqualTo("1234");
    assertThat(savedArtifact.getName()).isEqualTo(artifact.getName());
    assertThat(savedArtifact.getDescription()).isEqualTo(artifact.getDescription());
    assertThat(savedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());

    verify(this.artifactRepository, times(1)).save(artifact);
    verify(this.idWorker, times(1)).nextId();
  }

  @Test
  void testUpdateSuccess() {
    // given

    Artifact oldArtifact = new Artifact();
    oldArtifact.setId("1");
    oldArtifact.setName("Artifact Mock");
    oldArtifact.setDescription("Description...");
    oldArtifact.setImageUrl("Image....");

    // for expected update
    Artifact updateA = new Artifact(
        "1",
        "New Name",
        "new Desc",
        "new image"
    );

    // return oldArtifact lalu di kelola, di ganti isi nya dengan isi dari param ke 2
    // yaitu updateA
    given(this.artifactRepository.findById("1")).willReturn(Optional.of(oldArtifact));

    // update object oldArtifact in service, and will return same object with updated data
    // dan save object yang udh di ganti return objeck oldArtifact yang sama di memory, dengan data yang udh berubah
    given(this.artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

    // when

    // di sini pas di pas id update di paggil save nya, isi dari
    // oldArtifact bakalan ke ganti ama updateA...
    Artifact updatedArtifact = this.artifactService.update("1", updateA);


    // then
    assertThat(updatedArtifact.getId()).isEqualTo(updateA.getId());
    assertThat(updatedArtifact.getName()).isEqualTo(updateA.getName());
    assertThat(updatedArtifact.getDescription()).isEqualTo(updateA.getDescription());

    System.out.println(updateA.getName());
    System.out.println(updatedArtifact.getName());

    verify(this.artifactRepository, times(1)).findById("1");
    verify(this.artifactRepository, times(1)).save(oldArtifact);
  }

  @Test
  void testUpdateNotFound() {
    // given
    Artifact oldArtifact = new Artifact();
    oldArtifact.setName("Artifact Mock");
    oldArtifact.setDescription("Description...");
    oldArtifact.setImageUrl("Image....");

    given(this.artifactRepository.findById("1")).willReturn(Optional.empty());

    // when

    assertThrows(ObjectNotFoundException.class, () -> this.artifactService.update("1", oldArtifact));

    // then
    verify(this.artifactRepository, times(1)).findById("1");
    verify(this.artifactRepository, times(0)).save(oldArtifact);
  }

  @Test
  void testDeleteArtifactSuccess() {

    // given
    Artifact d = new Artifact();
    d.setId("1");
    d.setName("Artifact Mock");
    d.setDescription("Description...");
    d.setImageUrl("Image....");

    given(this.artifactRepository.findById("1")).willReturn(Optional.of(d));
    doNothing().when(this.artifactRepository).deleteById("1");

    // when
    this.artifactService.delete("1");

    // then
    verify(this.artifactRepository, times(1)).findById("1");
    verify(this.artifactRepository, times(1)).deleteById("1");

  }

  @Test
  void testDeleteArtifactNotFound() {

    // given
    given(this.artifactRepository.findById("1")).willReturn(Optional.empty());

    // when
    assertThrows(ObjectNotFoundException.class, () -> this.artifactService.delete("1"));

    // then
    verify(this.artifactRepository, times(1)).findById("1");
    verify(this.artifactRepository, times(0)).deleteById(anyString());
  }
}