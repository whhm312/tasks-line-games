package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewCommentRequest {
	private int postSeq;
	private int parentCommentSeq;
	private String nickName;
	private String content;
}
