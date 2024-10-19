package franxx.code.artifacts.artifact;

import franxx.code.artifacts.artifact.utils.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class ArtifactService {

  private final ArtifactRepository artifactRepository;
  private final IdWorker idWorker;

  public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
    this.artifactRepository = artifactRepository;
    this.idWorker = idWorker;
  }

  public Artifact findById(String artifactId) {
    return this.artifactRepository.findById(artifactId)
        .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
  }

  public List<Artifact> findAll() {
    return this.artifactRepository.findAll();
  }

  public Artifact save(Artifact artifact) {
    artifact.setId(idWorker.nextId() + "");
    return this.artifactRepository.save(artifact);
  }

  public Artifact update(String id, Artifact update) {
    return this.artifactRepository.findById(id)
        .map(old -> {

          old.setName(update.getName());
          old.setDescription(update.getDescription());
          old.setImageUrl(update.getImageUrl());

          return this.artifactRepository.save(old);
        })
        .orElseThrow(() -> new ArtifactNotFoundException(id));
  }

  public void delete(String id) {
    this.artifactRepository.findById(id)
        .orElseThrow(() -> new ArtifactNotFoundException(id));
    this.artifactRepository.deleteById(id);
  }

}
