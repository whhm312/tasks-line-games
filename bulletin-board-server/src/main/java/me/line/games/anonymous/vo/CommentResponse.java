package me.line.games.anonymous.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponse {
	private int seq;
	private int postSeq;
	private String nickName;
	private String content;
	private String deleteYn;
	private String registerDate;
	private String lastUpdateDate;
	private List<SubCommentResponse> subComments;
}
