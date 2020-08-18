package me.line.games.anonymous.dao;

import java.util.ArrayList;
import java.util.List;

import org.h2.util.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import me.line.games.common.domain.Comment;
import me.line.games.common.domain.CommonComment;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.domain.SubComment;
import me.line.games.common.exception.FailedCreateContentException;
import me.line.games.common.exception.ResourceNoContentException;
import me.line.games.common.vo.SearchCondition;

@Repository
public class AnonymousPostDAO {

	JdbcTemplate jdbcTemplate;

	public AnonymousPostDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int insert(Post post) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO TBL_BOARD_POST ( ");
		query.append("  SEQ, NICK_NAME, USER_ID, ");
		query.append("  TITLE, CONTENT, HIT, ");
		query.append("  DELETE_YN, REGISTER_DATE ");
		query.append(") ");
		query.append("VALUES ( ");
		query.append("  NEXTVAL('SEQ_BOARD_POST'), ?, ?, ");
		query.append("  ?, ?, 0, ");
		query.append("  'N', SYSDATE ");
		query.append(")");

		List<Object> args = new ArrayList<>();
		args.add(post.getNickName());
		args.add(post.getUserId());
		args.add(post.getTitle());
		args.add(post.getContent());

		int result = jdbcTemplate.update(query.toString(), args.toArray());
		if (result < 1) {
			throw new FailedCreateContentException("Failed to INSERT a Post that " + post.toString() + ".");
		}

		query.setLength(0);
		query.append("SELECT CURRVAL('SEQ_BOARD_POST')");
		return jdbcTemplate.queryForObject(query.toString(), Integer.class);
	}

	public List<Post> selectAll(SearchCondition condition) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * ");
		query.append("FROM ( ");

		query.append("SELECT ROWNUM() AS RNUM, *, ");
		query.append("  IFNULL(COMM_CNT, 0) AS COMMENT_COUNT ");

		query.append("FROM TBL_BOARD_POST AS P LEFT JOIN ( ");

		query.append("    SELECT POST_SEQ, COUNT(*) AS COMM_CNT ");
		query.append("      FROM TBL_BOARD_POST_COMMENT ");
		query.append("     WHERE DELETE_YN = 'N' ");
		query.append("     GROUP BY POST_SEQ ");

		query.append(") AS C ON P.SEQ = C.POST_SEQ ");

		query.append("WHERE P.DELETE_YN = 'N' ");

