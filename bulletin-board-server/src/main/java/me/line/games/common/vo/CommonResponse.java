package me.line.games.common.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonResponse {
	private String code;
	private String message;

	public void setSuccess() {
		this.code = "200";
		this.message = "요청이 처리되었습니다.";
	}
}
