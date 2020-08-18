package me.line.games.anonymous;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import me.line.games.anonymous.service.AnonymousMapperService;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.anonymous.vo.PostResponse;
import me.line.games.anonymous.vo.PostsResponse;
import me.line.games.common.domain.Post;

class AnonymousMapperTest {
	private AnonymousMapperService mapper = Mappers.getMapper(AnonymousMapperService.class);

	@Test
	void testPostsToResponse() {
		String nickName = "별명";
		String title = "제목입니다.";
		int commentCount = 1;

		List<Post> posts = new ArrayList<>();
		Post post = new Post();
		post.setCommentCount(commentCount);
		post.setTitle(title);
		post.setNickName(nickName);
		posts.add(post);

		int totalCount = 1;
		PostsResponse answer = mapper.postsToResponse(posts, totalCount);
		PostResponse postResponse = answer.getPosts().get(0);
		assertEquals(nickName, postResponse.getNickName());
		assertEquals(title, postResponse.getTitle());
		assertEquals(commentCount, postResponse.getCommentCount());
	}

	@Test
	void testRequestToPost() {
		String content = "내용입니다.";
		String nickName = "별명";
		String title = "제목입니다.";

		NewPostRequest request = new NewPostRequest();
		request.setContent(content);
		request.setNickName(nickName);
		request.setTitle(title);

		Post post = mapper.newRequestToPost(request);
		assertEquals(content, post.getContent());
		assertEquals(nickName, post.getNickName());
		assertEquals(title, post.getTitle());
	}

}
