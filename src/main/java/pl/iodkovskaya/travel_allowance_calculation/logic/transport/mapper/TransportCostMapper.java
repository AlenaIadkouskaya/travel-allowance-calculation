package pl.iodkovskaya.travel_allowance_calculation.logic.transport.mapper;

import org.springframework.stereotype.Component;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.dto.TransportCostResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.entity.TransportCostEntity;

@Component
public class TransportCostMapper {
    public TransportCostResponseDto toResponseDto(TransportCostEntity entity) {
        return new TransportCostResponseDto(entity.getId(), entity.getInputtedDaysNumberForTransportCost(),
                entity.getUndocumentedLocalTransportCost(), entity.getDocumentedLocalTransportCost(), entity.getMeansOfTransport(),
                entity.getCostOfTravelByPublicTransport(), entity.getCostOfTravelByOwnTransport(), entity.getTransportCostAmount());
    }
}
