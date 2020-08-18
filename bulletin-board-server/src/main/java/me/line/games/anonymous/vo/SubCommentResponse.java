package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubCommentResponse {
	private int seq;
	private int postSeq;
	private String nickName;
	private String content;
	private String deleteYn;
	private String lastUpdateDate;
	private String registerDate;
}
