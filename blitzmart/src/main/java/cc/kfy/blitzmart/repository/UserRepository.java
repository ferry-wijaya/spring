package cc.kfy.blitzmart.repository;

import cc.kfy.blitzmart.domain.User;
import cc.kfy.blitzmart.dto.UserDTO;

import java.util.Collection;

public interface UserRepository<T extends User> {
    /* Basic CRUD Operation */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    boolean delete(Long id);

    /* More Complex Operation */
    User getUserByEmail(String email);

    void sendVerificationCode(UserDTO userDTO);
    User verifyCode(String email, String code);
}
