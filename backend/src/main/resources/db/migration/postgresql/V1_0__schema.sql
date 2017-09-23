CREATE SEQUENCE universities_seq;
CREATE SEQUENCE faculties_seq;
CREATE SEQUENCE subjects_seq;
CREATE SEQUENCE assessments_seq;
CREATE SEQUENCE comments_seq;
CREATE SEQUENCE users_seq;

CREATE TABLE universities (
    university_id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('universities_seq'),
    name          VARCHAR(100) NOT NULL,
    abbreviation  VARCHAR(5)   NOT NULL
);

CREATE TABLE faculties (
    faculty_id    BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('faculties_seq'),
    name          VARCHAR(100) NOT NULL,
    code          VARCHAR(5)   DEFAULT NULL,
    university_id BIGINT       NOT NULL,
    FOREIGN KEY (university_id) REFERENCES universities (university_id)
);

CREATE TABLE subjects (
    subject_id       BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('subjects_seq'),
    code             VARCHAR(10)    NOT NULL,
    name             VARCHAR(100)   NOT NULL,
    credit_points    INT            NOT NULL,
    description      VARCHAR(10000) NOT NULL,
    min_requirements VARCHAR(10000) DEFAULT NULL,
    undergrad        BOOLEAN        NOT NULL,
    postgrad         BOOLEAN        NOT NULL,
    autumn           BOOLEAN        NOT NULL,
    spring           BOOLEAN        NOT NULL,
    summer           BOOLEAN        NOT NULL,
    rating           FLOAT  DEFAULT 0.0,
    num_ratings      INT    DEFAULT 0,
    faculty_id       BIGINT         NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)
);

CREATE TABLE assessments (
    assessment_id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('assessments_seq'),
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(100) NOT NULL,
    weighting     INT          NOT NULL,
    group_work    BOOLEAN      NOT NULL,
    length        VARCHAR(100) NOT NULL,
    type          INT          NOT NULL,
    subject_id    BIGINT       NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects (subject_id)
);

CREATE TABLE comments (
    comment_id      BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('comments_seq'),
    post            VARCHAR(200)    NOT NULL,
    subject_id      BIGINT          NOT NULL,
--     //is_flagged    BOOLEAN,
--     //thumbs_up     INT DEFAULT     0,
--     //thumbs_down   INT DEFAULT     0,
--     //user_id       BIGINT     NOT NULL,
--     //FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (subject_id) REFERENCES subjects (subject_id)
);

CREATE TABLE users (
    user_id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('users_seq'),
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
