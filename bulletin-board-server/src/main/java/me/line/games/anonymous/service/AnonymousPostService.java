package me.line.games.anonymous.service;

import java.util.List;

import org.springframework.stereotype.Service;

import me.line.games.anonymous.dao.AnonymousPostDAO;
import me.line.games.common.domain.Post;
import me.line.games.common.domain.PostDetail;
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

	public PostDetail find(String postId) {
		anonymousPostDAO.updateHit(postId);
		return anonymousPostDAO.select(postId);
	}

	public void modify(Post post) {
		anonymousPostDAO.update(post);
	}

	public void delete(String userId, String postId) {
		anonymousPostDAO.deletePost(userId, postId);
		anonymousPostDAO.deleteComments(userId, postId);
	}

}
