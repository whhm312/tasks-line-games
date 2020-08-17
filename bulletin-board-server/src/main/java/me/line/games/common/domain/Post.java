package me.line.games.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Post {
	private int rnum;
	private int seq;
	private String nickName;
	private String userId;
	private String title;
	private String content;
	private int hit;
	private int commentCount;
	private String deleteYn;
	private Date registerDate;
	private Date lastUpdateDate;

	@Override
	public String toString() {
		return "Post [rnum=" + rnum + ", seq=" + seq + ", nickName=" + nickName + ", title=" + title + ", content="
				+ content + ", hit=" + hit + ", commentCount=" + commentCount + ", deleteYn=" + deleteYn
				+ ", registerDate=" + registerDate + ", lastUpdateDate=" + lastUpdateDate + "]";
	}

}
