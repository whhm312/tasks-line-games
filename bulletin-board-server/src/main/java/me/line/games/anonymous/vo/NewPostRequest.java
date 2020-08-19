package me.line.games.anonymous.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewPostRequest {
	@NotEmpty
	@Size(min = 1, max = 200)
	private String nickName;
	@NotEmpty
	@Size(min = 1, max = 500)
	private String title;
	@NotEmpty
	@Size(min = 1, max = 2000)
	private String content;
}
