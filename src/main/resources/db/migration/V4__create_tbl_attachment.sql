CREATE TABLE tbl_attachment (
    attachment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(255) NOT NULL,
    saved_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    extension VARCHAR(50) NOT NULL,
    size BIGINT NOT NULL,
    file_path VARCHAR(255)  NOT NULL,
    post_id BIGINT NOT NULL,
    created_date DATETIME(6) NOT NULL,
    CONSTRAINT fk_attachment_post FOREIGN KEY (post_id) REFERENCES tbl_post (post_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;;