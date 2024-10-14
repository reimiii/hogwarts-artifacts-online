package franxx.code.artifacts.artifact;

import franxx.code.artifacts.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity(name = "artifacts")
public class Artifact implements Serializable {

  @Id
  private String id;
  private String name;
  private String description;
  private String imageUrl;

  @ManyToOne @JoinColumn(name = "wizard_id", referencedColumnName = "id")
  private Wizard wizard;

  public Artifact() {}

  public Artifact(String id, String name, String description, String imageUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Wizard getWizard() {
    return wizard;
  }

  public void setWizard(Wizard wizard) {
    this.wizard = wizard;
  }
}
