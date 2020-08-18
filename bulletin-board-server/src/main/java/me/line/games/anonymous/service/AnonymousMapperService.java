package me.line.games.anonymous.service;

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

	@Mappings({ @Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm") })
	PostResponse postToResponse(Post post);

	@Mappings({ @Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm"),
			@Mapping(target = "lastUpdateDate", source = "post.lastUpdateDate", dateFormat = "yyyy-MM-dd HH:mm") })
	PostDetailResponse postDetailToResponse(PostDetail post);

	Post modifyRequestToPost(ModifyPostRequest request);

	CommonComment newRequestToComment(NewCommentRequest request);

	Comment modifyRequestToComment(ModifyCommentRequest request);

	default CommentsResponse commentsToResponse(Comment comment, int totalCount) {
		CommentsResponse result = new CommentsResponse();

		List<SubCommentResponse> subCommentsResponse = new ArrayList<>();
		for (SubComment subComment : comment.getSubComments()) {
			subCommentsResponse.add(subCommentToResponse(subComment));
		}

		CommentResponse commentResponse = commentToResponse(comment);
		commentResponse.setSubComment(subCommentsResponse);

		result.setComment(commentResponse);
		result.setTotalCount(totalCount);
		return result;
	}

	CommentResponse commentToResponse(Comment comment);

	SubCommentResponse subCommentToResponse(SubComment comment);

}
