package me.line.games.anonymous;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnonymousPost {
	private int seq;
	private String title;
	private String registerDate;
	private String lastUpdateDate;
	private String writer;
	private String content;
	private int hit;
}
