package pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.service;

import org.springframework.stereotype.Service;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto.DietDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.exception.OvernightStayException;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.dto.OvernightStayDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.exception.TravelException;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;
import java.nio.channels.OverlappingFileLockException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    @Override
    public BigDecimal calculateOvernightStay(final TravelRequestDto travelRequestDto) {
        BigDecimal amountOfOvernightStayWithoutInvoice = calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);
        BigDecimal amountOfOvernightStayWithInvoice = calculateAmountOfOvernightStayWithInvoice(travelRequestDto);
        return amountOfOvernightStayWithInvoice.add(amountOfOvernightStayWithoutInvoice);
    }

    @Override
    public BigDecimal calculateAmountOfOvernightStayWithoutInvoice(TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();

        Integer inputQuantityOfOvernightStayWithoutInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();

        checkTotalQuantityAllNight(travelRequestDto, overnightStayDto, inputQuantityOfOvernightStayWithoutInvoice);

        DietDto dietDto = travelRequestDto.getDietDto();
        BigDecimal dailyAllowance = dietDto.getDailyAllowance();
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;
        amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
        return amountOfTotalOvernightsStayWithoutInvoice;
    }

    @Override
    public BigDecimal calculateAmountOfOvernightStayWithInvoice(TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();

        Integer inputQuantityOfOvernightStayWithoutInvoice = overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();

        checkTotalQuantityAllNight(travelRequestDto, overnightStayDto, inputQuantityOfOvernightStayWithoutInvoice);

        DietDto dietDto = travelRequestDto.getDietDto();
        BigDecimal dailyAllowance = dietDto.getDailyAllowance();
        BigDecimal amountOfTotalOvernightsStayWithInvoice = overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice();
        if (!travelRequestDto.getOvernightStayDto().isAllowedMoreHigherPayment()) {
            BigDecimal maxAmountForOneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
            if (amountOfTotalOvernightsStayWithInvoice.compareTo(maxAmountForOneNightWithInvoice) > 0) {
                throw new OvernightStayException("Total amount is more, then maximum allowable amount!");
            }
        }

        return amountOfTotalOvernightsStayWithInvoice;
    }

    @Override
    public Integer calculateQuantityOfOvernightStay(TravelRequestDto travelRequestDto) {
        Integer quantityOfOvernightStay = 0;
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);

            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                quantityOfOvernightStay++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return quantityOfOvernightStay;
    }

    @Override
    public Integer calculateTotalInputQuantityOfOvernightStay(TravelRequestDto travelRequestDto) {
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();
        Integer totalInputQuantityOfOvernightStay = overnightStayDto.getInputQuantityOfOvernightStayWithInvoice() +
                overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice();
        return totalInputQuantityOfOvernightStay;
    }

    private void checkTotalQuantityAllNight(TravelRequestDto travelRequestDto,
                                            OvernightStayDto overnightStayDto,
                                            Integer inputQuantityOfOvernightStayWithoutInvoice) {
        Integer quantityOfOvernightStay = calculateQuantityOfOvernightStay(travelRequestDto);
        if (overnightStayDto.getInputQuantityOfOvernightStayWithInvoice() > quantityOfOvernightStay ||
                inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay ||
                calculateTotalInputQuantityOfOvernightStay(travelRequestDto) > quantityOfOvernightStay) {
            throw new OvernightStayException("The number of nights entered for overnight stay is greater than the number of nights on the trip");
        }
    }
}
