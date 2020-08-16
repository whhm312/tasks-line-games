package me.line.games.anonymous;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import me.line.games.anonymous.vo.CommentResponse;
import me.line.games.anonymous.vo.ModifyCommentRequest;
import me.line.games.anonymous.vo.NewCommentRequest;
import me.line.games.common.domain.Comment;
import me.line.games.common.vo.CommonResponse;

@RestController
public class AnonymousCommentController {
	private AnonymousCommentService anonymousService;

	public AnonymousCommentController(AnonymousCommentService anonymousService) {
		this.anonymousService = anonymousService;
	}

	@SuppressWarnings("unused")
	@GetMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<List<CommentResponse>> getComment(@PathVariable String id, @PathVariable String commentId) {
		List<Comment> comments = anonymousService.find(id, commentId);

		// TODO @SuppressWarnings 지우기
		// TODO MapStruct 적용하기
		List<CommentResponse> response = new ArrayList<>();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	@GetMapping("/posts/{id}/comments")
	public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String id) {
		List<Comment> comments = anonymousService.findAll(id);

		// TODO @SuppressWarnings 지우기
		// TODO MapStruct 적용하기
		List<CommentResponse> response = new ArrayList<>();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/posts/{id}/comments")
	public ResponseEntity<CommonResponse> save(NewCommentRequest request) {
		// TODO request to vo 전환
		Comment comment = new Comment();

		// TODO insert 결과가 0이면 exception 처리
		anonymousService.save(comment);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}/comments/{commentId}")
	public ResponseEntity<CommonResponse> modify(ModifyCommentRequest request) {
		// TODO request to vo 전환
		Comment comment = new Comment();

		// TODO update 결과가 0이면 exception 처리
		anonymousService.modify(comment);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommonResponse> delete(@PathVariable String postId, @PathVariable String commentId) {
		// TODO update 결과가 0이면 exception 처리
		anonymousService.delete(postId, commentId);

		CommonResponse body = new CommonResponse();
		body.setSuccess();
		return new ResponseEntity<CommonResponse>(body, HttpStatus.OK);
	}
}
