package net.oleksin.paymentsystem.security.user.jpa;

import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.exception.UserNotFoundException;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceJpa implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User findByEmail(String email) {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found!"));
        return user;
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found!"));
        return user;
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!"));
        return user;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
