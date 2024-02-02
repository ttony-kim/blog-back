package project.blog.global.config;

import lombok.Getter;

@Getter
public enum BasicCode {
    ALL(-1L); // 전체

    private Long id;

    BasicCode(Long id) {
        this.id = id;
    }
}
