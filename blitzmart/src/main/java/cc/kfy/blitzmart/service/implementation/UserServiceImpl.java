package cc.kfy.blitzmart.service.implementation;

import cc.kfy.blitzmart.domain.User;
import cc.kfy.blitzmart.dto.UserDTO;
import cc.kfy.blitzmart.dtomapper.UserDTOMapper;
import cc.kfy.blitzmart.repository.UserRepository;
import cc.kfy.blitzmart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }
}
