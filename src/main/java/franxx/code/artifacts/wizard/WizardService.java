package franxx.code.artifacts.wizard;

import franxx.code.artifacts.artifact.Artifact;
import franxx.code.artifacts.artifact.ArtifactRepository;
import franxx.code.artifacts.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class WizardService {
  private final WizardRepository wizardRepository;
  private final ArtifactRepository artifactRepository;

  public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
    this.wizardRepository = wizardRepository;
    this.artifactRepository = artifactRepository;
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

  public void assignArtifact(Integer wizardId, String artifactId) {
    // find this wizard by id from db
    Wizard wizard = this.wizardRepository.findById(wizardId)
        .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

    // find this artifact by id from db
    Artifact artifactToBeAssign = this.artifactRepository.findById(artifactId)
        .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

    // artifact assignment
    // need to check if artifact is owned by some wizard
    if (artifactToBeAssign.getWizard() != null) {
      artifactToBeAssign
          .getWizard()
          .removeArtifact(artifactToBeAssign);
    }

    wizard.setArtifact(artifactToBeAssign);
  }
}