package me.line.games.common.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
	private String username;
	private String password;
}
