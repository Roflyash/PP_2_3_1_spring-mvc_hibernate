package web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.User;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        if (userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        return userDao.persist(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null || user.isDeleted()) {
            throw new IllegalArgumentException("Пользователь с таким id не найден: " + id);
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setDeleted(true);
        userDao.persist(user);
    }

    @Override
    @Transactional
    public User updateUser(User updatedUser) {
        User existingUser = getUserById(updatedUser.getId());

        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userDao.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setAge(updatedUser.getAge());

        return userDao.persist(existingUser);
    }
}
