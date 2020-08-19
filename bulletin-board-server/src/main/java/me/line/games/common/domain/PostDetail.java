package me.line.games.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDetail {
	private int seq;
	private String nickName;
	private String userId;
	private String title;
	private String content;
	private int hit;
	private String deleteYn;
	private Date registerDate;
	private Date lastUpdateDate;
}
