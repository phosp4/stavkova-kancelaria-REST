package org.example.stavkova_kancelaria_web.user;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private Double balance;
    private Role role;
}



