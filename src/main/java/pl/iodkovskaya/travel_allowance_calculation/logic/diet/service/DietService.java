package pl.iodkovskaya.travel_allowance_calculation.logic.diet.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface DietService {
    BigDecimal calculateDiet(TravelRequestDto travelRequestDto);

    BigDecimal calculateDietAmount(final TravelRequestDto travelRequestDto);

    BigDecimal calculateFoodAmount(TravelRequestDto travelRequestDto);
}
