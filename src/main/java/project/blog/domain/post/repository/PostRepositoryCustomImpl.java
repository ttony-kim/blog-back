package project.blog.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.blog.domain.post.entity.Post;

import java.util.List;

import static project.blog.domain.category.entity.QCategory.category;
import static project.blog.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findPostsByCategoryAndKeyword(Long categoryId, String searchValue, Pageable pageable) {
        List<Post> list = queryFactory.selectFrom(post)
                .leftJoin(post.category, category).fetchJoin()
                .where(category.enabled.isTrue(),
                        categoryIdEq(categoryId),
                        searchValueContains(searchValue))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.category, category)
                .where(categoryIdEq(categoryId),
                        searchValueContains(searchValue))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? post.category.id.eq(categoryId) : null;
    }

    private BooleanExpression searchValueContains(String searchValue) {
        return searchValue != null && !searchValue.isBlank()
                ? post.title.contains(searchValue).or(post.content.contains(searchValue))
                :null;
    }

}
