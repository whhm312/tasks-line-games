package me.line.games;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
	@RequestMapping("/")
	public String hello() {
		return "Hello Board.";
	}
}
