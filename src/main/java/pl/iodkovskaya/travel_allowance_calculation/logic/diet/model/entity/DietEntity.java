package pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "diet")
public class DietEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private BigDecimal dailyAllowance;

    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @Column(nullable = false)
    private Integer numberOfLunches;

    @Column(nullable = false)
    private Integer numberOfDinners;

    @Column(nullable = false)
    private BigDecimal dietAmount;

    @Column(nullable = false)
    private BigDecimal foodAmount;

    public DietEntity(TravelEntity travelEntity, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                      Integer numberOfLunches, Integer numberOfDinners) {
        this.dailyAllowance = dailyAllowance;
        this.travelEntity = travelEntity;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
    }

    public void updateDietAmount(BigDecimal dietAmount) {
        this.dietAmount = dietAmount;
    }

    public void updateFoodAmount(BigDecimal foodAmount) {
        this.foodAmount = foodAmount;
    }
}
