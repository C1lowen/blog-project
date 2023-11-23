package com.users.users.dto;

import lombok.Data;

import javax.validation.constraints.Size;
@Data
public class AnswerDTO {
    private String name;
    private String subject;
    private String email;
    private String message;
}
