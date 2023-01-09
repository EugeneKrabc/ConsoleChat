package edu.school21.sockets.repositories.Impl;

import edu.school21.sockets.models.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("drop table if exists users cascade ");
        jdbcTemplate.execute("create table if not exists users(" +
                "id serial primary key, username varchar, password varchar)");
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(User.class))
                .stream()
                .findAny();

    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void save(User entity) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            return ps;
        }, generatedKeyHolder);

        if (generatedKeyHolder.getKeys() != null) {
            if (generatedKeyHolder.getKeys().size() > 1) {
                entity.setId(Long.valueOf((Integer) generatedKeyHolder.getKeys().get("id")));
            } else {
                entity.setId(generatedKeyHolder.getKey().longValue());
            }
        }
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET username=?, password=? WHERE id = ?",
                entity.getUsername(), entity.getPassword(), entity.getId());

    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                        new Object[]{username}, new BeanPropertyRowMapper<>(User.class))
                .stream()
                .findAny();

    }
}
