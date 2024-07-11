package pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietResponseDto {
    private Long id;
    private BigDecimal dietAmount;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;
}
