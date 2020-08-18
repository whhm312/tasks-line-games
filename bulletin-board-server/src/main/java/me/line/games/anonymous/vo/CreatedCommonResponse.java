package me.line.games.anonymous.vo;

import lombok.Getter;
import lombok.Setter;
import me.line.games.common.vo.CommonResponse;

@Setter
@Getter
public class CreatedCommonResponse extends CommonResponse {
	private int seq;

	public CreatedCommonResponse() {
	}

	public CreatedCommonResponse(int seq) {
		this.seq = seq;

		setSuccess();
	}
}
