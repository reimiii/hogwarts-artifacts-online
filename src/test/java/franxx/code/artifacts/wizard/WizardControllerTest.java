package franxx.code.artifacts.wizard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.artifacts.artifact.Artifact;
import franxx.code.artifacts.system.Result;
import franxx.code.artifacts.system.exception.ObjectNotFoundException;
import franxx.code.artifacts.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockBean WizardService wizardService;

  List<Wizard> wizards;

  @Value("${api.endpoint.base-url}")
  String baseUrl;

  @BeforeEach
  void setUp() {
    Artifact a1 = new Artifact();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    a1.setImageUrl("ImageUrl");

    Artifact a2 = new Artifact();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImageUrl("ImageUrl");

    Artifact a3 = new Artifact();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
    a3.setImageUrl("ImageUrl");

    Artifact a4 = new Artifact();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
    a4.setImageUrl("ImageUrl");

    Artifact a5 = new Artifact();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
    a5.setImageUrl("ImageUrl");

    Artifact a6 = new Artifact();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
    a6.setImageUrl("ImageUrl");

    this.wizards = new ArrayList<>();

    Wizard w1 = new Wizard();
    w1.setId(1);
    w1.setName("Albus Dumbledore");
    w1.setArtifact(a1);
    w1.setArtifact(a3);
    this.wizards.add(w1);

    Wizard w2 = new Wizard();
    w2.setId(2);
    w2.setName("Harry Potter");
    w2.setArtifact(a2);
    w2.setArtifact(a4);
    this.wizards.add(w2);

    Wizard w3 = new Wizard();
    w3.setId(3);
    w3.setName("Neville Longbottom");
    w3.setArtifact(a5);
    this.wizards.add(w3);

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindAllWizardSuccess() throws Exception {

    // given
    given(this.wizardService.findAll()).willReturn(this.wizards);

    // when and then
    this.mockMvc.perform(
            get(baseUrl + "/wizards")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("find all success"))
        .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
        .andExpect(jsonPath("$.data[0].numberOfArtifact").value(2))
        .andExpect(jsonPath("$.data[1].id").value(2))
        .andExpect(jsonPath("$.data[1].name").value("Harry Potter"))
        .andExpect(jsonPath("$.data[1].numberOfArtifact").value(2))
        .andExpect(jsonPath("$.data[2].numberOfArtifact").value(1))
    ;

    // verify
    verify(this.wizardService, times(1)).findAll();
  }

  @Test
  void testFindByIdSuccess() throws Exception {
    // given
    given(this.wizardService.findById(1)).willReturn(this.wizards.getLast());

    // when and then
    this.mockMvc.perform(
            get(baseUrl + "/wizards/1")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("find one success"))
        .andExpect(jsonPath("$.data.id").value(3))
        .andExpect(jsonPath("$.data.name").value("Neville Longbottom"))
    ;

    // verify
    verify(this.wizardService, times(1)).findById(1);
  }

  @Test
  void testFindByIdNotFound() throws Exception {
    // given
    given(this.wizardService.findById(1))
        .willThrow(new ObjectNotFoundException("wizard", 1));

    // when and then
    this.mockMvc.perform(
            get(baseUrl + "/wizards/1")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found wizard with id: 1"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    // verify
    verify(this.wizardService, times(1)).findById(1);
  }

  @Test
  void testAddWizardSuccess() throws Exception {
    // given
    String json = objectMapper.writeValueAsString(new WizardDto(null, "MEE", 0));
    given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(new Wizard(2, "MEE"));

    // when and then
    this.mockMvc.perform(
            post(baseUrl + "/wizards")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("add success"))
        .andExpect(jsonPath("$.data.id").value(2))
        .andExpect(jsonPath("$.data.name").value("MEE"))
        .andExpect(jsonPath("$.data.numberOfArtifact").value(0))
    ;

    // verify
    verify(this.wizardService, times(1)).save(Mockito.any(Wizard.class));
  }

  @Test
  void testUpdateWizardSuccess() throws Exception {
    // from client
    String json = objectMapper.writeValueAsString(new WizardDto(null, "Update-name", 0));

    // make dto to Wizard object
    Wizard update = new Wizard(1, "Update-name");

    // given
    given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(update);

    // when and then
    this.mockMvc.perform(
            put(baseUrl + "/wizards/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("update success"))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.name").value("Update-name"))
        .andExpect(jsonPath("$.data.numberOfArtifact").value(0))
    ;

    // verify
    verify(this.wizardService, times(1)).update(eq(1), Mockito.any(Wizard.class));
  }

  @Test
  void testUpdateNotFound() throws Exception {
    // client
    String json = objectMapper.writeValueAsString(new WizardDto(null, "ERROR", 0));

    // given
    given(this.wizardService.update(eq(100), any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 100));

    // when and then
    this.mockMvc.perform(
            put(baseUrl + "/wizards/100")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found wizard with id: 100"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    // verify
    verify(this.wizardService, times(1)).update(eq(100), Mockito.any(Wizard.class));
  }

  @Test
  void testDeleteWizardSuccess() throws Exception {
    // form client
    // given
    doNothing().when(this.wizardService).delete(9);

    // when and then
    this.mockMvc.perform(
            delete(baseUrl + "/wizards/9")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("delete success"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    // verify
    verify(this.wizardService, times(1)).delete(9);
  }
  @Test
  void testDeleteWizardNotFound() throws Exception {
    // form client
    // given
    doThrow(new ObjectNotFoundException("wizard", 9))
        .when(this.wizardService).delete(9);

    // when and then
    this.mockMvc.perform(
            delete(baseUrl + "/wizards/9")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found wizard with id: 9"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    // verify
    verify(this.wizardService, times(1)).delete(9);
  }
}