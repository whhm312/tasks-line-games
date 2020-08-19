package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubCommentResponse {
	private int seq;
	private int postSeq;
	private int parentCommentSeq;
	private String nickName;
	private String content;
	private String deleteYn;
	private String lastUpdateDate;
	private String registerDate;

	public void deleted() {
		this.seq = 0;
		this.postSeq = 0;
		this.nickName = "";
		this.content = "";
		this.deleteYn = "Y";
		this.registerDate = "";
		this.lastUpdateDate = "";
	}
}
