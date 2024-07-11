package pl.iodkovskaya.travel_allowance_calculation.logic.travel.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;

public interface TravelService {
    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}
