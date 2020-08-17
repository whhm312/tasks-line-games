package me.line.games.anonymous.service;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import me.line.games.anonymous.vo.ModifyPostRequest;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.PostDetailResponse;
import me.line.games.anonymous.vo.PostResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;

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
		@Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm")
	})
	PostResponse postToResponse(Post post);

	@Mappings({ 
		@Mapping(target = "registerDate", source = "post.registerDate", dateFormat = "yyyy-MM-dd HH:mm"),
		@Mapping(target = "lastUpdateDate", source = "post.lastUpdateDate", dateFormat = "yyyy-MM-dd HH:mm")
	})
	PostDetailResponse postDetailToResponse(PostDetail post);

	Post modifyRequestToPost(ModifyPostRequest request);

}
