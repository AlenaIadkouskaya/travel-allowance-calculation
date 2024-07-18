package pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto.DietResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.dto.OvernightStayResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.dto.TransportCostResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private Long id;
    private Long pesel;
    private String fromCity;
    private String toCity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private BigDecimal otherExpenses;
    private BigDecimal totalAmount;
    private BigDecimal advancePayment;
    private DietResponseDto dietResponse;
    private OvernightStayResponseDto overnightStayResponseDto;
    private TransportCostResponseDto transportCostResponseDto;
}
