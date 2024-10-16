package franxx.code.artifacts.artifact;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
}