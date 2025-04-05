package hadup.server.server.mapper;

import hadup.server.server.dto.response.FoodHistoryResponse;
import hadup.server.server.entity.FoodHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    FoodHistoryResponse toFoodHistoryResponse(FoodHistory foodHistory);
}
