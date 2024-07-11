package pl.iodkovskaya.travel_allowance_calculation.logic.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;

import java.util.Optional;

public interface UserReaderRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPesel(Long Pesel);
}
