package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

//发送邮箱DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String checkCode;
    @NotBlank
    private String checkCodeKey;
}
