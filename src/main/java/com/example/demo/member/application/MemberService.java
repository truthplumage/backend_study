package com.example.demo.member.application;

import com.example.demo.member.application.dto.MemberCreateCommand;
import com.example.demo.member.application.dto.MemberResQuery;
import com.example.demo.member.application.usecase.MemberUsecase;
import com.example.demo.member.domain.Member;
import com.example.demo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberUsecase {
    public final MemberRepository memberRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public MemberResQuery join(MemberCreateCommand createCommand) {
        if(!memberRepository.findByEmail(createCommand.email())){
            if(!memberRepository.findByPhone(createCommand.phone()).isPresent()){
                Member member = Member.create(createCommand.email(), createCommand.name(), createCommand.address(), "BUYER",
                        createCommand.password(), createCommand.phone());
                member.setSaltKey(Base64.getEncoder().encodeToString(new SecureRandom().generateSeed(8)));
                member.setPassword(encoder.encode(createCommand.password()+member.getSaltKey()));
                memberRepository.save(member);
            }else {
                //TODO: throw 처리
            }
        }else{
            //TODO: throw 처리
        }
        return new MemberResQuery(createCommand.email(), createCommand.name(), "BUYER");
    }
}
