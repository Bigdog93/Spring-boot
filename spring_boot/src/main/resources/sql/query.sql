CREATE DATABASE facebook_clone;

CREATE TABLE t_user(
   iuser INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   email VARCHAR(50) UNIQUE NOT NULL,
   pw VARCHAR(100) NOT NULL,
   nm VARCHAR(10) NOT NULL,
   authCd CHAR(5) COMMENT '회원가입 인증코드 null이면 인증받은 상태, 값이 있으면 인증해야 되는 상태',
   regdt DATETIME DEFAULT NOW(),
   tel CHAR(13),
   INDEX idx_auth_cd (`authCd`)
);

# auth_cd 인덱스 생성
CREATE INDEX idx_auth_cd ON t_user (authCd);

