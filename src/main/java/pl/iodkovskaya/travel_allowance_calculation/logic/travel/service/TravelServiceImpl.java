package pl.iodkovskaya.travel_allowance_calculation.logic.travel.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.entity.DietEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.service.DietService;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.service.OvernightStayService;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.entity.TransportCostEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.service.TransportCostService;
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
    private final TransportCostService transportCostService;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper, DietService dietService,
                             OvernightStayService overnightStayService, UserReaderService userReaderService, TransportCostService transportCostService) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.dietService = dietService;
        this.overnightStayService = overnightStayService;
        this.userReaderService = userReaderService;
        this.transportCostService = transportCostService;
    }

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto) {
        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        BigDecimal totalAmount = calculateTotalAmount(travelRequestDto);
        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());

        DietEntity dietEntity = travelEntity.getDietEntity();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);

        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        Integer quantityOfOvernightStay = overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto);
        Integer totalInputQuantityOfOvernightStay = overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);

        TransportCostEntity transportCostEntity = travelEntity.getTransportCostEntity();
        BigDecimal transportCostAmount = transportCostService.calculateTransportCostAmount(travelRequestDto);
        BigDecimal costOfTravelByPublicTransport = transportCostService.calculateCostOfTravelByPublicTransport(travelRequestDto);
        BigDecimal costOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);
        BigDecimal undocumentedLocalTransportCost = transportCostService.calculateUndocumentedLocalTransportCost(travelRequestDto);
        BigDecimal documentedLocalTransportCost = transportCostService.calculateDocumentedLocalTransportCost(travelRequestDto);

        travelEntity.updateTotalAmount(totalAmount);
        travelEntity.updateUser(userByPesel);

        dietEntity.updateDietAmount(dietAmount);
        dietEntity.updateFoodAmount(foodAmount);

        overnightStayEntity.updateQuantityOfOvernightStay(quantityOfOvernightStay);
        overnightStayEntity.updateTotalInputQuantityOfOvernightStay(totalInputQuantityOfOvernightStay);
        overnightStayEntity.updateAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        overnightStayEntity.updateOvernightStayAmount(overnightStayAmount);

        transportCostEntity.updateTransportCostAmount(transportCostAmount);
        transportCostEntity.updateCostOfTravelByPublicTransport(costOfTravelByPublicTransport);
        transportCostEntity.updateCostOfTravelByOwnTransport(costOfTravelByOwnTransport);
        transportCostEntity.updateUndocumentedLocalTransportCost(undocumentedLocalTransportCost);
        transportCostEntity.updateDocumentedLocalTransportCost(documentedLocalTransportCost);

        travelRepository.save(travelEntity);

        return travelMapper.toResponseDto(travelEntity);
    }

    private BigDecimal calculateTotalAmount(final TravelRequestDto travelRequestDto) {
        BigDecimal advancePayment = travelRequestDto.getAdvancePayment();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        return dietAmount.add(overnightStayAmount).subtract(advancePayment);
    }
}
