package me.line.games.anonymous;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

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
import me.line.games.anonymous.vo.CommentsResponse;
import me.line.games.anonymous.vo.CreatedCommonResponse;
import me.line.games.anonymous.vo.ModifyCommentRequest;
import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewCommentRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.PostCommentsResponse;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.common.domain.Comment;
import me.line.games.common.domain.CommonComment;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.vo.CommonResponse;
import me.line.games.common.vo.SearchCondition;

@RestController
public class AnonymousPostController {
	private AnonymousPostService anonymousService;
	private AnonymousMapperService anonymousMapper;

	public AnonymousPostController(AnonymousPostService anonymousService, AnonymousMapperService anonymousMapper) {
		this.anonymousService = anonymousService;
		this.anonymousMapper = anonymousMapper;
	}

	@PostMapping("/posts")
	public ResponseEntity<CreatedCommonResponse> save(@Valid @RequestBody NewPostRequest request, Principal principal) {
		Post post = anonymousMapper.newRequestToPost(request);
		post.setUserId(principal.getName());

		int seq = anonymousService.save(post);

		CreatedCommonResponse body = new CreatedCommonResponse(seq);
		return new ResponseEntity<CreatedCommonResponse>(body, HttpStatus.CREATED);
	}

	@GetMapping("/posts")
	public ResponseEntity<PostsResponse> getPosts(@RequestParam String searchType, @RequestParam String searchText, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int row) {

		SearchCondition condition = new SearchCondition(searchType, searchText, page, row);

		List<Post> posts = anonymousService.findAll(condition);
		int totalCount = anonymousService.findAllCount(condition);

		PostsResponse response = anonymousMapper.postsToResponse(posts, totalCount);
		return new ResponseEntity<PostsResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<PostDetailResponse> getPost(@PathVariable(name = "id") int postSeq) {
		PostDetail post = anonymousService.find(postSeq);

		PostDetailResponse response = anonymousMapper.postDetailToResponse(post);
		return new ResponseEntity<PostDetailResponse>(response, HttpStatus.OK);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> modify(@PathVariable(name = "id") int postSeq, @Valid @RequestBody ModifyPostRequest request, Principal principal) {
		Post post = anonymousMapper.modifyRequestToPost(request);
		post.setUserId(principal.getName());
		post.setSeq(postSeq);

		anonymousService.modify(post);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<CommonResponse> delete(@PathVariable(name = "id") int postSeq, Principal principal) {
		anonymousService.delete(principal.getName(), postSeq);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@PostMapping("/posts/{id}/comments")
	public ResponseEntity<CreatedCommonResponse> saveComment(@PathVariable(name = "id") int postSeq, @Valid @RequestBody NewCommentRequest request,
			Principal principal) {
		CommonComment comment = anonymousMapper.newRequestToComment(request);
		comment.setUserId(principal.getName());
		comment.setPostSeq(postSeq);

		int seq = anonymousService.save(comment);

		CreatedCommonResponse body = new CreatedCommonResponse(seq);
		return new ResponseEntity<CreatedCommonResponse>(body, HttpStatus.CREATED);
	}

	@PostMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<CreatedCommonResponse> saveSubComment(@PathVariable(name = "id") int postSeq, @PathVariable(name = "commentId") int commentSeq,
			@Valid @RequestBody NewCommentRequest request, Principal principal) {
		CommonComment comment = anonymousMapper.newRequestToComment(request);
		comment.setUserId(principal.getName());
		comment.setPostSeq(postSeq);
		comment.setSeq(commentSeq);

		int seq = anonymousService.save(comment);

		CreatedCommonResponse body = new CreatedCommonResponse(seq);
		return new ResponseEntity<CreatedCommonResponse>(body, HttpStatus.CREATED);
	}

	@GetMapping("/posts/{id}/comments")
	public ResponseEntity<PostCommentsResponse> getComments(@PathVariable(name = "id") int postSeq) {
		List<Comment> comments = anonymousService.getComments(postSeq);
		int totalCount = anonymousService.getCommentsCount(postSeq);

		PostCommentsResponse body = anonymousMapper.postCommentsToResponse(comments, totalCount);
		return new ResponseEntity<PostCommentsResponse>(body, HttpStatus.OK);
	}

	@GetMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<CommentsResponse> getComment(@PathVariable(name = "id") int postSeq, @PathVariable(name = "commentId") int commentSeq) {
		Comment comments = anonymousService.getComment(postSeq, commentSeq);
		int totalCount = anonymousService.getCommentCount(postSeq, commentSeq);
		CommentsResponse body = anonymousMapper.commentsToResponse(comments, totalCount);
		return new ResponseEntity<CommentsResponse>(body, HttpStatus.OK);
	}

	@PutMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<CommonResponse> modify(@PathVariable(name = "id") int postSeq, @PathVariable(name = "commentId") int commentSeq,
			@Valid @RequestBody ModifyCommentRequest request, Principal principal) {
		Comment comment = anonymousMapper.modifyRequestToComment(request);
		comment.setUserId(principal.getName());
		comment.setPostSeq(postSeq);
		comment.setSeq(commentSeq);

		anonymousService.modify(comment);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<CommonResponse> delete(@PathVariable(name = "id") int postSeq, @PathVariable(name = "commentId") int commentSeq,
			Principal principal) {
		anonymousService.delete(principal.getName(), postSeq, commentSeq);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}
}
