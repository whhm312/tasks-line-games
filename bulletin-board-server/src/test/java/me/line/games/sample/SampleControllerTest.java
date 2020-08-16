package me.line.games.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import me.line.games.sample.vo.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

	@Test
	public void getUser() throws Exception {
		URL base = new URL("http://localhost:" + port + "/users/2");
		ResponseEntity<User> response = template.getForEntity(base.toString(), User.class);
		User user = getMockData();

		assertEquals(user.getId(), response.getBody().getId());
		assertEquals(user.getLast_name(), response.getBody().getLast_name());
		assertEquals(user.getFirst_name(), response.getBody().getFirst_name());
		assertEquals(user.getEmail(), response.getBody().getEmail());
	}

	private User getMockData() {
		User user = new User();
		user.setId(2);
		user.setLast_name("Vu");
		user.setFirst_name("Deja");
		user.setEmail("xyz@email.com");
		return user;
	}
}
