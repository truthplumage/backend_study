package com.example.demo.member.presentation.controller;

import com.example.demo.member.application.dto.MemberCreateCommand;
import com.example.demo.member.application.dto.MemberResQuery;
import com.example.demo.member.application.usecase.MemberUsecase;
import com.example.demo.member.presentation.dto.MemberReq;
import com.example.demo.member.presentation.dto.MemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.init}/member")
public class MemberController {
    public final MemberUsecase memberUsecase;
    @PostMapping("/")
    public MemberRes join(@RequestBody MemberReq memberReq){
        MemberResQuery memberResQuery = memberUsecase.join(new MemberCreateCommand(memberReq.email(), memberReq.name(), memberReq.password(), memberReq.phone(), memberReq.address()));
        return new MemberRes(memberResQuery.email(), memberResQuery.name(), memberResQuery.status());
    }
}
