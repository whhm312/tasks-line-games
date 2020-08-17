package me.line.games.anonymous;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.line.games.anonymous.service.AnonymousMapperService;
import me.line.games.anonymous.service.AnonymousPostService;
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
	private AnonymousMapperService anonymousMapper;
	private String userId = "test123";

	public AnonymousPostController(AnonymousPostService anonymousService, AnonymousMapperService anonymousMapper) {
		this.anonymousService = anonymousService;
		this.anonymousMapper = anonymousMapper;
	}

	@PostMapping("/posts")
	public ResponseEntity<NewPostResponse> save(@RequestBody NewPostRequest request) {
		Post post = anonymousMapper.newRequestToPost(request);
		// TODO Login id 셋팅하기
		post.setUserId(userId);

		int seq = anonymousService.save(post);

		NewPostResponse body = new NewPostResponse();
		body.setSuccess();
		body.setSeq(seq);

		return new ResponseEntity<NewPostResponse>(body, HttpStatus.CREATED);
	}

	@GetMapping("/posts")
	public ResponseEntity<PostsResponse> getPosts(@RequestParam String searchType, @RequestParam String searchText,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int row) {

		SearchCondition condition = new SearchCondition(searchType, searchText, page, row);

		List<Post> posts = anonymousService.findAll(condition);
		int totalCount = anonymousService.findAllCount(condition);

		PostsResponse response = anonymousMapper.postsToResponse(posts, totalCount);
		return new ResponseEntity<PostsResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<PostDetailResponse> getPost(@PathVariable String id) {
		PostDetail post = anonymousService.find(id);

		PostDetailResponse response = anonymousMapper.postDetailToResponse(post);
		return new ResponseEntity<PostDetailResponse>(response, HttpStatus.OK);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> modify(@PathVariable String id, @RequestBody ModifyPostRequest request) {
		Post post = anonymousMapper.modifyRequestToPost(request);
		// TODO Login id 셋팅하기
		post.setUserId(userId);
		post.setSeq(Integer.parseInt(id));

		anonymousService.modify(post);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> delete(@PathVariable String id) {
		// TODO Login id 셋팅하기
		anonymousService.delete(userId, id);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}
}
