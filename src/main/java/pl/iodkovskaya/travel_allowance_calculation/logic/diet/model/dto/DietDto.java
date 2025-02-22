package pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DietDto {
    @NotNull(message = "Daily allowance cannot be null")
    private BigDecimal dailyAllowance = BigDecimal.valueOf(45);

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    private Integer numberOfLunches;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    private Integer numberOfDinners;
}
