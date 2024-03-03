package cc.kfy.blitzmart.service.implementation;

import cc.kfy.blitzmart.domain.Role;
import cc.kfy.blitzmart.domain.User;
import cc.kfy.blitzmart.dto.UserDTO;
import cc.kfy.blitzmart.repository.RoleRepository;
import cc.kfy.blitzmart.repository.UserRepository;
import cc.kfy.blitzmart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cc.kfy.blitzmart.dtomapper.UserDTOMapper.fromUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(User user) {
        //return fromUser(userRepository.create(user));
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        //return fromUser(userRepository.getUserByEmail(email));
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        userRepository.sendVerificationCode(userDTO);
    }

//    @Override
//    public User getUser(String email) {
//        return userRepository.getUserByEmail(email);
//    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        //return fromUser(userRepository.verifyCode(email, code));
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    private UserDTO mapToUserDTO(User user) {
        return fromUser(user, roleRepository.getRoleByUserId(user.getId()));
    }

}
