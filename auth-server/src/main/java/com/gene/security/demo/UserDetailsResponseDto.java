package com.gene.security.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponseDto {
    private String sub;
    private List<String> user_role;
    private String email_verified;
    private String name;
    private String preferred_username;
    private String given_name;
    private String family_name;
    private String email;
    private String error;
    private String error_description;

}
