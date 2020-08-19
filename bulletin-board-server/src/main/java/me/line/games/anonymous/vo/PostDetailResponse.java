package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDetailResponse {
	private int seq;
	private String nickName;
	private String title;
	private String content;
	private int hit;
	private String registerDate;
	private String lastUpdateDate;
}
