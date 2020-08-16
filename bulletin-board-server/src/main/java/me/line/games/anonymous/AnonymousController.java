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

import me.line.games.common.domain.CommonResponse;
import me.line.games.common.domain.SearchCondition;

@RestController
public class AnonymousController {
	private AnonymousService anonymousService;

	public AnonymousController(AnonymousService anonymousService) {
		this.anonymousService = anonymousService;
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<AnonymousPostDetail> getPost(@PathVariable String id) {
		// TODO find 라는 메서드의 결과가 0일때 처리
		AnonymousPostDetail post = anonymousService.find(id);
		return new ResponseEntity<AnonymousPostDetail>(post, HttpStatus.OK);
	}

	@GetMapping("/posts")
	public ResponseEntity<List<AnonymousPost>> getPosts(@RequestParam String searchType,
			@RequestParam String searchText, @RequestParam int page, @RequestParam int row) {

		SearchCondition condition = new SearchCondition(searchType, searchText, page, row);

		// TODO find 라는 메서드의 결과가 0일때 처리
		List<AnonymousPost> post = anonymousService.findAll(condition);
		return new ResponseEntity<List<AnonymousPost>>(post, HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<CommonResponse> save(NewPostRequest request) {
		// TODO request to vo 전환
		AnonymousPost post = new AnonymousPost();

		// TODO insert 결과가 0이면 exception 처리
		anonymousService.save(post);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> modify(ModifyPostRequest request) {
		// TODO request to vo 전환
		AnonymousPost post = new AnonymousPost();

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
