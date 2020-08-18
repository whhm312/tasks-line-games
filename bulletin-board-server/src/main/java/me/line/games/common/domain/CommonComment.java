package me.line.games.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonComment {
	private int seq;
	private int postSeq;
	private String nickName;
	private String userId;
	private String content;
	private String deleteYn;
	private Date registerDate;
	private Date lastUpdateDate;

	@Override
	public String toString() {
		return "Comment [seq=" + seq + ", postSeq=" + postSeq + ", nickName=" + nickName + ", content=" + content + ", deleteYn=" + deleteYn + ", registerDate="
				+ registerDate + ", lastUpdateDate=" + lastUpdateDate + "]";
	}
}
