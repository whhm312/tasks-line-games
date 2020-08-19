package me.line.games.anonymous.vo;

import java.util.ArrayList;
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

	public void deleted() {
		this.seq = 0;
		this.postSeq = 0;
		this.nickName = "";
		this.content = "";
		this.deleteYn = "Y";
		this.registerDate = "";
		this.lastUpdateDate = "";
		this.subComments = new ArrayList<>();
	}
}
