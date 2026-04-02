package com.prod.project.airBnbApp.dto;

import com.prod.project.airBnbApp.entity.User;
import com.prod.project.airBnbApp.entity.enums.Gender;

public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
