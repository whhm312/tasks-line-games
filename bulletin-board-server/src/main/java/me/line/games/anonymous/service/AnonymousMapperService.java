package me.line.games.anonymous.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import me.line.games.anonymous.vo.CommentResponse;
import me.line.games.anonymous.vo.CommentsResponse;
import me.line.games.anonymous.vo.ModifyCommentRequest;
import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewCommentRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.PostCommentsResponse;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.anonymous.vo.SubCommentResponse;
import me.line.games.common.domain.Comment;
import me.line.games.common.domain.CommonComment;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.domain.SubComment;

@Mapper(componentModel = "spring")
public interface AnonymousMapperService {

	Post newRequestToPost(NewPostRequest request);

	default PostsResponse postsToResponse(List<Post> posts, int totalCount) {
		PostsResponse result = new PostsResponse();
		List<PostResponse> postsResponse = new ArrayList<>();
		for (Post post : posts) {
			postsResponse.add(postToResponse(post));
		}
		result.setPosts(postsResponse);
		result.setTotalCount(totalCount);
		return result;
	}

	@Mappings({ 
		@Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm", defaultValue = "") 
	})
	PostResponse postToResponse(Post post);

	@Mappings({ 
		@Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm", defaultValue = ""),
		@Mapping(target = "lastUpdateDate", source = "post.lastUpdateDate", dateFormat = "yyyy-MM-dd HH:mm", defaultValue = "")
	})
	PostDetailResponse postDetailToResponse(PostDetail post);

	Post modifyRequestToPost(ModifyPostRequest request);

	CommonComment newRequestToComment(NewCommentRequest request);

	Comment modifyRequestToComment(ModifyCommentRequest request);

	default CommentsResponse commentsToResponse(Comment comment, int totalCount) {
		CommentsResponse result = new CommentsResponse();
		result.setComment(commentToResponse(comment));
		result.setTotalCount(totalCount);
		return result;
	}

	default CommentResponse commentToResponse(Comment comment) {
		CommentResponse result = new CommentResponse();

		List<SubCommentResponse> subComments = new ArrayList<>();
		if (comment.getSubComments() != null) {
			for (SubComment subComment : comment.getSubComments()) {
				subComments.add(subCommentToResponse(subComment));
			}
		}
		result.setSubComments(subComments);
		
		if ("Y".equals(comment.getDeleteYn())) {
			result.deleted();
			return result;
		}

		result.setSeq(comment.getSeq());
		result.setPostSeq(comment.getPostSeq());
		result.setNickName(comment.getNickName());
		result.setContent(comment.getContent());
		result.setDeleteYn(comment.getDeleteYn());
		result.setRegisterDate("");
		result.setLastUpdateDate("");

		if (comment.getRegisterDate() != null) {
			result.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getRegisterDate()));
		}

		if (comment.getLastUpdateDate() != null) {
			result.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getLastUpdateDate()));
		}

		return result;
	}

	default SubCommentResponse subCommentToResponse(SubComment comment) {
		SubCommentResponse result = new SubCommentResponse();
		if ("Y".equals(comment.getDeleteYn())) {
			result.deleted();
			return result;
		}

		result.setSeq(comment.getSeq());
		result.setPostSeq(comment.getPostSeq());
		result.setNickName(comment.getNickName());
		result.setContent(comment.getContent());
		result.setDeleteYn(comment.getDeleteYn());
		result.setRegisterDate("");
		result.setLastUpdateDate("");

		if (comment.getRegisterDate() != null) {
			result.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getRegisterDate()));
		}
		if (comment.getLastUpdateDate() != null) {
			result.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getLastUpdateDate()));
		}

		return result;
	}

	default PostCommentsResponse postCommentsToResponse(List<Comment> comments, int totalCount) {
		PostCommentsResponse result = new PostCommentsResponse();
		List<CommentResponse> commentResponse = new ArrayList<>();
		for (Comment comment : comments) {
			commentResponse.add(commentToResponse(comment));
		}
		result.setComments(commentResponse);
		result.setTotalCount(totalCount);
		return result;
	}

}
