package pl.iodkovskaya.travel_allowance_calculation.logic.diet.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto.DietDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto.DietResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.entity.DietEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;

@Component

public class DietMapper {
    public DietEntity toEntity(DietDto requestDto, TravelEntity travelEntity) {
        return new DietEntity(travelEntity, requestDto.getDailyAllowance(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }

    public DietResponseDto toResponseDto(DietEntity entity) {
        return new DietResponseDto(entity.getId(), entity.getDietAmount(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners());
    }
}
