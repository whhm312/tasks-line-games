CREATE TABLE TBL_BOARD_POST (
  seq INT AUTO_INCREMENT  PRIMARY KEY,
  nick_name VARCHAR(200) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  title VARCHAR(500) NOT NULL,
  content VARCHAR(2000) NOT NULL,
  hit INT,
  delete_yn CHAR(1) NOT NULL,
  register_date DATE NOT NULL,
  last_update_date DATE NULL
);

CREATE TABLE TBL_BOARD_POST_COMMENT (
  seq INT AUTO_INCREMENT  PRIMARY KEY,
  parent_comment_seq INT,
  post_seq INT,
  nick_name VARCHAR(200) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  content CLOB NOT NULL,
  delete_yn CHAR(1) NOT NULL,
  register_date DATE NOT NULL,
  last_update_date DATE NULL
);

-- FOR TEST CASE START ----------------------------------
CREATE TABLE TBL_USER (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);
-- FOR TEST CASE END ------------------------------------