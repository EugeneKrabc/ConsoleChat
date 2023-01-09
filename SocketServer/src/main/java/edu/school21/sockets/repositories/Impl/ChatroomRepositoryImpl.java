package edu.school21.sockets.repositories.Impl;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class ChatroomRepositoryImpl implements ChatroomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatroomRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("drop table if exists chatroom cascade");
        jdbcTemplate.execute("create table if not exists chatroom (" +
                "id serial primary key, " +
                "chatRoomName varchar)");
    }

    @Override
    public Optional<Chatroom> findByChatName(String chatRoomName) {
        return jdbcTemplate.query("SELECT * FROM chatroom WHERE chatroomname = ?",
                        new Object[]{chatRoomName}, new BeanPropertyRowMapper<>(Chatroom.class))
                .stream()
                .findAny();
    }

    @Override
    public void save(Chatroom chatroom) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement("INSERT INTO chatroom (chatRoomName) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, chatroom.getChatRoomName());
            return ps;
        }, generatedKeyHolder);

        if (generatedKeyHolder.getKeys() != null) {
            if (generatedKeyHolder.getKeys().size() > 1) {
                chatroom.setId(Long.valueOf((Integer) generatedKeyHolder.getKeys().get("id")));
            } else {
                chatroom.setId(generatedKeyHolder.getKey().longValue());
            }
        }
    }

    @Override
    public List<Chatroom> findAll() {
        return jdbcTemplate.query("SELECT * FROM chatroom",
                new BeanPropertyRowMapper<>(Chatroom.class));
    }

    @Override
    public void update(Chatroom entity) {
        assert false; // not implemented cause not needed
    }

    @Override
    public void delete(Long id) {
        assert false; // not implemented cause not needed
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        assert false; // not implemented cause not needed
        return Optional.empty();
    }
}
