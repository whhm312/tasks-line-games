package me.line.games.sample;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import me.line.games.sample.vo.User;

@Service
public class SampleService {
	JdbcTemplate jdbcTemplate;

	public SampleService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User getUser(String userId) {
		return jdbcTemplate.queryForObject("SELECT id, first_name, last_name, email FROM TBL_USER WHERE id = ?",
				new Object[] { userId }, new BeanPropertyRowMapper<User>(User.class));
	}

}
