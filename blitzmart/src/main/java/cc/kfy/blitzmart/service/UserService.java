package cc.kfy.blitzmart.service;

import cc.kfy.blitzmart.domain.User;
import cc.kfy.blitzmart.dto.UserDTO;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);
    //User getUser(String email);
    UserDTO verifyCode(String email, String code);
}
