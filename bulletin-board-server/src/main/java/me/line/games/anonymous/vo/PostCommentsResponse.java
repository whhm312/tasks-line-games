package me.line.games.anonymous.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostCommentsResponse {
	private int totalCount;
	private List<CommentResponse> comments;
}
