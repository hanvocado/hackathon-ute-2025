package hadup.server.server.service;

import hadup.server.server.dto.request.ProfileRequest;
import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.ProfileUser;
import hadup.server.server.entity.User;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.ProfileUserMapper;
import hadup.server.server.mapper.UserMapper;
import hadup.server.server.repository.ProfileUserRepository;
import hadup.server.server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileUserService {
    UserRepository userRepository;
    ProfileUserRepository profileUserRepository;
    ProfileUserMapper profileUserMapper;

    public boolean init(ProfileRequest profileRequest) {
        profileRequest.setEmail("binh@gmail.com");
        User user = userRepository.findUserByEmail(profileRequest.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        ProfileUser profileUser = profileUserMapper.toProfileUser(profileRequest);
        profileUser.setUser(user);
        profileUserRepository.save(profileUser);
        return true;
    }
}
