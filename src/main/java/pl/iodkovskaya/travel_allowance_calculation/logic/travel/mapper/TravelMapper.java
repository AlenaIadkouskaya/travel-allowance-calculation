package pl.iodkovskaya.travel_allowance_calculation.logic.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.mapper.DietMapper;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.mapper.OvernightStayMapper;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.service.UserReaderService;

@Component
@AllArgsConstructor
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final UserReaderService userReaderService;

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getUserEntity().getPesel(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()));
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());
        return new TravelEntity(travelRequestDto.getFromCity(), travelRequestDto.getToCity(), travelRequestDto.getStartDate(),
                travelRequestDto.getStartTime(), travelRequestDto.getEndDate(), travelRequestDto.getEndTime(),
                userByPesel, travelRequestDto.getAdvancePayment(), travelRequestDto.getDietDto().getDailyAllowance(),
                travelRequestDto.getDietDto().getNumberOfBreakfasts(), travelRequestDto.getDietDto().getNumberOfLunches(),
                travelRequestDto.getDietDto().getNumberOfDinners(), travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
                travelRequestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
    }
}
