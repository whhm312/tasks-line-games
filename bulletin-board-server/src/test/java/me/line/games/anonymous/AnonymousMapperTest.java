package me.line.games.anonymous;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import me.line.games.anonymous.service.AnonymousMapperService;
import me.line.games.anonymous.vo.NewPostRequest;
import me.line.games.common.domain.Post;

class AnonymousMapperTest {
	private AnonymousMapperService mapper = Mappers.getMapper(AnonymousMapperService.class);

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
