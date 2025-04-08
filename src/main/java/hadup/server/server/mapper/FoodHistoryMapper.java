package hadup.server.server.mapper;

import hadup.server.server.dto.response.FoodHistoryResponse;
import hadup.server.server.entity.FoodHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodHistoryMapper {
    @Mapping(source = "food.foodName", target = "foodName")
    @Mapping(source = "food.calories", target = "calories")
    @Mapping(source = "food.fat", target = "fat")
    @Mapping(source = "food.sugar", target = "sugar")
    @Mapping(source = "food.protein", target = "protein")
    @Mapping(source = "food.fiber", target = "fiber")
    FoodHistoryResponse toResponse(FoodHistory foodHistory);
}

