package pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.mapper;

import org.springframework.stereotype.Component;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.dto.OvernightStayDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.dto.OvernightStayResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;

@Component
public class OvernightStayMapper {
    public OvernightStayEntity toEntity(OvernightStayDto requestDto, TravelEntity travelEntity) {
        return new OvernightStayEntity(travelEntity, requestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getInputQuantityOfOvernightStayWithInvoice(), requestDto.getAmountOfTotalOvernightsStayWithInvoice());
    }

    public OvernightStayResponseDto toResponseDto(OvernightStayEntity entity) {
        return new OvernightStayResponseDto(entity.getId(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getOvernightStayAmount());
    }
}
