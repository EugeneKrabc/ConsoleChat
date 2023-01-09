package edu.school21.sockets.repositories.Impl;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
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
public class MessagesRepositoryImpl implements MessagesRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("drop table if exists messages cascade");
        jdbcTemplate.execute("create table if not exists messages(" +
                "id serial primary key, " +
                "senderId bigint references users(id)," +
                "text varchar," +
                "chatroomId bigint references chatroom(id)," +
                "sendingTime timestamp)");
    }


    @Override
    public void save(Message message) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement("INSERT INTO messages (senderId, text,chatroomid, sendingTime) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, message.getSenderId());
            ps.setString(2, message.getText());
            ps.setLong(3, message.getChatroomId());
            ps.setTimestamp(4, message.getSendingTime());
            return ps;
        }, generatedKeyHolder);

        if (generatedKeyHolder.getKeys() != null) {
            if (generatedKeyHolder.getKeys().size() > 1) {
                message.setId(Long.valueOf((Integer) generatedKeyHolder.getKeys().get("id")));
            } else {
                message.setId(generatedKeyHolder.getKey().longValue());
            }
        }
    }

    @Override
    public List<Message> loadMessagesFromRoom(Long chatroomId) {
        return jdbcTemplate.query("SELECT * FROM messages WHERE chatroomid = ?" +
                        "ORDER BY sendingtime DESC LIMIT 30",
                new Object[]{chatroomId}, new BeanPropertyRowMapper<>(Message.class));
    }

    public Optional<Message> findById(Long id) {
        assert false; // not implemented cause not needed
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        assert false; // not implemented cause not needed
        return null;
    }

    @Override
    public void update(Message entity) {
        assert false; // not implemented cause not needed
    }

    @Override
    public void delete(Long id) {
        assert false; // not implemented cause not needed
    }
}
