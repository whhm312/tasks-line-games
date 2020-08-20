package me.line.games.common.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
	private int id;
	private String user_id;
	private String password;
	private String email;
}
