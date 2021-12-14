package net.oleksin.paymentsystem.security.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByEmail(String email);

    User findByUsername(String username);

    List<User> getAll();

    User findById(Long id);

    void deleteById(Long id);
}
