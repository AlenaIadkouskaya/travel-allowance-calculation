package pl.iodkovskaya.travel_allowance_calculation.logic.travel.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.entity.DietEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.service.DietService;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.service.OvernightStayService;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.mapper.TravelMapper;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.repository.TravelRepository;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.service.UserReaderService;

import java.math.BigDecimal;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final UserReaderService userReaderService;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper, DietService dietService, OvernightStayService overnightStayService, UserReaderService userReaderService) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.dietService = dietService;
        this.overnightStayService = overnightStayService;
        this.userReaderService = userReaderService;
    }

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto) {
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);
        //BigDecimal amountOfTotalOvernightsStayWithInvoice = overnightStayService.calculateAmountOfOvernightStayWithInvoice(travelRequestDto);
        BigDecimal totalAmount = calculateTotalAmount(travelRequestDto);
        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());

        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        travelEntity.setUserEntity(userByPesel);
        travelEntity.setTotalAmount(totalAmount);

        DietEntity dietEntity = travelEntity.getDietEntity();
        dietEntity.setDietAmount(dietAmount);
        dietEntity.setFoodAmount(dietService.calculateFoodAmount(travelRequestDto));

        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        overnightStayEntity.setQuantityOfOvernightStay(overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto));
        overnightStayEntity.setTotalInputQuantityOfOvernightStay(overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto));
        overnightStayEntity.setAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        //overnightStayEntity.setAmountOfTotalOvernightsStayWithInvoice(amountOfTotalOvernightsStayWithInvoice);
        overnightStayEntity.setOvernightStayAmount(overnightStayAmount);

        travelRepository.save(travelEntity);
        return travelMapper.toResponseDto(travelEntity);
    }

    private BigDecimal calculateTotalAmount(TravelRequestDto travelRequestDto) {
        BigDecimal advancePayment = travelRequestDto.getAdvancePayment();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        return dietAmount.add(overnightStayAmount).subtract(advancePayment);
    }
}
