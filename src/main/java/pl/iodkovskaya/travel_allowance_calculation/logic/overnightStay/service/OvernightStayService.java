package pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface OvernightStayService {
    BigDecimal calculateOvernightStay(TravelRequestDto travelRequestDto);

    BigDecimal calculateAmountOfOvernightStayWithoutInvoice(TravelRequestDto travelRequestDto);

    BigDecimal calculateAmountOfOvernightStayWithInvoice(TravelRequestDto travelRequestDto);

    Integer calculateQuantityOfOvernightStay(TravelRequestDto travelRequestDto);

    Integer calculateTotalInputQuantityOfOvernightStay(TravelRequestDto travelRequestDto);
}
