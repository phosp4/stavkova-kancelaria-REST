package org.example.stavkova_kancelaria_web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        User user1 = new User();
        user1.setEmail("samuel@gmail.com");
        user1.setBalance(45.5);

        return List.of(user1);
//        return userDAO.get
    }
}
