package org.example.stavkova_kancelaria_web.user;

import lombok.Data;
import org.example.stavkova_kancelaria_web.Role;

@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private Double balance;
    private Role role;

//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setBalance(Double balance) {
//        this.balance = balance;
//    }
}



