package com.example.demo.member.application.usecase;

import com.example.demo.member.application.dto.MemberCreateCommand;
import com.example.demo.member.application.dto.MemberResQuery;

public interface MemberUsecase {
    MemberResQuery join(MemberCreateCommand createCommand);
}
