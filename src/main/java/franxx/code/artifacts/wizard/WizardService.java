package franxx.code.artifacts.wizard;

import franxx.code.artifacts.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class WizardService {
  private final WizardRepository wizardRepository;

  public WizardService(WizardRepository wizardRepository) {
    this.wizardRepository = wizardRepository;
  }

  @Transactional(readOnly = true)
  public List<Wizard> findAll() {
    return this.wizardRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Wizard findById(Integer id) {
    return this.wizardRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("wizard", id));
  }

  public Wizard save(Wizard wizard) {
    return this.wizardRepository.save(wizard);
  }

  public Wizard update(Integer id, Wizard wizardUpdate) {
    return this.wizardRepository.findById(id)
        .map(old -> {
          old.setName(wizardUpdate.getName());
          return this.wizardRepository.save(old);
        })
        .orElseThrow(() -> new ObjectNotFoundException("wizard", id));
  }

  public void delete(Integer id) {
    Wizard wizardToBeDeleted = this.wizardRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("wizard", id));
    wizardToBeDeleted.removeAllArtifacts();
    this.wizardRepository.deleteById(id);
  }
}