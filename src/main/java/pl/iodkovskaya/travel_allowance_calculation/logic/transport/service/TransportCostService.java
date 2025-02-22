package pl.iodkovskaya.travel_allowance_calculation.logic.transport.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface TransportCostService {
    BigDecimal calculateTransportCostAmount(TravelRequestDto travelRequestDto);

    BigDecimal calculateUndocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateDocumentedLocalTransportCost(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByPublicTransport(TravelRequestDto travelRequestDto);

    BigDecimal calculateCostOfTravelByOwnTransport(TravelRequestDto travelRequestDto);

}
