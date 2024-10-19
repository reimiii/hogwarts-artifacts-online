package franxx.code.artifacts.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.artifacts.artifact.dto.ArtifactDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ArtifactService artifactService;

  @Autowired ObjectMapper objectMapper;

  List<Artifact> artifactList;

  @BeforeEach
  void setUp() {
    this.artifactList = new ArrayList<>();
    this.artifactList.add(new Artifact("1223", "Invisibility Cloak", "An invisibility cloak is used to make the wearer invisible.", "ImageUrl1"));
    this.artifactList.add(new Artifact("1224", "Excalibur", "The legendary sword of King Arthur.", "ImageUrl2"));
    this.artifactList.add(new Artifact("1225", "Philosopher's Stone", "A mystical stone that grants immortality and turns any metal into gold.", "ImageUrl3"));
    this.artifactList.add(new Artifact("1219", "Holy Grail", "A sacred relic with mysterious powers.", "ImageUrl4"));
    this.artifactList.add(new Artifact("1221", "Flying Carpet", "A magical carpet that grants the ability to fly.", "ImageUrl5"));
    this.artifactList.add(new Artifact("1220", "Ring of Power", "A ring that grants incredible power to its wearer.", "ImageUrl6"));
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindArtifactByIdSuccess() throws Exception {
    // given
    given(this.artifactService.findById("1220")).willReturn(this.artifactList.getLast());

    // when and then
    this.mockMvc.perform(
            get("/api/v1/artifacts/1220")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("find one success"))
        .andExpect(jsonPath("$.data.id").value("1220"))
        .andExpect(jsonPath("$.data.name").value("Ring of Power"));

    verify(this.artifactService, times(1)).findById("1220");
  }

  @Test
  void testFindArtifactByIdNotFound() throws Exception {
    // given
    given(this.artifactService.findById("1220"))
        .willThrow(new ArtifactNotFoundException("1220"));

    // when and then
    this.mockMvc.perform(
            get("/api/v1/artifacts/1220")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found artifact with id: 1220"))
        .andExpect(jsonPath("$.data").isEmpty());

    verify(this.artifactService, times(1)).findById("1220");
  }

  @Test
  void testFindAllArtifactsSuccess() throws Exception {
    // given
    given(this.artifactService.findAll()).willReturn(this.artifactList);

    // when and then
    this.mockMvc.perform(
            get("/api/v1/artifacts")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("find all success"))
        .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifactList.size())))
        .andExpect(jsonPath("$.data[0].id").value("1223"))
        .andExpect(jsonPath("$.data[0].name").value("Invisibility Cloak"))
        .andExpect(jsonPath("$.data[1].id").value("1224"))
        .andExpect(jsonPath("$.data[1].name").value("Excalibur"))
    ;

    verify(this.artifactService, times(1)).findAll();
  }

  @Test
  void testAddArtifactSuccess() throws Exception {

    // given
    // in client create only name, description, image_url
    ArtifactDto artifactDto = new ArtifactDto(null, "From Contoller", "Descrp...", "url...", null);
    String json = this.objectMapper.writeValueAsString(artifactDto);

    System.out.println(json);

    Artifact artifact = new Artifact("112", "From Contoller", "Descrp...", "url...");

    given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(artifact);

    // when and then
    this.mockMvc.perform(
            post("/api/v1/artifacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("add success"))
        .andExpect(jsonPath("$.data.id").isNotEmpty())
        .andExpect(jsonPath("$.data.name").value(artifact.getName()))
        .andExpect(jsonPath("$.data.owner").isEmpty())

    ;

    verify(this.artifactService, times(1)).save(Mockito.any(Artifact.class));

  }

  @Test
  void testUpdateArtifactSuccess() throws Exception {
    // given
    // in client create only name, description, image_url
    // from client side the front end
    ArtifactDto artifactDto = new ArtifactDto(
        "112",
        "New Name",
        "new Desc",
        "new image",
        null
    );

    String json = this.objectMapper.writeValueAsString(artifactDto);

    System.out.println(json);

    // this is return from the service
    Artifact updateArtifact = new Artifact(
        "112",
        "New Name",
        "new Desc",
        "new image"
    );

    given(this.artifactService.update(eq("112"), Mockito.any(Artifact.class))).willReturn(updateArtifact);

    // when and then
    this.mockMvc.perform(
            put("/api/v1/artifacts/112")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("update success"))
        .andExpect(jsonPath("$.data.id").value("112"))
        .andExpect(jsonPath("$.data.name").value(updateArtifact.getName()))
        .andExpect(jsonPath("$.data.description").value(updateArtifact.getDescription()))
        .andExpect(jsonPath("$.data.owner").isEmpty())

    ;

    verify(this.artifactService, times(1)).update(eq("112"), Mockito.any(Artifact.class));
  }

  @Test
  void testUpdateArtifactErrorWithNonExistingId() throws Exception {
    // given
    // in client create only name, description, image_url
    // from client side the front end
    ArtifactDto artifactDto = new ArtifactDto(
        "112",
        "New Name",
        "new Desc",
        "new image",
        null
    );

    String json = this.objectMapper.writeValueAsString(artifactDto);

    System.out.println(json);

    // this is return from the service

    given(this.artifactService.update(eq("112"), Mockito.any(Artifact.class)))
        .willThrow(new ArtifactNotFoundException("112"));

    // when and then
    this.mockMvc.perform(
            put("/api/v1/artifacts/112")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found artifact with id: 112"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    verify(this.artifactService, times(1)).update(eq("112"), Mockito.any(Artifact.class));
  }

  @Test
  void testDeleteArtifactSuccess() throws Exception {
    // given
    doNothing().when(this.artifactService).delete("1");

    // when and then
    this.mockMvc.perform(
            delete("/api/v1/artifacts/1")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.message").value("delete success"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    verify(this.artifactService, times(1)).delete(anyString());
  }

  @Test
  void testDeleteArtifactNotFound() throws Exception {
    // given
    doThrow(new ArtifactNotFoundException("1"))
        .when(this.artifactService).delete("1");

    // when and then
    this.mockMvc.perform(
            delete("/api/v1/artifacts/1")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("could not found artifact with id: 1"))
        .andExpect(jsonPath("$.data").isEmpty())
    ;

    verify(this.artifactService, times(1)).delete("1");
  }
}