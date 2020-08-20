package me.line.games.anonymous.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModifyCommentRequest {
	@NotEmpty
	@Size(min = 1, max = 2000)
	private String content;
}
