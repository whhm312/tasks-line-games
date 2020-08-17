package me.line.games.anonymous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.NewPostResponse;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.common.domain.Post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnonymousPostControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

	private static Post mockPost;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@BeforeAll
	public void before() {
		String nickName = "John";
		String title = "제목입니다.";
		String content = "내용입니다.";

		mockPost = new Post();
		mockPost.setNickName(nickName);
		mockPost.setTitle(title);
		mockPost.setContent(content);
	}

	@Test
	@Order(1)
	public void savePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts");
		NewPostRequest request = createMockPost(mockPost);

		ResponseEntity<NewPostResponse> saveResponse = template.postForEntity(base.toString(), request,
				NewPostResponse.class);
		NewPostResponse saveResult = new NewPostResponse();
		saveResult.setSuccess();

		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertEquals(saveResult.getCode(), saveResponse.getBody().getCode());
		assertEquals(saveResult.getMessage(), saveResponse.getBody().getMessage());
		assertTrue(0 < saveResponse.getBody().getSeq());

		mockPost.setSeq(saveResponse.getBody().getSeq());
	}

	private NewPostRequest createMockPost(Post mockPost) {
		NewPostRequest request = new NewPostRequest();
		request.setNickName(mockPost.getNickName());
		request.setTitle(mockPost.getTitle());
		request.setContent(mockPost.getContent());
		return request;
	}

	@Test
	@Order(2)
	public void getPosts() throws Exception {
		URI base = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path("/posts")
				.queryParam("searchType", "").queryParam("searchText", "").queryParam("page", "1")
				.queryParam("row", "10").build().toUri();
		ResponseEntity<PostsResponse> response = template.getForEntity(base.toString(), PostsResponse.class);

		assertTrue(0 < response.getBody().getTotalCount());
		assertTrue(0 < response.getBody().getPosts().size());
		assertTrue(!StringUtils.isEmpty(response.getBody().getPosts().get(0).getNickName()));
		assertTrue(!StringUtils.isEmpty(response.getBody().getPosts().get(0).getTitle()));
		assertTrue(!StringUtils.isEmpty(response.getBody().getPosts().get(0).getRegisterDate()));
		assertTrue(0 < response.getBody().getPosts().get(0).getSeq());
		assertTrue(0 <= response.getBody().getPosts().get(0).getHit());
		assertTrue(validationDate(response.getBody().getPosts().get(0).getRegisterDate()));
	}

	@Test
	@Order(3)
	public void getPost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockPost.getSeq(), response.getBody().getSeq());
		assertEquals(mockPost.getNickName(), response.getBody().getNickName());
		assertEquals(mockPost.getTitle(), response.getBody().getTitle());
		assertEquals(mockPost.getContent(), response.getBody().getContent());
		assertTrue(null == response.getBody().getComments());
		assertEquals(1, response.getBody().getHit());

		assertTrue(validationDate(response.getBody().getRegisterDate()));
		assertTrue(StringUtils.isEmpty(response.getBody().getLastUpdateDate()));

		mockPost.setLastUpdateDate(sdf.parse(response.getBody().getRegisterDate()));
	}

	@Test
	@Order(4)
	public void modifyPost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		String content = "변경합니다.";
		String title = "제목 변경!";

		ModifyPostRequest request = new ModifyPostRequest();
		request.setContent(content);
		request.setTitle(title);
		try {
			template.put(base.toString(), request);

			mockPost.setTitle(title);
			mockPost.setContent(content);
		} catch (RestClientException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	@Order(5)
	public void getPostAfterModifyPost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockPost.getSeq(), response.getBody().getSeq());
		assertEquals(mockPost.getNickName(), response.getBody().getNickName());
		assertEquals(mockPost.getTitle(), response.getBody().getTitle());
		assertEquals(mockPost.getContent(), response.getBody().getContent());
		assertEquals(2, response.getBody().getHit());
		assertTrue(validationDate(response.getBody().getRegisterDate()));
		assertTrue(validationDate(response.getBody().getLastUpdateDate()));
		assertTrue(!StringUtils.isEmpty(response.getBody().getLastUpdateDate()));

		mockPost.setLastUpdateDate(sdf.parse(response.getBody().getLastUpdateDate()));
	}

	@Test
	@Order(6)
	public void deletePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		try {
			template.delete(base.toString());
		} catch (RestClientException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	@Order(7)
	public void getPostAfterDeletePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		try {
			ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(),
					PostDetailResponse.class);
			assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		} catch (RestClientException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(8)
	public void noContent() throws Exception {
		URI base = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path("/posts")
				.queryParam("searchType", "").queryParam("searchText", "").queryParam("page", "1")
				.queryParam("row", "10").build().toUri();
		ResponseEntity<PostsResponse> response = template.getForEntity(base.toString(), PostsResponse.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	public boolean validationDate(String checkDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateFormat.setLenient(false);
			dateFormat.parse(checkDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
