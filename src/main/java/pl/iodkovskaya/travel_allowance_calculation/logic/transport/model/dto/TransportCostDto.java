package pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TransportCostDto {
    @NotNull(message = "Inputted days number for undocumented local transport cannot be null")
    private Integer inputtedDaysNumberForUndocumentedLocalTransportCost;

    @NotNull(message = "Documented local transport cost cannot be null")
    private BigDecimal documentedLocalTransportCost;

    @NotBlank(message = "Means of transport cannot be blank")
    private String meansOfTransport;

    @NotNull(message = "Cost of travel by public transport cannot be null")
    private BigDecimal costOfTravelByPublicTransport;

    @NotNull(message = "Kilometers by car engine up to 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine up to 900cc cannot be negative")
    private Long kilometersByCarEngineUpTo900cc;

    @NotNull(message = "Kilometers by car engine above 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine above 900cc cannot be negative")
    private Long kilometersByCarEngineAbove900cc;

    @NotNull(message = "Kilometers by motorcycle cannot be null")
    @Min(value = 0, message = "Kilometers by motorcycle cannot be negative")
    private Long kilometersByMotorcycle;

    @NotNull(message = "Kilometers by moped cannot be null")
    @Min(value = 0, message = "Kilometers by moped cannot be negative")
    private Long kilometersByMoped;

}
