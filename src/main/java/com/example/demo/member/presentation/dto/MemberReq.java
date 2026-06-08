package com.example.demo.member.presentation.dto;

public record MemberReq(String email,
                        String name,
                        String password,
                        String phone,
                        String address) {
}
