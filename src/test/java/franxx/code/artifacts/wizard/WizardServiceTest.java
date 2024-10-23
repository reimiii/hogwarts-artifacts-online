package franxx.code.artifacts.wizard;

import franxx.code.artifacts.system.exception.ObjectNotFoundException;
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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.throwable;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
  @Mock
  WizardRepository wizardRepository;

  @InjectMocks
  WizardService wizardService;

  List<Wizard> wizards;

  @BeforeEach
  void setUp() {
    this.wizards = new ArrayList<>();
    this.wizards.add(new Wizard(1, "Arif"));
    this.wizards.add(new Wizard(2, "Toriq"));
    this.wizards.add(new Wizard(3, "Kelvin"));
    this.wizards.add(new Wizard(4, "David"));
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindAllSuccess() {
    // given
    given(this.wizardRepository.findAll()).willReturn(this.wizards);

    // when
    List<Wizard> actualWizard = this.wizardService.findAll();

    // then, assert expected out come
    assertThat(actualWizard.size()).isEqualTo(this.wizards.size());

    // verify this.wizardRepository.findAll() is called exactly 1
    verify(this.wizardRepository, times(1)).findAll();
  }

  @Test
  void testFindByIdSuccess() {
    // given
    Wizard wizard = new Wizard(1, "Mee");
    given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));

    // when
    Wizard returnedWizard = this.wizardService.findById(1);

    // then
    assertThat(returnedWizard.getId()).isEqualTo(wizard.getId());
    assertThat(returnedWizard.getName()).isEqualTo(wizard.getName());

    // verify
    verify(this.wizardRepository, times(1)).findById(1);
  }

  @Test
  void testFindByIdNotFound() {
    // given
    given(this.wizardRepository.findById(Mockito.any(Integer.class)))
        .willReturn(Optional.empty());

    // when
    Throwable throwable = catchThrowable(() -> this.wizardService.findById(1));

    // then
    assertThat(throwable)
        .isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("could not found wizard with id: 1");

    // verify
    verify(this.wizardRepository, times(1)).findById(1);
  }

  @Test
  void testSaveWizardSuccess() {
    // given
    Wizard w = new Wizard();
    w.setName("Mee");

    given(this.wizardRepository.save(w)).willReturn(w);

    // when

    Wizard saved = this.wizardService.save(w);

    // then
    assertThat(saved.getName()).isEqualTo(w.getName());

    // verify
    verify(this.wizardRepository, times(1)).save(w);
  }

  @Test
  void testUpdateSuccess() {
    // given
    var oldWizard = new Wizard(2, "PPP");

    // input from client
    var updateWizard = new Wizard();
    updateWizard.setName("PPP - UPDATE");

    given(this.wizardRepository.findById(2)).willReturn(Optional.of(oldWizard));
    given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

    // when

    Wizard updated = this.wizardService.update(2, updateWizard);

    // then
    assertThat(updated.getId()).isEqualTo(2);
    assertThat(updated.getName()).isEqualTo(updateWizard.getName());

    // verify
    verify(this.wizardRepository, times(1)).findById(2);
    verify(this.wizardRepository, times(1)).save(oldWizard);
  }

  @Test
  void testUpdateNotFound() {
    // given
    Wizard wizard = new Wizard();
    wizard.setName("UPD");

    given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

    // when
    assertThrows(ObjectNotFoundException.class, () -> this.wizardService.update(1, wizard));

    // then
    verify(this.wizardRepository, times(1)).findById(1);
    verify(this.wizardRepository, times(0)).save(wizard);

  }

  @Test
  void testDeleteSuccess() {
    // pass from controller
    // given
    given(this.wizardRepository.findById(1))
        .willReturn(Optional.of(new Wizard(1, "MEE")));
    doNothing().when(this.wizardRepository).deleteById(1);

    // when
    this.wizardService.delete(1);

    // then and verify
    verify(this.wizardRepository, times(1)).findById(1);
    verify(this.wizardRepository, times(1)).deleteById(1);
  }

  @Test
  void testDeleteNotFound() {
    // given
    given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

    // when
    assertThrows(ObjectNotFoundException.class, () -> this.wizardService.delete(1));

    // then
    verify(this.wizardRepository, times(1)).findById(1);
    verify(this.wizardRepository, times(0)).deleteById(1);

  }
}