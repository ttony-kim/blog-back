package project.blog.domain.category.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.blog.domain.category.dto.CategoryDetailResponseDto;

import java.util.List;

import static project.blog.domain.category.entity.QCategory.category;
import static project.blog.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CategoryDetailResponseDto> findCategoryDetails() {
        List<CategoryDetailResponseDto> result = queryFactory
                .select(Projections.constructor(CategoryDetailResponseDto.class,
                        category.id,
                        category.name,
                        category.enabled,
                        category.displayOrder,
                        post.count().intValue()
                ))
                .from(category)
                .leftJoin(post).on(post.category.eq(category))
                .groupBy(category)
                .orderBy(category.displayOrder.asc())
                .fetch();

        return result;
    }

}
