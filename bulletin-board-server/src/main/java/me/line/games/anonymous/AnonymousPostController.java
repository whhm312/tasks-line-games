package me.line.games.anonymous;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.NewPostResponse;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.vo.CommonResponse;
import me.line.games.common.vo.SearchCondition;

@RestController
public class AnonymousPostController {
	private AnonymousPostService anonymousService;

	public AnonymousPostController(AnonymousPostService anonymousService) {
		this.anonymousService = anonymousService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/posts/{id}")
	public ResponseEntity<PostDetailResponse> getPost(@PathVariable String id) {
		// TODO find 라는 메서드의 결과가 0일때 처리
		PostDetail post = anonymousService.find(id);

		// TODO @SuppressWarnings 지우기
		// TODO MapStruct 적용하기
		PostDetailResponse response = new PostDetailResponse();
		return new ResponseEntity<PostDetailResponse>(response, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	@GetMapping("/posts")
	public ResponseEntity<PostsResponse> getPosts(@RequestParam String searchType, @RequestParam String searchText,
			@RequestParam int page, @RequestParam int row) {

		SearchCondition condition = new SearchCondition(searchType, searchText, page, row);

		// TODO @SuppressWarnings 지우기
		// TODO find 라는 메서드의 결과가 0일때 처리
		List<Post> posts = anonymousService.findAll(condition);

		// TODO MapStruct 적용하기
		PostsResponse response = new PostsResponse();
		return new ResponseEntity<PostsResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<NewPostResponse> save(NewPostRequest request) {
		// TODO request to vo 전환
		Post post = new Post();

		// TODO insert 결과가 0이면 exception 처리
		anonymousService.save(post);

		NewPostResponse body = new NewPostResponse();
		body.setSuccess();
		return new ResponseEntity<NewPostResponse>(body, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> modify(ModifyPostRequest request) {
		// TODO request to vo 전환
		Post post = new Post();

		// TODO update 결과가 0이면 exception 처리
		anonymousService.modify(post);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> delete(@PathVariable String id) {
		// TODO update 결과가 0이면 exception 처리
		anonymousService.delete(id);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}
}
