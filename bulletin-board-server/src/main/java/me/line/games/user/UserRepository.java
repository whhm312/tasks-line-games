package me.line.games.user;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import me.line.games.common.domain.User;

@Repository
public class UserRepository {
	private JdbcTemplate jdbcTemplate;

	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User findByUsername(String userName) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM TBL_USER WHERE user_id = ?", new Object[] { userName },
					new BeanPropertyRowMapper<User>(User.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public void saveUsers(List<Object> args) {
		jdbcTemplate.update("INSERT INTO TBL_USER (user_id, password, email) VALUES (?, ?, ?)", args.toArray());
	}
}
