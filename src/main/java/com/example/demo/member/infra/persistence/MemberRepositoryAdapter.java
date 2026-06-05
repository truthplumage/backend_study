package com.example.demo.member.infra.persistence;

import com.example.demo.member.domain.Member;
import com.example.demo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {
    public final MemberJpaRepository jpaRepository;
    @Override
    public Optional<Member> findByPhone(String phone) {
        return jpaRepository.findByPhone(phone);
    }

    @Override
    public boolean findByEmail(String email) {
        return jpaRepository.findByEmail(email).isPresent();
    }

    @Override
    public Member save(Member member) {
        return jpaRepository.save(member);
    }
}
