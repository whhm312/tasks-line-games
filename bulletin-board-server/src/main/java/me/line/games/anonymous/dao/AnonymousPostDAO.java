package me.line.games.anonymous.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.util.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.exception.FailedCreateContentException;
import me.line.games.common.exception.ResourceNoContentException;
import me.line.games.common.vo.SearchCondition;

@Repository
public class AnonymousPostDAO {

	JdbcTemplate jdbcTemplate;
	SimpleJdbcInsert simpleJdbcInsert;

	public AnonymousPostDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
	}

	public int insert(Post post) {
		simpleJdbcInsert.withTableName("TBL_BOARD_POST").setGeneratedKeyName("seq");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nick_name", post.getNickName());
		parameters.put("user_id", post.getUserId());
		parameters.put("title", post.getTitle());
		parameters.put("content", post.getContent());
		parameters.put("hit", 0);
		parameters.put("delete_yn", "N");
		parameters.put("register_date", LocalDateTime.now());

		Number seq = simpleJdbcInsert.executeAndReturnKey(parameters);
		if (seq.intValue() < 1) {
			throw new FailedCreateContentException("Failed to INSERT a Post that " + post.toString() + ".");
		}
		return seq.intValue();
	}

	public List<Post> selectAll(SearchCondition condition) {
		List<Object> argList = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT * ");
		query.append("FROM ( ");
		query.append("SELECT ROWNUM() AS rnum, ");
		query.append("SEQ AS seq, ");
		query.append("NICK_NAME AS nickName, ");
		query.append("USER_ID AS userId, ");
		query.append("TITLE AS title, ");
		query.append("CONTENT AS content, ");
		query.append("HIT AS hit, ");
		query.append("DELETE_YN AS deleteYn, ");
		query.append("REGISTER_DATE AS registerDate, ");
		query.append("LAST_UPDATE_DATE AS lastUpdateDate ");
		query.append("FROM TBL_BOARD_POST AS P ");
		query.append(
				"LEFT JOIN (SELECT POST_SEQ, COUNT(*) AS commentCount FROM TBL_BOARD_POST_COMMENT GROUP BY POST_SEQ) AS C ON P.SEQ = C.POST_SEQ ");
		query.append("WHERE delete_yn = 'N' ");

		if (!StringUtils.isNullOrEmpty(condition.getSearchText())) {
			argList.add(condition.getSearchText());

			if ("title+content".equals(condition.getSearchType())) {
				query.append("AND (title LIKE %?% OR content LIKE %?%) ");
				argList.add(condition.getSearchText());
			} else {
				query.append("AND " + condition.getSearchType() + " LIKE %?% ");
			}
		}

		query.append(") ");
		query.append("WHERE rnum >= ? AND rnum <= ? ");

		argList.add(condition.getStartNum());
		argList.add(condition.getEndNum());

		List<Post> result = jdbcTemplate.query(query.toString(), argList.toArray(),
				new BeanPropertyRowMapper<Post>(Post.class));

		if (result.size() < 1) {
			throw new ResourceNoContentException("Search Contition is that " + condition.toString() + ".");
		}
		return result;
	}

	public int selectAllCount(SearchCondition condition) {
		List<Object> argList = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) ");
		query.append("  FROM ( ");
		query.append("    SELECT * ");
		query.append("      FROM TBL_BOARD_POST ");
		query.append("WHERE delete_yn = 'N' ");

		if (!StringUtils.isNullOrEmpty(condition.getSearchText())) {
			argList.add(condition.getSearchText());

			if ("title+content".equals(condition.getSearchType())) {
				query.append("     AND (title LIKE %?% OR content LIKE %?%) ");
				argList.add(condition.getSearchText());
			} else {
				query.append("     AND " + condition.getSearchType() + " LIKE %?% ");
			}
		}

		query.append("       ) ");

		return jdbcTemplate.queryForObject(query.toString(), argList.toArray(), Integer.class);
	}

	public PostDetail select(String postId) {
		String query = "SELECT * FROM TBL_BOARD_POST WHERE seq = ? AND delete_yn = 'N' ";

		try {
			return jdbcTemplate.queryForObject(query, new Object[] { postId },
					new BeanPropertyRowMapper<PostDetail>(PostDetail.class));
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNoContentException("The Post SEQ is " + postId + ". Cannot SELECT the Post.");
		}
	}

	public void updateHit(String postId) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET HIT = HIT + 1 ");
		query.append("WHERE seq = ? ");
		query.append("  AND delete_yn = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { postId });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + postId + ". Cannot UPDATE hit of the Post.");
		}
	}

	public void update(Post post) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET title = ?, ");
		query.append("    content = ?, ");
		query.append("    last_update_date = sysdate ");
		query.append("WHERE seq = ? ");
		query.append("  AND delete_yn = 'N' ");
		int result = jdbcTemplate.update(query.toString(),
				new Object[] { post.getTitle(), post.getContent(), post.getSeq() });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + post.getSeq() + ". Cannot UPDATE the Post.");
		}
	}

	public void deletePost(String userId, String postId) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET delete_yn = 'Y', ");
		query.append("    last_update_date = sysdate ");
		query.append("WHERE user_id = ? ");
		query.append("  AND seq = ? ");
		query.append("  AND delete_yn = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { userId, postId });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + postId + ". Cannot DELETE the Post.");
		}
	}

	public void deleteComments(String userId, String postId) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST_COMMENT ");
		query.append("SET delete_yn = 'Y', ");
		query.append("    last_update_date = sysdate ");
		query.append("WHERE user_id = ? ");
		query.append("  AND post_seq = ? ");
		query.append("  AND delete_yn = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { userId, postId });
		if (result == 0) {
			throw new ResourceNoContentException(
					"The Post SEQ is " + postId + ". Cannot DELETE the Comments in the Post.");
		}
	}

}
