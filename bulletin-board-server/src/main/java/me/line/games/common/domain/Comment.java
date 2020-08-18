package me.line.games.common.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Comment extends CommonComment {
	private List<SubComment> subComments;

	@Override
	public String toString() {
		return "Comment [seq=" + getSeq() + ", postSeq=" + getPostSeq() + ", nickName=" + getNickName() + ", content=" + getContent() + ", deleteYn="
				+ getDeleteYn() + ", registerDate=" + getRegisterDate() + ", lastUpdateDate=" + getLastUpdateDate() + "]";
	}

}
