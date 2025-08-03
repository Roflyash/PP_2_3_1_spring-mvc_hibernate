package web.dao;

import web.model.User;
import java.util.List;

public interface UserDao {
    User persist(User user);
    User findById(Long id);
    void remove(User user);
    List<User> findAll();
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}