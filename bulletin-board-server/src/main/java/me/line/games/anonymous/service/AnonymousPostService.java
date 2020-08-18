package me.line.games.anonymous.service;

import java.util.List;

import org.springframework.stereotype.Service;

import me.line.games.anonymous.dao.AnonymousPostDAO;
import me.line.games.common.domain.Comment;
import me.line.games.common.domain.CommonComment;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
import me.line.games.common.domain.SubComment;
import me.line.games.common.vo.SearchCondition;

@Service
public class AnonymousPostService {
	private AnonymousPostDAO anonymousPostDAO;

	public AnonymousPostService(AnonymousPostDAO anonymousPostDAO) {
		this.anonymousPostDAO = anonymousPostDAO;
	}

	public int save(Post post) {
		return anonymousPostDAO.insert(post);
	}

	public List<Post> findAll(SearchCondition condition) {
		return anonymousPostDAO.selectAll(condition);
	}

	public int findAllCount(SearchCondition condition) {
		return anonymousPostDAO.selectAllCount(condition);
	}

	public PostDetail find(int postSeq) {
		anonymousPostDAO.updateHit(postSeq);
		return anonymousPostDAO.select(postSeq);
	}

	public void modify(Post post) {
		anonymousPostDAO.update(post);
	}

	public void delete(String userId, int postSeq) {
		anonymousPostDAO.deletePost(userId, postSeq);
		anonymousPostDAO.deleteComments(userId, postSeq);
	}

	public int save(CommonComment comment) {
		anonymousPostDAO.isExistPost(comment.getPostSeq());
		return anonymousPostDAO.insert(comment);
	}

	public void modify(Comment comment) {
		anonymousPostDAO.update(comment);
	}

	public void delete(String userId, int postSeq, int commentSeq) {
		anonymousPostDAO.deleteComment(userId, postSeq, commentSeq);
	}

	public List<Comment> getComments(int postSeq) {
		List<Comment> comments = anonymousPostDAO.selectComments(postSeq);
		for (Comment comment : comments) {
			comment.setSubComments(anonymousPostDAO.selectSubComments(postSeq, comment.getSeq()));
		}
		return comments;
	}

	public int getCommentsCount(int postSeq) {
		return anonymousPostDAO.selectCommentCount(postSeq);
	}

	public Comment getComment(int postSeq, int commentSeq) {
		Comment comment = anonymousPostDAO.selectComment(postSeq, commentSeq);
		List<SubComment> subComments = anonymousPostDAO.selectSubComments(postSeq, commentSeq);
		comment.setSubComments(subComments);
		return comment;
	}

	public int getCommentCount(int postSeq, int commentSeq) {
		int selectCommentCount = anonymousPostDAO.selectCount(postSeq, commentSeq);
		int selectSubCommentCount = anonymousPostDAO.selectSubCommentCount(postSeq, commentSeq);
		return selectCommentCount + selectSubCommentCount;
	}

}
