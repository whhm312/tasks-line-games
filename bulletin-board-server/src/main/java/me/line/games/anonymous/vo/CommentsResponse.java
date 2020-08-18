package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentsResponse {
	private int totalCount;
	private CommentResponse comment;
}
