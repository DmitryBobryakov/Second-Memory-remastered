package mipt.app.secondmemory;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletResponse;
import mipt.app.secondmemory.dto.user.UserDto;
import mipt.app.secondmemory.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecondMemoryApplicationTest {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;
  private HttpServletResponse response;

  @Test
  void endToEndTest() {
    // Step 1: add new user
    User mockUser = new User("boris1906@gmail.com", "Boris", "Boris123");
    ResponseEntity<UserDto> registrationResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/signup", mockUser, UserDto.class);

    assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
    assertEquals(mockUser.getEmail(), registrationResponse.getBody().email());

    // Step 2: Complete authentication
    ResponseEntity<String> authenticationResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/signin", mockUser, String.class, response);

    assertEquals(HttpStatus.OK, authenticationResponse.getStatusCode());

    // Step 3: Log out
    ResponseEntity<String> logOutResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/logout", "1", String.class, response);

    assertEquals(HttpStatus.OK, logOutResponse.getStatusCode());
  }
}
