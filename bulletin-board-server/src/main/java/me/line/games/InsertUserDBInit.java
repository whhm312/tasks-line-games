package me.line.games;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me.line.games.user.UserRepository;

@Service
public class InsertUserDBInit implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public InsertUserDBInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... arg) throws Exception {
		List<Object> args = new ArrayList<Object>();
		args.add("whily312");
		args.add(passwordEncoder.encode("test"));
		args.add("whily312@aaa.com");

		userRepository.saveUsers(args);

		args = new ArrayList<Object>();
		args.add("test");
		args.add(passwordEncoder.encode("0000"));
		args.add("user@aaa.com");

		userRepository.saveUsers(args);
	}

}
