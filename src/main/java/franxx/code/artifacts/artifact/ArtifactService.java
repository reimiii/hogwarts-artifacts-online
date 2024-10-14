package franxx.code.artifacts.artifact;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class ArtifactService {

  private final ArtifactRepository artifactRepository;

  public ArtifactService(ArtifactRepository artifactRepository) {
    this.artifactRepository = artifactRepository;
  }

}
