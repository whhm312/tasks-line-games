package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostResponse {
	private int seq;
	private String nickName;
	private String title;
	private String registerDate;
	private int hit;
	private int commentCount;
}