		List<Object> args = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(condition.getSearchText())) {
			args.add(like(condition.getSearchText()));

			if ("TITLE+CONTENT".equals(condition.getSearchType())) {
				query.append("AND (TITLE LIKE ? OR CONTENT LIKE ?) ");
				args.add(like(condition.getSearchText()));
			} else {
				query.append("AND " + condition.getSearchType() + " LIKE ? ");
			}
		}

		query.append(") ");
		query.append("WHERE RNUM >= ? AND RNUM <= ? ");

		args.add(condition.getStartNum());
		args.add(condition.getEndNum());

		return jdbcTemplate.query(query.toString(), args.toArray(), new BeanPropertyRowMapper<Post>(Post.class));
	}

	private String like(String searchText) {
		return "%" + searchText + "%";
	}

	public int selectAllCount(SearchCondition condition) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) ");
		query.append("  FROM ( ");
		query.append("    SELECT * ");
		query.append("      FROM TBL_BOARD_POST ");
		query.append("WHERE DELETE_YN = 'N' ");

		List<Object> args = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(condition.getSearchText())) {
			args.add(like(condition.getSearchText()));

			if ("TITLE+CONTENT".equals(condition.getSearchType())) {
				query.append("     AND (TITLE LIKE ? OR CONTENT LIKE ?) ");
				args.add(like(condition.getSearchText()));
			} else {
				query.append("     AND " + condition.getSearchType() + " LIKE ? ");
			}
		}

		query.append("       ) ");

		return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
	}

	public PostDetail select(int postSeq) {
		String query = "SELECT * FROM TBL_BOARD_POST WHERE SEQ = ? AND DELETE_YN = 'N' ";

		try {
			return jdbcTemplate.queryForObject(query, new Object[] { postSeq }, new BeanPropertyRowMapper<PostDetail>(PostDetail.class));
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNoContentException("The Post SEQ is " + postSeq + ". Cannot SELECT the Post.");
		}
	}

	public void updateHit(int postSeq) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET HIT = HIT + 1 ");
		query.append("WHERE SEQ = ? ");
		query.append("  AND DELETE_YN = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { postSeq });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + postSeq + ". Cannot UPDATE hit of the Post.");
		}
	}

	public void update(Post post) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET TITLE = ?, ");
		query.append("    CONTENT = ?, ");
		query.append("    LAST_UPDATE_DATE = SYSDATE ");
		query.append("WHERE USER_ID = ? ");
		query.append("  AND SEQ = ? ");
		query.append("  AND DELETE_YN = 'N' ");

		List<Object> args = new ArrayList<>();
		args.add(post.getTitle());
		args.add(post.getContent());
		args.add(post.getUserId());
		args.add(post.getSeq());

		int result = jdbcTemplate.update(query.toString(), args.toArray());
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + post.getSeq() + ". Cannot UPDATE the Post.");
		}
	}

	public void deletePost(String userId, int postSeq) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST ");
		query.append("SET DELETE_YN = 'Y', ");
		query.append("    LAST_UPDATE_DATE = SYSDATE ");
		query.append("WHERE USER_ID = ? ");
		query.append("  AND SEQ = ? ");
		query.append("  AND DELETE_YN = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { userId, postSeq });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + postSeq + ". Cannot DELETE the Post.");
		}
	}

	public void deleteComments(String userId, int postSeq) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST_COMMENT ");
		query.append("SET DELETE_YN = 'Y', ");
		query.append("    LAST_UPDATE_DATE = SYSDATE ");
		query.append("WHERE USER_ID = ? ");
		query.append("  AND POST_SEQ = ? ");
		query.append("  AND DELETE_YN = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { userId, postSeq });
		if (result == 0) {
			throw new ResourceNoContentException("The Post SEQ is " + postSeq + ". Cannot DELETE the Comments in the Post.");
		}
	}

	public int insert(CommonComment comment) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO TBL_BOARD_POST_COMMENT ( ");
		query.append("  SEQ, PARENT_COMMENT_SEQ, POST_SEQ, ");
		query.append("  NICK_NAME, USER_ID, CONTENT, ");
		query.append("  DELETE_YN, REGISTER_DATE ");
		query.append(") ");
		query.append("VALUES ( ");
		query.append("  NEXTVAL('SEQ_BOARD_POST_COMMENT'), ?, ?, ");
		query.append("  ?, ?, ?, ");
		query.append("  'N', SYSDATE ");
		query.append(")");

		List<Object> args = new ArrayList<>();
		args.add(comment.getSeq());
		args.add(comment.getPostSeq());
		args.add(comment.getNickName());
		args.add(comment.getUserId());
		args.add(comment.getContent());

		int result = jdbcTemplate.update(query.toString(), args.toArray());
		if (result == 0) {
			throw new FailedCreateContentException("Failed to INSERT a Comment that " + comment.toString() + ".");
		}

		query.setLength(0);
		query.append("SELECT CURRVAL('SEQ_BOARD_POST_COMMENT')");
		return jdbcTemplate.queryForObject(query.toString(), Integer.class);
	}

	public void update(Comment comment) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST_COMMENT ");
		query.append("SET CONTENT = ?, ");
		query.append("    LAST_UPDATE_DATE = SYSDATE ");
		query.append("WHERE SEQ = ? ");
		query.append("  AND POST_SEQ = ? ");
		query.append("  AND USER_ID = ? ");
		query.append("  AND DELETE_YN = 'N' ");

		List<Object> args = new ArrayList<>();
		args.add(comment.getContent());
		args.add(comment.getSeq());
		args.add(comment.getPostSeq());
		args.add(comment.getUserId());
		int result = jdbcTemplate.update(query.toString(), args.toArray());
		if (result == 0) {
			throw new ResourceNoContentException("The Comment SEQ is " + comment.getSeq() + ". Cannot UPDATE the Comment.");
		}
	}

	public void deleteComment(String userId, int postSeq, int commentSeq) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE TBL_BOARD_POST_COMMENT ");
		query.append("SET DELETE_YN = 'Y', ");
		query.append("    LAST_UPDATE_DATE = SYSDATE ");
		query.append("WHERE USER_ID = ? ");
		query.append("  AND POST_SEQ = ? ");
		query.append("  AND SEQ = ? ");
		query.append("  AND DELETE_YN = 'N' ");
		int result = jdbcTemplate.update(query.toString(), new Object[] { userId, postSeq, commentSeq });
		if (result == 0) {
			throw new ResourceNoContentException(
					"The Post SEQ is " + postSeq + " and Comment SEQ is " + commentSeq + ". Cannot DELETE the Comment in the Post.");
		}
	}

	public void isExistPost(int postSeq) {
		select(postSeq);
	}

	public List<Comment> selectComments(int postSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT *  ");
		query.append("  FROM TBL_BOARD_POST_COMMENT ");
		query.append(" WHERE POST_SEQ = ? ");
		query.append("   AND PARENT_COMMENT_SEQ = 0 ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);

		return jdbcTemplate.query(query.toString(), args.toArray(), new BeanPropertyRowMapper<Comment>(Comment.class));
	}

	public Comment selectComment(int postSeq, int commentSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT *  ");
		query.append("  FROM TBL_BOARD_POST_COMMENT ");
		query.append(" WHERE POST_SEQ = ? ");
		query.append("   AND SEQ = ? ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);
		args.add(commentSeq);

		try {
			return jdbcTemplate.queryForObject(query.toString(), args.toArray(), new BeanPropertyRowMapper<Comment>(Comment.class));
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNoContentException("The Post SEQ is " + postSeq + " and Comment SEQ is " + commentSeq + ".");
		}
	}

	public List<SubComment> selectSubComments(int postSeq, int commentSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * ");
		query.append("FROM TBL_BOARD_POST_COMMENT ");
		query.append("WHERE POST_SEQ = ? ");
		query.append("AND PARENT_COMMENT_SEQ = ? ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);
		args.add(commentSeq);

		return jdbcTemplate.query(query.toString(), args.toArray(), new BeanPropertyRowMapper<SubComment>(SubComment.class));
	}

	public int selectCount(int postSeq, int commentSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) ");
		query.append("  FROM TBL_BOARD_POST_COMMENT ");
		query.append("WHERE POST_SEQ = ? ");
		query.append("AND SEQ = ? ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);
		args.add(commentSeq);

		return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
	}

	public int selectSubCommentCount(int postSeq, int commentSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) ");
		query.append("  FROM TBL_BOARD_POST_COMMENT ");
		query.append("WHERE POST_SEQ = ? ");
		query.append("AND PARENT_COMMENT_SEQ = ? ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);
		args.add(commentSeq);

		return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
	}

	public int selectCommentCount(int postSeq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) ");
		query.append("  FROM TBL_BOARD_POST_COMMENT ");
		query.append("WHERE POST_SEQ = ? ");

		List<Object> args = new ArrayList<>();
		args.add(postSeq);

		return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
	}

}
