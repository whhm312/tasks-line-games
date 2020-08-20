CREATE TABLE TBL_BOARD_POST (
  seq INT AUTO_INCREMENT  PRIMARY KEY,
  nick_name VARCHAR(200) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  title VARCHAR(500) NOT NULL,
  content VARCHAR(2000) NOT NULL,
  hit INT,
  delete_yn CHAR(1) NOT NULL,
  register_date TIMESTAMP NOT NULL,
  last_update_date TIMESTAMP NULL
);

CREATE TABLE TBL_BOARD_POST_COMMENT (
  seq INT AUTO_INCREMENT  PRIMARY KEY,
  parent_comment_seq INT,
  post_seq INT,
  nick_name VARCHAR(200) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  content CLOB NOT NULL,
  delete_yn CHAR(1) NOT NULL,
  register_date TIMESTAMP NOT NULL,
  last_update_date TIMESTAMP NULL
);

-- SEQUENCES --------------------------------------------
CREATE SEQUENCE SEQ_BOARD_POST START WITH 1;
CREATE SEQUENCE SEQ_BOARD_POST_COMMENT START WITH 1;

CREATE TABLE TBL_USER (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_id VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);