package com.example.demo.member.presentation.controller;

import com.example.demo.member.application.dto.MemberCreateCommand;
import com.example.demo.member.application.dto.MemberLogin;
import com.example.demo.member.application.dto.MemberResQuery;
import com.example.demo.member.application.dto.Token;
import com.example.demo.member.application.usecase.MemberUsecase;
import com.example.demo.member.presentation.dto.Login;
import com.example.demo.member.presentation.dto.MemberReq;
import com.example.demo.member.presentation.dto.MemberRes;
import com.example.demo.member.presentation.dto.TokenRes;
import com.example.demo.member.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.init}/member")
public class MemberController {
    public final MemberUsecase memberUsecase;
    public final JwtProvider provider;
    @PostMapping("/")
    public MemberRes join(@RequestBody MemberReq memberReq){
        MemberResQuery memberResQuery = memberUsecase.join(new MemberCreateCommand(memberReq.email(), memberReq.name(), memberReq.password(), memberReq.phone(), memberReq.address()));
        return new MemberRes(memberResQuery.email(), memberResQuery.name(), memberResQuery.status());
    }

    @GetMapping("/generateKey")
    public void generateKey(){
        provider.makeRsaKey();
    }

    @PostMapping("/login")
    public TokenRes login(@RequestBody Login login) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Token tok = memberUsecase.login(new MemberLogin(login.email(), login.password()));
        return new TokenRes(tok.refreshToken(), tok.accessToken());
    }

//    @GetMapping("/refreshToken")
    @PostMapping("/refreshToken")// swagger 테스트 전용.
    public TokenRes refreshToken(
//            @RequestHeader("RefreshToken") String refreshToken,
            @RequestBody String refreshToken
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Token tok = memberUsecase.refreshToken(refreshToken);
        return new TokenRes(tok.refreshToken(), tok.accessToken());
    }
}
