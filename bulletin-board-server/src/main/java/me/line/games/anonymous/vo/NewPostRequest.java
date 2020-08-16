package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// TODO Vaildation
public class NewPostRequest {
	private String nickName;
	private String title;
	private String content;
}
