package project.blog.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.global.config.common.BooleanToYNConverter;

@Entity
@Getter
@Table(name = "tbl_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean enabled;

    @Column(nullable = false)
    private Integer displayOrder;

    private Category(String name, Boolean enabled, Integer displayOrder) {
        this.name = name;
        this.enabled = enabled;
        this.displayOrder = displayOrder;
    }

    public static Category of(String name, Boolean enabled, Integer displayOrder) {
        return new Category(name, enabled, displayOrder);
    }

    public void update(String name, Boolean enabled, Integer displayOrder) {
        this.name = name;
        this.enabled = enabled;
        this.displayOrder = displayOrder;
    }

}
