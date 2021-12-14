package net.oleksin.paymentsystem.security.user;

import java.util.List;

public interface UserService {

    User findByEmail(String email);

    User findByUsername(String username);

    List<User> getAll();

    User findById(Long id);

    void deleteById(Long id);
}
