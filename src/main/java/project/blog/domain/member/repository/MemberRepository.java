package project.blog.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
