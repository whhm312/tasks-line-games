package me.line.games.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Comment {
	private int seq;
	private int post_seq;
	private int parent_comment_seq;
	private String nickName;
	private String userId;
	private String content;
	private String deleteYn;
	private Date registerDate;
	private Date lastUpdateDate;
}
