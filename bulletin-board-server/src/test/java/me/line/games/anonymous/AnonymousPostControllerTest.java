package me.line.games.anonymous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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

		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertEquals(saveResult.getCode(), saveResponse.getBody().getCode());
		assertEquals(saveResult.getMessage(), saveResponse.getBody().getMessage());

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
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("searchType", "");
		parameters.put("searchText", "");
		parameters.put("page", 1);
		parameters.put("row", 10);
		ResponseEntity<PostsResponse> response = template.getForEntity(base.toString(), PostsResponse.class,
				parameters);

		assertEquals(1, response.getBody().getTotalCount());
		assertEquals(1, response.getBody().getPosts().size());
		assertEquals(0, response.getBody().getPosts().get(0).getHit());
		assertEquals(mockPost.getNickName(), response.getBody().getPosts().get(0).getNickName());
		assertEquals(mockPost.getTitle(), response.getBody().getPosts().get(0).getTitle());
		assertEquals(mockPost.getSeq(), response.getBody().getPosts().get(0).getSeq());
		assertEquals(0, response.getBody().getPosts().get(0).getHit());
		assertTrue(!"".equals(response.getBody().getPosts().get(0).getRegisterDate()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mockPost.setRegisterDate(sdf.parse(response.getBody().getPosts().get(0).getRegisterDate()));
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
		assertEquals(1, response.getBody().getHit());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		assertEquals(sdf.format(mockPost.getRegisterDate()), response.getBody().getRegisterDate());
		assertTrue("".equals(response.getBody().getLastUpdateDate()));
	}

	@Test
	@Order(4)
	public void modifyPost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ModifyPostRequest request = new ModifyPostRequest();
		template.put(base.toString(), request);
	}

	@Test
	@Order(5)
	public void getPostAfterModifyPost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockPost.getSeq(), response.getBody().getSeq());
		assertEquals(mockPost.getNickName(), response.getBody().getNickName());
		assertEquals(mockPost.getTitle(), response.getBody().getTitle());
		assertEquals(mockPost.getContent(), response.getBody().getContent());
		assertEquals(2, response.getBody().getHit());
		assertEquals(sdf.format(mockPost.getRegisterDate()), response.getBody().getRegisterDate());
		assertTrue(!"".equals(response.getBody().getLastUpdateDate()));

		mockPost.setLastUpdateDate(sdf.parse(response.getBody().getLastUpdateDate()));
	}

	@Test
	@Order(6)
	public void deletePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		template.delete(base.toString());
	}

	@Test
	@Order(7)
	public void getPostAfterDeletePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
