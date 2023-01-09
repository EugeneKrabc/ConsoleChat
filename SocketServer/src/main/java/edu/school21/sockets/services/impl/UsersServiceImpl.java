package edu.school21.sockets.services.impl;

import edu.school21.sockets.exception.AuthorizationException;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(String username, String password) {
        if (username == null || username.isEmpty())
            throw new AuthorizationException("Invalid username");

        if (password == null || password.isEmpty())
            throw new AuthorizationException("Invalid password");

        if (usersRepository.findByUsername(username).isPresent())
            throw new AuthorizationException("User with this username is already registered");

        usersRepository.save(new User(null, username, passwordEncoder.encode(password)));
    }

    @Override
    public void signIn(String username, String password) {
        if (username == null || username.isEmpty())
            throw new AuthorizationException("Invalid username");

        if (password == null || password.isEmpty())
            throw new AuthorizationException("Invalid password");

        if (!usersRepository.findByUsername(username).isPresent()) {
            throw new AuthorizationException("There is no user with this username");
        } else if (!passwordEncoder.matches(password, usersRepository.findByUsername(username).get().getPassword())) {
            throw new AuthorizationException("Username - password mismatch, try again pls");
        }
    }
}
