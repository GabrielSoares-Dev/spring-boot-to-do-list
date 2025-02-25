package spring_boot_to_do_list.spring_boot_to_do_list.integration.http.taskController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CreateTaskIntegrationTest {
  @Autowired private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private String path = "/v1/tasks";

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @Sql(value = "classpath:reset-tasks.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void testCreated() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("title", "test-title");
    input.put("description", "test-description");

    String inputJson = objectMapper.writeValueAsString(input);
    ResultActions output =
        this.mockMvc.perform(
            post(this.path).contentType(MediaType.APPLICATION_JSON).content(inputJson));

    output.andExpect(status().isCreated());
    output.andExpect(jsonPath("$.message").value("Task created successfully"));
  }

  @Test
  public void testEmptyFields() throws Exception {
    Map<String, String> input = new HashMap<>();
    input.put("title", null);
    input.put("description", null);

    String inputJson = objectMapper.writeValueAsString(input);
    ResultActions output =
        this.mockMvc.perform(
            post(this.path).contentType(MediaType.APPLICATION_JSON).content(inputJson));

    output.andExpect(status().isBadRequest());
  }
}
