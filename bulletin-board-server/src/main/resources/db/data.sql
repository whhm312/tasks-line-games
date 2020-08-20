INSERT INTO TBL_BOARD_POST (seq, nick_name, user_id, title, content, hit, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST'), '지워봐', 'whily312', 'title-', 'contents-', 0, 'N', sysdate);

INSERT INTO TBL_BOARD_POST (seq, nick_name, user_id, title, content, hit, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST'), 'Ben', 'ccccc', 'abcdefg', 'zzzzdddddd', 0, 'N', sysdate);

INSERT INTO TBL_BOARD_POST_COMMENT (seq, parent_comment_seq, post_seq, nick_name, user_id, content, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST_COMMENT'), 0, CURRVAL('SEQ_BOARD_POST'), 'Nana', 'tttt', '코멘트 달아요.', 'N', sysdate);

INSERT INTO TBL_BOARD_POST_COMMENT (seq, parent_comment_seq, post_seq, nick_name, user_id, content, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST_COMMENT'), 0, CURRVAL('SEQ_BOARD_POST'), 'Nana', 'tttt', '코멘트 하나 더 달아요.', 'N', sysdate);

INSERT INTO TBL_BOARD_POST_COMMENT (seq, parent_comment_seq, post_seq, nick_name, user_id, content, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST_COMMENT'), 1, CURRVAL('SEQ_BOARD_POST'), '룰루랄라', 'xxxx', '[Nana님] 대댓글  달아요.', 'N', sysdate);
INSERT INTO TBL_BOARD_POST_COMMENT (seq, parent_comment_seq, post_seq, nick_name, user_id, content, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST_COMMENT'), 1, CURRVAL('SEQ_BOARD_POST'), '룰루랄라', 'xxxx', '[룰루랄라님] 추가 후 삭제 시나리옹.', 'Y', sysdate);

INSERT INTO TBL_BOARD_POST_COMMENT (seq, parent_comment_seq, post_seq, nick_name, user_id, content, delete_yn, register_date) VALUES
(NEXTVAL('SEQ_BOARD_POST_COMMENT'), 0, CURRVAL('SEQ_BOARD_POST'), 'Nana', 'tttt', '코멘트 하나 더 달아요.', 'Y', sysdate);

-- SAMPLE
INSERT INTO TBL_USER (user_id, password, email) VALUES
  ('Lokesh', 'Gupta', 'abc@gmail.com'),
  ('Deja', 'Vu', 'xyz@email.com'),
  ('Caption', 'America', 'cap@marvel.com');