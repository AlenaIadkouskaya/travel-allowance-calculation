package pl.iodkovskaya.travel_allowance_calculation.logic.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.entity.TransportCostEntity;
@Repository
public interface TransportRepository extends JpaRepository<TransportCostEntity, Long> {

}
