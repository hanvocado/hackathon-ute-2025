package hadup.server.server.mapper;

import hadup.server.server.dto.request.PlanningRequest;
import hadup.server.server.dto.request.ProfileRequest;
import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.ProfileUser;
import hadup.server.server.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileUserMapper {
    ProfileUser toProfileUser(ProfileRequest profileRequest);
    PlanningRequest toPlanningRequest(ProfileUser profileUser);
}
