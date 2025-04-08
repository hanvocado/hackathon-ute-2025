package hadup.server.server.mapper;

import hadup.server.server.dto.request.PlanningRequest;
import hadup.server.server.dto.request.ProfileRequest;
import hadup.server.server.dto.response.PlanningResponse;
import hadup.server.server.entity.Plan;
import hadup.server.server.entity.ProfileUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan toPlan(PlanningResponse planningResponse);
    PlanningResponse toPlanningResponse(Plan plan);
}
