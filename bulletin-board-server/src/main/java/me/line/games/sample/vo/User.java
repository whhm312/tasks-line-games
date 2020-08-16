package me.line.games.sample.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
	private int id;
	private String first_name;
	private String last_name;
	private String email;
}
