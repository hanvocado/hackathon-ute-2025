package hadup.server.server.service;

import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.User;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.UserMapper;
import hadup.server.server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsUserByEmail(userRegisterRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userRepository.save(userMapper.toUser(userRegisterRequest));
    }
}
