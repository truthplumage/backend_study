package com.example.demo.member.application;

import com.example.demo.member.application.dto.MemberCreateCommand;
import com.example.demo.member.application.dto.MemberLogin;
import com.example.demo.member.application.dto.MemberResQuery;
import com.example.demo.member.application.dto.Token;
import com.example.demo.member.application.usecase.MemberUsecase;
import com.example.demo.member.domain.Member;
import com.example.demo.member.domain.repository.MemberRepository;
import com.example.demo.member.util.JwtProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements MemberUsecase {
    public final JwtProvider jwtProvider;
    public final MemberRepository memberRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public MemberResQuery join(MemberCreateCommand createCommand) {
        if(memberRepository.findByEmail(createCommand.email()).isEmpty()){
            if(memberRepository.findByPhone(createCommand.phone()).isEmpty()){
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

    @Override
    public Token login(MemberLogin memberLogin) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberLogin.email());

        if(memberOptional.isPresent()){
            if(encoder.matches(memberLogin.password()+memberOptional.get().getSaltKey(), memberOptional.get().getPassword())){
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberOptional.get().getId().toString(), null, null);
                return new Token(jwtProvider.generateToken(authentication), jwtProvider.generateRefreshToken(authentication));
            }else{
                //TODO: 패스워드가 맞지 않음.
            }
        }else{
            //TODO: 존재하지 않는 유저
        }
        return null;
    }

    @Override
    public Token refreshToken(String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String subjects = jwtProvider.verifyToken(refreshToken);
        log.info("subjects = {}", subjects);
        //TODO: DB구조에 맞춰서 호출
        Authentication authentication = new UsernamePasswordAuthenticationToken(subjects, null, null);
        //TODO: DB에 입력
        return new Token(jwtProvider.generateToken(authentication), jwtProvider.generateRefreshToken(authentication));
    }

}
