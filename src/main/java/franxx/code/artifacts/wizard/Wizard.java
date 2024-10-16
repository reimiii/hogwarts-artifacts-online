package franxx.code.artifacts.wizard;

import franxx.code.artifacts.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity @Table(name = "wizards")
public class Wizard implements Serializable {

  @Id
  private Integer id;
  private String name;

  @OneToMany(cascade = {PERSIST, MERGE}, mappedBy = "wizard")
  private List<Artifact> artifacts = new ArrayList<>();

  public Wizard() {}

  public Wizard(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Artifact> getArtifacts() {
    return artifacts;
  }

  public void setArtifact(List<Artifact> artifacts) {
    this.artifacts = artifacts;
  }

  public void setArtifact(Artifact artifact) {
    artifact.setWizard(this);
    this.artifacts.add(artifact);
  }

  // source.getArtifacts().size() in Converter class is know to much about the internal working (law of demeter)
  public Integer getNumberOfArtifacts() {
    return this.artifacts.size();
  }
}
