-- =========================================
-- Database (Created manually or via connection, script focuses on tables)
-- =========================================

-- =========================================
-- Drop Tables
-- =========================================
DROP TABLE IF EXISTS TBL_SAMPLE;

DROP TABLE IF EXISTS COL_META;

DROP TABLE IF EXISTS TBL_META;

DROP TABLE IF EXISTS USERS;

DROP TABLE IF EXISTS DATA_TYPES;

-- =========================================
-- 1. Users Table
-- =========================================
CREATE TABLE USERS (
    USER_ID INT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 고유 ID',
    USER_NM VARCHAR(50) NOT NULL COMMENT '사용자 이름',
    EMAIL VARCHAR(100) NOT NULL UNIQUE COMMENT '사용자 이메일',
    PASSWD VARCHAR(100) NOT NULL COMMENT '암호화된 비밀번호',
    ACTIVE_FL CHAR(1) DEFAULT 'Y' COMMENT '활성 여부 (Y/N)',
    REG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
    CONSTRAINT CK_USERS_ACTIVE_FL CHECK (ACTIVE_FL IN ('Y', 'N')),
    CONSTRAINT CK_USERS_EMAIL CHECK (EMAIL LIKE '%@%')
) ENGINE = InnoDB COMMENT = '사용자 테이블';

-- =========================================
-- 2. Data Types Table (New)
-- =========================================
CREATE TABLE DATA_TYPES (
    TYPE_ID INT AUTO_INCREMENT PRIMARY KEY COMMENT '타입 고유 ID',
    TYPE_NM VARCHAR(50) NOT NULL UNIQUE COMMENT '데이터 타입 명(INT, VARCHAR 등)',
    REG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자'
) ENGINE = InnoDB COMMENT = '지원하는 DB 데이터 타입 정의';

-- 초기 데이터 타입 적재
INSERT INTO
    DATA_TYPES (TYPE_NM)
VALUES ('INT'),
    ('VARCHAR'),
    ('TEXT'),
    ('DATE'),
    ('TIMESTAMP'),
    ('CHAR'),
    ('FLOAT'),
    ('DOUBLE'),
    ('BOOLEAN');

-- =========================================
-- 3. Tables Metadata
-- =========================================
CREATE TABLE TBL_META (
    TBL_ID INT AUTO_INCREMENT PRIMARY KEY COMMENT '테이블 고유 ID',
    TBL_NM VARCHAR(50) NOT NULL COMMENT '테이블 이름',
    TBL_DESC VARCHAR(255) COMMENT '테이블 설명',
    USER_ID INT NOT NULL COMMENT '생성자 ID(FK)',
    REG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    CONSTRAINT TBL_META_FK1 FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
) ENGINE = InnoDB COMMENT = '테이블 메타 데이터';

-- =========================================
-- 4. Columns Metadata
-- =========================================
CREATE TABLE COL_META (
    COL_ID INT AUTO_INCREMENT PRIMARY KEY COMMENT '컬럼 고유 ID',
    TBL_ID INT NOT NULL COMMENT '테이블 ID(FK)',
    COL_NM VARCHAR(50) NOT NULL COMMENT '컬럼 이름',
    TYPE_ID INT NOT NULL COMMENT '데이터 타입 ID(FK)',
    TYPE_LENGTH INT DEFAULT 0 COMMENT '데이터 길이 (VARCHAR 등)',
    IS_NULLABLE CHAR(1) DEFAULT 'Y' COMMENT 'NULL 허용 여부 (Y/N)',
    IS_PK CHAR(1) DEFAULT 'N' COMMENT 'PK 여부 (Y/N)',
    IS_AUTO_INCREMENT CHAR(1) DEFAULT 'N' COMMENT 'Auto Increment 여부 (Y/N)',
    ORDER_NO INT DEFAULT 0 COMMENT '컬럼 순서',
    REG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    CONSTRAINT CK_COL_META_NULLABLE CHECK (IS_NULLABLE IN ('Y', 'N')),
    CONSTRAINT CK_COL_META_PK CHECK (IS_PK IN ('Y', 'N')),
    CONSTRAINT CK_COL_META_AI CHECK (
        IS_AUTO_INCREMENT IN ('Y', 'N')
    ),
    CONSTRAINT COL_META_FK1 FOREIGN KEY (TBL_ID) REFERENCES TBL_META (TBL_ID) ON DELETE CASCADE,
    CONSTRAINT COL_META_FK2 FOREIGN KEY (TYPE_ID) REFERENCES DATA_TYPES (TYPE_ID) ON DELETE RESTRICT
) ENGINE = InnoDB COMMENT = '컬럼 메타 데이터';

-- =========================================
-- 4. Sample Data Table
-- =========================================
CREATE TABLE TBL_SAMPLE (
    SAMPLE_ID INT AUTO_INCREMENT PRIMARY KEY COMMENT '데이터 고유 ID',
    TBL_ID INT NOT NULL COMMENT '테이블 ID(FK)',
    DATA_JSON JSON NOT NULL COMMENT '컬럼-값 매핑(JSON)',
    ACTIVE_FL CHAR(1) DEFAULT 'Y' COMMENT '활성 여부 (Y/N)',
    REG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    CHG_DT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    CONSTRAINT CK_TBL_SAMPLE_ACTIVE_FL CHECK (ACTIVE_FL IN ('Y', 'N')),
    CONSTRAINT TBL_SAMPLE_FK1 FOREIGN KEY (TBL_ID) REFERENCES TBL_META (TBL_ID) ON DELETE CASCADE
) ENGINE = InnoDB COMMENT = '샘플 CRUD 학습용 데이터';

-- =========================================
-- Index Example
-- =========================================
CREATE INDEX IDX_USERS_EMAIL ON USERS (EMAIL);