package me.line.games.anonymous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.util.UriComponentsBuilder;

import me.line.games.anonymous.vo.CommentResponse;
import me.line.games.anonymous.vo.CommentsResponse;
import me.line.games.anonymous.vo.CreatedCommonResponse;
import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewCommentRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.anonymous.vo.SubCommentResponse;
import me.line.games.common.domain.Comment;
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
	private static Comment mockComment;

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

		mockComment = new Comment();
		mockComment.setContent(content);
		mockComment.setNickName(nickName);
	}

	@Test
	@Order(1)
	public void savePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts");
		NewPostRequest request = new NewPostRequest();
		request.setNickName(mockPost.getNickName());
		request.setTitle(mockPost.getTitle());
		request.setContent(mockPost.getContent());

		ResponseEntity<CreatedCommonResponse> saveResponse = template.postForEntity(base.toString(), request, CreatedCommonResponse.class);

		CreatedCommonResponse saveResult = new CreatedCommonResponse(saveResponse.getBody().getSeq());

		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertEquals(saveResult.getCode(), saveResponse.getBody().getCode());
		assertEquals(saveResult.getMessage(), saveResponse.getBody().getMessage());
		assertTrue(0 < saveResponse.getBody().getSeq());

		mockPost.setSeq(saveResponse.getBody().getSeq());
	}

	@Test
	@Order(2)
	public void getPosts() throws Exception {
		URI base = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path("/posts").queryParam("searchType", "").queryParam("searchText", "")
				.queryParam("page", "1").queryParam("row", "10").build().toUri();
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
		template.put(base.toString(), request);

		mockPost.setTitle(title);
		mockPost.setContent(content);

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockPost.getTitle(), response.getBody().getTitle());
		assertEquals(mockPost.getContent(), response.getBody().getContent());

		mockPost.setLastUpdateDate(sdf.parse(response.getBody().getLastUpdateDate()));
	}

	@Disabled
	@Test
	@Order(5)
	public void deletePost() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		template.delete(base.toString());

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq());
		ResponseEntity<PostDetailResponse> response = template.getForEntity(base.toString(), PostDetailResponse.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	@Order(6)
	public void saveComment() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments");
		NewCommentRequest request = new NewCommentRequest();
		request.setContent(mockComment.getContent());
		request.setNickName(mockComment.getNickName());

		ResponseEntity<CreatedCommonResponse> saveResponse = template.postForEntity(base.toString(), request, CreatedCommonResponse.class);
		CreatedCommonResponse saveResult = new CreatedCommonResponse(saveResponse.getBody().getSeq());

		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertEquals(saveResult.getCode(), saveResponse.getBody().getCode());
		assertEquals(saveResult.getMessage(), saveResponse.getBody().getMessage());
		assertTrue(0 < saveResponse.getBody().getSeq());

		mockComment.setSeq(saveResponse.getBody().getSeq());

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		ResponseEntity<CommentsResponse> commentResponse = template.getForEntity(base.toString(), CommentsResponse.class);

		assertEquals(HttpStatus.OK, commentResponse.getStatusCode());
		assertEquals(1, commentResponse.getBody().getTotalCount());
		CommentResponse cr = commentResponse.getBody().getComment();
		assertEquals(mockComment.getNickName(), cr.getNickName());
		assertEquals(mockComment.getContent(), cr.getContent());
		assertEquals(mockComment.getSeq(), cr.getSeq());

		URI uriBase = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path("/posts").queryParam("searchType", "").queryParam("searchText", "")
				.queryParam("page", "1").queryParam("row", "10").build().toUri();
		ResponseEntity<PostsResponse> listResponse = template.getForEntity(uriBase.toString(), PostsResponse.class);
		for (PostResponse postResponse : listResponse.getBody().getPosts()) {
			if (mockPost.getSeq() == postResponse.getSeq()) {
				assertEquals(1, postResponse.getCommentCount());
			}
		}
	}

	@Test
	@Order(7)
	public void saveSubComment() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		NewCommentRequest request = new NewCommentRequest();
		String content = "[John] 추댓 답니다.";
		String nickName = "Maria";
		request.setContent(content);
		request.setNickName(nickName);

		ResponseEntity<CreatedCommonResponse> saveResponse = template.postForEntity(base.toString(), request, CreatedCommonResponse.class);
		CreatedCommonResponse saveResult = new CreatedCommonResponse(saveResponse.getBody().getSeq());

		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertEquals(saveResult.getCode(), saveResponse.getBody().getCode());
		assertEquals(saveResult.getMessage(), saveResponse.getBody().getMessage());
		assertTrue(0 < saveResponse.getBody().getSeq());

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		ResponseEntity<CommentsResponse> commentResponse = template.getForEntity(base.toString(), CommentsResponse.class);
		assertEquals(HttpStatus.OK, commentResponse.getStatusCode());
		assertEquals(2, commentResponse.getBody().getTotalCount());

		CommentResponse cr = commentResponse.getBody().getComment();
		assertEquals(mockComment.getNickName(), cr.getNickName());
		assertEquals(mockComment.getContent(), cr.getContent());
		assertEquals(mockComment.getSeq(), cr.getSeq());

		List<SubCommentResponse> subComment = cr.getSubComments();
		for (SubCommentResponse subCommentResponse : subComment) {
			assertEquals(nickName, subCommentResponse.getNickName());
			assertEquals(content, subCommentResponse.getContent());
		}
	}

	@Test
	@Order(8)
	public void modifyComment() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		String content = "코멘트 내용 수정!";

		ModifyPostRequest request = new ModifyPostRequest();
		request.setContent(content);
		template.put(base.toString(), request);

		mockComment.setContent(content);

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		ResponseEntity<CommentsResponse> commentResponse = template.getForEntity(base.toString(), CommentsResponse.class);
		assertEquals(HttpStatus.OK, commentResponse.getStatusCode());
		assertEquals(2, commentResponse.getBody().getTotalCount());
		CommentResponse cr = commentResponse.getBody().getComment();
		assertEquals(mockComment.getContent(), cr.getContent());
	}

	@Test
	@Order(9)
	public void deleteComment() throws Exception {
		URL base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		template.delete(base.toString());

		base = new URL("http://localhost:" + port + "/posts/" + mockPost.getSeq() + "/comments/" + mockComment.getSeq());
		ResponseEntity<CommentsResponse> commentResponse = template.getForEntity(base.toString(), CommentsResponse.class);
		assertEquals(2, commentResponse.getBody().getTotalCount());
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
