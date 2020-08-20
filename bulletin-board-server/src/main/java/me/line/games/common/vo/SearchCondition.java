package me.line.games.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SearchCondition {
	private String searchType;
	private String searchText;
	private int page;
	private int row;

	public SearchCondition(String searchType, String searchText, int page, int row) {
		this.searchType = searchType;
		this.searchText = searchText;
		this.page = page;
		this.row = row;
	}

	public int getStartNum() {
		if (page == 1) {
			return page;
		}
		return (page - 1) * row + 1;
	}

	public int getEndNum() {
		if (page == 1) {
			return row;
		}
		return page * row;
	}
}
