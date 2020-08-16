package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponse {
	private int seq;
	private int post_seq;
	private int parent_comment_seq;
	private String nickName;
	private String content;
	private String deleteYn;
	private String lastUpdateDate;

}
