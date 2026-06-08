package com.example.demo.member.application.dto;

public record MemberCreateCommand(
        String email,
        String name,
        String password,
        String phone,
        String address
) {
}
