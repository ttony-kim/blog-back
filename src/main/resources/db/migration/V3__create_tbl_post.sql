CREATE TABLE tbl_post (
    post_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content LONGTEXT NOT NULL,
    category_id BIGINT NOT NULL,
    created_date DATETIME(6) NOT NULL,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES tbl_category (category_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;