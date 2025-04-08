package hadup.server.server.mapper;

import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest userRegisterRequest);
}
