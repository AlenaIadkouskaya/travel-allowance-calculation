package pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "overnight_stay")
public class OvernightStayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

    private boolean isAllowedMoreHigherPayment;

    public OvernightStayEntity(TravelEntity travelEntity, Integer inputQuantityOfOvernightStayWithoutInvoice,
                               Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice, boolean isAllowedMoreHigherPayment) {
        this.travelEntity = travelEntity;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.totalInputQuantityOfOvernightStay = inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice;
        this.isAllowedMoreHigherPayment = isAllowedMoreHigherPayment;
    }

    public void updateQuantityOfOvernightStay(Integer quantityOfOvernightStay) {
        this.quantityOfOvernightStay = quantityOfOvernightStay;
    }

    public void updateTotalInputQuantityOfOvernightStay(Integer totalInputQuantityOfOvernightStay) {
        this.totalInputQuantityOfOvernightStay = totalInputQuantityOfOvernightStay;
    }

    public void updateAmountOfTotalOvernightsStayWithoutInvoice(BigDecimal amountOfTotalOvernightsStayWithoutInvoice) {
        this.amountOfTotalOvernightsStayWithoutInvoice = amountOfTotalOvernightsStayWithoutInvoice;
    }

    public void updateOvernightStayAmount(BigDecimal overnightStayAmount) {
        this.overnightStayAmount = overnightStayAmount;
    }
}
