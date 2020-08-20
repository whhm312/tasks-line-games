package me.line.games.common.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.line.games.common.domain.User;
import me.line.games.user.UserRepository;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	private UserRepository authRepository;

	public UserPrincipalDetailsService(UserRepository authRepository) {
		this.authRepository = authRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = authRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("no user");
		}
		return new UserPrincipal(user);
	}

}
