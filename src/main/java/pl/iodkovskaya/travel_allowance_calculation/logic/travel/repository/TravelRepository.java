package pl.iodkovskaya.travel_allowance_calculation.logic.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.entity.TravelEntity;

@Repository
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
